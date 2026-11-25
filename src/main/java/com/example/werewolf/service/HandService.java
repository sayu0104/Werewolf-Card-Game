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

	private static final String IN_HAND = "in_hand"; // 手札
	private static final String IN_DECK = "in_deck"; // 山札
	private static final String DISCARDED = "discarded"; // 捨て札
	private static final int RANDOM_SLOT_SIZE = 5; // ※毎ターン山札から手札へ引くための枚数

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

	// その試合の全プレイヤーの山札（デッキ）を用意する（疑う/擁護 の基本カード6種類）
	public void createDecks(Game game) {
		if (game == null) {
			throw new IllegalArgumentException("試合は必須");
			// もし、試合がないなら、エラー
		}
		List<DeckCard> deckCards = deckCardRepository.findAll();
		// 山札用のカードを全て取ってきて、deckCardsのリストに入れる
		
		List<GamePlayerHand> deck = new ArrayList<>();
		// 全プレイヤーの山札用に、deckのリストを作る
		
		for (GamePlayer player : gamePlayerRepository.findByGameId(game.getId())) {
			// プレイヤーを1人ずつ取り出して
			
			for (DeckCard deckCard : deckCards) {
				// 山札用のカード全6種類を、1種類1つずつ取り出して
				
				for (int i = 0; i < deckCard.getCopies(); i++) {
					// カード1種類を、Copiesの枚数（3枚）になるまで、カードを増やす
					
					deck.add(new GamePlayerHand(player.getId(), deckCard.getCardId(), IN_DECK, game.getDayNumber()));
					// 山札にそのカードを追加して、繰り返す
				}
			}
		}
		gamePlayerHandRepository.saveAll(deck);
		// プレイヤーそれぞれに山札を用意したら、この状態をまとめて保存する
	}

	// 山札から手札になるカードを5枚ランダムに引く（保証枠のカードは含まない）
	public void drawRandomCards(Game game) {
		if (game == null) {
			throw new IllegalArgumentException("試合は必須");
			// もし、試合がなければ、エラー
		}
		for (GamePlayer player : gamePlayerRepository.findByGameId(game.getId())) {
			// プレイヤーを1人ずつ取り出して
			
			Set<Long> guaranteedCardIds = roleGuaranteedCardRepository.findByRoleId(player.getRoleId()).stream()
					// 重複なしの箱を用意して、保証枠のカードを以下の処理をしてから入れる
					
					.map(RoleGuaranteedCard::getCardId)
					// 「カードIDだけ」に変換する
					
					.collect(Collectors.toSet());
					// 終わったら、Setに集める

			for (GamePlayerHand hand : gamePlayerHandRepository.findByGamePlayerIdAndStatus(player.getId(), IN_HAND)) {
				// そのプレイヤーの手札を1枚ずつ取り出して
				
				if (!guaranteedCardIds.contains(hand.getCardId())) {
					// もし、この手札のカードが、保証枠の集合(guaranteedCardIds)に入っていない（＝保証枠じゃない）なら
					
					hand.setStatus(DISCARDED);
					// 捨て札に移動する
					
					gamePlayerHandRepository.save(hand);
					// この捨て札に移動した手札の状態を保存する
				}
			}

			List<GamePlayerHand> deck = new ArrayList<>(
					// 各プレイヤーの山札用に、カードのリストを作る
					
					gamePlayerHandRepository.findByGamePlayerIdAndStatus(player.getId(), IN_DECK));
					// このプレイヤーの山札のカードを取ってくる
			
			Collections.shuffle(deck);
			// 山札にあるカードをシャッフルする
			
			for (GamePlayerHand drawn : deck.subList(0, Math.min(RANDOM_SLOT_SIZE, deck.size()))) {
				// 山札のカードの、0から5枚引く（5枚になるまで、山札からある分引く）
				
				drawn.setStatus(IN_HAND);
				// 引いたカードを手札にセットする
				
				drawn.setAcquiredAtDay(game.getDayNumber());
				// その試合の今日の日付をセットする
				
				gamePlayerHandRepository.save(drawn);
				// この山札から手札を引き終えた状態を保存する
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
