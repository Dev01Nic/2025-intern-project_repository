package com.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.portal.model.TechnicalUser;
import com.portal.repository.TechnicalUserRepository;

@Controller
@RequestMapping("/nodal")
public class NodalController {
	
	 private final TechnicalUserRepository userRepository;
	  public NodalController(TechnicalUserRepository userRepository) {
	        this.userRepository = userRepository;
	  }
	        @GetMapping
	 public String dashboard() {
	        return "nodal";
	 }


    // Show the create form
    @GetMapping("/create")
    public String createPage(Model model) {
    	model.addAttribute("nodalUser", new TechnicalUser()); 
        return "fragments/nodal_form_fragment"; // Thymeleaf fragment
    }
    @GetMapping("/view/{id}")
    public String viewNodalUser(@PathVariable Long id, Model model) {
    	TechnicalUser nodalUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Nodal User ID: " + id));
        model.addAttribute("nodalUser", nodalUser);
        return "nodal_view"; // Thymeleaf page created previously
    }
}
