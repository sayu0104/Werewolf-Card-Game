package com.example.werewolf.service;

import com.example.werewolf.entity.GamePlayer;
import com.example.werewolf.repository.GamePlayerRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ExecutionService {

	private final GamePlayerRepository gamePlayerRepository;

	public ExecutionService(GamePlayerRepository gamePlayerRepository) {
		this.gamePlayerRepository = gamePlayerRepository;
	}

	public ExecutionResult execute(List<Long> mostVotedGamePlayerIds) {
		if (mostVotedGamePlayerIds.size() != 1) {
			return ExecutionResult.NEEDS_REVOTE;
		}

		Long targetId = mostVotedGamePlayerIds.get(0);
		GamePlayer target = gamePlayerRepository.findById(targetId).orElseThrow();
		target.setIsAlive(false);
		gamePlayerRepository.save(target);

		return ExecutionResult.EXECUTED;
	}
}
