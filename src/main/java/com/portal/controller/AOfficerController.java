package com.portal.controller;

import com.portal.dto.AoDto;
import com.portal.model.ChangeRequest;
import com.portal.model.Department;
import com.portal.model.IssueTracking;
import com.portal.model.Priority;
import com.portal.model.ProjectDetails;
import com.portal.model.Status;
import com.portal.model.TechnicalUser;
import com.portal.repository.TechnicalUserRepository;
import com.portal.repository.RoleRepository;
import com.portal.repository.StatusRepository;
import com.portal.repository.CategoryRepository;
import com.portal.repository.ChangeRequestRepository;
import com.portal.repository.DepartmentRepository;
import com.portal.repository.IssueTrackingRepository;
import com.portal.repository.PriorityRepository;
import com.portal.repository.ProjectDetailsRepository;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/aofficer")
public class AOfficerController {

    private final TechnicalUserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DepartmentRepository departmentRepository;
    private final ProjectDetailsRepository projectDetailsRepository;
    private final IssueTrackingRepository issueTrackingRepository;
    private final PriorityRepository priorityRepository;
    private final StatusRepository statusRepository;
    private final ChangeRequestRepository changeRequestRepository;
    private final CategoryRepository categoryRepository;

    public AOfficerController(TechnicalUserRepository userRepository,
    		RoleRepository roleRepository,
    		DepartmentRepository departmentRepository,
    		ProjectDetailsRepository projectDetailsRepository,
            IssueTrackingRepository issueTrackingRepository,
            PriorityRepository priorityRepository,
            StatusRepository statusRepository,
            ChangeRequestRepository changeRequestRepository,
            CategoryRepository categoryRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.departmentRepository = departmentRepository;
        this.projectDetailsRepository = projectDetailsRepository;
        this.issueTrackingRepository = issueTrackingRepository;
        this.priorityRepository = priorityRepository;
        this.statusRepository = statusRepository;
        this.changeRequestRepository = changeRequestRepository;
        this.categoryRepository = categoryRepository;
    }

    // AO Dashboard landing page
    @GetMapping
    public String assistingDashboard(Model model, Authentication authentication) {
        // 🧍 Get logged-in AO
        String username = authentication.getName();
        TechnicalUser ao = userRepository.findByUsername(username);
        if (ao == null) {
            model.addAttribute("error", "Assisting Officer not found!");
            return "error";
        }

        // 🏢 AO Department name (if exists)
        String departmentName = (!ao.getDepartment().isEmpty())
                ? ao.getDepartment().get(0).getDeptName()
                : "N/A";

        model.addAttribute("loggedUser", ao.getName());
        model.addAttribute("departmentName", departmentName);

        // 📊 Project Stats
        List<ProjectDetails> allProjects = projectDetailsRepository.findAll();

        long totalProjects = allProjects.size();
        long inProgressProjects = allProjects.stream()
                .filter(p -> p.getStatus() != null && p.getStatus().getName().equalsIgnoreCase("In Progress"))
                .count();
        long pendingProjects = allProjects.stream()
                .filter(p -> p.getStatus() != null && p.getStatus().getName().equalsIgnoreCase("Pending"))
                .count();
        long completedProjects = allProjects.stream()
                .filter(p -> p.getStatus() != null && p.getStatus().getName().equalsIgnoreCase("Completed"))
                .count();

        // 🕒 Recent 5 Projects
        List<ProjectDetails> recentProjects = allProjects.stream()
                .sorted((p1, p2) -> Long.compare(p2.getProjectId(), p1.getProjectId()))
                .limit(5)
                .toList();

        // ✅ Add data to model
        model.addAttribute("totalProjects", totalProjects);
        model.addAttribute("inProgressProjects", inProgressProjects);
        model.addAttribute("pendingProjects", pendingProjects);
        model.addAttribute("completedProjects", completedProjects);
        model.addAttribute("recentProjects", recentProjects);

        return "aofficer";
    }




    // Show form to create new AO
    @GetMapping("/create")
    public String showAOForm(Model model) {
        model.addAttribute("aoUser", new TechnicalUser());
        model.addAttribute("aoUsers", userRepository.findAll()); // fetch all AOs
        model.addAttribute("departments", departmentRepository.findAll());
        model.addAttribute("roles", roleRepository.findAll());
        return "fragments/ao_form_fragment";
    }

