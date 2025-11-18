package com.portal.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "resource_assignment")
public class ResourceAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate issuedDate;

    @Column(columnDefinition = "TEXT")
    private String make;

    @Column(length = 100)
    private String model;

    @Column(length = 50)
    private String serialNo;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 255)
    private String attachmentPath;

    // Relationships
    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private ProjectDetails project;

    @ManyToOne
    @JoinColumn(name = "assigned_to_id", nullable = false)
    private TechnicalUser assignedTo;

    public ResourceAssignment() {
    }

	public ResourceAssignment(Long id, LocalDate issuedDate, String make, String model, String serialNo,
			String description, String attachmentPath, ProjectDetails project, TechnicalUser assignedTo) {
		super();
		this.id = id;
		this.issuedDate = issuedDate;
		this.make = make;
		this.model = model;
		this.serialNo = serialNo;
		this.description = description;
		this.attachmentPath = attachmentPath;
		this.project = project;
		this.assignedTo = assignedTo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getIssuedDate() {
		return issuedDate;
	}

	public void setIssuedDate(LocalDate issuedDate) {
		this.issuedDate = issuedDate;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAttachmentPath() {
		return attachmentPath;
	}

	public void setAttachmentPath(String attachmentPath) {
		this.attachmentPath = attachmentPath;
	}

	public ProjectDetails getProject() {
		return project;
	}

	public void setProject(ProjectDetails project) {
		this.project = project;
	}

	public TechnicalUser getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(TechnicalUser assignedTo) {
		this.assignedTo = assignedTo;
	}
}
