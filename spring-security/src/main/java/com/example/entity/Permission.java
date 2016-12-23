package com.example.entity;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Table(name = "permissions")
public class Permission implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "{error.permission.permissionName.null}")
    @NotEmpty(message = "{error.permission.permissionName.empty}")
    @Size(max = 50, message = "{permission.permissionName.max}")
    @Column(length = 50)
    private String permissionName;

    @NotNull(message = "{error.permission.description.null}")
    @NotEmpty(message = "{error.permission.description.empty}")
    @Size(max = 50, message = "{permission.description.max}")
    @Column(length = 50)
    private String description;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "role_permissions",
            joinColumns = {@JoinColumn(name = "permission_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")}
    )
    private Set<Role> permRoles;

    @Override
    public String getAuthority() {
        return permissionName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Role> getPermRoles() {
        return permRoles;
    }

    public void setPermRoles(Set<Role> permRoles) {
        this.permRoles = permRoles;
    }
}