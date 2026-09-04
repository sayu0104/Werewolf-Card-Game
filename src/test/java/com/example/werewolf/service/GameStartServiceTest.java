package com.example.werewolf.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.werewolf.entity.Game;
import com.example.werewolf.entity.GamePlayer;
import com.example.werewolf.entity.Role;
import com.example.werewolf.repository.GamePlayerRepository;
import com.example.werewolf.repository.RoleRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class GameStartServiceTest {

	@Autowired
	private GameStartService gameStartService;

	@Autowired
	private GamePlayerRepository gamePlayerRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Test
	void 試合を開始するとGameが1件保存される() {
		Game game = gameStartService.startGame();

		assertThat(game.getId()).isNotNull();
		assertThat(game.getStatus()).isEqualTo("in_progress");
		assertThat(game.getDayNumber()).isEqualTo(1);
		assertThat(game.getIsSinglePlayer()).isTrue();
	}

	@Test
	void 試合を開始するとGamePlayerが6人保存される() {
		Game game = gameStartService.startGame();

		List<GamePlayer> players = gamePlayerRepository.findAll().stream()
				.filter(player -> player.getGameId().equals(game.getId()))
				.toList();

		assertThat(players).hasSize(6);
		List<Integer> seatOrders = players.stream().map(GamePlayer::getSeatOrder).sorted().toList();
		assertThat(seatOrders).containsExactly(1, 2, 3, 4, 5, 6);
	}

	@Test
	void 六人全員に役職が割り当てられ内訳が想定通りになる() {
		Game game = gameStartService.startGame();

		List<GamePlayer> players = gamePlayerRepository.findAll().stream()
				.filter(player -> player.getGameId().equals(game.getId()))
				.toList();

		Map<String, Long> countByName = new HashMap<>();
		for (GamePlayer player : players) {
			assertThat(player.getRoleId()).isNotNull();
			Role role = roleRepository.findById(player.getRoleId()).orElseThrow();
			countByName.merge(role.getName(), 1L, Long::sum);
		}

		assertThat(countByName.get("人狼")).isEqualTo(1);
		assertThat(countByName.get("占い師")).isEqualTo(1);
		assertThat(countByName.get("狩人")).isEqualTo(1);
		assertThat(countByName.get("狂人")).isEqualTo(1);
		assertThat(countByName.get("村人")).isEqualTo(2);
	}
}
