package com.portal.controller;

import com.portal.model.Department;
import com.portal.model.Role;
import com.portal.model.TechnicalUser;
import com.portal.repository.DepartmentRepository;
import com.portal.repository.RoleRepository;
import com.portal.repository.TechnicalUserRepository;
import com.portal.repository.DepartmentUserRepository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final DepartmentRepository departmentRepository;
    private final TechnicalUserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TechnicalUserRepository technicalUserRepository;
    private final DepartmentUserRepository departmentUserRepository;

    public AdminController(DepartmentRepository departmentRepository,
                           TechnicalUserRepository userRepository,
                           RoleRepository roleRepository,
                           TechnicalUserRepository technicalUserRepository,
                           DepartmentUserRepository departmentUserRepository) {
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.technicalUserRepository = technicalUserRepository;
        this.departmentUserRepository = departmentUserRepository;
    }

    // ===== Dashboard =====
    @GetMapping
    public String adminDashboard(Model model) {

        // ===== Basic Counts =====
        long totalDepartments = departmentRepository.count();
        long totalTechnicalUsers = technicalUserRepository.count();
        long totalDepartmentUsers = departmentUserRepository.count();

        model.addAttribute("totalDepartments", totalDepartments);
        model.addAttribute("totalTechnicalUsers", totalTechnicalUsers);
        model.addAttribute("totalDepartmentUsers", totalDepartmentUsers);

        // ===== Recent Departments (latest 5) =====
        List<Department> recentDepartments = departmentRepository.findAll()
                .stream()
                .sorted((a, b) -> Long.compare(b.getDeptId(), a.getDeptId())) // sort by latest
                .limit(5)
                .collect(Collectors.toList());
        model.addAttribute("recentDepartments", recentDepartments);

        // ===== Recent Technical Users (latest 5) =====
        List<TechnicalUser> recentUsers = technicalUserRepository.findAll()
                .stream()
                .sorted((a, b) -> Long.compare(b.getId(), a.getId())) // sort by latest
                .limit(5)
                .collect(Collectors.toList());
        model.addAttribute("recentUsers", recentUsers);

        return "admin"; // Thymeleaf template
    }

    // ===== Departments =====
    @GetMapping("/departments")
    public String listDepartments(Model model) {
        model.addAttribute("departments", departmentRepository.findAll());
        return "admin_departments";
    }

    @GetMapping("/departments/new")
    public String newDepartmentForm(Model model) {
        model.addAttribute("department", new Department());
        return "admin_department_form";
    }

    @PostMapping("/departments/save")
    public String saveDepartment(@ModelAttribute("department") Department department) {
        departmentRepository.save(department);
        return "redirect:/admin/departments";
    }

    @GetMapping("/departments/edit/{id}")
    public String editDepartmentForm(@PathVariable Long id, Model model) {
        Department dept = departmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid department Id:" + id));
        model.addAttribute("department", dept);
        return "admin_department_form";
    }

    @GetMapping("/departments/delete/{id}")
    public String deleteDepartment(@PathVariable Long id) {
        departmentRepository.deleteById(id);
        return "redirect:/admin/departments";
    }

    // ===== Users =====
    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "admin_users";
    }

    @GetMapping("/users/new")
    public String newUserForm(Model model) {
        model.addAttribute("user", new TechnicalUser());
        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute("departments", departmentRepository.findAll());
        return "admin_user_form";
    }

    @GetMapping("/users/edit/{id}")
    public String editUserForm(@PathVariable Long id, Model model) {
    	TechnicalUser user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        model.addAttribute("user", user);
        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute("departments", departmentRepository.findAll());
        return "admin_user_form";
    }

    @PostMapping("/users/save")
    public String saveUser(@ModelAttribute("user") TechnicalUser user,
                           @RequestParam("roleId") Long roleId,
                           @RequestParam(value = "departmentIds", required = false) List<Long> departmentIds) {

        // Set Role
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid role Id:" + roleId));
        user.setRole(role);

        // Set Departments (optional)
        if (departmentIds != null && !departmentIds.isEmpty()) {
            List<Department> departments = departmentRepository.findAllById(departmentIds);
            user.setDepartment(departments);     // List<Department>
        } else {
            user.setDepartment(null);
        }

        userRepository.save(user);
        return "redirect:/admin/users";
    }


    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return "redirect:/admin/users";
    }
}
