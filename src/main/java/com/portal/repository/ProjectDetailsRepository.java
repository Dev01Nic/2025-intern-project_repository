package com.portal.repository;

import com.portal.model.ProjectDetails;
import com.portal.model.Department;
import com.portal.model.TechnicalUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectDetailsRepository extends JpaRepository<ProjectDetails, Long> {

    List<ProjectDetails> findByDepartment(Department department);

        List<ProjectDetails> findByProjectManager(TechnicalUser projectManager);
      
        List<ProjectDetails> findByDepartmentIn(List<Department> departments);
        
}

