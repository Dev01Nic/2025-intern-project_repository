package com.portal.controller;

import com.portal.model.ProjectManual;
import com.portal.model.ProjectDetails;
import com.portal.model.TechnicalUser;
import com.portal.repository.ProjectManualRepository;
import com.portal.repository.ProjectDetailsRepository;
import com.portal.repository.TechnicalUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/manuals")
public class ProjectManualController {

    @Autowired
    private ProjectManualRepository projectManualRepository;

    @Autowired
    private ProjectDetailsRepository projectDetailsRepository;

    @Autowired
    private TechnicalUserRepository userRepository;

    // Show form
    @GetMapping("/new")
    public String showForm(Model model) {
        ProjectManual manual = new ProjectManual();
        List<ProjectDetails> projects = projectDetailsRepository.findAll();
        List<TechnicalUser> users = userRepository.findAll();

        model.addAttribute("manual", manual);
        model.addAttribute("projects", projects);
        model.addAttribute("users", users);

        return "project_manual";
    }

    // Save form
    @PostMapping("/save")
    public String saveManual(@ModelAttribute("manual") ProjectManual manual) {
        if (manual.getSubmittedDate() == null) {
            manual.setSubmittedDate(LocalDate.now());
        }
        projectManualRepository.save(manual);
        return "redirect:/manuals/list";
    }

    // Edit
    @GetMapping("/edit/{id}")
    public String editManual(@PathVariable Long id, Model model) {
        ProjectManual manual = projectManualRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Manual Id: " + id));
        model.addAttribute("manual", manual);
        model.addAttribute("projects", projectDetailsRepository.findAll());
        model.addAttribute("users", userRepository.findAll());
        return "project_manual";
    }
    // List all manuals
    @GetMapping("/list")
    public String listManuals(Model model) {
        List<ProjectManual> manuals = projectManualRepository.findAll();
        model.addAttribute("manuals", manuals);
        return "project_manual_list"; 
    }
    @GetMapping("/view/{id}")
    public String viewManual(@PathVariable Long id, Model model) {
        ProjectManual manual = projectManualRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Manual Id: " + id));

        model.addAttribute("manual", manual);
        return "project_manual_view"; // points to manual_view.html
    }
}
