package com.skshazena.blogFinalProject.dtos;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;

/**
 *
 * @author Shazena Khan
 *
 * Date Created: Oct 18, 2020
 */
public class User {

    private int userId;

    @NotNull(message = "Username must be entered")
    @NotBlank(message = "Username must not be blank")
    @Size(max = 30, message = "Username must be less than 30 characters.")
    private String username;

    @NotNull(message = "Password must be entered")
    @NotBlank(message = "Password must not be blank")
    @Size(max = 100, message = "Password must be less than 100 characters.")
    private String password;

    private boolean enabled;

    private LocalDateTime lastLogin;

    @NotNull(message = "First Name must be entered")
    @NotBlank(message = "First Name must not be blank")
    @Size(max = 50, message = "First Name must be less than 50 characters.")
    private String firstName;

    @NotNull(message = "Last Name must be entered")
    @NotBlank(message = "Last Name must not be blank")
    @Size(max = 50, message = "Last Name must be less than 50 characters.")
    private String lastName;

    @Size(max = 255, message = "Photo file path must be less than 255 characters.")
    private String profilePicture;

    @NotNull(message = "User must be assigned one role")
    @Valid
    private Set<Role> roles;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin.withNano(0);
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin.withNano(0);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 11 * hash + this.userId;
        hash = 11 * hash + Objects.hashCode(this.username);
        hash = 11 * hash + Objects.hashCode(this.password);
        hash = 11 * hash + (this.enabled ? 1 : 0);
        hash = 11 * hash + Objects.hashCode(this.lastLogin);
        hash = 11 * hash + Objects.hashCode(this.firstName);
        hash = 11 * hash + Objects.hashCode(this.lastName);
        hash = 11 * hash + Objects.hashCode(this.profilePicture);
        hash = 11 * hash + Objects.hashCode(this.roles);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        if (this.userId != other.userId) {
            return false;
        }
        if (this.enabled != other.enabled) {
            return false;
        }
        if (!Objects.equals(this.username, other.username)) {
            return false;
        }
        if (!Objects.equals(this.password, other.password)) {
            return false;
        }
        if (!Objects.equals(this.firstName, other.firstName)) {
            return false;
        }
        if (!Objects.equals(this.lastName, other.lastName)) {
            return false;
        }
        if (!Objects.equals(this.profilePicture, other.profilePicture)) {
            return false;
        }
        if (!Objects.equals(this.lastLogin, other.lastLogin)) {
            return false;
        }
        if (!Objects.equals(this.roles, other.roles)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "User{" + "userId=" + userId + ", username=" + username + ", password=" + password + ", enabled=" + enabled + ", lastLogin=" + lastLogin + ", firstName=" + firstName + ", lastName=" + lastName + ", profilePicture=" + profilePicture + ", roles=" + roles + '}';
    }

}
