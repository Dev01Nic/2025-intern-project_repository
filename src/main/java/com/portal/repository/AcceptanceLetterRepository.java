package com.portal.repository;

import com.portal.model.AcceptanceLetter;
import com.portal.model.ProjectDetails;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcceptanceLetterRepository extends JpaRepository<AcceptanceLetter, Long> {
	List<AcceptanceLetter> findByProject(ProjectDetails project);
}
