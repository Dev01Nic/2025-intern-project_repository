package com.portal.controller;

import com.portal.model.ProjectDetails;
import com.portal.model.ResourceAssignment;
import com.portal.model.TechnicalUser;
import com.portal.repository.AcceptanceLetterRepository;
import com.portal.repository.CategoryRepository;
import com.portal.repository.ChangeRequestRepository;
import com.portal.repository.DataCenterRepository;
import com.portal.repository.DepartmentRepository;
import com.portal.repository.DepartmentUserRepository;
import com.portal.repository.HostingRepository;
import com.portal.repository.IssueTrackingRepository;
import com.portal.repository.PriorityRepository;
import com.portal.repository.ProjectDetailsRepository;
import com.portal.repository.ProjectManualRepository;
import com.portal.repository.ProjectProposalRepository;
import com.portal.repository.ProjectSignoffRepository;
import com.portal.repository.RequestLetterRepository;
import com.portal.repository.ResourceAssignmentRepository;
import com.portal.repository.RoleRepository;
import com.portal.repository.StatusRepository;
import com.portal.repository.TechnicalUserRepository;
import com.portal.repository.UATLetterRepository;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/hoo")
public class HooController {

    private final ProjectDetailsRepository projectRepository;
    private final TechnicalUserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final RoleRepository roleRepository;
    private final CategoryRepository categoryRepository;
    private final StatusRepository statusRepository;
    private final PriorityRepository priorityRepository;
    private final ProjectProposalRepository projectProposalRepository;
    private final RequestLetterRepository requestLetterRepository;
    private final AcceptanceLetterRepository acceptanceLetterRepository;
    private final ProjectSignoffRepository projectSignoffRepository;
    private final ProjectManualRepository projectManualRepository;
    private final ResourceAssignmentRepository resourceAssignmentRepository;
    private final HostingRepository hostingRepository;
    private final DataCenterRepository dataCenterRepository;
    private final UATLetterRepository uatLetterRepository;
    private final IssueTrackingRepository issueTrackingRepository;
    private final DepartmentUserRepository departmentUserRepository;
    private final ProjectDetailsRepository projectDetailsRepository;
    private final ChangeRequestRepository changeRequestRepository;
    
    public HooController(ProjectDetailsRepository projectRepository,
            DepartmentRepository departmentRepository,
            TechnicalUserRepository userRepository,
            PriorityRepository priorityRepository,
            CategoryRepository categoryRepository,
            ProjectProposalRepository projectProposalRepository,
            RequestLetterRepository requestLetterRepository,
            AcceptanceLetterRepository acceptanceLetterRepository,
            ProjectSignoffRepository projectSignoffRepository,
            ProjectManualRepository projectManualRepository,
            ResourceAssignmentRepository resourceAssignmentRepository,
            DataCenterRepository dataCenterRepository,
            HostingRepository hostingRepository,
            RoleRepository roleRepository,
            UATLetterRepository uatLetterRepository,
            StatusRepository statusRepository,
            IssueTrackingRepository issueTrackingRepository,
            DepartmentUserRepository departmentUserRepository,
            ProjectDetailsRepository projectDetailsRepository,
            ChangeRequestRepository changeRequestRepository) {
    	this.projectRepository = projectRepository;
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
        this.priorityRepository = priorityRepository;
        this.categoryRepository = categoryRepository;
        this.projectProposalRepository = projectProposalRepository;
        this.requestLetterRepository = requestLetterRepository;
        this.acceptanceLetterRepository = acceptanceLetterRepository; 
        this.projectSignoffRepository = projectSignoffRepository;
        this.projectManualRepository = projectManualRepository;
        this.resourceAssignmentRepository = resourceAssignmentRepository;
        this.hostingRepository = hostingRepository;
        this.dataCenterRepository = dataCenterRepository;
        this.roleRepository = roleRepository;
        this.uatLetterRepository = uatLetterRepository;
        this.statusRepository = statusRepository;
        this.issueTrackingRepository = issueTrackingRepository;
        this.departmentUserRepository = departmentUserRepository;
        this.projectDetailsRepository = projectDetailsRepository;
        this.changeRequestRepository = changeRequestRepository;
    }

    

    @GetMapping
    public String hooDashboard(Model model, Authentication authentication) {
        String username = authentication.getName();
        TechnicalUser hoo = userRepository.findByUsername(username);

        if (hoo == null) {
            model.addAttribute("error", "Head of Office not found!");
            return "error";
        }

        model.addAttribute("hooName", hoo.getName());

        // ✅ Fetch all projects
        List<ProjectDetails> projects = projectRepository.findAll();

        // ✅ Totals for dashboard
        long totalProjects = projectRepository.count();
        long totalDepartments = departmentRepository.count();
        long totalHods = userRepository.findByRole_RoleName("Head Of Department").size();
        long totalAos = userRepository.findByRole_RoleName("Assisting Officer").size();
        long totalDevelopers = userRepository.findByRole_RoleName("Developer").size();

        // ✅ Add all attributes to the model
        model.addAttribute("projects", projects);
        model.addAttribute("totalProjects", totalProjects);
        model.addAttribute("totalDepartments", totalDepartments);
        model.addAttribute("totalHods", totalHods);
        model.addAttribute("totalAos", totalAos);
        model.addAttribute("totalDevelopers", totalDevelopers);

        return "hoo"; // your thymeleaf dashboard page
    }

