package dev.thiagooliveira.poc_autofill_processor.infrastructure.database.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "user_entity")
public class UserEntity {
    @Id
    private String username;
    private String name;
    private String email;
    private String password;
    private String role;
    @Column(name = "balance")
    private int limit;
    private String commercialCode;
    private String teamName;
    @Version
    private int version;

    public UserEntity(String name, String username, String email, String password, String role, int limit, String commercialCode, String teamName, int version) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.limit = limit;
        this.commercialCode = commercialCode;
        this.teamName = teamName;
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

    public String getCommercialCode() {
        return commercialCode;
    }

    public void setCommercialCode(String commercialCode) {
        this.commercialCode = commercialCode;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
