package com.codingronin.google.drive;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class DriveAppArgs {

  @Parameters(commandNames = {"pull"}, commandDescription = "Pulls a file from a Google Drive.")
  @NoArgsConstructor
  public static class PullCommand {
    @Parameter(names = {"--file", "-f"}, required = true)
    @Getter
    @Setter
    String file;
  }

  @Parameters(commandNames = {"push"}, commandDescription = "Pushes a file from a Google Drive.")
  public static class PushCommand {
    @Parameter(names = {"--file", "-f"}, required = true)
    @Getter
    @Setter
    String file;
  }

  @Parameter(names = "--logLevel")
  @Getter
  @Setter
  String logLevel;

  @Parameter(names = "--secure")
  @Getter
  @Setter
  Boolean secure;


}
