package com.codingronin.sandbox.crypto.app;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ActionSet {

  Action action;
  String encryptFile;
  String decryptFile;
  ActionPerformer performer;

}
