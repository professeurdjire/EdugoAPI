package com.example.edugo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.PostConstruct;

/**
 * Configuration class for development mode
 * This class is active only when the "dev" profile is active
 */
@Configuration
@Profile("dev")
public class DevModeConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(DevModeConfig.class);
    
    @PostConstruct
    public void init() {
        logger.info("===========================================");
        logger.info("DEVELOPMENT MODE ACTIVE - AUTHENTICATION BYPASSED");
        logger.info("===========================================");
        System.setProperty("DEV_MODE", "true");
    }
}