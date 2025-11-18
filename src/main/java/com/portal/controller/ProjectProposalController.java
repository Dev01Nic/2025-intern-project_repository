package com.portal.controller;

import com.portal.model.ProjectProposal;
import com.portal.model.ProjectDetails;
import com.portal.model.TechnicalUser;
import com.portal.repository.ProjectProposalRepository;
import com.portal.repository.ProjectDetailsRepository;
import com.portal.repository.TechnicalUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/proposals")
public class ProjectProposalController {

    @Autowired
    private ProjectProposalRepository projectProposalRepository;

    @Autowired
    private ProjectDetailsRepository projectDetailsRepository;

    @Autowired
    private TechnicalUserRepository userRepository;

    // Show form
    @GetMapping("/new")
    public String showForm(Model model) {
        ProjectProposal proposal = new ProjectProposal(null, null, null, null, null, null, null);

        List<ProjectDetails> projects = projectDetailsRepository.findAll();
        List<TechnicalUser> users = userRepository.findAll();

        model.addAttribute("proposal", proposal);
        model.addAttribute("projects", projects);
        model.addAttribute("users", users);

        return "project_proposal";
    }

    // Save form data
    @PostMapping("/save")
    public String saveProposal(@ModelAttribute("proposal") ProjectProposal proposal) {
        projectProposalRepository.save(proposal);
        return "redirect:/proposals/list";
    }
    // Edit
    @GetMapping("/edit/{id}")
    public String editProposal(@PathVariable Long id, Model model) {
        ProjectProposal proposal = projectProposalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Proposal Id: " + id));
        model.addAttribute("proposal", proposal);
        model.addAttribute("projects", projectDetailsRepository.findAll());
        model.addAttribute("users", userRepository.findAll());
        return "project_proposal";
    }

    // List all proposals
    @GetMapping("/list")
    public String listProposals(Model model) {
        List<ProjectProposal> proposals = projectProposalRepository.findAll();
        model.addAttribute("proposals", proposals);
        return "project_proposal_list";
    }
    @GetMapping("/view/{id}")
    public String viewProposal(@PathVariable Long id, Model model) {
        ProjectProposal proposal = projectProposalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Proposal Id: " + id));

        model.addAttribute("proposal", proposal);
        return "project_proposal_view"; // this will point to proposal_view.html
    }
}