    @GetMapping("/projects/list")
    public String listProjects(@RequestParam(required = false) Long departmentId,
                              @RequestParam(required = false) Long categoryId,
                              @RequestParam(required = false) Long statusId,
                              Model model, 
                              Authentication authentication) {

        String username = authentication.getName();
        TechnicalUser ao = userRepository.findByUsername(username);

        if (ao == null) {
            model.addAttribute("error", "Assisting Officer not found!");
            return "error";
        }

        String departmentName = (!ao.getDepartment().isEmpty())
                ? ao.getDepartment().get(0).getDeptName()
                : "N/A";

        model.addAttribute("loggedUser", ao.getName());
        model.addAttribute("departmentName", departmentName);

        // ✅ Start with only projects belonging to AO's departments
        List<ProjectDetails> projects = projectDetailsRepository.findAll();

        // ✅ Populate dropdowns with all available options
        model.addAttribute("projects", projects);
        model.addAttribute("departments", departmentRepository.findAll());
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("statuses", statusRepository.findAll());

        // ✅ Keep selected filters in the view
        model.addAttribute("selectedDepartment", departmentId);
        model.addAttribute("selectedCategory", categoryId);
        model.addAttribute("selectedStatus", statusId);

        return "project_lists"; 
    }


    // Show edit AO form
    @GetMapping("/projects/edit/{id}")
    public String editAO(@PathVariable("id") Long id, Model model) {
    	TechnicalUser aoUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid AO user Id: " + id));

        model.addAttribute("aoUser", aoUser);
        model.addAttribute("departments", departmentRepository.findAll());
        model.addAttribute("roles", roleRepository.findAll());

        return "ao_edit"; // your Thymeleaf template for editing AO
    }

    // Update AO after form submission
    @PostMapping("/projects/update/{id}")
    public String updateAO(@PathVariable("id") Long id, @ModelAttribute("aoUser") TechnicalUser aoUser) {
        // fetch existing AO
    	TechnicalUser existingAO = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid AO user Id: " + id));

        // update fields
        existingAO.setName(aoUser.getName());
        existingAO.setEmail(aoUser.getEmail());
        existingAO.setPhoneNo(aoUser.getPhoneNo());
        existingAO.setUsername(aoUser.getUsername());
        if (!aoUser.getPassword().isEmpty()) {
            existingAO.setPassword(aoUser.getPassword());
        }
        existingAO.setDesignation(aoUser.getDesignation());
        existingAO.setDepartment(aoUser.getDepartment());
        existingAO.setRole(aoUser.getRole());

        userRepository.save(existingAO);

        return "redirect:/aofficer"; // redirect back to AO dashboard
    }
    @GetMapping("/projects/details/{id}")
    @ResponseBody
    public AoDto getAODetails(@PathVariable("id") Long id) {
        TechnicalUser ao = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid AO user Id: " + id));

        List<Long> deptIds = null;
        if (ao.getDepartment() != null) {
            deptIds = ao.getDepartment().stream()
                         .map(Department::getDeptId)
                         .toList(); // Java 16+ ; otherwise use Collectors.toList()
        }

        Long roleId = ao.getRole() != null ? ao.getRole().getRoleId() : null;

        return new AoDto(
                ao.getId(),
                ao.getName(),
                ao.getEmail(),
                ao.getUsername(),
                ao.getPhoneNo(),
                ao.getDesignation(),
                deptIds,
                roleId
        );
    }
    @GetMapping("/issues")
    public String viewAOfficerIssues(Model model, Authentication authentication) {
        String username = authentication.getName();
        TechnicalUser ao = userRepository.findByUsername(username);

        if (ao == null) {
            model.addAttribute("error", "Assisting Officer not found!");
            return "error";
        }
        String departmentName = (!ao.getDepartment().isEmpty())
                ? ao.getDepartment().get(0).getDeptName()
                : "N/A";
        model.addAttribute("loggedUser", ao.getName());
        model.addAttribute("departmentName", departmentName);
        // 🟢 Fetch ALL issues (not department/project specific)
        List<IssueTracking> issues = issueTrackingRepository.findAll();

        // 🟢 Prepare dropdown filters and assignment lists
        List<Priority> priorities = priorityRepository.findAll();
        List<Status> statuses = statusRepository.findAll();
        List<TechnicalUser> hodUsers = userRepository.findByRole_RoleName("Head of Department");
        List<TechnicalUser> allAOs = userRepository.findByRole_RoleName("Assisting Officer");
        List<TechnicalUser> allDevelopers = userRepository.findByRole_RoleName("Developer");

        // 🟢 Add everything to the model
        model.addAttribute("issues", issues);
        model.addAttribute("priorities", priorities);
        model.addAttribute("statuses", statuses);
        model.addAttribute("hodUsers", hodUsers);
        model.addAttribute("allAOs", allAOs);
        model.addAttribute("allDevelopers", allDevelopers);

        // 🟢 Optional message if no issues found
        if (issues.isEmpty()) {
            model.addAttribute("info", "No issues found in the system.");
        }

        return "ao_issue_tracking"; // keep your Thymeleaf file name
    }



