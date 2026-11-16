package com.example.werewolf.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.werewolf.entity.Game;
import com.example.werewolf.entity.GamePlayer;
import com.example.werewolf.entity.GamePlayerHand;
import com.example.werewolf.entity.Phase;
import com.example.werewolf.repository.CardRepository;
import com.example.werewolf.repository.GamePlayerHandRepository;
import com.example.werewolf.repository.GamePlayerRepository;
import com.example.werewolf.repository.RoleRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class HandServiceTest {

	@Autowired
	private HandService handService;

	@Autowired
	private GameStartService gameStartService;

	@Autowired
	private PhaseService phaseService;

	@Autowired
	private GamePlayerRepository gamePlayerRepository;

	@Autowired
	private GamePlayerHandRepository gamePlayerHandRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private CardRepository cardRepository;

	private GamePlayer findByRoleName(Game game, String roleName) {
		return gamePlayerRepository.findByGameId(game.getId()).stream()
				.filter(player -> roleName.equals(roleRepository.findById(player.getRoleId()).orElseThrow().getName()))
				.findFirst()
				.orElseThrow();
	}

	private List<String> inHandCardNames(GamePlayer player) {
		return gamePlayerHandRepository.findByGamePlayerIdAndStatus(player.getId(), "in_hand").stream()
				.map(GamePlayerHand::getCardId)
				.map(cardId -> cardRepository.findById(cardId).orElseThrow().getName())
				.toList();
	}

	@Test
	void 試合開始後の占い師の手札に保証枠が入っている() {
		Game game = gameStartService.startGame();

		assertThat(inHandCardNames(findByRoleName(game, "占い師")))
				.containsExactlyInAnyOrder("占い", "名乗り", "結果報告");
	}

	@Test
	void 村人の手札は投票観察だけ() {
		Game game = gameStartService.startGame();

		assertThat(inHandCardNames(findByRoleName(game, "村人"))).containsExactly("投票観察");
	}

	@Test
	void 同じゲームで2回配っても二重にならない() {
		Game game = gameStartService.startGame();

		handService.dealGuaranteedCards(game);

		assertThat(inHandCardNames(findByRoleName(game, "占い師")))
				.containsExactlyInAnyOrder("占い", "名乗り", "結果報告");
	}

	@Test
	void 朝から昼に進むとPhaseService経由で保証枠が配られる() {
		Game game = gameStartService.startGame();
		GamePlayer seer = findByRoleName(game, "占い師");
		gamePlayerHandRepository.deleteAll(gamePlayerHandRepository.findByGamePlayerIdAndStatus(seer.getId(), "in_hand"));
		game.setDayNumber(2);
		game.setCurrentPhase(Phase.MORNING);

		phaseService.advancePhase(game);

		assertThat(inHandCardNames(seer)).containsExactlyInAnyOrder("占い", "名乗り", "結果報告");
	}

	@Test
	void 試合がnullなら例外() {
		org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class,
				() -> handService.dealGuaranteedCards(null));
	}
}
