package com.codingronin.google.drive;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.common.net.MediaType;

public class DriveAppService {

  // Allows app to view and manage Google Drive files and folders that you have opened or created
  // with this app.
  private static final List<String> SCOPES =
      Arrays.asList(DriveScopes.DRIVE_APPDATA, DriveScopes.DRIVE_FILE);
  private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
  public static final String DRIVE_FOLDER_MEDIA_TYPE = "application/vnd.google-apps.folder";
  public static final String DRIVE_APP_DATA_PARENT = "appDataFolder";
  public static final String DRIVE_RETURN_FIELDS = "id, name, parents";


  Drive service;
  boolean isAppData;

  public static DriveAppService create(String driveAppName, InputStream credentialFile,
      String tokenDirectoryPath) throws GeneralSecurityException, IOException {

    final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
    Credential credential = getCredentials(httpTransport, credentialFile, tokenDirectoryPath);
    Drive service = new Drive.Builder(httpTransport, JSON_FACTORY, credential)
        .setApplicationName(driveAppName).build();

    return new DriveAppService(service);
  }


  /**
   * Creates an authorized Credential object.
   * 
   * @param httpTransport The network HTTP Transport.
   * @return An authorized Credential object.
   * @throws IOException If the credentials.json file cannot be found.
   */
  private static Credential getCredentials(final NetHttpTransport httpTransport,
      InputStream credentialFile, String tokenDirectoryPath) throws IOException {

    GoogleClientSecrets clientSecrets =
        GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(credentialFile));

    // Build flow and trigger user authorization request.
    GoogleAuthorizationCodeFlow flow =
        new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
            .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(tokenDirectoryPath)))
            .setAccessType("offline").build();

    LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();

    // return the Credential object
    return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");

  }

  public static MediaType textFile() {
    return MediaType.PLAIN_TEXT_UTF_8;
  }


  DriveAppService(Drive service) {
    this.service = service;
  }

  /**
   * 
   * @return
   * @throws IOException
   */
  List<File> getFileNames() throws IOException {
    // If Scope is "APP_DATA", then set call setSpaces("appDataFolder")
    FileList result = service.files().list()//
        .setQ("trashed=false")//
        .setPageSize(10) //
        .setFields("nextPageToken, files(" + DRIVE_RETURN_FIELDS + ")")//
        .execute();
    return result.getFiles();
  }

  /**
   * 
   * @param folderName
   * @param parentId
   * @return
   * @throws IOException
   */
  public File createFolder(String folderName, String parentId) throws IOException {
    File fileMetadata = new File();
    fileMetadata.setName(folderName);
    fileMetadata.setMimeType(DRIVE_FOLDER_MEDIA_TYPE);

    if (parentId != null)
      fileMetadata.setParents(Arrays.asList(parentId));

    return service.files().create(fileMetadata).setFields(DRIVE_RETURN_FIELDS).execute();
  }

  /**
   * 
   * @param fileContent
   * @param fileName
   * @param parentId
   * @param mediaType
   * @return
   * @throws IOException
   */
  public File uploadFile(java.io.File fileContent, String fileName, String parentId,
      MediaType mediaType) throws IOException {

    FileContent mediaContent = new FileContent(mediaType.toString(), fileContent);
    File fileMetadata = new File();
    fileMetadata.setName(fileName);

    if (parentId != null)
      fileMetadata.setParents(Arrays.asList(parentId));

    return service.files().create(fileMetadata, mediaContent).setFields(DRIVE_RETURN_FIELDS)
        .execute();
  }

  /**
   * 
   * @param fileContent
   * @param fileId
   * @param mediaType
   * @return
   * @throws IOException
   */
  public File uploadFile(java.io.File fileContent, String fileId, MediaType mediaType)
      throws IOException {
    File file = new File();
    FileContent mediaContent = new FileContent(mediaType.toString(), fileContent);
    return service.files().update(fileId, file, mediaContent).execute();
  }

  public void downloadFile(String fileId, java.io.File outFile) throws IOException {

    if (!outFile.exists() && !outFile.createNewFile())
      throw new IllegalArgumentException(
          String.format("File[%s] does not exist, but not able to create new it.", outFile));

    try (FileOutputStream outStream = new FileOutputStream(outFile)) {
      service.files().get(fileId).executeMediaAndDownloadTo(outStream);
      outStream.flush();
    }
  }

}
