package com.codingronin.sandbox.crypto.app;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import javax.crypto.SecretKey;
import com.codingronin.sandbox.crypto.PasswordBasedFileEncrypter;

class EncryptFilePerformer implements ActionPerformer {

  @Override
  public void performAction(PasswordBasedFileEncrypter encrypter, SecretKey secretKey,
      ActionSet actionSet) throws IOException, GeneralSecurityException {
    byte[] iv = encrypter.generateIv();
    File source = new File(actionSet.getDecryptFile());
    File target = new File(actionSet.getEncryptFile());
    encrypter.encryptFile(secretKey, iv, source, target, true);
  }
}
