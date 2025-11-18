package com.portal.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "data_center")
public class DataCenter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long datacenterId;

    @Column(length = 150, nullable = false)
    private String name;

    @Column(length = 255)
    private String location;

    @Column(length = 50)
    private String type;

    @Column(length = 100)
    private String capacity;

    @Column(length = 100)
    private String contactPerson;

    @Column(length = 15)
    private String contactNo;

    @Column(length = 100)
    private String email;

    @Column(length = 50)
    private String status;

    private LocalDate establishedDate;

    @Column(columnDefinition = "TEXT")
    private String description;

    // Relationships
    @OneToMany(mappedBy = "dataCenter", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Hosting> hostings;
    
    public DataCenter() {}

	public DataCenter(Long datacenterId, String name, String location, String type, String capacity,
			String contactPerson, String contactNo, String email, String status, LocalDate establishedDate,
			String description, List<Hosting> hostings) {
		super();
		this.datacenterId = datacenterId;
		this.name = name;
		this.location = location;
		this.type = type;
		this.capacity = capacity;
		this.contactPerson = contactPerson;
		this.contactNo = contactNo;
		this.email = email;
		this.status = status;
		this.establishedDate = establishedDate;
		this.description = description;
		this.hostings = hostings;
	}

	public Long getDatacenterId() {
		return datacenterId;
	}

	public void setDatacenterId(Long datacenterId) {
		this.datacenterId = datacenterId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCapacity() {
		return capacity;
	}

	public void setCapacity(String capacity) {
		this.capacity = capacity;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDate getEstablishedDate() {
		return establishedDate;
	}

	public void setEstablishedDate(LocalDate establishedDate) {
		this.establishedDate = establishedDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Hosting> getHostings() {
		return hostings;
	}

	public void setHostings(List<Hosting> hostings) {
		this.hostings = hostings;
	}
}
