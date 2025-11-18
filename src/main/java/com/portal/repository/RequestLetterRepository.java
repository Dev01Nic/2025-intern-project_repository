package com.portal.repository;

import com.portal.model.RequestLetter;
import com.portal.model.ProjectDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RequestLetterRepository extends JpaRepository<RequestLetter, Long> {
    List<RequestLetter> findByProject(ProjectDetails project);
}
