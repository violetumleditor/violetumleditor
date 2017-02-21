package com.horstmann.violet.framework.google.drive;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;

/**
 * This class can be used for connecting and uploading files to Google Drive.
 * There is no need to store or ask for credentials, because when you try to authorize,
 * a web browser will open with a Google Drive website and you will enter your credentials there.
 */
public class GoogleDriveAgent
{
    private static final String APPLICATION_NAME = "Violet UML Editor";
    private static final String FILE_TYPE_MIME = "text/html";
    private static final String CLIENT_SECRETS_RESOURCE_PATH = "/google/drive/client_secrets.json";
    private static final String USER_ID = "user";

    private final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private HttpTransport httpTransport;
    private Drive drive;

    /**
     * Saves a file from specific path to Google Drive
     *
     * @param filePath Path of file to save
     * @throws GeneralSecurityException Throws this exception if there's problem with security.
     *                                  Can be thrown by GoogleNetHttpTransport.newTrustedTransport()
     * @throws IOException              Throws this exception if there's a problem with IO operations for example
     *                                  the file path is wrong or there was an error while loading a client
     *                                  secrets file (client_secrets.json).
     */
    public void saveFile(final String filePath) throws GeneralSecurityException, IOException
    {
        if (filePath == null)
        {
            throw new IllegalArgumentException("File path can't be null");
        }

        final java.io.File file = new java.io.File(filePath);
        httpTransport = GoogleNetHttpTransport.newTrustedTransport();

        final Credential credential = authorize();
        drive = new Drive.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();

        uploadFile(file);
    }

    private Credential authorize() throws IOException
    {
        final GoogleClientSecrets clientSecrets = getClientSecrets();
        final GoogleAuthorizationCodeFlow authorizationCodeFlow = buildAuthorizationCodeFlow(clientSecrets);
        final AuthorizationCodeInstalledApp authorizationCode = new AuthorizationCodeInstalledApp(
                authorizationCodeFlow, new LocalServerReceiver());

        return authorizationCode.authorize(USER_ID);
    }

    private GoogleClientSecrets getClientSecrets() throws IOException
    {
        final InputStreamReader clientSecretsReader = new InputStreamReader(
                ClassLoader.class.getResourceAsStream(CLIENT_SECRETS_RESOURCE_PATH));

        return GoogleClientSecrets.load(JSON_FACTORY, clientSecretsReader);
    }

    private GoogleAuthorizationCodeFlow buildAuthorizationCodeFlow(final GoogleClientSecrets clientSecrets)
    {
        return new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets,
                Collections.singleton(DriveScopes.DRIVE_FILE))
                .build();
    }

    private void uploadFile(final java.io.File file) throws IOException
    {
        final File fileMetadata = new File();
        fileMetadata.setTitle(file.getName());

        final FileContent fileContent = new FileContent(FILE_TYPE_MIME, file);
        final Drive.Files.Insert insert = drive.files().insert(fileMetadata, fileContent);
        insert.execute();
    }
}
