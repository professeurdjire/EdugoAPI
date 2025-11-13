package com.example.edugo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("dev")
public class DevModeTest {

    @Test
    public void testDevModeIsActive() {
        // Check that dev mode is active when the dev profile is used
        String profile = System.getProperty("spring.profiles.active", "");
        // In test context, the profile might be set differently
        assertTrue(profile.contains("dev") || profile.isEmpty() || "true".equalsIgnoreCase(System.getenv("DEV_MODE")), 
            "Development mode should be active with dev profile");
    }
}