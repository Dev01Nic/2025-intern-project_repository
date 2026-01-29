package com.portal.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.portal.model.ChangeRequest;
import com.portal.model.Department;
import com.portal.model.DepartmentUser;
import com.portal.model.IssueTracking;
import com.portal.model.ProjectDetails;
import com.portal.model.TechnicalUser;
import com.portal.repository.DepartmentUserRepository;
import com.portal.repository.ProjectDetailsRepository;
import com.portal.repository.StatusRepository;
import com.portal.repository.CategoryRepository;
import com.portal.repository.ChangeRequestRepository;
import com.portal.repository.IssueTrackingRepository;
import com.portal.repository.PriorityRepository;
import com.portal.repository.TechnicalUserRepository;

@Controller
public class DepartmentUserController {

    private final ProjectDetailsRepository projectDetailsRepository;
    private final DepartmentUserRepository departmentUserRepository;
    private final TechnicalUserRepository technicalUserRepository;
    private final CategoryRepository categoryRepository;
    private final IssueTrackingRepository issueTrackingRepository;
    private final PriorityRepository priorityRepository;
    private final StatusRepository statusRepository;
    private final ChangeRequestRepository changeRequestRepository;
    
    public DepartmentUserController(ProjectDetailsRepository projectDetailsRepository,
                                    DepartmentUserRepository departmentUserRepository,
                                    TechnicalUserRepository technicalUserRepository,
                                    CategoryRepository categoryRepository,
                                    IssueTrackingRepository issueTrackingRepository,
                                    PriorityRepository priorityRepository,
                                    StatusRepository statusRepository,
                                    ChangeRequestRepository changeRequestRepository) {
        this.projectDetailsRepository = projectDetailsRepository;
        this.departmentUserRepository = departmentUserRepository;
        this.technicalUserRepository = technicalUserRepository;
        this.categoryRepository = categoryRepository;
        this.issueTrackingRepository = issueTrackingRepository;
        this.priorityRepository = priorityRepository;
        this.statusRepository = statusRepository;
        this.changeRequestRepository = changeRequestRepository;
    }
    // ----------------- Dashboard -----------------
    @GetMapping("/department/dashboard")
    public String departmentDashboard(Model model, Authentication authentication) {
        String username = authentication.getName();
        DepartmentUser user = departmentUserRepository.findByUsername(username);

        if (user == null) {
            model.addAttribute("error", "Department user not found!");
            return "error";
        }

        // ✅ Department name (first if multiple)
        String departmentName = (!user.getDepartments().isEmpty())
                ? user.getDepartments().get(0).getDeptName()
                : "N/A";

        model.addAttribute("username", user.getName());
        model.addAttribute("departmentName", departmentName);

        // ✅ Fetch department projects
        List<ProjectDetails> projects = projectDetailsRepository.findByDepartmentIn(user.getDepartments());
        model.addAttribute("totalProjects", projects.size());

        // ✅ Fetch all issues linked to the user's departments
        List<IssueTracking> allIssues = issueTrackingRepository.findAll()
                .stream()
                .filter(i -> user.getDepartments().contains(i.getProject().getDepartment()))
                .toList();

        // ✅ Total issues
        model.addAttribute("totalIssues", allIssues.size());

        // ✅ Fetch change requests linked to the user's departments
        List<ChangeRequest> allChangeRequests = changeRequestRepository.findAll()
                .stream()
                .filter(cr -> user.getDepartments().contains(cr.getProject().getDepartment()))
                .toList();

        model.addAttribute("totalChangeRequests", allChangeRequests.size());

        // ✅ Get recent 5 issues
        List<IssueTracking> recentIssues = allIssues.stream()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .limit(5)
                .toList();

        model.addAttribute("recentIssues", recentIssues);
        
     // ✅ Get recent 5 change requests
        List<ChangeRequest> recentChangeRequests = allChangeRequests.stream()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .limit(5)
                .toList();

        model.addAttribute("recentChangeRequests", recentChangeRequests);

        return "department_dashboard";
    }



    // ----------------- Project List -----------------
    @GetMapping("/department/projects")
    public String viewProjects(@RequestParam(required = false) Long categoryId,
    		@RequestParam(required = false) Long statusId,

                               Model model,
                               Authentication authentication) {
        String username = authentication.getName();

        DepartmentUser user = departmentUserRepository.findByUsername(username);
        if (user == null) {
            model.addAttribute("error", "Department user not found!");
            return "error";
        }
        String departmentName = (!user.getDepartments().isEmpty())
        	    ? user.getDepartments().get(0).getDeptName()
        	    : "N/A";

        model.addAttribute("loggedUser", user.getName());
        model.addAttribute("departmentName", departmentName);
        


        List<Department> departments = user.getDepartments();
        List<ProjectDetails> projects = projectDetailsRepository.findByDepartmentIn(departments);

        model.addAttribute("projects", projects);
        model.addAttribute("departmentUser", user);
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("statuses", statusRepository.findAll());

        model.addAttribute("selectedCategory", categoryId);
        model.addAttribute("selectedStatus", statusId);

        return "projects_lists";
    }

