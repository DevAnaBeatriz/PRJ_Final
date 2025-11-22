package com.modulo2.loginapp.web;

import com.modulo2.loginapp.web.dto.LoginRequest;
import com.modulo2.loginapp.web.dto.LoginResponse;
import com.modulo2.loginapp.usecase.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return authService.authenticate(request.getUsername(), request.getPassword())
            .map(token -> ResponseEntity.ok(new LoginResponse("Bearer " + token)))
            .orElse(ResponseEntity.status(401).build());
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(@RequestHeader(name = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).build();
        }
        String token = authHeader.substring(7);
        // for simplicity return username from token
        try {
            String username = io.jsonwebtoken.Jwts.parserBuilder()
                .setSigningKey(io.jsonwebtoken.security.Keys.hmacShaKeyFor(System.getenv().getOrDefault("JWT_SECRET", "" ).getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
            return ResponseEntity.ok(java.util.Map.of("username", username));
        } catch (Exception e) {
            return ResponseEntity.status(401).build();
        }
    }
}
