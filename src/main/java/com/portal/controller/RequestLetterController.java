package com.portal.controller;

import com.portal.model.ProjectDetails;
import com.portal.model.RequestLetter;
import com.portal.repository.RequestLetterRepository;
import com.portal.repository.ProjectDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/request-letters")
public class RequestLetterController {

    @Autowired
    private RequestLetterRepository requestLetterRepository;

    @Autowired
    private ProjectDetailsRepository projectDetailsRepository;

    // Show new request letter form
    @GetMapping("/new")
    public String showForm(Model model) {
        RequestLetter requestLetter = new RequestLetter();
        List<ProjectDetails> projects = projectDetailsRepository.findAll();

        model.addAttribute("requestLetter", requestLetter);
        model.addAttribute("projects", projects);

        return "request_letter_fragment";  // form page
    }

    // Save request letter
    @PostMapping("/save")
    public String saveRequestLetter(@ModelAttribute("requestLetter") RequestLetter requestLetter) {
        requestLetterRepository.save(requestLetter);
        return "redirect:/request-letters/list";  // redirect to list after saving
    }

    // Show all request letters
    @GetMapping("/list")
    public String showDashboard(Model model) {
        model.addAttribute("letters", requestLetterRepository.findAll()); // all letters
        model.addAttribute("projects", projectDetailsRepository.findAll()); // projects for dropdowns
        return "hod/projects_list"; // your dashboard template
    }
    @GetMapping("/view/{id}")
    public String viewRequestLetter(@PathVariable Long id, Model model) {
        RequestLetter letter = requestLetterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid request letter Id:" + id));
        model.addAttribute("letter", letter);
        return "request_letter_view";
    }
 // Show edit form
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        RequestLetter letter = requestLetterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid request letter Id:" + id));
        List<ProjectDetails> projects = projectDetailsRepository.findAll();
        model.addAttribute("requestLetter", letter);
        model.addAttribute("projects", projects);
        return "fragments/request_letter_edit_fragment :: requestLetterEditFragment";
    }

    // Update request letter
    @PostMapping("/update/{id}")
    public String updateRequestLetter(@PathVariable Long id,
                                      @ModelAttribute("letter") RequestLetter updatedLetter) {
        RequestLetter existingLetter = requestLetterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid request letter Id:" + id));

        // Copy fields from updatedLetter to existingLetter
        existingLetter.setSubject(updatedLetter.getSubject());
        existingLetter.setLetterDate(updatedLetter.getLetterDate());
        existingLetter.setSenderName(updatedLetter.getSenderName());
        existingLetter.setSenderDesignation(updatedLetter.getSenderDesignation());
        existingLetter.setSenderEmail(updatedLetter.getSenderEmail());
        existingLetter.setPhoneNo(updatedLetter.getPhoneNo());
        existingLetter.setSenderAddress(updatedLetter.getSenderAddress());
        existingLetter.setProject(updatedLetter.getProject());
        existingLetter.setStatus(updatedLetter.getStatus());

        requestLetterRepository.save(existingLetter);
        return "redirect:/request-letters/list";
    }

    // Delete request letter
    @PostMapping("/delete/{id}")
    public String deleteRequestLetter(@PathVariable Long id) {
        RequestLetter letter = requestLetterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid request letter Id:" + id));
        requestLetterRepository.delete(letter);
        return "redirect:/request-letters/list";
    }
}