package com.example.werewolf.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.werewolf.entity.GamePlayer;
import com.example.werewolf.entity.Role;
import com.example.werewolf.repository.GamePlayerRepository;
import com.example.werewolf.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class GameResultServiceTest {

	@Autowired
	private GameResultService gameResultService;

	@Autowired
	private GamePlayerRepository gamePlayerRepository;

	@Autowired
	private RoleRepository roleRepository;

	private Long werewolfRoleId() {
		return roleRepository.findByName("人狼").orElseThrow().getId();
	}

	private Long villagerRoleId() {
		return roleRepository.findByName("村人").orElseThrow().getId();
	}

	@Test
	void 人狼が0人なら村人陣営の勝ち() {
		long gameId = System.nanoTime();
		gamePlayerRepository.save(new GamePlayer(gameId, villagerRoleId(), 1));
		gamePlayerRepository.save(new GamePlayer(gameId, villagerRoleId(), 2));

		GameResult result = gameResultService.judge(gameId);

		assertThat(result).isEqualTo(GameResult.VILLAGER_WIN);
	}

	@Test
	void 人狼と村人が同数なら人狼陣営の勝ち() {
		long gameId = System.nanoTime();
		gamePlayerRepository.save(new GamePlayer(gameId, werewolfRoleId(), 1));
		gamePlayerRepository.save(new GamePlayer(gameId, werewolfRoleId(), 2));
		gamePlayerRepository.save(new GamePlayer(gameId, villagerRoleId(), 3));
		gamePlayerRepository.save(new GamePlayer(gameId, villagerRoleId(), 4));

		GameResult result = gameResultService.judge(gameId);

		assertThat(result).isEqualTo(GameResult.WEREWOLF_WIN);
	}

	@Test
	void 人狼が村人陣営より少なければ続行() {
		long gameId = System.nanoTime();
		gamePlayerRepository.save(new GamePlayer(gameId, werewolfRoleId(), 1));
		gamePlayerRepository.save(new GamePlayer(gameId, villagerRoleId(), 2));
		gamePlayerRepository.save(new GamePlayer(gameId, villagerRoleId(), 3));
		gamePlayerRepository.save(new GamePlayer(gameId, villagerRoleId(), 4));

		GameResult result = gameResultService.judge(gameId);

		assertThat(result).isEqualTo(GameResult.CONTINUE);
	}

	@Test
	void 死亡したプレイヤーは数に入れない() {
		long gameId = System.nanoTime();
		GamePlayer deadWerewolf = gamePlayerRepository.save(new GamePlayer(gameId, werewolfRoleId(), 1));
		deadWerewolf.setIsAlive(false);
		gamePlayerRepository.save(deadWerewolf);
		gamePlayerRepository.save(new GamePlayer(gameId, villagerRoleId(), 2));

		GameResult result = gameResultService.judge(gameId);

		assertThat(result).isEqualTo(GameResult.VILLAGER_WIN);
	}
}
