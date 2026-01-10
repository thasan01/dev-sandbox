package com.codingronin.google.drive;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesFileUtil {

  private PropertiesFileUtil() {
    // This class should not be initialized.
  }

  public static interface PropertyCallback {
    void onLoad(Properties prop);
  }

  static void load(String filename, PropertyCallback callback) throws IOException {

    int idx = filename.indexOf(":");
    String filenameValue = filename;
    String fileType = null;

    if (idx > 0) {
      filenameValue = filename.substring(idx + 1);
      fileType = filename.substring(0, idx);
    }

    if ("classpath".equals(fileType))
      loadFromClassPath(filenameValue, callback);
    else
      loadFromFile(filenameValue, callback);
  }

  static void loadFromClassPath(String filename, PropertyCallback callback) throws IOException {
    Properties prop = new Properties();
    try (InputStream input = DriveApp.class.getClassLoader().getResourceAsStream(filename)) {

      if (input == null)
        throw new FileNotFoundException(
            String.format("Unable to find file[%s] in classpath.", filename));

      prop.load(input);
      callback.onLoad(prop);
    }
  }

  static void loadFromFile(String filename, PropertyCallback callback) throws IOException {
    Properties prop = new Properties();
    try (InputStream input = new FileInputStream(filename)) {

      prop.load(input);
      callback.onLoad(prop);
    }
  }


}
