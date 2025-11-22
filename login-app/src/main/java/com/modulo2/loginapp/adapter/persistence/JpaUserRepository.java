package com.modulo2.loginapp.adapter.persistence;

import com.modulo2.loginapp.domain.User;
import com.modulo2.loginapp.port.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JpaUserRepository implements UserRepository {

    private final SpringDataUserRepo repo;

    public JpaUserRepository(SpringDataUserRepo repo) {
        this.repo = repo;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return repo.findByUsername(username)
                .map(e -> new User(
                        e.getId(),
                        e.getUsername(),
                        e.getPasswordHash(),
                        e.isActive()
                ));
    }

    @Override
    public User save(User user) {
        UserEntity e = new UserEntity(
                user.getId(),
                user.getUsername(),
                user.getPasswordHash(),
                user.isActive()
        );
        UserEntity saved = repo.save(e);
        return new User(
                saved.getId(),
                saved.getUsername(),
                saved.getPasswordHash(),
                saved.isActive()
        );
    }
}
