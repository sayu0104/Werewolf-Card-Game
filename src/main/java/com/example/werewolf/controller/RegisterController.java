package com.example.werewolf.controller;

import com.example.werewolf.entity.User;
import com.example.werewolf.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegisterController {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public RegisterController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@GetMapping("/register")
	public String showRegisterForm() {
		return "register";
	}

	@PostMapping("/register")
	public String register(
			@RequestParam String username,
			@RequestParam String email,
			@RequestParam String password) {
		userRepository.save(new User(username, passwordEncoder.encode(password), email));
		return "redirect:/login";
	}
}
