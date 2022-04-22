package com.codingronin.sandbox.crypto.app;

import java.util.Arrays;
import java.util.Optional;

enum Action {
  ENCRYPT("encrypt"), ENCRYPT_FILE("encryptFile"), DECRYPT("decrypt"), DECRYPT_FILE("decryptFile");

  private String value;

  Action(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public static Optional<Action> getKey(String key) {
    return Arrays.stream(Action.values()).filter(env -> env.value.equals(key)).findFirst();
  }
}
