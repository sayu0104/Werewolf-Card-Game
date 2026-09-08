package com.example.werewolf.service;

import com.example.werewolf.entity.GamePlayer;
import com.example.werewolf.entity.Role;
import com.example.werewolf.repository.GamePlayerRepository;
import com.example.werewolf.repository.RoleRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class GameResultService {

	private static final String WEREWOLF_FACTION = "人狼陣営";

	private final GamePlayerRepository gamePlayerRepository;
	private final RoleRepository roleRepository;

	public GameResultService(GamePlayerRepository gamePlayerRepository, RoleRepository roleRepository) {
		this.gamePlayerRepository = gamePlayerRepository;
		this.roleRepository = roleRepository;
	}

	public GameResult judge(Long gameId) {
		List<GamePlayer> players = gamePlayerRepository.findByGameId(gameId);

		long werewolfCount = 0;
		long villagerCount = 0;

		for (GamePlayer player : players) {
			if (!player.getIsAlive()) {
				continue;
			}

			Role role = roleRepository.findById(player.getRoleId()).orElseThrow();

			if (WEREWOLF_FACTION.equals(role.getFaction())) {
				werewolfCount++;
			} else {
				villagerCount++;
			}
		}

		if (werewolfCount == 0) {
			return GameResult.VILLAGER_WIN;
		}

		if (werewolfCount >= villagerCount) {
			return GameResult.WEREWOLF_WIN;
		}

		return GameResult.CONTINUE;
	}
}
