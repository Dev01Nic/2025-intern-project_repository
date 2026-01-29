package com.portal.controller;

import com.portal.model.ChangeRequest;
import com.portal.model.Department;
import com.portal.model.DepartmentUser;
import com.portal.model.IssueTracking;
import com.portal.model.ProjectDetails;
import com.portal.model.ResourceAssignment;
import com.portal.model.Status;
import com.portal.model.TechnicalUser;
import com.portal.repository.CategoryRepository;
import com.portal.repository.ChangeRequestRepository;
import com.portal.repository.DataCenterRepository;
import com.portal.repository.DepartmentRepository;
import com.portal.repository.DepartmentUserRepository;
import com.portal.repository.HostingRepository;
import com.portal.repository.IssueTrackingRepository;
import com.portal.repository.PriorityRepository;
import com.portal.repository.ProjectDetailsRepository;
import com.portal.repository.TechnicalUserRepository;
import com.portal.repository.ProjectProposalRepository;
import com.portal.repository.ProjectSignoffRepository;
import com.portal.repository.RequestLetterRepository;
import com.portal.repository.ResourceAssignmentRepository;
import com.portal.repository.RoleRepository;
import com.portal.repository.StatusRepository;
import com.portal.repository.UATLetterRepository;
import com.portal.repository.AcceptanceLetterRepository;
import com.portal.repository.ProjectManualRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/hod")
public class HodController {

    private final ProjectDetailsRepository projectRepository;
    private final DepartmentRepository departmentRepository;
    private final TechnicalUserRepository userRepository;
    private final PriorityRepository priorityRepository;
    private final CategoryRepository categoryRepository;
    private final ProjectProposalRepository projectProposalRepository;
    private final RequestLetterRepository requestLetterRepository;
    private final AcceptanceLetterRepository acceptanceLetterRepository;
    private final ProjectSignoffRepository projectSignoffRepository;
    private final ProjectManualRepository projectManualRepository;
    private final ResourceAssignmentRepository resourceAssignmentRepository;
    private final HostingRepository hostingRepository;
    private final DataCenterRepository dataCenterRepository;
    private final RoleRepository roleRepository; 
    private final UATLetterRepository uatLetterRepository;
    private final StatusRepository statusRepository;
    private final IssueTrackingRepository issueTrackingRepository;
    private final DepartmentUserRepository departmentUserRepository;
    private final ProjectDetailsRepository projectDetailsRepository;
    private final ChangeRequestRepository changeRequestRepository;
    
