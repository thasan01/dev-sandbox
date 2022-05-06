package com.codingronin.sandbox.crypto.app;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Properties;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.naming.directory.InvalidAttributesException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import com.beust.jcommander.JCommander;
import com.codingronin.sandbox.crypto.PasswordBasedFileEncrypter;
import com.codingronin.sandbox.crypto.app.EncrypterAppArgs.Command;
import com.codingronin.sandbox.crypto.app.EncrypterAppArgs.DecryptFileCommand;
import com.codingronin.sandbox.crypto.app.EncrypterAppArgs.EncryptFileCommand;

public class EncrypterApp {


  SecretKey secretKey;
  PasswordBasedFileEncrypter encrypter;

  Logger console = LogManager.getLogger(EncrypterApp.class);

  public static void main(String[] args) throws IOException, GeneralSecurityException {

    EncrypterApp app = new EncrypterApp();
    ActionSet actionSet = app.processArgs(args);
    app.init();
    app.performAction(actionSet);

  }

  public EncrypterApp() throws IOException, GeneralSecurityException {
    encrypter = new PasswordBasedFileEncrypter(
        Cipher.getInstance(PasswordBasedFileEncrypter.CIPHER_ALGORITHM), new SecureRandom());
  }

  void init() throws IOException {
    Properties prop = new Properties();
    String filePathPropFile = "app.secret.properties";

    Configurator.setLevel(console.getName(), Level.DEBUG);
    console.debug("looking for {} in classpath.", filePathPropFile);
    try (InputStream input =
        PasswordBasedFileEncrypter.class.getClassLoader().getResourceAsStream(filePathPropFile)) {

      if (input == null)
        throw new FileNotFoundException(
            String.format("Unable to find file[%s] in classpath.", filePathPropFile));

      prop.load(input);
      String password = prop.getProperty("password");
      String salt = prop.getProperty("salt");

      secretKey = PasswordBasedFileEncrypter.getKeyFromPassword(password, salt);

    } catch (Exception ex) {
      exitOnError("Encountered an error during initialization. ", ex);
    }
  }

  /**
   * 
   * @param args
   * @return
   */
  ActionSet processArgs(String[] args) {

    ActionSet result = new ActionSet();
    try {
      EncryptFileCommand encryptFileCmd = new EncryptFileCommand();
      DecryptFileCommand decryptFileCmd = new DecryptFileCommand();

      EncrypterAppArgs appArgs = new EncrypterAppArgs();
      JCommander jcmd = JCommander.newBuilder()//
          .addObject(appArgs)//
          .addCommand(encryptFileCmd)//
          .addCommand(decryptFileCmd)//
          .build();

      jcmd.parse(args);

      String logLevel = appArgs.getLogLevel();

      if (logLevel != null) {
        Configurator.setLevel(console.getName(), Level.valueOf(logLevel));
        console.debug("Setting logging level: {}", logLevel);
      }

      String actionValue = jcmd.getParsedCommand();
      Action action = Action.getKey(actionValue)
          .orElseThrow(() -> new InvalidAttributesException("Invalid action:" + actionValue));

      result.setAction(action);

      switch (action) {

        case ENCRYPT_FILE:
          processEncryptFileCommand(encryptFileCmd, result);
          break;

        case DECRYPT_FILE:
          processDecryptFileCommand(decryptFileCmd, result);
          break;

        default:
          break;
      }


    } catch (Exception ex) {
      exitOnError("Encountered an error while processing args. ", ex);
    }
    return result;
  }

  void processEncryptFileCommand(Command command, ActionSet result) {

    ActionPerformer performer = ActionPerformerFactory.create(Action.ENCRYPT_FILE);
    result.setEncryptFile(command.getDestination());
    result.setDecryptFile(command.getSource());
    result.setPerformer(performer);
  }

  void processDecryptFileCommand(Command command, ActionSet result) {

    ActionPerformer performer = ActionPerformerFactory.create(Action.DECRYPT_FILE);
    result.setEncryptFile(command.getSource());
    result.setDecryptFile(command.getDestination());
    result.setPerformer(performer);
  }


  void performAction(ActionSet actionSet) {
    try {
      ActionPerformer performer = actionSet.getPerformer();
      performer.performAction(encrypter, secretKey, actionSet);
    } catch (Exception ex) {
      exitOnError("Encountered an error while performing action. ", ex);
    }

  }

  void exitOnError(String prefix, Exception ex) {
    String msg = prefix + " {}";
    console.error(msg, ex.getMessage());
    System.exit(-1);
  }

}
