package com.codingronin.sandbox.crypto.app;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import javax.crypto.SecretKey;
import com.codingronin.sandbox.crypto.PasswordBasedFileEncrypter;

public class DecryptFilePerformer implements ActionPerformer {

  @Override
  public void performAction(PasswordBasedFileEncrypter encrypter, SecretKey secretKey,
      ActionSet actionSet) throws IOException, GeneralSecurityException {
    File source = new File(actionSet.getEncryptFile());
    File target = new File(actionSet.getDecryptFile());
    encrypter.decryptFileWithIV(secretKey, source, target);
  }

}
