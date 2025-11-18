package com.portal.repository;

import com.portal.model.ProjectDetails;
import com.portal.model.ResourceAssignment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceAssignmentRepository extends JpaRepository<ResourceAssignment, Long> {
	List<ResourceAssignment> findByProject(ProjectDetails project);
	
}
