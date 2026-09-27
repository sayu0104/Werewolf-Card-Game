package com.example.werewolf.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.werewolf.entity.Card;
import com.example.werewolf.entity.CardEffectType;
import com.example.werewolf.entity.Game;
import com.example.werewolf.entity.GamePlayer;
import com.example.werewolf.entity.NightAction;
import com.example.werewolf.repository.GamePlayerRepository;
import com.example.werewolf.repository.NightActionRepository;
import com.example.werewolf.repository.RoleRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class CardPlayServiceTest {

	@Autowired
	private CardPlayService cardPlayService;

	@Autowired
	private GameStartService gameStartService;

	@Autowired
	private GamePlayerRepository gamePlayerRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private NightActionRepository nightActionRepository;

	@Test
	void 情報取得のカードで人狼を占うと黒の記録が作られる() {
		Game game = gameStartService.startGame();
		GamePlayer actor = findByRoleName(game, "占い師");
		GamePlayer target = findByRoleName(game, "人狼");
		Card card = new Card("占いカード", CardEffectType.INFORMATION, "昼", 1);
		// 占いをする人として占い師の役職、占われる人として人狼の役職、そして占いのカードを用意する

		boolean handled = cardPlayService.playCard(game, actor, card, target);
		// カードを使用して占いをし、その結果を人狼（true/黒）か人狼ではない（false/白）で出す

		assertThat(handled).isTrue();
		// 人狼を占ったので、true（人狼だった）という結果のはず
		
		NightAction nightAction = findOnlyDivination(game);
		// このゲームの夜の行動の記録を取ってくる
		
		assertThat(nightAction.getActorGamePlayerId()).isEqualTo(actor.getId());
		assertThat(nightAction.getTargetGamePlayerId()).isEqualTo(target.getId());
		assertThat(nightAction.getIsWerewolf()).isTrue();
		// 行動した人のIDは同じなはず（占い師）
		// ターゲットになった人のIDは同じなはず（人狼）
		// その人狼の記録は、true（人狼である・黒）という結果になっているはず
	}

	@Test
	void 情報取得のカードで村人を占うと白の記録が作られる() {
		Game game = gameStartService.startGame();
		GamePlayer actor = findByRoleName(game, "占い師");
		GamePlayer target = findByRoleName(game, "村人");
		Card card = new Card("占いカード", CardEffectType.INFORMATION, "昼", 1);
		// 占いをする人として占い師の役職、占われる人として村人の役職、そして占いのカードを用意する

		boolean handled = cardPlayService.playCard(game, actor, card, target);
		// カードを使用して占いをし、その結果を人狼（true/黒）か人狼ではない（false/白）で出す

		assertThat(handled).isTrue();
		// 村人を占ったので、false（人狼ではなかった）という結果のはず
		
		NightAction nightAction = findOnlyDivination(game);
		// このゲームの夜の行動の記録を取ってくる
		
		assertThat(nightAction.getTargetGamePlayerId()).isEqualTo(target.getId());
		assertThat(nightAction.getIsWerewolf()).isFalse();
		// ターゲットになった人のIDは同じなはず（村人）
		// その人狼の記録は、false（人狼ではない・白）という結果になっているはず
	}

	@Test
	void 未実装の種類のカードでも落ちずに何も起きない() {
		Game game = gameStartService.startGame();
		GamePlayer actor = findByRoleName(game, "占い師");
		GamePlayer target = findByRoleName(game, "村人");
		Card card = new Card("護衛カード", CardEffectType.DEFENSE, "夜", 1);

		boolean handled = cardPlayService.playCard(game, actor, card, target);

		assertThat(handled).isFalse();
		assertThat(nightActionRepository.findByGameIdAndActionType(game.getId(), "占い")).isEmpty();
	}

	// 役職の名前で絞って探す
	private GamePlayer findByRoleName(Game game, String roleName) {
		return gamePlayerRepository.findByGameId(game.getId()).stream()
				.filter(player -> roleName.equals(roleRepository.findById(player.getRoleId()).orElseThrow().getName()))
				.findFirst()
				.orElseThrow();
	}

	// 夜の行動の記録の占い結果が１件あることを確認して、その記録を取ってくる
	private NightAction findOnlyDivination(Game game) {
		List<NightAction> nightActions = nightActionRepository.findByGameIdAndActionType(game.getId(), "占い");
		assertThat(nightActions).hasSize(1);
		return nightActions.get(0);
	}
}
