package com.example.werewolf.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.werewolf.entity.Game;
import com.example.werewolf.entity.Phase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class PhaseServiceTest {

	@Autowired
	private PhaseService phaseService;

	@Autowired
	private GameStartService gameStartService;

	@Test
	void 朝の次は昼になる() {
		Game game = new Game("in_progress");
		game.setDayNumber(1);
		game.setCurrentPhase(Phase.MORNING);

		Game result = phaseService.advancePhase(game);

		assertThat(result.getCurrentPhase()).isEqualTo(Phase.DAY);
	}

	@Test
	void 昼の次は初日なら夜1になる() {
		Game game = new Game("in_progress");
		game.setDayNumber(1);
		game.setCurrentPhase(Phase.DAY);

		Game result = phaseService.advancePhase(game);

		assertThat(result.getCurrentPhase()).isEqualTo(Phase.NIGHT1);
	}

	@Test
	void 昼の次は二日目以降なら投票になる() {
		Game game = new Game("in_progress");
		game.setDayNumber(2);
		game.setCurrentPhase(Phase.DAY);

		Game result = phaseService.advancePhase(game);

		assertThat(result.getCurrentPhase()).isEqualTo(Phase.VOTE);
	}

	@Test
	void 投票の次は夜1になる() {
		Game game = new Game("in_progress");
		game.setDayNumber(2);
		game.setCurrentPhase(Phase.VOTE);

		Game result = phaseService.advancePhase(game);

		assertThat(result.getCurrentPhase()).isEqualTo(Phase.NIGHT1);
	}

	@Test
	void 夜1の次は夜2になる() {
		Game game = new Game("in_progress");
		game.setDayNumber(1);
		game.setCurrentPhase(Phase.NIGHT1);

		Game result = phaseService.advancePhase(game);

		assertThat(result.getCurrentPhase()).isEqualTo(Phase.NIGHT2);
	}

	@Test
	void 夜2の次は朝になりdayNumberが1増える() {
		Game game = new Game("in_progress");
		game.setDayNumber(1);
		game.setCurrentPhase(Phase.NIGHT2);

		Game result = phaseService.advancePhase(game);

		assertThat(result.getCurrentPhase()).isEqualTo(Phase.MORNING);
		assertThat(result.getDayNumber()).isEqualTo(2);
	}

	@Test
	void 試合開始直後のcurrentPhaseが昼になっている() {
		Game game = gameStartService.startGame();

		assertThat(game.getCurrentPhase()).isEqualTo(Phase.DAY);
	}
}
