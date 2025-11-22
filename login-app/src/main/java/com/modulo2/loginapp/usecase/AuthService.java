package com.modulo2.loginapp.usecase;

import com.modulo2.loginapp.port.UserRepository;
import com.modulo2.loginapp.domain.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.Optional;

public class AuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final String jwtSecret;
    private final long jwtExpirationMs;

    public AuthService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, String jwtSecret, long jwtExpirationMs) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtSecret = jwtSecret;
        this.jwtExpirationMs = jwtExpirationMs;
    }

    public Optional<String> authenticate(String username, String password) {
        return userRepository.findByUsername(username)
            .filter(User::isActive)
            .filter(u -> passwordEncoder.matches(password, u.getPasswordHash()))
            .map(u -> createToken(u));
    }

    private String createToken(User user) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + jwtExpirationMs);
        return Jwts.builder()
            .setSubject(user.getUsername())
            .setIssuedAt(now)
            .setExpiration(exp)
            .signWith(io.jsonwebtoken.security.Keys.hmacShaKeyFor(jwtSecret.getBytes()))
            .compact();
    }
}
