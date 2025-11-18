
package com.portal.controller;

import com.portal.model.AcceptanceLetter;
import com.portal.model.ProjectDetails;
import com.portal.model.ProjectProposal;
import com.portal.repository.AcceptanceLetterRepository;
import com.portal.repository.ProjectDetailsRepository;
import com.portal.repository.ProjectProposalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/acceptance-letters")
public class AcceptanceLetterController {

    @Autowired
    private AcceptanceLetterRepository acceptanceLetterRepository;

    @Autowired
    private ProjectDetailsRepository projectDetailsRepository;

    @Autowired
    private ProjectProposalRepository projectProposalRepository;

    // Show form
    @GetMapping("/new")
    public String showForm(Model model) {
        AcceptanceLetter letter = new AcceptanceLetter();

        List<ProjectDetails> projects = projectDetailsRepository.findAll();
        List<ProjectProposal> proposals = projectProposalRepository.findAll();

        model.addAttribute("acceptanceLetter", letter);
        model.addAttribute("projects", projects);
        model.addAttribute("proposals", proposals);

        return "acceptance_letter";
    }

    // Save form data
    @PostMapping("/save")
    public String saveLetter(@ModelAttribute("acceptanceLetter") AcceptanceLetter letter) {
        acceptanceLetterRepository.save(letter);
        return "redirect:/acceptance-letters/list";
    }
    // Edit
    @GetMapping("/edit/{id}")
    public String editLetter(@PathVariable Long id, Model model) {
        AcceptanceLetter letter = acceptanceLetterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Acceptance Letter Id: " + id));
        model.addAttribute("letter", letter);
        model.addAttribute("projects", projectDetailsRepository.findAll());
        model.addAttribute("proposals", projectProposalRepository.findAll());
        return "acceptance_letter";
    }
    // List all acceptance letters
    @GetMapping("/list")
    public String listLetters(Model model) {
        List<AcceptanceLetter> letters = acceptanceLetterRepository.findAll();
        model.addAttribute("letters", letters);
        return "acceptance_letter_list";
    }
    @GetMapping("/view/{id}")
    public String viewAcceptanceLetter(@PathVariable Long id, Model model) {
        AcceptanceLetter letter = acceptanceLetterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Acceptance Letter Id: " + id));
        
        model.addAttribute("letter", letter);
        return "acceptance_letter_view"; // points to acceptance_letter_view.html
    }
}
