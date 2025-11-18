package com.portal.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "issue")
public class IssueTracking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relationship to ProjectDetails
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private ProjectDetails project;

    // Relationship to reporting user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_by", nullable = false)
    private DepartmentUser reportedBy;

    // Relationship to assigned user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to")
    private TechnicalUser assignedTo;

    @Column(length = 255, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    // Relationship to priority
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "priority_id", nullable = false)
    private Priority priority;

    // Relationship to status
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", nullable = false)
    private Status status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "due_date")
    private LocalDate dueDate;

    public IssueTracking() {
    }

	public IssueTracking(Long id, ProjectDetails project, DepartmentUser reportedBy, TechnicalUser assignedTo, String title, String description,
			Priority priority, Status status, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDate dueDate) {
		super();
		this.id = id;
		this.project = project;
		this.reportedBy = reportedBy;
		this.assignedTo = assignedTo;
		this.title = title;
		this.description = description;
		this.priority = priority;
		this.status = status;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.dueDate = dueDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ProjectDetails getProject() {
		return project;
	}

	public void setProject(ProjectDetails project) {
		this.project = project;
	}

	public DepartmentUser getReportedBy() {
		return reportedBy;
	}

	public void setReportedBy(DepartmentUser reportedBy) {
		this.reportedBy = reportedBy;
	}

	public TechnicalUser getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(TechnicalUser assignedTo) {
		this.assignedTo = assignedTo;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public LocalDate getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}
    
}