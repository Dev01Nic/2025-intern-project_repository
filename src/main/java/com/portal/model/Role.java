package com.portal.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    @Column(length = 50, nullable = false, unique = true)
    private String roleName;

    // Example relationship
    @OneToMany(mappedBy = "role")
    private List<TechnicalUser> users;

    // ✅ Default constructor required by Hibernate
    public Role() {
    }

    // Optional: full constructor
    public Role(Long roleId, String roleName, List<TechnicalUser> users) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.users = users;
    }

    // Getters and setters
    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public List<TechnicalUser> getUsers() {
        return users;
    }

    public void setUsers(List<TechnicalUser> users) {
        this.users = users;
    }
}
