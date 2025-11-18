package com.portal.controller;

import com.portal.model.ProjectSignoff;
import com.portal.model.ProjectDetails;
import com.portal.repository.ProjectSignoffRepository;
import com.portal.repository.ProjectDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/signoffs")
public class ProjectSignoffController {

    @Autowired
    private ProjectSignoffRepository projectSignoffRepository;

    @Autowired
    private ProjectDetailsRepository projectDetailsRepository;

    // Show form
    @GetMapping("/new")
    public String showForm(Model model) {
        ProjectSignoff signoff = new ProjectSignoff();
        List<ProjectDetails> projects = projectDetailsRepository.findAll();

        model.addAttribute("signoff", signoff);
        model.addAttribute("projects", projects);

        return "project_signoff";
    }

    // Save form
    @PostMapping("/save")
    public String saveSignoff(@ModelAttribute("signoff") ProjectSignoff signoff) {
        projectSignoffRepository.save(signoff);
        return "redirect:/signoffs/list";
    }
 // Edit
    @GetMapping("/edit/{id}")
    public String editSignoff(@PathVariable Long id, Model model) {
        ProjectSignoff signoff = projectSignoffRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Project Signoff ID: " + id));
        model.addAttribute("signoff", signoff);
        model.addAttribute("projects", projectDetailsRepository.findAll());
        return "project_signoff";
    }
    // List all
    @GetMapping("/list")
    public String listSignoffs(Model model) {
        List<ProjectSignoff> signoffs = projectSignoffRepository.findAll();
        model.addAttribute("signoffs", signoffs);
        return "signoff_list";
    }
    @GetMapping("/view/{id}")
    public String viewSignoff(@PathVariable Long id, Model model) {
        ProjectSignoff signoff = projectSignoffRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Project Signoff ID: " + id));
        model.addAttribute("signoff", signoff);
        return "project_signoff_view"; // points to Thymeleaf template
    }
}
