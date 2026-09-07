package com.example.werewolf.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.werewolf.entity.GamePlayer;
import com.example.werewolf.repository.GamePlayerRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ExecutionServiceTest {

	@Autowired
	private ExecutionService executionService;

	@Autowired
	private GamePlayerRepository gamePlayerRepository;

	@Test
	void 最多得票者が1人なら処刑されてEXECUTEDが返る() {
		GamePlayer player = gamePlayerRepository.save(new GamePlayer(1L, 1L, 1));

		ExecutionResult result = executionService.execute(List.of(player.getId()));

		GamePlayer updated = gamePlayerRepository.findById(player.getId()).orElseThrow();
		assertThat(updated.getIsAlive()).isFalse();
		assertThat(result).isEqualTo(ExecutionResult.EXECUTED);
	}

	@Test
	void 最多得票者が複数なら誰も処刑されずNEEDS_REVOTEが返る() {
		GamePlayer player1 = gamePlayerRepository.save(new GamePlayer(1L, 1L, 1));
		GamePlayer player2 = gamePlayerRepository.save(new GamePlayer(1L, 1L, 2));

		ExecutionResult result = executionService.execute(List.of(player1.getId(), player2.getId()));

		GamePlayer updated1 = gamePlayerRepository.findById(player1.getId()).orElseThrow();
		GamePlayer updated2 = gamePlayerRepository.findById(player2.getId()).orElseThrow();
		assertThat(updated1.getIsAlive()).isTrue();
		assertThat(updated2.getIsAlive()).isTrue();
		assertThat(result).isEqualTo(ExecutionResult.NEEDS_REVOTE);
	}

	@Test
	void 最多得票者が空リストならNEEDS_REVOTEが返る() {
		ExecutionResult result = executionService.execute(List.of());

		assertThat(result).isEqualTo(ExecutionResult.NEEDS_REVOTE);
	}
}
