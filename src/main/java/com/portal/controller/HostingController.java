package com.portal.controller;

import com.portal.model.DataCenter;
import com.portal.model.Hosting;
import com.portal.model.ProjectDetails;
import com.portal.repository.HostingRepository;
import com.portal.repository.ProjectDetailsRepository;
import com.portal.repository.DataCenterRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/hosting")
public class HostingController {

    private final HostingRepository hostingRepository;
    private final ProjectDetailsRepository projectDetailsRepository;
    private final DataCenterRepository dataCenterRepository;

    public HostingController(HostingRepository hostingRepository,
                             ProjectDetailsRepository projectDetailsRepository,
                             DataCenterRepository dataCenterRepository) {
        this.hostingRepository = hostingRepository;
        this.projectDetailsRepository = projectDetailsRepository;
        this.dataCenterRepository = dataCenterRepository;
    }

    // Show Hosting List (for hostingListFragment)
    @GetMapping("/list")
    public String listHostings(Model model) {
        List<Hosting> hostings = hostingRepository.findAll();
        model.addAttribute("hostings", hostings);
        return "hosting_list"; // Thymeleaf page that includes hostingListFragment
    }

    // Show Hosting Form (for hostingFormFragment)
    @GetMapping("/new")
    public String newHostingForm(Model model) {
        model.addAttribute("hosting", new Hosting());
        model.addAttribute("projects", projectDetailsRepository.findAll());
        model.addAttribute("datacenters", dataCenterRepository.findAll());
        return "hosting_form"; // Thymeleaf page that includes hostingFormFragment
    }

    // Save Hosting Entry
    @PostMapping("/save")
    public String saveHosting(@ModelAttribute("hosting") Hosting hosting) {
        hostingRepository.save(hosting);
        return "redirect:/hosting/list";
    }
    @GetMapping("/view/{id}")
    public String viewHosting(@PathVariable Long id, Model model) {
        Hosting hosting = hostingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid hosting ID: " + id));
        model.addAttribute("hosting", hosting);
        return "hosting_view"; // Thymeleaf template
    }
    @GetMapping("/edit/{id}")
    public String editHosting(@PathVariable Long id, Model model) {
        Hosting hosting = hostingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Hosting ID: " + id));

        List<ProjectDetails> projects = projectDetailsRepository.findAll();
        List<DataCenter> datacenters = dataCenterRepository.findAll();

        model.addAttribute("hosting", hosting);
        model.addAttribute("projects", projects);
        model.addAttribute("datacenters", datacenters);

        return "hosting_form";
    }
}
