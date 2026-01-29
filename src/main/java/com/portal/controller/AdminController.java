package com.portal.controller;

import com.portal.model.Department;
import com.portal.model.Role;
import com.portal.model.TechnicalUser;
import com.portal.model.DataCenter;
import com.portal.repository.DepartmentRepository;
import com.portal.repository.RoleRepository;
import com.portal.repository.TechnicalUserRepository;
import com.portal.repository.DepartmentUserRepository;
import com.portal.repository.DataCenterRepository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final DataCenterRepository dataCenterRepository;
    private final PasswordEncoder passwordEncoder;
    
    public AdminController(DepartmentRepository departmentRepository,
                           TechnicalUserRepository userRepository,
                           RoleRepository roleRepository,
                           TechnicalUserRepository technicalUserRepository,
                           DepartmentUserRepository departmentUserRepository,
                           DataCenterRepository dataCenterRepository,
                           PasswordEncoder passwordEncoder) {
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.technicalUserRepository = technicalUserRepository;
        this.departmentUserRepository = departmentUserRepository;
        this.dataCenterRepository = dataCenterRepository;
        this.passwordEncoder = passwordEncoder;
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
    @PostMapping("/departments/update/{id}")
    public String updateDepartment(@PathVariable("id") Long id,
                                   @ModelAttribute("department") Department department) {

        department.setDeptId(id);
        departmentRepository.save(department);

        return "redirect:/admin/departments";
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
    @PostMapping("/users/update/{id}")
    public String updateUser(@PathVariable("id") Long id,
                             @ModelAttribute("user") TechnicalUser user,
                             @RequestParam("roleId") Long roleId,
                             @RequestParam(value = "departmentIds", required = false) List<Long> departmentIds) {

        user.setId(id);

        // Role
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid role Id:" + roleId));
        user.setRole(role);

        // Departments
        if (departmentIds != null && !departmentIds.isEmpty()) {
            List<Department> departments = departmentRepository.findAllById(departmentIds);
            user.setDepartment(departments);
        } else {
            user.setDepartment(null);
        }

        // Password
        TechnicalUser existing = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

        if (user.getPassword() == null || user.getPassword().isBlank()) {
            user.setPassword(existing.getPassword());
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        userRepository.save(user);

        return "redirect:/admin/users";
    }

    @PostMapping("/users/save")
    public String saveUser(@ModelAttribute("user") TechnicalUser user,
                           @RequestParam("roleId") Long roleId,
                           @RequestParam(value = "departmentIds", required = false) List<Long> departmentIds) {

        // Load role
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid role Id:" + roleId));
        user.setRole(role);

        // Load departments
        if (departmentIds != null && !departmentIds.isEmpty()) {
            List<Department> departments = departmentRepository.findAllById(departmentIds);
            user.setDepartment(departments);
        } else {
            user.setDepartment(null);
        }

        // ⭐ PASSWORD HANDLING ⭐

        // Case 1: New user → encode password
        if (user.getId() == null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));

        } else {
            // Case 2: Existing user → fetch old user
            TechnicalUser existing = userRepository.findById(user.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + user.getId()));

            if (user.getPassword() == null || user.getPassword().isBlank()) {
                // No new password entered → keep old encoded password
                user.setPassword(existing.getPassword());
            } else {
                // New password entered → encode new one
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
        }

        // Save user
        userRepository.save(user);

        return "redirect:/admin/users";
    }



    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return "redirect:/admin/users";
    }
    @GetMapping("/datacenters")
    public String listDataCenters(Model model) {
        model.addAttribute("datacenters", dataCenterRepository.findAll());
        model.addAttribute("newDataCenter", new DataCenter());
        return "admin_datacenters";
    }

    // SAVE NEW
    @PostMapping("/datacenters/save")
    public String saveDataCenter(@ModelAttribute("newDataCenter") DataCenter datacenter) {
        dataCenterRepository.save(datacenter);
        return "redirect:/admin/datacenters";
    }
    @GetMapping("/datacenters/edit/{id}")
    public String editDataCenterForm(@PathVariable Long id, Model model) {
        DataCenter datacenter = dataCenterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid DataCenter Id: " + id));
        model.addAttribute("datacenter", datacenter);
        return "admin_datacenters_form";
    }

    // UPDATE
    @PostMapping("/datacenters/update/{id}")
    public String updateDataCenter(
            @PathVariable Long id,
            @ModelAttribute("datacenter") DataCenter updatedDataCenter) {

        DataCenter dc = dataCenterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid DataCenter Id:" + id));

        dc.setName(updatedDataCenter.getName());
        dc.setLocation(updatedDataCenter.getLocation());
        dc.setType(updatedDataCenter.getType());
        dc.setCapacity(updatedDataCenter.getCapacity());
        dc.setContactPerson(updatedDataCenter.getContactPerson());
        dc.setContactNo(updatedDataCenter.getContactNo());
        dc.setEmail(updatedDataCenter.getEmail());
        dc.setStatus(updatedDataCenter.getStatus());
        dc.setEstablishedDate(updatedDataCenter.getEstablishedDate());
        dc.setDescription(updatedDataCenter.getDescription());

        dataCenterRepository.save(dc);

        return "redirect:/admin/datacenters";
    }

    // DELETE
    @GetMapping("/datacenters/delete/{id}")
    public String deleteDataCenter(@PathVariable Long id) {
        dataCenterRepository.deleteById(id);
        return "redirect:/admin/datacenters";
    }
    @GetMapping("/datacenters/new")
    public String newDataCenterForm(Model model) {
        model.addAttribute("datacenter", new DataCenter());
        return "admin_datacenters_form";
    }
}
