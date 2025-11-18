package com.portal.config;

import com.portal.model.DepartmentUser;
import com.portal.model.TechnicalUser;
import com.portal.repository.DepartmentUserRepository;
import com.portal.repository.TechnicalUserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final TechnicalUserRepository technicalRepo;
    private final DepartmentUserRepository departmentRepo;

    public CustomUserDetailsService(TechnicalUserRepository technicalRepo,
                                    DepartmentUserRepository departmentRepo) {
        this.technicalRepo = technicalRepo;
        this.departmentRepo = departmentRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1️⃣ Check TechnicalUser
        TechnicalUser techUser = technicalRepo.findByUsername(username);
        if (techUser != null) {
            String roleName = techUser.getRole().getRoleName().replace(" ", "_").toUpperCase();
            return User.builder()
                    .username(techUser.getUsername())
                    .password(techUser.getPassword()) // BCrypt encoded
                    .roles(roleName)
                    .build();
        }

        // 2️⃣ Check DepartmentUser
        DepartmentUser deptUser = departmentRepo.findByUsername(username);
        		if (deptUser != null) {

        return User.builder()
                .username(deptUser.getUsername())
                .password(deptUser.getPassword()) // BCrypt or plain text (match your encoder)
                .roles("DEPARTMENT_USER")        // Must match SecurityConfig
                .build();
    }
        		throw new UsernameNotFoundException("User not found: " + username);
    }
}
