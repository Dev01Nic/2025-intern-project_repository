package com.portal.repository;

import com.portal.model.TechnicalUser;
import com.portal.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TechnicalUserRepository extends JpaRepository<TechnicalUser, Long> {

	TechnicalUser findByUsername(String username);
    

	@Query("SELECT DISTINCT u FROM TechnicalUser u JOIN u.department d WHERE d = :department")
	List<TechnicalUser> findByDepartment(@Param("department") Department department);

	@Query("SELECT DISTINCT u FROM TechnicalUser u JOIN u.department d " +
		       "WHERE d = :department AND u.role.roleName = :roleName")
		List<TechnicalUser> findByDepartmentAndRoleName(@Param("department") Department department,
		                                                @Param("roleName") String roleName);

    List<TechnicalUser> findByRole_RoleName(String roleName);
    
    // New queries to fetch users present in projects
    @Query("SELECT DISTINCT p.projectManager FROM ProjectDetails p WHERE p.projectManager.role.roleName = :roleName")
    List<TechnicalUser> findProjectManagersByRole(@Param("roleName") String roleName);

    @Query("SELECT DISTINCT p.createdBy FROM ProjectDetails p WHERE p.createdBy.role.roleName = :roleName")
    List<TechnicalUser> findCreatorsByRole(@Param("roleName") String roleName);

    // ✅ New method for multiple departments
    @Query("SELECT DISTINCT u FROM TechnicalUser u JOIN u.department d WHERE d IN :departments")
    List<TechnicalUser> findByDepartmentIn(@Param("departments") List<Department> departments);
    
}
