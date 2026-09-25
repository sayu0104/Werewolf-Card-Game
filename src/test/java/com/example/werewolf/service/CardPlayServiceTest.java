package com.example.werewolf.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.werewolf.entity.Card;
import com.example.werewolf.entity.CardEffectType;
import com.example.werewolf.entity.GamePlayer;
import org.junit.jupiter.api.Test;

class CardPlayServiceTest {

	private final CardPlayService cardPlayService = new CardPlayService();

	@Test
	void 情報取得のカードは情報取得の枝に振り分けられる() {
		GamePlayer actor = new GamePlayer(1L, 1L, 1);
		GamePlayer target = new GamePlayer(1L, 2L, 2);
		Card card = new Card("占いカード", CardEffectType.INFORMATION, "昼", 1);

		boolean handled = cardPlayService.playCard(actor, card, target);

		assertThat(handled).isTrue();
	}

	@Test
	void 未実装の種類のカードでも落ちずに何も起きない() {
		GamePlayer actor = new GamePlayer(1L, 1L, 1);
		GamePlayer target = new GamePlayer(1L, 2L, 2);
		Card card = new Card("護衛カード", CardEffectType.DEFENSE, "夜", 1);

		boolean handled = cardPlayService.playCard(actor, card, target);

		assertThat(handled).isFalse();
	}
}
