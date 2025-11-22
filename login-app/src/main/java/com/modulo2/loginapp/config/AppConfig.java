package com.modulo2.loginapp.config;

import com.modulo2.loginapp.usecase.AuthService;
import com.modulo2.loginapp.port.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class AppConfig {

    @Value("${JWT_SECRET:#{null}}")
    private String jwtSecret;

    @Value("${jwt.expiration-ms:3600000}")
    private long jwtExpirationMs;

    @Bean
    public AuthService authService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        String secret = jwtSecret != null ? jwtSecret : System.getenv().getOrDefault("JWT_SECRET", "defaultsecretdefaultsecret");
        return new AuthService(userRepository, passwordEncoder, secret, jwtExpirationMs);
    }
}
