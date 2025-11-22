package com.modulo2.loginapp.usecase;

import com.modulo2.loginapp.port.UserRepository;
import com.modulo2.loginapp.domain.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {

    @Test
    void authenticate_success() {
        UserRepository repo = Mockito.mock(UserRepository.class);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = encoder.encode("senha123");
        Mockito.when(repo.findByUsername("aluno")).thenReturn(Optional.of(new User(1L, "aluno", hash, true)));

        AuthService service = new AuthService(repo, encoder, "12345678901234567890123456789012", 3600000);
        var tokenOpt = service.authenticate("aluno", "senha123");
        assertTrue(tokenOpt.isPresent());
    }

    @Test
    void authenticate_fail_bad_password() {
        UserRepository repo = Mockito.mock(UserRepository.class);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = encoder.encode("outrasenha");
        Mockito.when(repo.findByUsername("aluno")).thenReturn(Optional.of(new User(1L, "aluno", hash, true)));

        AuthService service = new AuthService(repo, encoder, "12345678901234567890123456789012", 3600000);
        var tokenOpt = service.authenticate("aluno", "senha123");
        assertFalse(tokenOpt.isPresent());
    }
}
