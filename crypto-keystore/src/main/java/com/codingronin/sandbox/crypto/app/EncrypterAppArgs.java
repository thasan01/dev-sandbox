package com.codingronin.sandbox.crypto.app;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import lombok.Getter;
import lombok.Setter;

public class EncrypterAppArgs {

  public static interface Command {
    public String getSource();

    public String getDestination();
  }

  @Parameters(commandNames = "encryptFile")
  public static class EncryptFileCommand implements Command {
    @Parameter(names = "--source")
    @Getter
    @Setter
    String source;

    @Parameter(names = "--destination")
    @Getter
    @Setter
    String destination;

  }

  @Parameters(commandNames = "decryptFile")
  public static class DecryptFileCommand implements Command {
    @Parameter(names = "--source")
    @Getter
    @Setter
    String source;

    @Parameter(names = "--destination")
    @Getter
    @Setter
    String destination;
  }

  @Parameter(names = "--logLevel")
  @Getter
  @Setter
  String logLevel;
}
