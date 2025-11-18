package com.portal.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @Column(length = 100, nullable = false, unique = true)
    private String categoryName;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectDetails> projects;
    
    public Category() {
    }

	public Category(Long categoryId, String categoryName, List<ProjectDetails> projects) {
		super();
		this.categoryId = categoryId;
		this.categoryName = categoryName;
		this.projects = projects;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public List<ProjectDetails> getProjects() {
		return projects;
	}

	public void setProjects(List<ProjectDetails> projects) {
		this.projects = projects;
	}
}
