package com.example.werewolf.service;

import com.example.werewolf.entity.Card;
import com.example.werewolf.entity.CardEffectType;
import com.example.werewolf.entity.Game;
import com.example.werewolf.entity.GamePlayer;
import com.example.werewolf.repository.GamePlayerRepository;
import org.springframework.stereotype.Service;

@Service
public class CardPlayService { // カード使用用のサービス

	private final FortuneTellerService fortuneTellerService;
	private final HunterService hunterService;
	private final WerewolfService werewolfService;
	private final SuspicionService suspicionService;
	private final GamePlayerRepository gamePlayerRepository;

	public CardPlayService(FortuneTellerService fortuneTellerService, HunterService hunterService,
			WerewolfService werewolfService, SuspicionService suspicionService,
			GamePlayerRepository gamePlayerRepository) {
		this.fortuneTellerService = fortuneTellerService;
		this.hunterService = hunterService;
		this.werewolfService = werewolfService;
		this.suspicionService = suspicionService;
		this.gamePlayerRepository = gamePlayerRepository;
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
		
		// 「情報取得」のカードの場合
		case CardEffectType.INFORMATION:
			if (target == null) {
				throw new IllegalArgumentException("情報取得のカードには対象が必須");
			}
			fortuneTellerService.divineOne(game, actor, target);
			return true;
			
		// 「被疑心操作」のカードの場合
		case CardEffectType.SUSPICION:
			if (target == null) {
				throw new IllegalArgumentException("被疑心操作のカードには対象が必須");
			}
			if (card.getValue() == null) {
				throw new IllegalArgumentException("被疑心操作のカードには値が必須");
			}
			if (card.getSelfRate() != null && card.getSelfRate() > 0) {
				suspicionService.incite(game, actor, target, card.getValue(), card.getSelfRate());
				// もし、このカードの self_rate が「空じゃない」かつ「0より大きい」なら
				// 扇動カード（incite で相手＋本人の両方に記録する）
				
			} else {
				suspicionService.changeSuspicion(game, target, card.getValue());
				// もし、そうでなければ（0、または空）、相手にだけ記録する
			}
			return true;
			
		// 「宣言」のカードの場合
		case CardEffectType.DECLARATION:
			if (actor.getClaimedRoleId() != null) {
				return false;
				// 既に名乗ってたら false＝名乗りは1回だけ
			}
			actor.setClaimedRoleId(actor.getRoleId());
			actor.setClaimedAtDay(game.getDayNumber());
			gamePlayerRepository.save(actor);
			// 役職を宣言した（名乗った）時に、名乗った役職と、その名乗った日付を記録
			
			return true;
			
		// 「報告」のカードの場合
		case CardEffectType.REPORT:
			return false;
			
		// 「防御」のカードの場合
		case CardEffectType.DEFENSE:
			if (target == null) {
				throw new IllegalArgumentException("防御のカードには対象が必須");
			}
			hunterService.guardOne(game, actor, target);
			return true;
			
		// 「除去」のカードの場合
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
