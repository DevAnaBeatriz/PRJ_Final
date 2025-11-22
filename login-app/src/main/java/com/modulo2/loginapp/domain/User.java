package com.modulo2.loginapp.domain;

public class User {
    private Long id;
    private String username;
    private String passwordHash;
    private boolean active = true;

    public User() {}

    public User(Long id, String username, String passwordHash, boolean active) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.active = active;
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    public boolean isActive() { return active; }
}
