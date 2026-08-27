package com.example.werewolf.controller;

import com.example.werewolf.entity.Card;
import com.example.werewolf.repository.CardRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminCardController {

	private final CardRepository cardRepository;

	public AdminCardController(CardRepository cardRepository) {
		this.cardRepository = cardRepository;
	}

	@GetMapping("/admin/cards")
	public String listCards(Model model) {
		model.addAttribute("cards", cardRepository.findAll());
		return "card-list";
	}

	@PostMapping("/admin/cards")
	public String createCard(
			@RequestParam String name,
			@RequestParam String cardType,
			@RequestParam(required = false) Long requiredRoleId,
			@RequestParam Integer cost,
			@RequestParam(required = false, defaultValue = "false") Boolean isMandatory,
			@RequestParam(required = false, defaultValue = "false") Boolean isRare,
			@RequestParam(required = false) Integer value,
			@RequestParam(required = false, defaultValue = "false") Boolean isUsagePublic,
			@RequestParam(required = false) String description) {
		Card card = new Card();
		card.setName(name);
		card.setCardType(cardType);
		card.setRequiredRoleId(requiredRoleId);
		card.setCost(cost);
		card.setIsMandatory(isMandatory);
		card.setIsRare(isRare);
		card.setValue(value);
		card.setIsUsagePublic(isUsagePublic);
		card.setDescription(description);
		cardRepository.save(card);
		return "redirect:/admin/cards";
	}

	@PostMapping("/admin/cards/{id}/delete")
	public String deleteCard(@PathVariable Long id) {
		cardRepository.deleteById(id);
		return "redirect:/admin/cards";
	}
}
