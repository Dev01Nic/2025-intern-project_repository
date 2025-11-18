package com.portal.dto;

import java.time.LocalDate;

public class RequestLetterDTO {
    private Long id;
    private String subject;
    private LocalDate letterDate;
    private String senderName;
    private String senderDesignation;
    private String senderEmail;
    private String phoneNo;
    private String senderAddress;
    private String filePath;
    private String status;
    private Long projectId;
    
    public RequestLetterDTO() {}
	public RequestLetterDTO(Long id, String subject, LocalDate letterDate, String senderName, String senderDesignation,
			String senderEmail, String phoneNo, String senderAddress, String filePath, String status, Long projectId) {
		super();
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
		this.projectId = projectId;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public LocalDate getLetterDate() {
		return letterDate;
	}
	public void setLetterDate(LocalDate letterDate) {
		this.letterDate = letterDate;
	}
	public String getSenderName() {
		return senderName;
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	public String getSenderDesignation() {
		return senderDesignation;
	}
	public void setSenderDesignation(String senderDesignation) {
		this.senderDesignation = senderDesignation;
	}
	public String getSenderEmail() {
		return senderEmail;
	}
	public void setSenderEmail(String senderEmail) {
		this.senderEmail = senderEmail;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getSenderAddress() {
		return senderAddress;
	}
	public void setSenderAddress(String senderAddress) {
		this.senderAddress = senderAddress;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Long getProjectId() {
		return projectId;
	}
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	} 
    
    
}
