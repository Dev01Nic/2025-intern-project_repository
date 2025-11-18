package com.portal.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "department")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dept_id")
    private Long deptId;

    @Column(name = "dept_name", nullable = false, length = 100)
    private String deptName;

    @Column(name = "dept_address", length = 255)
    private String deptAddress;

    @Column(name = "dept_phone", length = 15)
    private String deptPhone;

    @Column(name = "dept_email", length = 100, unique = true)
    private String deptEmail;

    @Column(name = "hod_name", length = 100)
    private String hodName;

    @Column(name = "hod_phone_no", length = 15)
    private String hodPhoneNo;

    @Column(name = "hod_designation", length = 50)
    private String hodDesignation;

    @Column(name = "hod_email", length = 100)
    private String hodEmail;

    // Relationships
    @ManyToMany(mappedBy = "department")
    private List<TechnicalUser> users;

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectDetails> projects;

    // Constructors
    public Department() {}

    public Department(Long deptId, String deptName, String deptAddress, String deptPhone, String deptEmail,
                      String hodName, String hodPhoneNo, String hodDesignation, String hodEmail,
                      List<TechnicalUser> users, List<ProjectDetails> projects) {
        this.deptId = deptId;
        this.deptName = deptName;
        this.deptAddress = deptAddress;
        this.deptPhone = deptPhone;
        this.deptEmail = deptEmail;
        this.hodName = hodName;
        this.hodPhoneNo = hodPhoneNo;
        this.hodDesignation = hodDesignation;
        this.hodEmail = hodEmail;
        this.users = users;
        this.projects = projects;
    }

    // Getters and Setters
    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptAddress() {
        return deptAddress;
    }

    public void setDeptAddress(String deptAddress) {
        this.deptAddress = deptAddress;
    }

    public String getDeptPhone() {
        return deptPhone;
    }

    public void setDeptPhone(String deptPhone) {
        this.deptPhone = deptPhone;
    }

    public String getDeptEmail() {
        return deptEmail;
    }

    public void setDeptEmail(String deptEmail) {
        this.deptEmail = deptEmail;
    }

    public String getHodName() {
        return hodName;
    }

    public void setHodName(String hodName) {
        this.hodName = hodName;
    }

    public String getHodPhoneNo() {
        return hodPhoneNo;
    }

    public void setHodPhoneNo(String hodPhoneNo) {
        this.hodPhoneNo = hodPhoneNo;
    }

    public String getHodDesignation() {
        return hodDesignation;
    }

    public void setHodDesignation(String hodDesignation) {
        this.hodDesignation = hodDesignation;
    }

    public String getHodEmail() {
        return hodEmail;
    }

    public void setHodEmail(String hodEmail) {
        this.hodEmail = hodEmail;
    }

    public List<TechnicalUser> getUsers() {
        return users;
    }

    public void setUsers(List<TechnicalUser> users) {
        this.users = users;
    }

    public List<ProjectDetails> getProjects() {
        return projects;
    }

    public void setProjects(List<ProjectDetails> projects) {
        this.projects = projects;
    }
}
