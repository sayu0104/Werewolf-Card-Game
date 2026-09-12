package com.example.werewolf.controller;

import com.example.werewolf.entity.Game;
import com.example.werewolf.entity.GamePlayer;
import com.example.werewolf.entity.Role;
import com.example.werewolf.repository.GamePlayerRepository;
import com.example.werewolf.repository.GameRepository;
import com.example.werewolf.repository.RoleRepository;
import com.example.werewolf.service.GameStartService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class GameController {

	private final GameStartService gameStartService;
	private final GameRepository gameRepository;
	private final GamePlayerRepository gamePlayerRepository;
	private final RoleRepository roleRepository;

	public GameController(GameStartService gameStartService, GameRepository gameRepository,
			GamePlayerRepository gamePlayerRepository, RoleRepository roleRepository) {
		this.gameStartService = gameStartService;
		this.gameRepository = gameRepository;
		this.gamePlayerRepository = gamePlayerRepository;
		this.roleRepository = roleRepository;
	}

	@PostMapping("/game/start")
	public String startGame() {
		Game game = gameStartService.startGame();
		return "redirect:/game/" + game.getId();
	}

	@GetMapping("/game/{id}")
	public String showGame(@PathVariable Long id, Model model) {
		Game game = gameRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("試合が見つからない: " + id));

		List<GamePlayer> gamePlayers = gamePlayerRepository.findByGameIdOrderBySeatOrderAsc(id);

		List<PlayerView> playerViews = new ArrayList<>();
		for (GamePlayer gamePlayer : gamePlayers) {
			Role role = roleRepository.findById(gamePlayer.getRoleId())
					.orElseThrow(() -> new IllegalArgumentException("役職が見つからない: " + gamePlayer.getRoleId()));
			playerViews.add(new PlayerView(gamePlayer.getSeatOrder(), role.getName(), gamePlayer.getIsAlive()));
		}

		model.addAttribute("gameId", game.getId());
		model.addAttribute("currentPhase", game.getCurrentPhase());
		model.addAttribute("players", playerViews);

		return "game";
	}
}
