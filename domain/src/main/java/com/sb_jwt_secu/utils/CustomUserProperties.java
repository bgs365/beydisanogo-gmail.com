package com.sb_jwt_secu.utils;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "custom.user")
public class CustomUserProperties {
    private String passwordTokenExpiration;

    public String getPasswordTokenExpiration() {
        return passwordTokenExpiration;
    }

    public void setPasswordTokenExpiration(String passwordTokenExpiration) {
        this.passwordTokenExpiration = passwordTokenExpiration;
    }
}