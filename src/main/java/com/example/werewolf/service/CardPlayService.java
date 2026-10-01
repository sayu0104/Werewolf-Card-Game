package com.example.werewolf.service;

import com.example.werewolf.entity.Card;
import com.example.werewolf.entity.CardEffectType;
import com.example.werewolf.entity.Game;
import com.example.werewolf.entity.GamePlayer;
import org.springframework.stereotype.Service;

@Service
public class CardPlayService { // カード使用用のサービス

	private final FortuneTellerService fortuneTellerService;
	private final HunterService hunterService;
	private final WerewolfService werewolfService;
	private final SuspicionService suspicionService;

	public CardPlayService(FortuneTellerService fortuneTellerService, HunterService hunterService,
			WerewolfService werewolfService, SuspicionService suspicionService) {
		this.fortuneTellerService = fortuneTellerService;
		this.hunterService = hunterService;
		this.werewolfService = werewolfService;
		this.suspicionService = suspicionService;
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
			if (target == null) {
				throw new IllegalArgumentException("被疑心操作のカードには対象が必須");
			}
			if (card.getValue() == null) {
				throw new IllegalArgumentException("被疑心操作のカードには値が必須");
			}
			suspicionService.changeSuspicion(game, target, card.getValue());
			return true;
		case CardEffectType.INCITE:
			if (target == null) {
				throw new IllegalArgumentException("扇動のカードには対象が必須");
			}
			if (card.getValue() == null) {
				throw new IllegalArgumentException("扇動のカードには値が必須");
			}
			suspicionService.incite(game, actor, target, card.getValue());
			return true;
		case CardEffectType.DECLARATION:
			return false;
		case CardEffectType.REPORT:
			return false;
		case CardEffectType.DEFENSE:
			if (target == null) {
				throw new IllegalArgumentException("防御のカードには対象が必須");
			}
			hunterService.guardOne(game, actor, target);
			return true;
		case CardEffectType.REMOVAL:
			if (target == null) {
				throw new IllegalArgumentException("除去のカードには対象が必須");
			}
			werewolfService.attackOne(game, actor, target);
			return true;
		default:
			return false;
		}
	}
}