    @GetMapping("/department/projects/dashboard")
    public String projectDashboard(Model model, Authentication authentication) {

        // 1️⃣ Logged-in Department User
        String username = authentication.getName();
        DepartmentUser user = departmentUserRepository.findByUsername(username);

        if (user == null) {
            model.addAttribute("error", "User not found!");
            return "error";
        }

        // 2️⃣ Get all departments assigned to the user
        List<Department> departments = user.getDepartments();

        // 3️⃣ Get all projects under those departments
        List<ProjectDetails> projects = projectDetailsRepository.findByDepartmentIn(departments);

        // 4️⃣ Total Projects
        model.addAttribute("totalProjects", projects.size());

        // 5️⃣ Count by STATUS → with null safety
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

        // Add values for charts + summary cards
        model.addAttribute("notStarted", notStarted);
        model.addAttribute("inProgress", inProgress);
        model.addAttribute("completed", completed);
        model.addAttribute("onHold", onHold);

        model.addAttribute("activeProjects", inProgress);  // Active = In Progress
        model.addAttribute("completedProjects", completed);
        model.addAttribute("onHoldProjects", onHold);

        // 6️⃣ Count project categories (optional)
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

        // 7️⃣ Recent 5 Projects (sorted)
        List<ProjectDetails> recentProjects = projects.stream()
                .filter(p -> p.getStartDate() != null)
                .sorted((a, b) -> b.getStartDate().compareTo(a.getStartDate()))
                .limit(5)
                .toList();

        model.addAttribute("recentProjects", recentProjects);

        return "projects_lists_1";
    }


    // ----------------- Issue List -----------------
    @GetMapping("/department/issues")
    public String viewIssues(@RequestParam(required = false) Long priorityId,
                             @RequestParam(required = false) Long statusId,
                             Model model,
                             Authentication authentication) {
        String username = authentication.getName();

        DepartmentUser user = departmentUserRepository.findByUsername(username);
        if (user == null) {
            model.addAttribute("error", "Department user not found!");
            return "error";
        }

        String departmentName = (!user.getDepartments().isEmpty())
        	    ? user.getDepartments().get(0).getDeptName()
        	    : "N/A";

        model.addAttribute("loggedUser", user.getName());
        model.addAttribute("departmentName", departmentName);

        // Only show issues reported by this department user
        List<IssueTracking> issues = issueTrackingRepository.findAll()
                .stream()
                .filter(i -> i.getReportedBy().equals(user))
                .toList();

        model.addAttribute("issues", issues);
        model.addAttribute("departmentUser", user);

        List<Department> departments = user.getDepartments();
        List<ProjectDetails> projects = projectDetailsRepository.findByDepartmentIn(departments);

        model.addAttribute("projects", projects);
        model.addAttribute("departmentUser", user);
        model.addAttribute("priorities", priorityRepository.findAll());
        model.addAttribute("statuses", statusRepository.findAll());

        model.addAttribute("selectedPriority", priorityId);
        model.addAttribute("selectedStatus", statusId);

        return "issue_tracking";
    }

    // ----------------- Show Issue Form -----------------
    @GetMapping("/department/issues/new")
    public String showIssueForm(Model model, Authentication authentication) {
        String username = authentication.getName();
        DepartmentUser currentUser = departmentUserRepository.findByUsername(username);

        IssueTracking issue = new IssueTracking();
        issue.setReportedBy(currentUser); // default reporter

        model.addAttribute("issue", issue);
        model.addAttribute("projects", projectDetailsRepository.findByDepartmentIn(currentUser.getDepartments()));
        model.addAttribute("priorities", priorityRepository.findAll());
        model.addAttribute("statuses", statusRepository.findAll());
        model.addAttribute("technicalUsers", technicalUserRepository.findAll()); // Assigned to options

        return "issue_form";
    }

    // ----------------- Save Issue -----------------
    @PostMapping("/department/issues/save")
    public String saveIssue(@ModelAttribute("issue") IssueTracking issue, Authentication authentication) {
        DepartmentUser currentUser = departmentUserRepository.findByUsername(authentication.getName());
        issue.setReportedBy(currentUser);
        issue.setCreatedAt(LocalDateTime.now());
        issue.setUpdatedAt(LocalDateTime.now());

        issueTrackingRepository.save(issue);

        return "redirect:/department/issues";
    }
 // ----------------- Cancel Issue -----------------
    @PostMapping("/department/issues/cancel")
    public String cancelIssue(@RequestParam Long issueId, 
                             @RequestParam String remarks,
                             Authentication authentication) {
        IssueTracking issue = issueTrackingRepository.findById(issueId)
                .orElseThrow(() -> new RuntimeException("Issue not found"));
        
        // Update status to "Cancelled"
        issue.setStatus(statusRepository.findByName("Cancelled")
                .orElseThrow(() -> new RuntimeException("Cancelled status not found")));
        issue.setUpdatedAt(LocalDateTime.now());
        
        // You can store remarks in a separate field if needed
        // For now, we'll just update the status
        
        issueTrackingRepository.save(issue);
        
        return "redirect:/department/issues";
    }

