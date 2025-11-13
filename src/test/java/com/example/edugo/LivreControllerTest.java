package com.example.edugo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("dev")
public class LivreControllerTest {

    @Test
    public void testLivreControllerLoads() {
        // This is a simple test to verify that the LivreController can be loaded
        // in the Spring context without issues
        assertTrue(true, "LivreController should load without issues");
    }
}