package com.hdda.configs;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import java.io.InputStream;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

/**
 *
 * @author mahai
 */
@Configuration
public class FirebaseConfig {
    
    @PostConstruct
    public void init() throws Exception {
        if (FirebaseApp.getApps().isEmpty()) {
            try (InputStream is = new ClassPathResource(
                    "firebase/traffic-b0217-firebase-adminsdk-fbsvc-ba797a1002.json").getInputStream()) {
                
                FirebaseOptions opts = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(is))
                        .setDatabaseUrl("https://traffic-b0217-default-rtdb.asia-southeast1.firebasedatabase.app")
                        .build();
                
                FirebaseApp.initializeApp(opts);
                System.out.println("✅ Firebase initialized successfully!");
                
            } catch (Exception e) {
                System.err.println("🔥 Firebase initialization FAILED: " + e.getMessage());
                e.printStackTrace();
                throw e; // Re-throw để Spring biết có lỗi
            }
        } else {
            System.out.println("ℹ️ Firebase app already initialized");
        }
    }
}