    @GetMapping("/projects/list")
    public String projectList(@RequestParam(required = false) Long departmentId,
                             @RequestParam(required = false) Long categoryId,
                             @RequestParam(required = false) Long statusId,
                             Model model,
                             Authentication authentication) {

        String username = authentication.getName();
        TechnicalUser hoo = userRepository.findByUsername(username);

        if (hoo == null) {
            model.addAttribute("error", "Head of Operations not found!");
            return "error";
        }

        model.addAttribute("hooName", hoo.getName());

        // ✅ Start with all projects
        List<ProjectDetails> projects = projectRepository.findAll();

        // ✅ Populate dropdowns with all available options
        model.addAttribute("projects", projects);
        model.addAttribute("departments", departmentRepository.findAll());
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("statuses", statusRepository.findAll());

        // ✅ Keep selected filters in the view
        model.addAttribute("selectedDepartment", departmentId);
        model.addAttribute("selectedCategory", categoryId);
        model.addAttribute("selectedStatus", statusId);

        return "projects_list";
    }
    @GetMapping("/create")
    public String createHooForm(Model model) {
        model.addAttribute("hooUser", new TechnicalUser());
        model.addAttribute("departments", departmentRepository.findAll());
        model.addAttribute("roles", roleRepository.findAll());
        return "fragments/hoo_form_fragment :: hooFormFragment";  // <-- make hoo_form.html or reuse fragment in wrapper
    }
    @PostMapping("/save")
    public String saveHoo(@ModelAttribute("hooUser") TechnicalUser user) {
        userRepository.save(user);
        return "redirect:/hoo"; // back to dashboard
    }
    @GetMapping("/projects/view/{id}")
    public String viewProject(@PathVariable Long id, Model model) {
        ProjectDetails project = projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid project Id:" + id));

        model.addAttribute("project", project);
        model.addAttribute("departments", departmentRepository.findAll());
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("priorities", priorityRepository.findAll());
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("roles", roleRepository.findAll());

        List<ResourceAssignment> assignments = resourceAssignmentRepository.findByProject(project);

        List<TechnicalUser> hooUsers = new ArrayList<>();
        hooUsers.addAll(userRepository.findProjectManagersByRole("Head of Office"));
        hooUsers.addAll(userRepository.findCreatorsByRole("Head of Office"));
        model.addAttribute("hooUsers", hooUsers);
        model.addAttribute("hooUser", new TechnicalUser());

        List<TechnicalUser> hodUsers = new ArrayList<>();
        hodUsers.addAll(userRepository.findProjectManagersByRole("Head of Department"));
        hodUsers.addAll(userRepository.findCreatorsByRole("Head of Department"));
        model.addAttribute("hodUsers", hodUsers);
        model.addAttribute("hodUser", new TechnicalUser());

        model.addAttribute("nodalUsers", userRepository.findByRole_RoleName("Nodal Officer"));
        model.addAttribute("nodalUser", new TechnicalUser());

        List<TechnicalUser> aoUsers = assignments.stream()
                .map(ResourceAssignment::getAssignedTo)
                .filter(u -> u.getRole().getRoleName().equals("Assisting Officer"))
                .collect(Collectors.toList());
        model.addAttribute("aoUsers", aoUsers);
        model.addAttribute("aoUser", new TechnicalUser());

        List<TechnicalUser> developerUsers = assignments.stream()
                .map(ResourceAssignment::getAssignedTo)
                .filter(u -> u.getRole().getRoleName().equals("Developer"))
                .collect(Collectors.toList());
        model.addAttribute("developerUsers", developerUsers);
        model.addAttribute("developerUser", new TechnicalUser());

        model.addAttribute("allAOs", userRepository.findByRole_RoleName("Assisting Officer"));
        model.addAttribute("allDevelopers", userRepository.findByRole_RoleName("Developer"));

        model.addAttribute("hod", userRepository.findByRole_RoleName("Head of Department"));

        model.addAttribute("resourceAssignment", new com.portal.model.ResourceAssignment());
        model.addAttribute("devices", resourceAssignmentRepository.findAll());

        model.addAttribute("dataCenter", new com.portal.model.DataCenter());
        model.addAttribute("hosting", new com.portal.model.Hosting());
        model.addAttribute("hostings", hostingRepository.findAll());
        model.addAttribute("datacenters", dataCenterRepository.findAll());

        model.addAttribute("requestLetters", requestLetterRepository.findByProject(project));
        model.addAttribute("projectProposals", projectProposalRepository.findByProject(project));
        model.addAttribute("acceptanceLetters", acceptanceLetterRepository.findByProject(project));
        model.addAttribute("uatLetters", uatLetterRepository.findByProject(project));
        model.addAttribute("projectSignoffs", projectSignoffRepository.findByProject(project));
        model.addAttribute("projectManuals", projectManualRepository.findByProject(project));

        model.addAttribute("requestLetter", new com.portal.model.RequestLetter());
        model.addAttribute("proposal", new com.portal.model.ProjectProposal());
        model.addAttribute("acceptanceLetter", new com.portal.model.AcceptanceLetter());
        model.addAttribute("uatLetter", new com.portal.model.UATLetter());
        model.addAttribute("signoff", new com.portal.model.ProjectSignoff());
        model.addAttribute("manual", new com.portal.model.ProjectManual());

        return "project_view";
    }

    }