    // ----------------- Close Issue -----------------
    @PostMapping("/department/issues/close")
    public String closeIssue(@RequestParam Long issueId,
                            @RequestParam String remarks,
                            Authentication authentication) {
        IssueTracking issue = issueTrackingRepository.findById(issueId)
                .orElseThrow(() -> new RuntimeException("Issue not found"));
        
        // Update status to "Completed"
        issue.setStatus(statusRepository.findByName("Completed")
                .orElseThrow(() -> new RuntimeException("Completed status not found")));
        issue.setUpdatedAt(LocalDateTime.now());
        
        // You can store remarks in a separate field if needed
        
        issueTrackingRepository.save(issue);
        
        return "redirect:/department/issues";
    }

 // ----------------- Issue Tracking Dashboard -----------------
    @GetMapping("/department/issues/dashboard")
    public String issueTrackingDashboard(Model model, Authentication authentication) {
        String username = authentication.getName();
        DepartmentUser user = departmentUserRepository.findByUsername(username);

        if (user == null) {
            model.addAttribute("error", "Department user not found!");
            return "error";
        }

        // ✅ Fetch all issues linked to user’s departments
        List<IssueTracking> issues = issueTrackingRepository.findAll()
                .stream()
                .filter(i -> user.getDepartments().contains(i.getProject().getDepartment()))
                .toList();

        model.addAttribute("totalIssues", issues.size());

        // ✅ Count by Status
        long openCount = issues.stream().filter(i -> i.getStatus().getName().equalsIgnoreCase("Open")).count();
        long inProgressCount = issues.stream().filter(i -> i.getStatus().getName().equalsIgnoreCase("In Progress")).count();
        long resolvedCount = issues.stream().filter(i -> i.getStatus().getName().equalsIgnoreCase("Resolved")).count();
        long closedCount = issues.stream().filter(i -> i.getStatus().getName().equalsIgnoreCase("Closed")).count();

        model.addAttribute("openCount", openCount);
        model.addAttribute("inProgressCount", inProgressCount);
        model.addAttribute("resolvedCount", resolvedCount);
        model.addAttribute("closedCount", closedCount);

        // ✅ Count by Priority
        long highPriority = issues.stream().filter(i -> i.getPriority().getPriorityName().equalsIgnoreCase("High")).count();
        long mediumPriority = issues.stream().filter(i -> i.getPriority().getPriorityName().equalsIgnoreCase("Medium")).count();
        long lowPriority = issues.stream().filter(i -> i.getPriority().getPriorityName().equalsIgnoreCase("Low")).count();

        model.addAttribute("highPriority", highPriority);
        model.addAttribute("mediumPriority", mediumPriority);
        model.addAttribute("lowPriority", lowPriority);

        // ✅ Recent 5 issues for quick view
        List<IssueTracking> recentIssues = issues.stream()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .limit(5)
                .toList();

        model.addAttribute("recentIssues", recentIssues);

        model.addAttribute("loggedUser", user.getName());

        return "issue_dashboard";
    }

    @GetMapping("/department/change-requests")
    public String viewChangeRequests(@RequestParam(required = false) Long priorityId,
                                     @RequestParam(required = false) Long statusId,
                                     Model model,
                                     Authentication authentication) {
        String username = authentication.getName();
        DepartmentUser user = departmentUserRepository.findByUsername(username);
        if (user == null) {
            model.addAttribute("error", "Department user not found!");
            return "error";
        }
        String departmentName = (!user.getDepartments().isEmpty())
        	    ? user.getDepartments().get(0).getDeptName()
        	    : "N/A";

        model.addAttribute("loggedUser", user.getName());
        model.addAttribute("departmentName", departmentName);

        // Only requests created by this department user
        List<ChangeRequest> changeRequests = changeRequestRepository.findAll()
                .stream()
                .filter(cr -> cr.getRequestedBy().equals(user))
                .toList();

        model.addAttribute("changeRequests", changeRequests);
        model.addAttribute("priorities", priorityRepository.findAll());
        model.addAttribute("statuses", statusRepository.findAll());
        model.addAttribute("departmentUser", user);

        model.addAttribute("selectedPriority", priorityId);
        model.addAttribute("selectedStatus", statusId);

        return "change_requests";
    }

