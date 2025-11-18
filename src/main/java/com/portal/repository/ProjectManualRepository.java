package com.portal.repository;

import com.portal.model.ProjectDetails;
import com.portal.model.ProjectManual;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectManualRepository extends JpaRepository<ProjectManual, Long> {
	List<ProjectManual> findByProject(ProjectDetails project);
}
