package com.portal.model;

import jakarta.persistence.*;

@Entity
@Table(name = "hosting")
public class Hosting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hostingId;

    @Column(length = 50)
    private String hostingType;

    @Column(length = 50)
    private String description;

    @Column(length = 45)
    private String publicIp;

    @Column(length = 45)
    private String privateIp;

    @Column(length = 150)
    private String domainName;

    @Column(length = 100)
    private String operatingSystem;

    @Column(length = 255)
    private String serverSpecs;

    // Relationships
    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private ProjectDetails project;

    @ManyToOne
    @JoinColumn(name = "datacenter_id", nullable = false)
    private DataCenter dataCenter;
    
    public Hosting() {
    }

	public Hosting(Long hostingId, String hostingType, String description, String publicIp, String privateIp,
			String domainName, String operatingSystem, String serverSpecs, ProjectDetails project,
			DataCenter dataCenter) {
		super();
		this.hostingId = hostingId;
		this.hostingType = hostingType;
		this.description = description;
		this.publicIp = publicIp;
		this.privateIp = privateIp;
		this.domainName = domainName;
		this.operatingSystem = operatingSystem;
		this.serverSpecs = serverSpecs;
		this.project = project;
		this.dataCenter = dataCenter;
	}

	public Long getHostingId() {
		return hostingId;
	}

	public void setHostingId(Long hostingId) {
		this.hostingId = hostingId;
	}

	public String getHostingType() {
		return hostingType;
	}

	public void setHostingType(String hostingType) {
		this.hostingType = hostingType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPublicIp() {
		return publicIp;
	}

	public void setPublicIp(String publicIp) {
		this.publicIp = publicIp;
	}

	public String getPrivateIp() {
		return privateIp;
	}

	public void setPrivateIp(String privateIp) {
		this.privateIp = privateIp;
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public String getOperatingSystem() {
		return operatingSystem;
	}

	public void setOperatingSystem(String operatingSystem) {
		this.operatingSystem = operatingSystem;
	}

	public String getServerSpecs() {
		return serverSpecs;
	}

	public void setServerSpecs(String serverSpecs) {
		this.serverSpecs = serverSpecs;
	}

	public ProjectDetails getProject() {
		return project;
	}

	public void setProject(ProjectDetails project) {
		this.project = project;
	}

	public DataCenter getDataCenter() {
		return dataCenter;
	}

	public void setDataCenter(DataCenter dataCenter) {
		this.dataCenter = dataCenter;
	}
}