    @PostMapping("/issues/assign")
    public String assignIssue(@RequestParam Long issueId,
                              @RequestParam Long assignedToId) {

        IssueTracking issue = issueTrackingRepository.findById(issueId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid issue Id:" + issueId));

        TechnicalUser assignedUser = userRepository.findById(assignedToId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + assignedToId));

        issue.setAssignedTo(assignedUser);
        issueTrackingRepository.save(issue);

        return "redirect:/aofficer/issues";
    }
    @GetMapping("/change-requests")
    public String viewAOChangeRequests(@RequestParam(required = false) Long priorityId,
                                       @RequestParam(required = false) Long statusId,
                                       Model model,
                                       Authentication authentication) {

        // 1️⃣ Get logged-in AO
        String username = authentication.getName();
        TechnicalUser ao = userRepository.findByUsername(username);
        if (ao == null) {
            model.addAttribute("error", "Assisting Officer not found!");
            return "error";
        }
        String departmentName = (!ao.getDepartment().isEmpty())
                ? ao.getDepartment().get(0).getDeptName()
                : "N/A";
        model.addAttribute("loggedUser", ao.getName());
        model.addAttribute("departmentName", departmentName);
        // 2️⃣ Get all change requests (AOs are not tied to departments)
        List<ChangeRequest> changeRequests = changeRequestRepository.findAll();

        // 3️⃣ Optional filters
        if (priorityId != null) {
            changeRequests = changeRequests.stream()
                    .filter(cr -> cr.getPriority() != null && cr.getPriority().getPriorityId().equals(priorityId))
                    .toList();
        }

        if (statusId != null) {
            changeRequests = changeRequests.stream()
                    .filter(cr -> cr.getStatus() != null && cr.getStatus().getId().equals(statusId))
                    .toList();
        }

        // ✅ Optional: Show only requests assigned to this AO
        // (Uncomment this block if you want per-AO filtering)
        /*
        changeRequests = changeRequests.stream()
                .filter(cr -> cr.getApprovedBy() != null && cr.getApprovedBy().equals(ao))
                .toList();
        */

        // 4️⃣ Prepare model attributes
        model.addAttribute("aoUser", ao);
        model.addAttribute("changeRequests", changeRequests);
        model.addAttribute("priorities", priorityRepository.findAll());
        model.addAttribute("statuses", statusRepository.findAll());
        model.addAttribute("selectedPriority", priorityId);
        model.addAttribute("selectedStatus", statusId);

        // For dropdowns in modal (assign to)
        model.addAttribute("hodUsers", userRepository.findByRole_RoleName("Head Of Department"));
        model.addAttribute("allAOs", userRepository.findByRole_RoleName("Assisting Officer"));
        model.addAttribute("allDevelopers", userRepository.findByRole_RoleName("Developer"));

        // 5️⃣ Return the correct template
        return "ao_change_requests";
    }


    @PostMapping("/change-requests/assign")
    public String assignAOChangeRequest(@RequestParam Long changeRequestId,
                                        @RequestParam Long approvedById) {

        ChangeRequest changeRequest = changeRequestRepository.findById(changeRequestId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Change Request Id: " + changeRequestId));

        TechnicalUser approvedBy = userRepository.findById(approvedById)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Technical User Id: " + approvedById));

        // Assign approver (AO)
        changeRequest.setApprovedBy(approvedBy);

        // Optionally update status
        if (changeRequest.getStatus() != null) {
            changeRequest.getStatus().setName("Approved by AO");
        }

        changeRequestRepository.save(changeRequest);

        return "redirect:/aofficer/change-requests";
    }
}
