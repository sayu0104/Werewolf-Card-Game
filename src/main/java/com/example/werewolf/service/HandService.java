package com.example.werewolf.service;

import com.example.werewolf.entity.DeckCard;
import com.example.werewolf.entity.Game;
import com.example.werewolf.entity.GamePlayer;
import com.example.werewolf.entity.GamePlayerHand;
import com.example.werewolf.entity.RoleGuaranteedCard;
import com.example.werewolf.repository.DeckCardRepository;
import com.example.werewolf.repository.GamePlayerHandRepository;
import com.example.werewolf.repository.GamePlayerRepository;
import com.example.werewolf.repository.RoleGuaranteedCardRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class HandService { // プレイヤーのカードの手札を管理する係

	private static final String IN_HAND = "in_hand";
	private static final String IN_DECK = "in_deck";
	private static final String DISCARDED = "discarded";
	private static final int RANDOM_SLOT_SIZE = 5;

	private final GamePlayerRepository gamePlayerRepository;
	private final RoleGuaranteedCardRepository roleGuaranteedCardRepository;
	private final GamePlayerHandRepository gamePlayerHandRepository;
	private final DeckCardRepository deckCardRepository;

	public HandService(GamePlayerRepository gamePlayerRepository,
			RoleGuaranteedCardRepository roleGuaranteedCardRepository,
			GamePlayerHandRepository gamePlayerHandRepository,
			DeckCardRepository deckCardRepository) {
		this.gamePlayerRepository = gamePlayerRepository;
		this.roleGuaranteedCardRepository = roleGuaranteedCardRepository;
		this.gamePlayerHandRepository = gamePlayerHandRepository;
		this.deckCardRepository = deckCardRepository;
	}

	public void createDecks(Game game) {
		if (game == null) {
			throw new IllegalArgumentException("試合は必須");
		}
		List<DeckCard> deckCards = deckCardRepository.findAll();
		List<GamePlayerHand> deck = new ArrayList<>();
		for (GamePlayer player : gamePlayerRepository.findByGameId(game.getId())) {
			for (DeckCard deckCard : deckCards) {
				for (int i = 0; i < deckCard.getCopies(); i++) {
					deck.add(new GamePlayerHand(player.getId(), deckCard.getCardId(), IN_DECK, game.getDayNumber()));
				}
			}
		}
		gamePlayerHandRepository.saveAll(deck);
	}

	public void drawRandomCards(Game game) {
		if (game == null) {
			throw new IllegalArgumentException("試合は必須");
		}
		for (GamePlayer player : gamePlayerRepository.findByGameId(game.getId())) {
			Set<Long> guaranteedCardIds = roleGuaranteedCardRepository.findByRoleId(player.getRoleId()).stream()
					.map(RoleGuaranteedCard::getCardId)
					.collect(Collectors.toSet());

			for (GamePlayerHand hand : gamePlayerHandRepository.findByGamePlayerIdAndStatus(player.getId(), IN_HAND)) {
				if (!guaranteedCardIds.contains(hand.getCardId())) {
					hand.setStatus(DISCARDED);
					gamePlayerHandRepository.save(hand);
				}
			}

			List<GamePlayerHand> deck = new ArrayList<>(
					gamePlayerHandRepository.findByGamePlayerIdAndStatus(player.getId(), IN_DECK));
			Collections.shuffle(deck);
			for (GamePlayerHand drawn : deck.subList(0, Math.min(RANDOM_SLOT_SIZE, deck.size()))) {
				drawn.setStatus(IN_HAND);
				drawn.setAcquiredAtDay(game.getDayNumber());
				gamePlayerHandRepository.save(drawn);
			}
		}
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
