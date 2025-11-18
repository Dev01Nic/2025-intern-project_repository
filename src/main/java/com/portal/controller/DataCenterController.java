package com.portal.controller;

import com.portal.model.DataCenter;
import com.portal.repository.DataCenterRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/datacenters")
public class DataCenterController {

    private final DataCenterRepository dataCenterRepository;

    public DataCenterController(DataCenterRepository dataCenterRepository) {
        this.dataCenterRepository = dataCenterRepository;
    }

    // Show all Data Centers
    @GetMapping("/list")
    public String listDataCenters(Model model) {
        model.addAttribute("datacenters", dataCenterRepository.findAll());
        return "datacenter_list"; // includes datacenterListFragment
    }

    // Show form to create a new Data Center
    @GetMapping("/new")
    public String newDataCenterForm(Model model) {
        model.addAttribute("dataCenter", new DataCenter());
        return "datacenter_form"; // includes datacenterFormFragment
    }
    @GetMapping("/view/{id}")
    public String viewDataCenter(@PathVariable Long id, Model model) {
        DataCenter datacenter = dataCenterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Data Center ID: " + id));
        model.addAttribute("datacenter", datacenter);
        return "datacenter_view"; // Must match your Thymeleaf template
    }
    // Edit Data Center
    @GetMapping("/edit/{id}")
    public String editDataCenterForm(@PathVariable Long id, Model model) {
        DataCenter datacenter = dataCenterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Data Center ID: " + id));
        model.addAttribute("datacenter", datacenter);
        return "datacenter_form"; // Thymeleaf edit template
    }
}

