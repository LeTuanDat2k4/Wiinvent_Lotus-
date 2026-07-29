package com.Wiinvent.Lotus.core.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "lotus")
public class LotusProperties {

    private Jwt jwt = new Jwt();
    private String internalApiKey;
    private Admin admin = new Admin();

    @Getter
    @Setter
    public static class Jwt {
        private String secret;
        private long accessTokenExpirationMs;
        private long refreshTokenExpirationMs;
    }

    @Getter
    @Setter
    public static class Admin {
        private String phone;
        private String displayName;
        private String defaultPassword;
    }
}
