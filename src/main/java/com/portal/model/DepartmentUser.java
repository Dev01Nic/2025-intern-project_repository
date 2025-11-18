package com.portal.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "department_users")
public class DepartmentUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 100, nullable = false, unique = true)
    private String email;

    @Column(length = 15)
    private String phoneNo;

    @Column(length = 50, nullable = false, unique = true)
    private String username;

    @Column(length = 255, nullable = false)
    private String password;

    @Column(length = 50)
    private String designation;

    @Column(length = 20) // active/inactive/other
    private String status;

    private LocalDate assigningDate;
    private LocalDate relievingDate;
   
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    // Department mapping (Many-to-Many)
    @ManyToMany
    @JoinTable(
        name = "department_user_mapping",
        joinColumns = @JoinColumn(name = "department_user_id"),
        inverseJoinColumns = @JoinColumn(name = "department_id")
    )
    private List<Department> departments;

    public DepartmentUser() {}

    public DepartmentUser(Long id, String name, String email, String phoneNo, String username,
                          String password, String designation, String status,Role role,
                          LocalDate assigningDate, LocalDate relievingDate,
                          List<Department> departments) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNo = phoneNo;
        this.username = username;
        this.password = password;
        this.designation = designation;
        this.status = status;
        this.role = role;
        this.assigningDate = assigningDate;
        this.relievingDate = relievingDate;
        this.departments = departments;
    }

	// ===== Getters & Setters =====
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNo() { return phoneNo; }
    public void setPhoneNo(String phoneNo) { this.phoneNo = phoneNo; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public Role getRole() { return role; }
	public void setRole(Role role) { this.role = role; }

	public LocalDate getAssigningDate() { return assigningDate; }
    public void setAssigningDate(LocalDate assigningDate) { this.assigningDate = assigningDate; }

    public LocalDate getRelievingDate() { return relievingDate; }
    public void setRelievingDate(LocalDate relievingDate) { this.relievingDate = relievingDate; }

    public List<Department> getDepartments() { return departments; }
    public void setDepartments(List<Department> departments) { this.departments = departments; }
}
