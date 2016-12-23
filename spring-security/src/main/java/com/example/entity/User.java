package com.example.entity;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.*;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "{error.user.username.null}")
    @NotEmpty(message = "{error.user.username.empty}")
    @Size(max = 50, message = "{error.user.username.max}")
    @Column(length = 50)
    private String userName;

    @NotNull(message = "{error.user.password.null}")
    @NotEmpty(message = "{error.user.password.empty}")
    @Size(max = 50, message = "{error.user.password.max}")
    @Column(length = 50)
    private String password;
    private String email;
    private boolean enabled;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")}
    )
    private Set<Role> roles;

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("User{");
        sb.append("id=").append(id);
        sb.append(", username='").append(userName).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", enabled=").append(enabled);
        sb.append(", roles=").append(roles);
        sb.append('}');
        return sb.toString();
    }

    @Transient
    private Set<Permission> getPermissions() {
        Set<Permission> perms = new HashSet<Permission>();
        for (Role role : roles) {
            perms.addAll(role.getPermissions());
        }
        return perms;
    }

    @Override
    @Transient
    public Collection<GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
        authorities.addAll(getRoles());
        authorities.addAll(getPermissions());
        return authorities;
    }

}