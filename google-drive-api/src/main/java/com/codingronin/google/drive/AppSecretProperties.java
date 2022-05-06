package com.codingronin.google.drive;

import java.io.IOException;
import java.util.Properties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class AppSecretProperties {

  static AppSecretProperties create(String fileName) throws IOException {

    AppSecretProperties appProp = new AppSecretProperties();
    PropertiesFileUtil.load(fileName, (Properties prop) -> {
      appProp.setSalt((String) prop.get("salt"));
      appProp.setPassword((String) prop.get("password"));
    });

    return appProp;
  }

  @Getter
  @Setter
  String salt;

  @Getter
  @Setter
  String password;

}
