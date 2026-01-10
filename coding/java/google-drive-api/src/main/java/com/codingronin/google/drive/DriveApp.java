package com.codingronin.google.drive;

import static com.codingronin.google.drive.DriveAppService.textFile;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.regex.Pattern;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import com.beust.jcommander.JCommander;
import com.codingronin.google.drive.DriveAppArgs.PullCommand;
import com.codingronin.google.drive.DriveAppArgs.PushCommand;
import com.codingronin.google.drive.PropertiesFileUtil.PropertyCallback;
import com.codingronin.sandbox.crypto.PasswordBasedFileEncrypter;
import com.google.api.services.drive.model.File;

public class DriveApp {

  static Logger console = LogManager.getLogger(DriveApp.class);
  static final String APP_NAME = "appName";

  enum Action {
    PULL, PUSH
  }

  Action inputAction;
  String inputCredentialsFile;
  String inputDriveFile;
  String inputLocalFile;
  String inputTokensFolder;

  AppSecretProperties secretProperties;

  String searchFileName = null;
  String searchFileParentId = null;
  List<String> searchDirNames = null;
  File searchFile = null;

  DriveAppService driveService;

  PasswordBasedFileEncrypter encryptionService;

  public static void main(String[] args) throws GeneralSecurityException, IOException {
    DriveApp app = new DriveApp();
    app.prcessArgs(args);
    app.init();
    app.performJob();

  }

  static java.io.File createTempFile() throws IOException {
    String prefix = "java-sandbox-driveapp-";
    String suffix = String.format("-%d.dat", System.currentTimeMillis());
    java.io.File tempFile = java.io.File.createTempFile(prefix, suffix);
    tempFile.deleteOnExit();
    return tempFile;
  }

  SecretKey createSecretKey() throws GeneralSecurityException {
    String pass = secretProperties.getPassword();
    String salt = secretProperties.getSalt();
    return PasswordBasedFileEncrypter.getKeyFromPassword(salt, pass);
  }

  void prcessArgs(String[] args)
      throws IOException, NoSuchAlgorithmException, NoSuchPaddingException {

    DriveAppArgs appArgs = new DriveAppArgs();
    PushCommand pushCmd = new PushCommand();
    PullCommand pullCmd = new PullCommand();

    JCommander jcmd = JCommander.newBuilder()//
        .addObject(appArgs)//
        .addCommand(pushCmd)//
        .addCommand(pullCmd)//
        .build();

    jcmd.parse(args);

    String logLevel = appArgs.getLogLevel();
    if (logLevel != null) {
      Configurator.setLevel(console.getName(), Level.valueOf(logLevel));
      console.debug("Setting logging level: {}", logLevel);
    }

    if (Boolean.TRUE.equals(appArgs.getSecure())) {
      console.debug("Initializing encryptionService.");
      Cipher cipher = Cipher.getInstance(PasswordBasedFileEncrypter.CIPHER_ALGORITHM);
      SecureRandom secureRandom = new SecureRandom();
      encryptionService = new PasswordBasedFileEncrypter(cipher, secureRandom);
    }

    String action = jcmd.getParsedCommand();
    PropertyCallback callback = (Properties props) -> {
      inputDriveFile = (String) props.get("driveFile");
      inputLocalFile = (String) props.get("localFile");
      inputCredentialsFile = (String) props.get("credentialsFile");
      inputTokensFolder = (String) props.get("tokensFolder");
    };

    switch (action) {
      case "pull":
        PropertiesFileUtil.loadFromFile(pullCmd.getFile(), callback);
        break;

      case "push":
        PropertiesFileUtil.loadFromFile(pushCmd.getFile(), callback);
        break;

      default:
        throw new IllegalArgumentException("Invalid command " + action);
    }

    inputAction = Action.valueOf(action.toUpperCase());
    console.debug("Input action: {}, driveFile: {}, localFile: {}, tokensFolder: {}", inputAction,
        inputDriveFile, inputLocalFile, inputTokensFolder);

    console.debug("Loading app.secret.properties from classpath.");
    secretProperties = AppSecretProperties.create("classpath:app.secret.properties");
  }

