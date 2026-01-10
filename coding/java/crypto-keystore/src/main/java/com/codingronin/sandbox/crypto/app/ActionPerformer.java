package com.codingronin.sandbox.crypto.app;

import java.io.IOException;
import java.security.GeneralSecurityException;
import javax.crypto.SecretKey;
import com.codingronin.sandbox.crypto.PasswordBasedFileEncrypter;

public interface ActionPerformer {

  void performAction(PasswordBasedFileEncrypter encrypter, SecretKey secretKey, ActionSet actionSet)
      throws IOException, GeneralSecurityException;

}
