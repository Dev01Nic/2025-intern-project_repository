package com.portal.controller;

import com.portal.model.ProjectDetails;
import com.portal.model.TechnicalUser;
import com.portal.repository.CategoryRepository;
import com.portal.repository.DepartmentRepository;
import com.portal.repository.ProjectDetailsRepository;
import com.portal.repository.RoleRepository;
import com.portal.repository.StatusRepository;
import com.portal.repository.TechnicalUserRepository;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/hoo")
public class HooController {

    private final ProjectDetailsRepository projectRepository;
    private final TechnicalUserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final RoleRepository roleRepository;
    private final CategoryRepository categoryRepository;
    private final StatusRepository statusRepository;
    
    public HooController(TechnicalUserRepository userRepository,
            DepartmentRepository departmentRepository,
            RoleRepository roleRepository,
            ProjectDetailsRepository projectRepository,
            CategoryRepository categoryRepository,
            StatusRepository statusRepository) {
this.projectRepository = projectRepository;
this.userRepository = userRepository;
this.departmentRepository = departmentRepository;
this.roleRepository = roleRepository;
this.categoryRepository = categoryRepository;
this.statusRepository = statusRepository;
}

    

    @GetMapping
    public String hooDashboard(Model model, Authentication authentication) {
        String username = authentication.getName();
        TechnicalUser hoo = userRepository.findByUsername(username);

        if (hoo == null) {
            model.addAttribute("error", "Head of Office not found!");
            return "error";
        }

        model.addAttribute("hooName", hoo.getName());

        // ✅ Fetch all projects
        List<ProjectDetails> projects = projectRepository.findAll();

        // ✅ Totals for dashboard
        long totalProjects = projectRepository.count();
        long totalDepartments = departmentRepository.count();
        long totalHods = userRepository.findByRole_RoleName("Head Of Department").size();
        long totalAos = userRepository.findByRole_RoleName("Assisting Officer").size();
        long totalDevelopers = userRepository.findByRole_RoleName("Developer").size();

        // ✅ Add all attributes to the model
        model.addAttribute("projects", projects);
        model.addAttribute("totalProjects", totalProjects);
        model.addAttribute("totalDepartments", totalDepartments);
        model.addAttribute("totalHods", totalHods);
        model.addAttribute("totalAos", totalAos);
        model.addAttribute("totalDevelopers", totalDevelopers);

        return "hoo"; // your thymeleaf dashboard page
    }

    @GetMapping("/projects/list")
    public String projectList(@RequestParam(required = false) Long departmentId,
                             @RequestParam(required = false) Long categoryId,
                             @RequestParam(required = false) Long statusId,
                             Model model,
                             Authentication authentication) {

        String username = authentication.getName();
        TechnicalUser hoo = userRepository.findByUsername(username);

        if (hoo == null) {
            model.addAttribute("error", "Head of Operations not found!");
            return "error";
        }

        model.addAttribute("hooName", hoo.getName());

        // ✅ Start with all projects
        List<ProjectDetails> projects = projectRepository.findAll();

        // ✅ Populate dropdowns with all available options
        model.addAttribute("projects", projects);
        model.addAttribute("departments", departmentRepository.findAll());
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("statuses", statusRepository.findAll());

        // ✅ Keep selected filters in the view
        model.addAttribute("selectedDepartment", departmentId);
        model.addAttribute("selectedCategory", categoryId);
        model.addAttribute("selectedStatus", statusId);

        return "projects_list";
    }
    @GetMapping("/create")
    public String createHooForm(Model model) {
        model.addAttribute("hooUser", new TechnicalUser());
        model.addAttribute("departments", departmentRepository.findAll());
        model.addAttribute("roles", roleRepository.findAll());
        return "fragments/hoo_form_fragment :: hooFormFragment";  // <-- make hoo_form.html or reuse fragment in wrapper
    }
    @PostMapping("/save")
    public String saveHoo(@ModelAttribute("hooUser") TechnicalUser user) {
        userRepository.save(user);
        return "redirect:/hoo"; // back to dashboard
    }
    @GetMapping("/view/{id}")
    public String viewHooUser(@PathVariable("id") Long id, Model model) {
    	TechnicalUser user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        model.addAttribute("hooUser", user);
        return "hoo_user_view"; // Thymeleaf template name
    }
}