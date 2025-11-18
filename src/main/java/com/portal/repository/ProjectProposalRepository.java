package com.portal.repository;

import com.portal.model.ProjectProposal;
import com.portal.model.ProjectDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProjectProposalRepository extends JpaRepository<ProjectProposal, Long> {
    List<ProjectProposal> findByProject(ProjectDetails project);
}
