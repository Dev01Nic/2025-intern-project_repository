package com.portal.dto;

import java.util.List;

public class AoDto {
    private Long id;
    private String name;
    private String email;
    private String username;
    private String phoneNo;
    private String designation;
    List<Long> departmentIds;
    private Long roleId;

    public AoDto() {}

    public AoDto(Long id, String name, String email, String username, String phoneNo, String designation,
    		List<Long> departmentIds, Long roleId) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.username = username;
        this.phoneNo = phoneNo;
        this.designation = designation;
        this.departmentIds = departmentIds;
        this.roleId = roleId;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

  
    public List<Long> getDepartmentIds() {
		return departmentIds;
	}

	public void setDepartmentIds(List<Long> departmentIds) {
		this.departmentIds = departmentIds;
	}

	public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
