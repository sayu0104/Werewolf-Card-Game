package com.example.werewolf.service;

import com.example.werewolf.entity.Game;
import com.example.werewolf.entity.Phase;
import com.example.werewolf.repository.GameRepository;
import org.springframework.stereotype.Service;

@Service
public class PhaseService {

	private final GameRepository gameRepository;

	public PhaseService(GameRepository gameRepository) {
		this.gameRepository = gameRepository;
	}

	public Game advancePhase(Game game) {
		Phase currentPhase = game.getCurrentPhase();

		switch (currentPhase) {
			case MORNING -> game.setCurrentPhase(Phase.DAY);
			case DAY -> {
				if (game.getDayNumber() == 1) {
					game.setCurrentPhase(Phase.NIGHT1);
				} else {
					game.setCurrentPhase(Phase.VOTE);
				}
			}
			case VOTE -> game.setCurrentPhase(Phase.NIGHT1);
			case NIGHT1 -> game.setCurrentPhase(Phase.NIGHT2);
			case NIGHT2 -> {
				game.setCurrentPhase(Phase.MORNING);
				game.setDayNumber(game.getDayNumber() + 1);
			}
		}

		return gameRepository.save(game);
	}
}