    public HodController(ProjectDetailsRepository projectRepository,
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

    // ✅ HoD Dashboard
    @GetMapping
    public String hodDashboard(Model model, Authentication authentication) {

        // 1️⃣ Get logged-in HoD
        String username = authentication.getName();
        TechnicalUser hod = userRepository.findByUsername(username);
        if (hod == null) {
            throw new IllegalStateException("HoD not found for username: " + username);
        }

        // 2️⃣ Get departments managed by HoD
        List<Department> departments = hod.getDepartment();
        if (departments == null || departments.isEmpty()) {
            model.addAttribute("error", "No departments assigned to this HoD!");
            return "error";
        }

        // 3️⃣ Get projects belonging to HoD's departments
        List<ProjectDetails> projects = projectRepository.findByDepartmentIn(departments);

        // 4️⃣ Get issues linked to those projects
        List<IssueTracking> issues = issueTrackingRepository.findAll()
                .stream()
                .filter(i -> i.getProject() != null && projects.contains(i.getProject()))
                .toList();

        // 5️⃣ Get change requests linked to those projects
        List<ChangeRequest> changeRequests = changeRequestRepository.findAll()
                .stream()
                .filter(cr -> cr.getProject() != null && projects.contains(cr.getProject()))
                .toList();

        // 6️⃣ Dashboard metrics
        int totalProjects = projects.size();
        int facultyCount = userRepository.findByDepartmentIn(departments).size();
        int totalIssues = issues.size();
        int totalChangeRequests = changeRequests.size();

        // 7️⃣ Sort by ID (for recent items) instead of date fields
        List<ProjectDetails> recentProjects = projects.stream()
        		.sorted(Comparator.comparing(ProjectDetails::getProjectId))
                .limit(5)
                .toList();

        List<IssueTracking> recentIssues = issues.stream()
                .sorted(Comparator.comparing(IssueTracking::getId).reversed())
                .limit(5)
                .toList();

        List<ChangeRequest> recentChangeRequests = changeRequests.stream()
                .sorted(Comparator.comparing(ChangeRequest::getId).reversed())
                .limit(5)
                .toList();

        // 8️⃣ Add attributes to model
        model.addAttribute("hodName", hod.getName());
        model.addAttribute("hodDepartmentName", hod.getDepartment().get(0).getDeptName());

        model.addAttribute("totalProjects", totalProjects);
        model.addAttribute("facultyCount", facultyCount);
        model.addAttribute("activeIssues", totalIssues);  // showing total issues
        model.addAttribute("pendingChangeRequests", totalChangeRequests); // showing total CRs

        model.addAttribute("recentProjects", recentProjects);
        model.addAttribute("recentIssues", recentIssues);
        model.addAttribute("recentChangeRequests", recentChangeRequests);

        return "hod"; // Thymeleaf dashboard template
    }


    // ✅ Show all projects (list view)
    @GetMapping("/projects/list")
    public String projectList(@RequestParam(required = false) Long departmentId,
                              @RequestParam(required = false) Long categoryId,
                              @RequestParam(required = false) Long statusId,
                              Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        TechnicalUser hod = userRepository.findByUsername(username);
        if (hod == null) {
            throw new IllegalStateException("HoD not found for username: " + username);
        }
        String hodDepartmentName = (hod.getDepartment() != null)
                ? hod.getDepartment().get(0).getDeptName()
                : "N/A";

        // ✅ Add to model
        model.addAttribute("hodName", hod.getName());
        model.addAttribute("hodDepartmentName", hodDepartmentName);
        model.addAttribute("hodRole", hod.getRole().getRoleName());


        // ✅ Get all departments HoD manages
        List<Department> departments = hod.getDepartment();

        // ✅ Start with only projects belonging to HoD's departments
        List<ProjectDetails> projects = projectRepository.findByDepartmentIn(departments);

       

        // ✅ Populate dropdowns
        model.addAttribute("projects", projects);
        model.addAttribute("departments", departmentRepository.findAll());
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("statuses", statusRepository.findAll());

        // ✅ Keep selected filters in the view
        model.addAttribute("selectedDepartment", departmentId);
        model.addAttribute("selectedCategory", categoryId);
        model.addAttribute("selectedStatus", statusId);

        return "project_list";
    }

 // ✅ HoD Project Dashboard (similar to department user project dashboard)
    @GetMapping("/projects/dashboard")
    public String hodProjectDashboard(Model model, Authentication authentication) {

        // 1️⃣ Get logged-in HoD
        String username = authentication.getName();
        TechnicalUser hod = userRepository.findByUsername(username);

        if (hod == null) {
            model.addAttribute("error", "HoD not found!");
            return "error";
        }

        // 2️⃣ Get departments managed by the HoD
        List<Department> departments = hod.getDepartment();

        // 3️⃣ Get all projects belonging to these departments
        List<ProjectDetails> projects = projectDetailsRepository.findByDepartmentIn(departments);

        // Total Projects
        model.addAttribute("totalProjects", projects.size());

        // 4️⃣ Count by Status
        long notStarted = projects.stream()
                .filter(p -> p.getStatus() != null &&
                             p.getStatus().getName().equalsIgnoreCase("Not Started"))
                .count();

        long inProgress = projects.stream()
                .filter(p -> p.getStatus() != null &&
                             p.getStatus().getName().equalsIgnoreCase("In Progress"))
                .count();

        long completed = projects.stream()
                .filter(p -> p.getStatus() != null &&
                             p.getStatus().getName().equalsIgnoreCase("Completed"))
                .count();

        long onHold = projects.stream()
                .filter(p -> p.getStatus() != null &&
                             p.getStatus().getName().equalsIgnoreCase("On Hold"))
                .count();

        // Add attributes for charts
        model.addAttribute("notStarted", notStarted);
        model.addAttribute("inProgress", inProgress);
        model.addAttribute("completed", completed);
        model.addAttribute("onHold", onHold);

        // Add attributes for summary cards
        model.addAttribute("activeProjects", inProgress);   // Active = In Progress
        model.addAttribute("completedProjects", completed);
        model.addAttribute("onHoldProjects", onHold);

        // 5️⃣ Count by Category (Optional)
        long categoryA = projects.stream()
                .filter(p -> p.getCategory() != null &&
                             p.getCategory().getCategoryName().equalsIgnoreCase("Category A"))
                .count();

        long categoryB = projects.stream()
                .filter(p -> p.getCategory() != null &&
                             p.getCategory().getCategoryName().equalsIgnoreCase("Category B"))
                .count();

        long categoryC = projects.stream()
                .filter(p -> p.getCategory() != null &&
                             p.getCategory().getCategoryName().equalsIgnoreCase("Category C"))
                .count();

        model.addAttribute("categoryA", categoryA);
        model.addAttribute("categoryB", categoryB);
        model.addAttribute("categoryC", categoryC);

        // 6️⃣ Recent projects (latest 5)
        List<ProjectDetails> recentProjects = projects.stream()
                .filter(p -> p.getStartDate() != null)
                .sorted((a, b) -> b.getStartDate().compareTo(a.getStartDate()))
                .limit(5)
                .toList();

        model.addAttribute("recentProjects", recentProjects);

        // 7️⃣ HoD personal details
        model.addAttribute("hodName", hod.getName());
        model.addAttribute("hodDepartmentName", hod.getDepartment().get(0).getDeptName());
        model.addAttribute("role", hod.getRole().getRoleName());

        return "project_list_1"; 
    }

    // ✅ Show form to create new project
    @GetMapping("/projects/new")
    public String showProjectForm(Model model) {
        model.addAttribute("departments", departmentRepository.findAll());
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("priorities", priorityRepository.findAll());
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("statuses", statusRepository.findAll()); 
        return "project_form";
    }

    // ✅ Save new project
    @PostMapping("/projects/save")
    public String saveProject(@RequestParam String projectName,
                              @RequestParam Long departmentId,
                              @RequestParam Long categoryId,
                              @RequestParam Long projectManagerId,
                              @RequestParam String startDate,
                              @RequestParam String description,
                              @RequestParam String statusId,
                              @RequestParam Long priorityId,
                              @RequestParam(required = false) String technologiesUsed) {

        ProjectDetails project = new ProjectDetails();
        project.setProjectName(projectName);
        project.setDescription(description);
        project.setStartDate(LocalDate.parse(startDate));
        project.setTechnologiesUsed(technologiesUsed);

        // ✅ fetch full entity instead of setting ID
        Status status = statusRepository.findById(Long.parseLong(statusId))
                .orElseThrow(() -> new IllegalArgumentException("Invalid status Id: " + statusId));
        project.setStatus(status);

        project.setDepartment(departmentRepository.findById(departmentId).orElse(null));
        project.setProjectManager(userRepository.findById(projectManagerId).orElse(null));
        project.setPriority(priorityRepository.findById(priorityId).orElse(null));
        project.setCategory(categoryRepository.findById(categoryId).orElse(null));

        projectRepository.save(project);
        return "redirect:/hod/projects/list";
    }


    // ✅ Show edit form
    @GetMapping("/projects/edit/{id}")
    public String editProject(@PathVariable Long id, Model model) {
        ProjectDetails project = projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid project Id:" + id));

        
        model.addAttribute("project", project);
        model.addAttribute("departments", departmentRepository.findAll());
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("priorities", priorityRepository.findAll());
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute("statuses", statusRepository.findAll()); 
        List<ResourceAssignment> assignments = resourceAssignmentRepository.findByProject(project);
        
       
        List<TechnicalUser> hooUsers = new ArrayList<>();
        hooUsers.addAll(userRepository.findProjectManagersByRole("Head of Office"));
        hooUsers.addAll(userRepository.findCreatorsByRole("Head of Office"));
        model.addAttribute("hooUsers", hooUsers);
        model.addAttribute("hooUser", new TechnicalUser());
        model.addAttribute("roles", roleRepository.findAll());


        List<TechnicalUser> hodUsers = new ArrayList<>();
        hodUsers.addAll(userRepository.findProjectManagersByRole("Head of Department"));
        hodUsers.addAll(userRepository.findCreatorsByRole("Head of Department"));
        model.addAttribute("hodUsers", hodUsers);
        model.addAttribute("hodUser", new TechnicalUser());
        
        model.addAttribute("nodalUsers", userRepository.findByRole_RoleName("Nodal Officer"));
        model.addAttribute("nodalUser", new TechnicalUser());

        // Filter Developers
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

     // ----------------- Filtered Document Lists -----------------
        model.addAttribute("requestLetters", requestLetterRepository.findByProject(project));
        model.addAttribute("projectProposals", projectProposalRepository.findByProject(project));
        model.addAttribute("acceptanceLetters", acceptanceLetterRepository.findByProject(project));
        model.addAttribute("uatLetters", uatLetterRepository.findByProject(project));
        model.addAttribute("projectSignoffs", projectSignoffRepository.findByProject(project));
        model.addAttribute("projectManuals", projectManualRepository.findByProject(project));

        // ----------------- For forms / creating new entries -----------------
        model.addAttribute("requestLetter", new com.portal.model.RequestLetter());
        model.addAttribute("proposal", new com.portal.model.ProjectProposal());
        model.addAttribute("acceptanceLetter", new com.portal.model.AcceptanceLetter());
        model.addAttribute("uatLetter", new com.portal.model.UATLetter());
        model.addAttribute("signoff", new com.portal.model.ProjectSignoff());
        model.addAttribute("manual", new com.portal.model.ProjectManual());

        return "project_details_edit";
    }
    // ✅ Update project
    @PostMapping("/projects/update/{id}")
    public String updateProject(@PathVariable Long id,
                                @RequestParam String projectName,
                                @RequestParam Long departmentId,
                                @RequestParam Long projectManagerId,
                                @RequestParam Long createdById,
                                @RequestParam String startDate,
                                @RequestParam String statusId,
                                @RequestParam Long categoryId,
                                @RequestParam String technologiesUsed,
                                @RequestParam Long priorityId,
                                @RequestParam String description) {

        ProjectDetails project = projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid project Id:" + id));

        project.setProjectName(projectName);
        project.setStartDate(LocalDate.parse(startDate));
        project.setTechnologiesUsed(technologiesUsed);
        project.setDescription(description);

        // ✅ fetch related entities properly
        Status status = statusRepository.findById(Long.parseLong(statusId))
                .orElseThrow(() -> new IllegalArgumentException("Invalid status Id: " + statusId));
        project.setStatus(status);

        project.setDepartment(departmentRepository.findById(departmentId).orElse(null));
        project.setProjectManager(userRepository.findById(projectManagerId).orElse(null));
        project.setCreatedBy(userRepository.findById(createdById).orElse(null));
        project.setCategory(categoryRepository.findById(categoryId).orElse(null));
        project.setPriority(priorityRepository.findById(priorityId).orElse(null));

        projectRepository.save(project);
        return "redirect:/hod/projects/list";
    }

    @GetMapping("/create")
    public String showCreateHodForm(Model model) {
        model.addAttribute("hodUser", new TechnicalUser());
        model.addAttribute("departments", departmentRepository.findAll());
        model.addAttribute("roles", roleRepository.findAll());
        return "fragments/hod_form_fragment"; // assuming your dashboard template is 'hod.html'
    }
    @PostMapping("/create")
    public String saveHod(@ModelAttribute("hodUser") TechnicalUser hodUser) {
        userRepository.save(hodUser);
        return "redirect:/hod"; // redirect back to dashboard after saving
    }
    @GetMapping("/issues")
    public String viewHodIssues(@RequestParam(required = false) Long priorityId,
                                @RequestParam(required = false) Long statusId,
                                Model model,
                                Authentication authentication) {

        // 1️⃣ Get the logged-in HoD user
        String username = authentication.getName();
        TechnicalUser hod = userRepository.findByUsername(username);
        if (hod == null) {
            model.addAttribute("error", "HoD not found!");
            return "error";
        }
        String hodDepartmentName = (hod.getDepartment() != null)
                ? hod.getDepartment().get(0).getDeptName()
                : "N/A";

        // ✅ Add to model
        model.addAttribute("hodName", hod.getName());
        model.addAttribute("hodDepartmentName", hodDepartmentName);
        model.addAttribute("hodRole", hod.getRole().getRoleName());


        // 2️⃣ Get departments managed by this HoD
        List<Department> departments = hod.getDepartment();
        if (departments == null || departments.isEmpty()) {
            model.addAttribute("error", "No departments assigned to this HoD!");
            return "error";
        }

        // 3️⃣ Get projects under those departments
        List<ProjectDetails> projects = projectDetailsRepository.findByDepartmentIn(departments);

        // 4️⃣ Get issues related to those projects
        List<IssueTracking> issues = issueTrackingRepository.findAll()
                .stream()
                .filter(i -> i.getProject() != null && projects.contains(i.getProject()))
                .toList();

        model.addAttribute("issues", issues);
        model.addAttribute("hodUser", hod);
        model.addAttribute("projects", projects);
        model.addAttribute("departments", departments);
        model.addAttribute("priorities", priorityRepository.findAll());
        model.addAttribute("statuses", statusRepository.findAll());
        model.addAttribute("selectedPriority", priorityId);
        model.addAttribute("selectedStatus", statusId);
        model.addAttribute("hodUsers", userRepository.findByRole_RoleName("Head Of Department"));
        model.addAttribute("allAOs", userRepository.findByRole_RoleName("Assisting Officer"));
        model.addAttribute("allDevelopers", userRepository.findByRole_RoleName("Developer"));

        // 7️⃣ Return the view
        return "hod_issue_tracking";
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

        return "redirect:/hod/issues";
    }
    @GetMapping("/issues/dashboard")
    public String issueTrackingDashboard(Model model, Authentication authentication) {
        String username = authentication.getName();
        TechnicalUser hod = userRepository.findByUsername(username);

        if (hod == null) {
            model.addAttribute("error", "HoD not found!");
            return "error";
        }

        // ✅ Get all issues linked to HoD’s departments
        List<Department> departments = hod.getDepartment();
        List<IssueTracking> issues = issueTrackingRepository.findAll()
                .stream()
                .filter(i -> i.getProject() != null && departments.contains(i.getProject().getDepartment()))
                .toList();

        model.addAttribute("totalIssues", issues.size());

        // ✅ Count by Status
        long openCount = issues.stream()
                .filter(i -> i.getStatus() != null && i.getStatus().getName().equalsIgnoreCase("Open"))
                .count();
        long inProgressCount = issues.stream()
                .filter(i -> i.getStatus() != null && i.getStatus().getName().equalsIgnoreCase("In Progress"))
                .count();
        long resolvedCount = issues.stream()
                .filter(i -> i.getStatus() != null && i.getStatus().getName().equalsIgnoreCase("Resolved"))
                .count();
        long closedCount = issues.stream()
                .filter(i -> i.getStatus() != null && i.getStatus().getName().equalsIgnoreCase("Closed"))
                .count();

        model.addAttribute("openCount", openCount);
        model.addAttribute("inProgressCount", inProgressCount);
        model.addAttribute("resolvedCount", resolvedCount);
        model.addAttribute("closedCount", closedCount);

        // ✅ Count by Priority
        long highPriority = issues.stream()
                .filter(i -> i.getPriority() != null && i.getPriority().getPriorityName().equalsIgnoreCase("High"))
                .count();
        long mediumPriority = issues.stream()
                .filter(i -> i.getPriority() != null && i.getPriority().getPriorityName().equalsIgnoreCase("Medium"))
                .count();
        long lowPriority = issues.stream()
                .filter(i -> i.getPriority() != null && i.getPriority().getPriorityName().equalsIgnoreCase("Low"))
                .count();

        model.addAttribute("highPriority", highPriority);
        model.addAttribute("mediumPriority", mediumPriority);
        model.addAttribute("lowPriority", lowPriority);

        // ✅ Recent 5 issues
        List<IssueTracking> recentIssues = issues.stream()
                .filter(i -> i.getCreatedAt() != null)
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .limit(5)
                .toList();

        model.addAttribute("recentIssues", recentIssues);
        model.addAttribute("hodName", hod.getName());

        return "hod_issue_dashboard";
    }
    @GetMapping("/change-requests")
    public String viewHodChangeRequests(@RequestParam(required = false) Long priorityId,
                                        @RequestParam(required = false) Long statusId,
                                        Model model,
                                        Authentication authentication) {

        // 1️⃣ Get logged-in HoD
        String username = authentication.getName();
        TechnicalUser hod = userRepository.findByUsername(username);
        if (hod == null) {
            model.addAttribute("error", "HoD not found!");
            return "error";
        }
        String hodDepartmentName = (hod.getDepartment() != null)
                ? hod.getDepartment().get(0).getDeptName()
                : "N/A";

        // ✅ Add to model
        model.addAttribute("hodName", hod.getName());
        model.addAttribute("hodDepartmentName", hodDepartmentName);
        model.addAttribute("hodRole", hod.getRole().getRoleName());


        // 2️⃣ Get departments managed by this HoD
        List<Department> departments = hod.getDepartment();
        if (departments == null || departments.isEmpty()) {
            model.addAttribute("error", "No departments assigned to this HoD!");
            return "error";
        }

        // 3️⃣ Get projects under these departments
        List<ProjectDetails> projects = projectDetailsRepository.findByDepartmentIn(departments);

        // 4️⃣ Get all change requests related to those projects
        List<ChangeRequest> changeRequests = changeRequestRepository.findAll()
                .stream()
                .filter(cr -> cr.getProject() != null && projects.contains(cr.getProject()))
                .toList();

        // ✅ Optional: Apply filters
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

        // 5️⃣ Send data to view
        model.addAttribute("hodUser", hod);
        model.addAttribute("departments", departments);
        model.addAttribute("projects", projects);
        model.addAttribute("changeRequests", changeRequests);
        model.addAttribute("priorities", priorityRepository.findAll());
        model.addAttribute("statuses", statusRepository.findAll());
        model.addAttribute("selectedPriority", priorityId);
        model.addAttribute("selectedStatus", statusId);
        model.addAttribute("hodUsers", userRepository.findByRole_RoleName("Head Of Department"));
        model.addAttribute("allAOs", userRepository.findByRole_RoleName("Assisting Officer"));
        model.addAttribute("allDevelopers", userRepository.findByRole_RoleName("Developer"));

        return "hod_change_requests";
    }
    @PostMapping("/change-requests/assign")
    public String assignChangeRequest(@RequestParam Long changeRequestId,
                                      @RequestParam Long approvedById) {

        ChangeRequest changeRequest = changeRequestRepository.findById(changeRequestId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Change Request Id: " + changeRequestId));

        TechnicalUser approvedBy = userRepository.findById(approvedById)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Technical User Id: " + approvedById));

        // Assign the technical user (approver)
        changeRequest.setApprovedBy(approvedBy);

        // Optionally update status
        if (changeRequest.getStatus() != null) {
            changeRequest.getStatus().setName("Approved");
        }

        changeRequestRepository.save(changeRequest);

        return "redirect:/hod/change-requests";
    }
    @GetMapping("/change-requests/dashboard")
    public String hodChangeRequestDashboard(Model model, Authentication authentication) {
        // ✅ Get logged-in HoD user
        String username = authentication.getName();
        TechnicalUser hod = userRepository.findByUsername(username);

        if (hod == null) {
            model.addAttribute("error", "HoD not found!");
            return "error";
        }

        // ✅ Get departments assigned to this HoD
        List<Department> hodDepartments = hod.getDepartment();

        // ✅ Fetch change requests only from HoD’s departments
        List<ChangeRequest> requests = changeRequestRepository.findAll()
                .stream()
                .filter(r ->
                        r.getProject() != null &&
                        r.getProject().getDepartment() != null &&
                        hodDepartments.contains(r.getProject().getDepartment())
                )
                .toList();

        // ✅ Summary counts
        model.addAttribute("totalRequests", requests.size());
        long pendingCount = requests.stream()
                .filter(r -> r.getStatus() != null && r.getStatus().getName().equalsIgnoreCase("Pending"))
                .count();
        long approvedCount = requests.stream()
                .filter(r -> r.getStatus() != null && r.getStatus().getName().equalsIgnoreCase("Approved"))
                .count();
        long rejectedCount = requests.stream()
                .filter(r -> r.getStatus() != null && r.getStatus().getName().equalsIgnoreCase("Rejected"))
                .count();

        model.addAttribute("pendingCount", pendingCount);
        model.addAttribute("approvedCount", approvedCount);
        model.addAttribute("rejectedCount", rejectedCount);

        // ✅ Priority counts
        long highPriority = requests.stream()
                .filter(r -> r.getPriority() != null && r.getPriority().getPriorityName().equalsIgnoreCase("High"))
                .count();
        long mediumPriority = requests.stream()
                .filter(r -> r.getPriority() != null && r.getPriority().getPriorityName().equalsIgnoreCase("Medium"))
                .count();
        long lowPriority = requests.stream()
                .filter(r -> r.getPriority() != null && r.getPriority().getPriorityName().equalsIgnoreCase("Low"))
                .count();

        model.addAttribute("highPriority", highPriority);
        model.addAttribute("mediumPriority", mediumPriority);
        model.addAttribute("lowPriority", lowPriority);

        // ✅ Recent 5 change requests
        List<ChangeRequest> recentChangeRequests = requests.stream()
                .filter(r -> r.getCreatedAt() != null)
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .limit(5)
                .toList();

        model.addAttribute("recentChangeRequests", recentChangeRequests);
        model.addAttribute("hodName", hod.getName());

        // ✅ Return HoD dashboard view
        return "hod_change_request_dashboard";
    }

}