  /**
   * 
   * @throws IOException
   * @throws GeneralSecurityException
   */
  void init() throws IOException, GeneralSecurityException {

    try (InputStream credsFile = new FileInputStream(new java.io.File(inputCredentialsFile))) {
      driveService = DriveAppService.create(APP_NAME, credsFile, inputTokensFolder);
    }

    searchFileName = null;
    searchDirNames = new ArrayList<>();

    String[] array = inputDriveFile.split(Pattern.quote("/"));
    if (array.length <= 1) {
      searchFileName = array[0];
    } else {
      searchFileName = array[array.length - 1];
      searchDirNames = Arrays.asList(Arrays.copyOfRange(array, 0, array.length - 1));
    }

    List<File> allFiles = driveService.getFileNames();

    String previousDirId = null;
    for (String dirName : searchDirNames) {
      console.debug("Checking for Folder [{}] in Drive.", dirName);
      File file = getFileFromList(allFiles, dirName);

      if (file == null) {
        console.debug("Folder[{}] did not exist. Creating it now with parent[{}].", dirName,
            previousDirId);

        file = driveService.createFolder(dirName, previousDirId);
        previousDirId = file.getId();
      }
    }

    searchFileParentId = previousDirId;
    searchFile = getFileFromList(allFiles, searchFileName);
  }

  void performJob() throws IOException, GeneralSecurityException {
    console.info("Performing action {}", inputAction);

    if (Action.PULL.equals(inputAction))
      pull();

    else if (Action.PUSH.equals(inputAction))
      push();
  }

  void push() throws IOException, GeneralSecurityException {
    java.io.File fileContent = new java.io.File(inputLocalFile);

    // If encryptionService is not null, then use it to encrypt the file before pushing it to Google
    // Drive
    if (encryptionService != null) {
      java.io.File tempFile = createTempFile();
      console.debug("Encrypted file location {}", tempFile.getAbsolutePath());

      SecretKey secretKey = createSecretKey();
      byte[] iv = encryptionService.generateIv();
      encryptionService.encryptFile(secretKey, iv, fileContent, tempFile, true);
      fileContent = tempFile;
    }

    if (searchFile == null) {
      console.info("Drive File[{}] did not exist. Creating it now with parent {}.", searchFileName,
          searchFileParentId);
      driveService.uploadFile(fileContent, searchFileName, searchFileParentId, textFile());
    } else {
      console.info("Drive File[{}] exist. Updating it now.", searchFileName);
      driveService.uploadFile(fileContent, searchFile.getId(), textFile());
    }
  }

  void pull() throws IOException, GeneralSecurityException {
    if (searchFile != null) {

      boolean isSecure = encryptionService != null;

      java.io.File fileContent = isSecure ? createTempFile() : new java.io.File(inputLocalFile);

      driveService.downloadFile(searchFile.getId(), fileContent);

      if (isSecure) {
        SecretKey secretKey = createSecretKey();
        java.io.File decryptedFile = new java.io.File(inputLocalFile);
        console.debug("Decrypting file from {} to {}", fileContent.getAbsolutePath(),
            decryptedFile.getAbsolutePath());
        encryptionService.decryptFileWithIV(secretKey, fileContent, decryptedFile);
      }

    } else {
      console.warn("Unable to pull file[{}], because it does not exist.", inputDriveFile);
    }
  }


  /**
   * 
   * @param files
   * @param searchName
   * @return
   */
  File getFileFromList(List<File> files, String searchName) {
    Optional<File> search =
        files.stream().filter(elem -> searchName.equals(elem.getName())).findFirst();
    return search.orElse(null);
  }


}
