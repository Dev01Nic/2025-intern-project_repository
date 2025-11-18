package com.portal.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "project_proposal")
public class ProjectProposal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200, nullable = false)
    private String proposalTitle;

    private LocalDate proposalDate;

    @Column(length = 255)
    private String filePath;

    // Relationships
    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private ProjectDetails project;

    @ManyToOne
    @JoinColumn(name = "submitted_by", nullable = false)
    private TechnicalUser submittedBy;

    @OneToOne(mappedBy = "proposal", cascade = CascadeType.ALL, orphanRemoval = true)
    private AcceptanceLetter acceptanceLetter;

	public ProjectProposal(Long id, String proposalTitle, LocalDate proposalDate, String filePath,
			ProjectDetails project, TechnicalUser submittedBy, AcceptanceLetter acceptanceLetter) {
		super();
		this.id = id;
		this.proposalTitle = proposalTitle;
		this.proposalDate = proposalDate;
		this.filePath = filePath;
		this.project = project;
		this.submittedBy = submittedBy;
		this.acceptanceLetter = acceptanceLetter;
	}

	public ProjectProposal() {
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProposalTitle() {
		return proposalTitle;
	}

	public void setProposalTitle(String proposalTitle) {
		this.proposalTitle = proposalTitle;
	}

	public LocalDate getProposalDate() {
		return proposalDate;
	}

	public void setProposalDate(LocalDate proposalDate) {
		this.proposalDate = proposalDate;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public ProjectDetails getProject() {
		return project;
	}

	public void setProject(ProjectDetails project) {
		this.project = project;
	}

	public TechnicalUser getSubmittedBy() {
		return submittedBy;
	}

	public void setSubmittedBy(TechnicalUser submittedBy) {
		this.submittedBy = submittedBy;
	}

	public AcceptanceLetter getAcceptanceLetter() {
		return acceptanceLetter;
	}

	public void setAcceptanceLetter(AcceptanceLetter acceptanceLetter) {
		this.acceptanceLetter = acceptanceLetter;
	}
}
