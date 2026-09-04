package com.example.werewolf.service;

import com.example.werewolf.entity.Game;
import com.example.werewolf.entity.GamePlayer;
import com.example.werewolf.repository.GameRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class GameStartService {

	private static final int PLAYER_COUNT = 6;

	private final GameRepository gameRepository;
	private final RoleAssignmentService roleAssignmentService;

	public GameStartService(GameRepository gameRepository, RoleAssignmentService roleAssignmentService) {
		this.gameRepository = gameRepository;
		this.roleAssignmentService = roleAssignmentService;
	}

	public Game startGame() {
		Game game = new Game("in_progress");
		game.setDayNumber(1);
		game.setIsSinglePlayer(true);
		game = gameRepository.save(game);

		List<GamePlayer> players = new ArrayList<>();
		for (int seatOrder = 1; seatOrder <= PLAYER_COUNT; seatOrder++) {
			players.add(new GamePlayer(game.getId(), null, seatOrder));
		}

		roleAssignmentService.assignRoles(players);

		return game;
	}
}
