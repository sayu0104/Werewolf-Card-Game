package com.example.werewolf.controller;

import com.example.werewolf.repository.RoleRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminRoleController {

    private final RoleRepository roleRepository;

    public AdminRoleController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @GetMapping("/admin/roles")
    public String listRoles(Model model) {
        model.addAttribute("roles", roleRepository.findAll());
        return "role-list";
    }
}
