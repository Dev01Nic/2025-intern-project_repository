package com.portal.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "technical_users")
public class TechnicalUser {

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

    // Status: active/inactive/other
    @Column(length = 20)
    private String status;

    // Assigning and relieving dates
    private LocalDate assigningDate;
    private LocalDate relievingDate;

    // Relationships
    @ManyToMany
    @JoinTable(
        name = "technical_user_department", // Name of the join table
        joinColumns = @JoinColumn(name = "technical_user_id"),
        inverseJoinColumns = @JoinColumn(name = "department_id")
    )
    private List<Department> department;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @OneToMany(mappedBy = "createdBy")
    private List<ProjectDetails> createdProjects;

    @OneToMany(mappedBy = "projectManager")
    private List<ProjectDetails> managedProjects;

    public TechnicalUser() {}

    public TechnicalUser(Long id, String name, String email, String phoneNo, String username, String password,
                         String designation, String status, LocalDate assigningDate, LocalDate relievingDate,
                         Role role, List<ProjectDetails> createdProjects, List<ProjectDetails> managedProjects,List<Department> department) {
        super();
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNo = phoneNo;
        this.username = username;
        this.password = password;
        this.designation = designation;
        this.status = status;
        this.assigningDate = assigningDate;
        this.relievingDate = relievingDate;
        this.role = role;
        this.createdProjects = createdProjects;
        this.managedProjects = managedProjects;
        this.department = department;
    }

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
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

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getAssigningDate() {
        return assigningDate;
    }

    public void setAssigningDate(LocalDate assigningDate) {
        this.assigningDate = assigningDate;
    }

    public LocalDate getRelievingDate() {
        return relievingDate;
    }

    public void setRelievingDate(LocalDate relievingDate) {
        this.relievingDate = relievingDate;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<ProjectDetails> getCreatedProjects() {
        return createdProjects;
    }

    public void setCreatedProjects(List<ProjectDetails> createdProjects) {
        this.createdProjects = createdProjects;
    }

    public List<ProjectDetails> getManagedProjects() {
        return managedProjects;
    }

    public void setManagedProjects(List<ProjectDetails> managedProjects) {
        this.managedProjects = managedProjects;
    }

	public List<Department> getDepartment() {
		return department;
	}

	public void setDepartment(List<Department> department) {
		this.department = department;
	}

   
}
