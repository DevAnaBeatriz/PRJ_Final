package com.modulo2.loginapp.config;

import com.modulo2.loginapp.adapter.persistence.JpaUserRepository;
import com.modulo2.loginapp.domain.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class DataSeed {

    @Bean
    public CommandLineRunner seed(JpaUserRepository repo, BCryptPasswordEncoder encoder) {
        return args -> {
            // seed default user aluno / senha123 (bcrypt)
            var maybe = repo.findByUsername("aluno");
            if (maybe.isEmpty()) {
                String hash = encoder.encode("senha123");
                repo.save(new User(null, "aluno", hash, true));
            }
        };
    }
}
