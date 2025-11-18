package com.portal.controller;

import com.portal.model.UATLetter;
import com.portal.model.ProjectDetails;
import com.portal.repository.UATLetterRepository;
import com.portal.repository.ProjectDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/uat-letters")
public class UATLetterController {

    @Autowired
    private UATLetterRepository uatLetterRepository;

    @Autowired
    private ProjectDetailsRepository projectDetailsRepository;

    // Show form
    @GetMapping("/new")
    public String showForm(Model model) {
        UATLetter uatLetter = new UATLetter();
        List<ProjectDetails> projects = projectDetailsRepository.findAll();

        model.addAttribute("uatLetter", uatLetter);
        model.addAttribute("projects", projects);

        return "uat_letter";
    }

    // Save form
    @PostMapping("/save")
    public String saveLetter(@ModelAttribute("uatLetter") UATLetter uatLetter) {
        uatLetterRepository.save(uatLetter);
        return "redirect:/uat-letters/list";
    }
    // Edit
    @GetMapping("/edit/{id}")
    public String editLetter(@PathVariable Long id, Model model) {
        UATLetter letter = uatLetterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid UAT Letter Id: " + id));
        model.addAttribute("letter", letter);
        model.addAttribute("projects", projectDetailsRepository.findAll());
        return "uat_letter";
    }
    // List all UAT letters
    @GetMapping("/list")
    public String listLetters(Model model) {
        List<UATLetter> letters = uatLetterRepository.findAll();
        model.addAttribute("letters", letters);
        return "uat_letter_list";
    }
    @GetMapping("/view/{id}")
    public String viewUATLetter(@PathVariable Long id, Model model) {
        UATLetter letter = uatLetterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid UAT Letter Id: " + id));
        model.addAttribute("letter", letter);
        return "uat_letter_view"; // points to Thymeleaf template
    }
}
