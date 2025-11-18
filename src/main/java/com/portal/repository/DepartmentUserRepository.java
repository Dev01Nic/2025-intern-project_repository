package com.portal.repository;

import com.portal.model.DepartmentUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DepartmentUserRepository extends JpaRepository<DepartmentUser, Long> {
	DepartmentUser findByUsername(String username);
}
