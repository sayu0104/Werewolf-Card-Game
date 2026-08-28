package com.example.werewolf.controller;

import com.example.werewolf.entity.Character;
import com.example.werewolf.repository.CharacterRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminCharacterController {

	private final CharacterRepository characterRepository;

	public AdminCharacterController(CharacterRepository characterRepository) {
		this.characterRepository = characterRepository;
	}

	@GetMapping("/admin/characters")
	public String listCharacters(Model model) {
		model.addAttribute("characters", characterRepository.findAll());
		return "character-list";
	}

	@PostMapping("/admin/characters")
	public String createCharacter(@RequestParam String name, @RequestParam(required = false) String personality,
			@RequestParam Integer optimality, @RequestParam Integer consistency,
			@RequestParam Integer deceptionSkill) {
		Character character = new Character();
		character.setName(name);
		character.setPersonality(personality);
		character.setOptimality(optimality);
		character.setConsistency(consistency);
		character.setDeceptionSkill(deceptionSkill);
		characterRepository.save(character);
		return "redirect:/admin/characters";
	}

	@PostMapping("/admin/characters/{id}/delete")
	public String deleteCharacter(@PathVariable Long id) {
		characterRepository.deleteById(id);
		return "redirect:/admin/characters";
	}
}
