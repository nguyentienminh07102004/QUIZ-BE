package com.ptitB22CN539.QuizRemake.Utils;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.ptitB22CN539.QuizRemake.Exception.DataInvalidException;
import com.ptitB22CN539.QuizRemake.Exception.ExceptionVariable;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.UUID;

public class FileGoogleDrive {
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final String CREDENTIALS_FILE_PATH = "/Cred.json";
    public static String uploadFileGoogleDrive(MultipartFile fileUpload) {
        try {
            Drive service = getDrive();
            File fileMetadata = new File();
            fileMetadata.setName(fileUpload.getOriginalFilename() + UUID.randomUUID());
            fileMetadata.setParents(Collections.singletonList("1LoYtDWuiNSGnMSyVatqRzg8g_osD8JOK"));
            java.io.File fileUploadMetadata = java.io.File.createTempFile(UUID.randomUUID() + "_", fileUpload.getOriginalFilename());
            fileUpload.transferTo(fileUploadMetadata);
            FileContent mediaContent = new FileContent("image/**", fileUploadMetadata);
            File file = service.files().create(fileMetadata, mediaContent)
                    .setFields("id, webContentLink, webViewLink, thumbnailLink")
                    .execute();
            System.out.println(file.getWebViewLink());
            System.out.println(file.getThumbnailLink());
            return file.getId();
        } catch (IOException e) {
            throw new DataInvalidException(ExceptionVariable.SERVER_ERROR);
        }
    }

    public static void deleteAvatar(String id) {
        try {
            Drive drive = getDrive();
            drive.files().delete(id).execute();
        } catch (IOException exception) {
            throw new DataInvalidException(ExceptionVariable.SERVER_ERROR);
        }
    }

    private static Drive getDrive() throws IOException {
        HttpRequestInitializer requestInitializer = getCredentials(new NetHttpTransport());
        return new Drive.Builder(new NetHttpTransport(),
                GsonFactory.getDefaultInstance(),
                requestInitializer)
                .setApplicationName("QUIZ")
                .build();
    }
    private static String getServiceAccountKey() {
        String currentDirectory = System.getProperty("user.dir");
        return Paths.get(currentDirectory, CREDENTIALS_FILE_PATH).toString();
    }

    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
            throws IOException {
        // Load client secrets.
        InputStream in = new FileInputStream(getServiceAccountKey());
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(GsonFactory.getDefaultInstance(), new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, GsonFactory.getDefaultInstance(), clientSecrets, DriveScopes.all())
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }
}
