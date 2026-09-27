package com.example.werewolf.service;

import com.example.werewolf.entity.Card;
import com.example.werewolf.entity.CardEffectType;
import com.example.werewolf.entity.Game;
import com.example.werewolf.entity.GamePlayer;
import org.springframework.stereotype.Service;

@Service
public class CardPlayService { // カード使用用のサービス

	private final FortuneTellerService fortuneTellerService;

	public CardPlayService(FortuneTellerService fortuneTellerService) {
		this.fortuneTellerService = fortuneTellerService;
	}

	// カードを使用したか、true または false　で返す
	public boolean playCard(Game game, GamePlayer actor, Card card, GamePlayer target) {
		if (game == null) {
			throw new IllegalArgumentException("試合は必須");
			// もし、ゲームが空なら、エラーを出す
		}
		if (actor == null || card == null) {
			throw new IllegalArgumentException("使用者とカードは必須");
			// もし　使用者　または　カード　のどちらかが空なら
			// エラーを出す
			// ※両方空（true）ではなく、ある（false）なら通過
		}
		if (card.getEffectType() == null) {
			return false;
			// もし、カード効果の種類が空なら
			// false
		}

		// それぞれのカードを効果の種類で仕分けて、その効果を発揮させる
		// ※まだ中身を作ってない種類なので、今は false（第3歩以降で埋める）
		switch (card.getEffectType()) {
		case CardEffectType.INFORMATION:
			if (target == null) {
				throw new IllegalArgumentException("情報取得のカードには対象が必須");
			}
			fortuneTellerService.divineOne(game, actor, target);
			return true;
		case CardEffectType.SUSPICION:
			return false;
		case CardEffectType.DECLARATION:
			return false;
		case CardEffectType.REPORT:
			return false;
		case CardEffectType.DEFENSE:
			return false;
		case CardEffectType.REMOVAL:
			return false;
		default:
			return false;
		}
	}
}
