package model;

import java.sql.Timestamp;

import java.sql.Timestamp;

public class User {
    private int id;
    private String username;
    private String fullName;
    private String email;
    private String role;
    private String passwordHash; // mot de passe hashé
    private Timestamp createdAt;

    // Constructeur pour nouvel utilisateur (sans id)
    public User(String username, String fullName, String email, String role, String passwordHash) {
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
        this.passwordHash = passwordHash;
    }

    // Constructeur complet (extraction depuis DB)
    public User(int id, String username, String fullName, String email, String role,
                String passwordHash, Timestamp createdAt) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
        this.passwordHash = passwordHash;
        this.createdAt = createdAt;
    }

    // Getters et setters (omitted pour la concision, mais à créer)
    // ...

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public String getPasswordHash() { return passwordHash; }
    public Timestamp getCreatedAt() { return createdAt; }


    public void setId(int id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setEmail(String email) { this.email = email; }
    public void setRole(String role) { this.role = role; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

}




