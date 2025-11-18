package com.portal.controller;

import com.portal.model.ResourceAssignment;
import com.portal.model.ProjectDetails;
import com.portal.model.TechnicalUser;
import com.portal.repository.ProjectDetailsRepository;
import com.portal.repository.ResourceAssignmentRepository;
import com.portal.repository.TechnicalUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/resources")
public class ResourceAssignmentController {

    @Autowired
    private ResourceAssignmentRepository resourceAssignmentRepository;

    @Autowired
    private ProjectDetailsRepository projectDetailsRepository;

    @Autowired
    private TechnicalUserRepository userRepository;

    // List all resources
    @GetMapping("/list")
    public String listResources(Model model) {
        List<ResourceAssignment> resources = resourceAssignmentRepository.findAll();
        model.addAttribute("resources", resources);
        return "resource_list";
    }

    // Show form to add new resource
    @GetMapping("/new")
    public String showForm(Model model) {
        ResourceAssignment resource = new ResourceAssignment();
        List<ProjectDetails> projects = projectDetailsRepository.findAll();
        List<TechnicalUser> users = userRepository.findAll();

        model.addAttribute("resource", resource);
        model.addAttribute("projects", projects);
        model.addAttribute("users", users);

        return "devices_form";
    }

    // Save new resource
    @PostMapping("/save")
    public String saveResource(@ModelAttribute("resource") ResourceAssignment resource) {
        if (resource.getIssuedDate() == null) {
            resource.setIssuedDate(LocalDate.now());
        }
        resourceAssignmentRepository.save(resource);
        return "redirect:/resources/list";
    }
    @GetMapping("/view/{id}")
    public String viewDevice(@PathVariable Long id, Model model) {
        ResourceAssignment device = resourceAssignmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid device ID: " + id));
        model.addAttribute("device", device);
        return "devices_view"; // Thymeleaf template name
    }
    @GetMapping("/edit/{id}")
    public String editResourceForm(@PathVariable Long id, Model model) {
        ResourceAssignment resource = resourceAssignmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Resource ID: " + id));
        model.addAttribute("assignment", resource);
        model.addAttribute("projects", projectDetailsRepository.findAll());
        model.addAttribute("users", userRepository.findAll());
        return "devices_form"; // Thymeleaf edit template
    }
}
