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
public class HandService { // プレイヤーのカードの手札を管理する係

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

	// 役職ごとの保証枠のカードを配る（毎ターン手札に配られるカード）
	public void dealGuaranteedCards(Game game) {
		if (game == null) {
			throw new IllegalArgumentException("試合は必須");
			// もし、試合が空なら、エラー
		}
		for (GamePlayer player : gamePlayerRepository.findByGameId(game.getId())) {
			// その試合の全プレイヤーを1人ずつ取り出して、playerに入れて回す
			
			for (RoleGuaranteedCard rgc : roleGuaranteedCardRepository.findByRoleId(player.getRoleId())) {
				// そのプレイヤーの保証枠のカードを1枚ずつ取り出して、rgcに入れて回す
				
				if (!gamePlayerHandRepository.existsByGamePlayerIdAndCardIdAndStatus(
						player.getId(), rgc.getCardId(), IN_HAND)) {
					// もし、この人の・この保証枠カードが・まだ手札(in_hand)に無いなら…
					
					gamePlayerHandRepository.save(
							new GamePlayerHand(player.getId(), rgc.getCardId(), IN_HAND, game.getDayNumber()));
					// その保証枠のカードを、そのプレイヤーの手札(in_hand)として、今日の日付で1枚加える
				}
			}
		}
	}
}
