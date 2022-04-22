package com.codingronin.sandbox.crypto.app;

public class ActionPerformerFactory {

  private ActionPerformerFactory() {
    // singleton constructor
  }

  public static ActionPerformer create(Action action) {

    switch (action) {
      case ENCRYPT_FILE:
        return new EncryptFilePerformer();

      case DECRYPT_FILE:
        return new DecryptFilePerformer();

      default:
        throw new IllegalStateException();
    }
  }

}
