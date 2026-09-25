package com.example.werewolf.service;

import com.example.werewolf.entity.Card;
import com.example.werewolf.entity.CardEffectType;
import com.example.werewolf.entity.GamePlayer;
import org.springframework.stereotype.Service;

@Service
public class CardPlayService { // カード使用用のサービス

	// カードを使用したか、true または false　で返す
	public boolean playCard(GamePlayer actor, Card card, GamePlayer target) {
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

		// カード効果の種類　今は情報取得のみ　true
		// まだ中身を作ってない種類なので、今は false（第3歩以降で埋める）
		switch (card.getEffectType()) {
		case CardEffectType.INFORMATION:
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
