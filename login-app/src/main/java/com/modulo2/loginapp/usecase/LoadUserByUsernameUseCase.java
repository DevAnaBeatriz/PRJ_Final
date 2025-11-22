package com.modulo2.loginapp.usecase;

import com.modulo2.loginapp.port.UserRepository;
import com.modulo2.loginapp.web.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoadUserByUsernameUseCase {

    private final UserRepository userRepository;

    public UserDetails load(String username) {
        return userRepository.findByUsername(username)
                .map(UserPrincipal::new)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
