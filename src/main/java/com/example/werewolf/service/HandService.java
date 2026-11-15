package com.example.werewolf.service;

import com.example.werewolf.entity.Game;
import com.example.werewolf.entity.GamePlayer;
import com.example.werewolf.entity.GamePlayerHand;
import com.example.werewolf.entity.RoleGuaranteedCard;
import com.example.werewolf.repository.GamePlayerHandRepository;
import com.example.werewolf.repository.GamePlayerRepository;
import com.example.werewolf.repository.RoleGuaranteedCardRepository;
import org.springframework.stereotype.Service;

@Service
public class HandService {

	private static final String IN_HAND = "in_hand";

	private final GamePlayerRepository gamePlayerRepository;
	private final RoleGuaranteedCardRepository roleGuaranteedCardRepository;
	private final GamePlayerHandRepository gamePlayerHandRepository;

	public HandService(GamePlayerRepository gamePlayerRepository,
			RoleGuaranteedCardRepository roleGuaranteedCardRepository,
			GamePlayerHandRepository gamePlayerHandRepository) {
		this.gamePlayerRepository = gamePlayerRepository;
		this.roleGuaranteedCardRepository = roleGuaranteedCardRepository;
		this.gamePlayerHandRepository = gamePlayerHandRepository;
	}

	public void dealGuaranteedCards(Game game) {
		if (game == null) {
			throw new IllegalArgumentException("試合は必須");
		}
		for (GamePlayer player : gamePlayerRepository.findByGameId(game.getId())) {
			for (RoleGuaranteedCard rgc : roleGuaranteedCardRepository.findByRoleId(player.getRoleId())) {
				if (!gamePlayerHandRepository.existsByGamePlayerIdAndCardIdAndStatus(
						player.getId(), rgc.getCardId(), IN_HAND)) {
					gamePlayerHandRepository.save(
							new GamePlayerHand(player.getId(), rgc.getCardId(), IN_HAND, game.getDayNumber()));
				}
			}
		}
	}
}
