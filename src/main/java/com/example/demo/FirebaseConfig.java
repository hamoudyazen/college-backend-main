package com.example.demo;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import java.io.FileInputStream;
import java.io.IOException;

public class FirebaseConfig {
    private static final String FIREBASE_CONFIG_PATH = "./serviceAccountKey.json";

    public static void init() throws IOException {
        FileInputStream serviceAccount = new FileInputStream(FIREBASE_CONFIG_PATH);

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        FirebaseApp.initializeApp(options);
    }
}
