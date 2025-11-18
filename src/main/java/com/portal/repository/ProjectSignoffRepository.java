package com.portal.repository;

import com.portal.model.ProjectDetails;
import com.portal.model.ProjectSignoff;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectSignoffRepository extends JpaRepository<ProjectSignoff, Long> {
	List<ProjectSignoff> findByProject(ProjectDetails project);
}
