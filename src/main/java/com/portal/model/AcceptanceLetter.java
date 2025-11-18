package com.portal.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "acceptance_letter")
public class AcceptanceLetter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200, nullable = false)
    private String subject;  // ✅ same as RequestLetter

    private LocalDate letterDate;  // ✅ unified field name

    @Column(length = 100)
    private String senderName;

    @Column(length = 50)
    private String senderDesignation;

    @Column(length = 100)
    private String senderEmail;

    @Column(length = 15)
    private String phoneNo;

    @Column(length = 255)
    private String senderAddress;

    @Column(length = 255)
    private String filePath;

    @Column(length = 20, nullable = false)
    private String status;   // ✅ NEW field (e.g. Pending, Approved, Rejected)

    // Relationships
    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private ProjectDetails project;

    @OneToOne
    @JoinColumn(name = "proposal_id", nullable = false)
    private ProjectProposal proposal;

    public AcceptanceLetter() {
    }

    public AcceptanceLetter(Long id, String subject, LocalDate letterDate,
                            String senderName, String senderDesignation,
                            String senderEmail, String phoneNo,
                            String senderAddress, String filePath,
                            String status, ProjectDetails project, ProjectProposal proposal) {
        this.id = id;
        this.subject = subject;
        this.letterDate = letterDate;
        this.senderName = senderName;
        this.senderDesignation = senderDesignation;
        this.senderEmail = senderEmail;
        this.phoneNo = phoneNo;
        this.senderAddress = senderAddress;
        this.filePath = filePath;
        this.status = status;
        this.project = project;
        this.proposal = proposal;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public LocalDate getLetterDate() { return letterDate; }
    public void setLetterDate(LocalDate letterDate) { this.letterDate = letterDate; }

    public String getSenderName() { return senderName; }
    public void setSenderName(String senderName) { this.senderName = senderName; }

    public String getSenderDesignation() { return senderDesignation; }
    public void setSenderDesignation(String senderDesignation) { this.senderDesignation = senderDesignation; }

    public String getSenderEmail() { return senderEmail; }
    public void setSenderEmail(String senderEmail) { this.senderEmail = senderEmail; }

    public String getPhoneNo() { return phoneNo; }
    public void setPhoneNo(String phoneNo) { this.phoneNo = phoneNo; }

    public String getSenderAddress() { return senderAddress; }
    public void setSenderAddress(String senderAddress) { this.senderAddress = senderAddress; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public ProjectDetails getProject() { return project; }
    public void setProject(ProjectDetails project) { this.project = project; }

    public ProjectProposal getProposal() { return proposal; }
    public void setProposal(ProjectProposal proposal) { this.proposal = proposal; }
}
