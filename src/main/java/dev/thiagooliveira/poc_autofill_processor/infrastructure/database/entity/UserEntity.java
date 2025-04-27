package dev.thiagooliveira.poc_autofill_processor.infrastructure.database.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "user_entity")
public class UserEntity {
    @Id
    private String username;
    private String password;
    private String role;
    @Column(name = "balance")
    private int limit;
    @Version
    private int version;

    public UserEntity(String username, String password, String role, int limit, int version) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.limit = limit;
        this.version = version;
    }

    public UserEntity() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
