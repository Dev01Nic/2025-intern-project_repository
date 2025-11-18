package com.portal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.portal.model.Status;

@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {

}
