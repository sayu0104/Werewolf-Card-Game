package com.example.werewolf.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.werewolf.entity.Game;
import com.example.werewolf.repository.NightActionRepository;
import com.example.werewolf.service.GameStartService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class GameControllerTest {

	@Autowired
	private GameController gameController;

	@Autowired
	private GameStartService gameStartService;

	@Autowired
	private NightActionRepository nightActionRepository;

	@Test
	void night1を二重に実行しても夜の行動の記録が増えない() {
		Game game = gameStartService.startGame();

		gameController.night1(game.getId());
		int firstCount = nightActionRepository.findByGameIdAndDayNumber(game.getId(), game.getDayNumber()).size();

		gameController.night1(game.getId());
		int secondCount = nightActionRepository.findByGameIdAndDayNumber(game.getId(), game.getDayNumber()).size();

		assertThat(firstCount).isGreaterThan(0);
		assertThat(secondCount).isEqualTo(firstCount);
	}
}
