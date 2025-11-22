package com.modulo2.loginapp.port;

import com.modulo2.loginapp.domain.User;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findByUsername(String username);
    User save(User user);
}
