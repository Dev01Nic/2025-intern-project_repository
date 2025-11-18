package com.portal.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.portal.dto.DeveloperDto;
import com.portal.model.TechnicalUser;
import com.portal.repository.TechnicalUserRepository;
import com.portal.repository.DepartmentRepository;
import com.portal.repository.RoleRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/developer")
public class DeveloperController {
	 
	    private final TechnicalUserRepository userRepository;

	    private final DepartmentRepository departmentRepository;

	    private final RoleRepository roleRepository;
	    
	    public DeveloperController(DepartmentRepository departmentRepository,
                TechnicalUserRepository userRepository,
                RoleRepository roleRepository) {
this.departmentRepository = departmentRepository;
this.userRepository = userRepository;
this.roleRepository = roleRepository;
}


    // Developer dashboard
    @GetMapping
    public String dashboard() {
        return "developer"; // developer.html in templates
    }

    // Create page
    @GetMapping("/create")
    public String createPage(Model model) {
        // add attributes if needed
    	model.addAttribute("developerUser", new com.portal.model.TechnicalUser()); 
        return "fragments/developer_form_fragment"; // developer_create.html in templates
    }
    // Save new developer
    @PostMapping("/create")
    public String saveDeveloper(@ModelAttribute("developerUser") TechnicalUser developerUser) {
        userRepository.save(developerUser);
        return "redirect:/developer"; 
    }

    // Edit page
    @GetMapping("/edit/{id}")
    public String editPage(@PathVariable Long id, Model model) {
    	TechnicalUser developer = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid developer Id:" + id));

        model.addAttribute("developerUser", developer);
        model.addAttribute("departments", departmentRepository.findAll());
        model.addAttribute("roles", roleRepository.findAll());
        return "developer_edit"; // 👈 create developer_edit.html
    }

    // Update developer
    @PostMapping("/update/{id}")
    public String updateDeveloper(@PathVariable Long id,
                                  @ModelAttribute("developerUser") TechnicalUser developerUser) {
    	TechnicalUser existing = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid developer Id:" + id));

        existing.setName(developerUser.getName());
        existing.setEmail(developerUser.getEmail());
        existing.setPhoneNo(developerUser.getPhoneNo());
        existing.setUsername(developerUser.getUsername());
        existing.setDesignation(developerUser.getDesignation());
        existing.setDepartment(developerUser.getDepartment());
        existing.setRole(developerUser.getRole());

        // update password only if filled
        if (developerUser.getPassword() != null && !developerUser.getPassword().isEmpty()) {
            existing.setPassword(developerUser.getPassword());
        }

        userRepository.save(existing);
        return "redirect:/developer"; 
    }
    @GetMapping("/details/{id}")
    @ResponseBody
    public DeveloperDto getDeveloperDetails(@PathVariable Long id) {
        TechnicalUser developer = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid developer Id:" + id));

        // Convert List<Department> to List<Long> departmentIds
        List<Long> deptIds = null;
        if (developer.getDepartment() != null) {
            deptIds = developer.getDepartment()
                               .stream()
                               .map(dept -> dept.getDeptId())
                               .toList(); // Java 16+, otherwise use Collectors.toList()
        }

        Long roleId = developer.getRole() != null ? developer.getRole().getRoleId() : null;

        return new DeveloperDto(
            developer.getId(),
            developer.getName(),
            developer.getEmail(),
            developer.getUsername(),
            developer.getPhoneNo(),
            developer.getDesignation(),
            deptIds, // ✅ now matches the DTO
            roleId
        );
    }


}
