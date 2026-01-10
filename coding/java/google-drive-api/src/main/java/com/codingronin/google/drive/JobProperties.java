package com.codingronin.google.drive;

import java.io.IOException;
import java.util.Properties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class JobProperties {

  static JobProperties create(String fileName) throws IOException {

    JobProperties jobProp = new JobProperties();
    PropertiesFileUtil.load(fileName, (Properties prop) -> {
      jobProp.setDriveFile((String) prop.get("driveFile"));
      jobProp.setLocalFile((String) prop.get("localFile"));
      jobProp.setCredentialsFile((String) prop.get("credentialsFile"));
      jobProp.setTokensFolder((String) prop.get("tokensFolder"));
    });

    return jobProp;
  }

  @Getter
  @Setter
  String driveFile;

  @Getter
  @Setter
  String localFile;

  @Getter
  @Setter
  String credentialsFile;

  @Getter
  @Setter
  String tokensFolder;
}