    // ----------------- Show Form -----------------
    @GetMapping("/department/change-requests/new")
    public String showChangeRequestForm(Model model, Authentication authentication) {
        String username = authentication.getName();
        DepartmentUser currentUser = departmentUserRepository.findByUsername(username);

        ChangeRequest cr = new ChangeRequest();
        cr.setRequestedBy(currentUser);

        model.addAttribute("changeRequest", cr);
        model.addAttribute("projects", projectDetailsRepository.findByDepartmentIn(currentUser.getDepartments()));
        model.addAttribute("priorities", priorityRepository.findAll());
        model.addAttribute("statuses", statusRepository.findAll());
        model.addAttribute("technicalUsers", technicalUserRepository.findAll());

        return "change_request_form";
    }
 // ----------------- Cancel Change Request -----------------
    @PostMapping("/department/change-requests/cancel")
    public String cancelChangeRequest(@RequestParam Long changeId, 
                                     @RequestParam String remarks,
                                     Authentication authentication) {
        ChangeRequest changeRequest = changeRequestRepository.findById(changeId)
                .orElseThrow(() -> new RuntimeException("Change request not found"));
        
        // Update status to "Cancelled"
        changeRequest.setStatus(statusRepository.findByName("Cancelled")
                .orElseThrow(() -> new RuntimeException("Cancelled status not found")));
        changeRequest.setUpdatedAt(LocalDateTime.now());
        
        // Store cancellation remarks if you have a field for it
        // changeRequest.setCancellationRemarks(remarks);
        
        changeRequestRepository.save(changeRequest);
        
        return "redirect:/department/change-requests";
    }

    // ----------------- Close Change Request -----------------
    @PostMapping("/department/change-requests/close")
    public String closeChangeRequest(@RequestParam Long changeId,
                                    @RequestParam String remarks,
                                    Authentication authentication) {
        ChangeRequest changeRequest = changeRequestRepository.findById(changeId)
                .orElseThrow(() -> new RuntimeException("Change request not found"));
        
        // Update status to "Completed"
        changeRequest.setStatus(statusRepository.findByName("Completed")
                .orElseThrow(() -> new RuntimeException("Completed status not found")));
        changeRequest.setUpdatedAt(LocalDateTime.now());
        
        // Store completion remarks if you have a field for it
        // changeRequest.setCompletionRemarks(remarks);
        
        changeRequestRepository.save(changeRequest);
        
        return "redirect:/department/change-requests";
    }
    // ----------------- Save Change Request -----------------
    @PostMapping("/department/change-requests/save")
    public String saveChangeRequest(@ModelAttribute("changeRequest") ChangeRequest cr,
                                    Authentication authentication) {
        DepartmentUser currentUser = departmentUserRepository.findByUsername(authentication.getName());
        cr.setRequestedBy(currentUser);
        cr.setCreatedAt(LocalDateTime.now());
        cr.setUpdatedAt(LocalDateTime.now());

        changeRequestRepository.save(cr);
        return "redirect:/department/change-requests";
    }
    @GetMapping("/department/change-requests/dashboard")
    public String changeRequestDashboard(Model model, Authentication authentication) {
        String username = authentication.getName();
        DepartmentUser user = departmentUserRepository.findByUsername(username);

        List<ChangeRequest> requests = changeRequestRepository.findAll()
                .stream()
                .filter(r -> user.getDepartments().contains(r.getProject().getDepartment()))
                .toList();

        model.addAttribute("totalRequests", requests.size());
        long pendingCount = requests.stream().filter(r -> r.getStatus().getName().equalsIgnoreCase("Pending")).count();
        long approvedCount = requests.stream().filter(r -> r.getStatus().getName().equalsIgnoreCase("Approved")).count();
        long rejectedCount = requests.stream().filter(r -> r.getStatus().getName().equalsIgnoreCase("Rejected")).count();

        model.addAttribute("pendingCount", pendingCount);
        model.addAttribute("approvedCount", approvedCount);
        model.addAttribute("rejectedCount", rejectedCount);

        long highPriority = requests.stream().filter(r -> r.getPriority().getPriorityName().equalsIgnoreCase("High")).count();
        long mediumPriority = requests.stream().filter(r -> r.getPriority().getPriorityName().equalsIgnoreCase("Medium")).count();
        long lowPriority = requests.stream().filter(r -> r.getPriority().getPriorityName().equalsIgnoreCase("Low")).count();

        model.addAttribute("highPriority", highPriority);
        model.addAttribute("mediumPriority", mediumPriority);
        model.addAttribute("lowPriority", lowPriority);

        List<ChangeRequest> recentChangeRequests = requests.stream()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .limit(5)
                .toList();

        model.addAttribute("recentChangeRequests", recentChangeRequests);

        return "change_request_dashboard";
    }

}
