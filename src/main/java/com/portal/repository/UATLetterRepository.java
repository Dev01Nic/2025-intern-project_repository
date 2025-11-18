package com.portal.repository;

import com.portal.model.ProjectDetails;
import com.portal.model.UATLetter;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UATLetterRepository extends JpaRepository<UATLetter, Long> {
	List<UATLetter> findByProject(ProjectDetails project);
}
