package com.portal.repository;

import com.portal.model.IssueTracking;
import com.portal.model.ProjectDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IssueTrackingRepository extends JpaRepository<IssueTracking, Long> {
    List<IssueTracking> findByProject(ProjectDetails project);
    List<IssueTracking> findByProjectIn(List<ProjectDetails> projects);
}
