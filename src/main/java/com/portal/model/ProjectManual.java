package com.portal.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "project_manual")
public class ProjectManual {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20)
    private String version;

    @Column(length = 150)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private LocalDate submittedDate;
    private LocalDate approvedDate;

    @Column(length = 255)
    private String filePath;

    @Column(length = 20, nullable = false)
    private String status;   // ✅ Added status field (Draft, Submitted, Approved, Rejected, etc.)

    // Relationships
    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private ProjectDetails project;

    @ManyToOne
    @JoinColumn(name = "submitted_by", nullable = false)
    private TechnicalUser submittedBy;

    @ManyToOne
    @JoinColumn(name = "approved_by")
    private TechnicalUser approvedBy; // kept nullable in case not yet approved

    public ProjectManual() {}

    public ProjectManual(Long id, String version, String title, String description,
                         LocalDate submittedDate, LocalDate approvedDate, String filePath,
                         String status, ProjectDetails project, TechnicalUser submittedBy, TechnicalUser approvedBy) {
        this.id = id;
        this.version = version;
        this.title = title;
        this.description = description;
        this.submittedDate = submittedDate;
        this.approvedDate = approvedDate;
        this.filePath = filePath;
        this.status = status;
        this.project = project;
        this.submittedBy = submittedBy;
        this.approvedBy = approvedBy;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getSubmittedDate() { return submittedDate; }
    public void setSubmittedDate(LocalDate submittedDate) { this.submittedDate = submittedDate; }

    public LocalDate getApprovedDate() { return approvedDate; }
    public void setApprovedDate(LocalDate approvedDate) { this.approvedDate = approvedDate; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public ProjectDetails getProject() { return project; }
    public void setProject(ProjectDetails project) { this.project = project; }

    public TechnicalUser getSubmittedBy() { return submittedBy; }
    public void setSubmittedBy(TechnicalUser submittedBy) { this.submittedBy = submittedBy; }

    public TechnicalUser getApprovedBy() { return approvedBy; }
    public void setApprovedBy(TechnicalUser approvedBy) { this.approvedBy = approvedBy; }
}
