package com.portal.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "project_details")
public class ProjectDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectId;

    @Column(length = 150, nullable = false)
    private String projectName;

    @Column(columnDefinition = "TEXT")
    private String description;

    private LocalDate startDate;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private Status status;

    @Column(length = 255)
    private String technologiesUsed;

    @ManyToOne
    @JoinColumn(name = "priority_id")
    private Priority priority;

    // Relationships
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private TechnicalUser createdBy;

    @ManyToOne
    @JoinColumn(name = "project_manager_id")
    private TechnicalUser projectManager;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RequestLetter> requestLetters;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectProposal> proposals;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AcceptanceLetter> acceptanceLetters;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ResourceAssignment> resourceAssignments;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UATLetter> userAcceptanceTests;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectSignoff> projectSignoffs;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectManual> manuals;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Hosting> hostings;
    
    @ManyToOne
    @JoinColumn(name = "hoo_id")
    private TechnicalUser hoo;

    @ManyToOne
    @JoinColumn(name = "hod_id")
    private TechnicalUser hod;

    @ManyToOne
    @JoinColumn(name = "assisting_officer_id")
    private TechnicalUser assistingOfficer;

    public ProjectDetails() {}

	public ProjectDetails(Long projectId, String projectName, String description, LocalDate startDate, Status status,
                          String technologiesUsed, Priority priority, Department department, Category category,
                          TechnicalUser createdBy, TechnicalUser projectManager, List<RequestLetter> requestLetters,
                          List<ProjectProposal> proposals, List<AcceptanceLetter> acceptanceLetters,
                          List<ResourceAssignment> resourceAssignments, List<UATLetter> userAcceptanceTests,
                          List<ProjectSignoff> projectSignoffs, List<ProjectManual> manuals, List<Hosting> hostings) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.description = description;
        this.startDate = startDate;
        this.status = status;
        this.technologiesUsed = technologiesUsed;
        this.priority = priority;
        this.department = department;
        this.category = category;
        this.createdBy = createdBy;
        this.projectManager = projectManager;
        this.requestLetters = requestLetters;
        this.proposals = proposals;
        this.acceptanceLetters = acceptanceLetters;
        this.resourceAssignments = resourceAssignments;
        this.userAcceptanceTests = userAcceptanceTests;
        this.projectSignoffs = projectSignoffs;
        this.manuals = manuals;
        this.hostings = hostings;
    }

	public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    

    public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getTechnologiesUsed() {
        return technologiesUsed;
    }

    public void setTechnologiesUsed(String technologiesUsed) {
        this.technologiesUsed = technologiesUsed;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }


	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public TechnicalUser getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(TechnicalUser createdBy) {
        this.createdBy = createdBy;
    }

    public TechnicalUser getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(TechnicalUser projectManager) {
        this.projectManager = projectManager;
    }

    public List<RequestLetter> getRequestLetters() {
        return requestLetters;
    }

    public void setRequestLetters(List<RequestLetter> requestLetters) {
        this.requestLetters = requestLetters;
    }

    public List<ProjectProposal> getProposals() {
        return proposals;
    }

    public void setProposals(List<ProjectProposal> proposals) {
        this.proposals = proposals;
    }

    public List<AcceptanceLetter> getAcceptanceLetters() {
        return acceptanceLetters;
    }

    public void setAcceptanceLetters(List<AcceptanceLetter> acceptanceLetters) {
        this.acceptanceLetters = acceptanceLetters;
    }

    public List<ResourceAssignment> getResourceAssignments() {
        return resourceAssignments;
    }

    public void setResourceAssignments(List<ResourceAssignment> resourceAssignments) {
        this.resourceAssignments = resourceAssignments;
    }

    public List<UATLetter> getUserAcceptanceTests() {
        return userAcceptanceTests;
    }

    public void setUserAcceptanceTests(List<UATLetter> userAcceptanceTests) {
        this.userAcceptanceTests = userAcceptanceTests;
    }

    public List<ProjectSignoff> getProjectSignoffs() {
        return projectSignoffs;
    }

    public void setProjectSignoffs(List<ProjectSignoff> projectSignoffs) {
        this.projectSignoffs = projectSignoffs;
    }

    public List<ProjectManual> getManuals() {
        return manuals;
    }

    public void setManuals(List<ProjectManual> manuals) {
        this.manuals = manuals;
    }

    public List<Hosting> getHostings() {
        return hostings;
    }

    public void setHostings(List<Hosting> hostings) {
        this.hostings = hostings;
    }

	public TechnicalUser getHoo() {
		return hoo;
	}

	public void setHoo(TechnicalUser hoo) {
		this.hoo = hoo;
	}

	public TechnicalUser getHod() {
		return hod;
	}

	public void setHod(TechnicalUser hod) {
		this.hod = hod;
	}

	public TechnicalUser getAssistingOfficer() {
		return assistingOfficer;
	}

	public void setAssistingOfficer(TechnicalUser assistingOfficer) {
		this.assistingOfficer = assistingOfficer;
	}
}
