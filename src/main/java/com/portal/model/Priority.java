package com.portal.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "priority")
public class Priority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long priorityId;

    @Column(length = 50, nullable = false, unique = true)
    private String priorityName;  // e.g., High, Medium, Low

    // Relationships
    @OneToMany(mappedBy = "priority", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectDetails> projects;

    public Priority() {}

	public Priority(Long priorityId, String priorityName, List<ProjectDetails> projects) {
		super();
		this.priorityId = priorityId;
		this.priorityName = priorityName;
		this.projects = projects;
	}

	public Long getPriorityId() {
		return priorityId;
	}

	public void setPriorityId(Long priorityId) {
		this.priorityId = priorityId;
	}

	public String getPriorityName() {
		return priorityName;
	}

	public void setPriorityName(String priorityName) {
		this.priorityName = priorityName;
	}

	public List<ProjectDetails> getProjects() {
		return projects;
	}

	public void setProjects(List<ProjectDetails> projects) {
		this.projects = projects;
	}
}
