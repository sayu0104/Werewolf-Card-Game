package com.example.werewolf.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.werewolf.entity.Card;
import com.example.werewolf.entity.CardEffectType;
import com.example.werewolf.entity.Game;
import com.example.werewolf.entity.GamePlayer;
import com.example.werewolf.entity.NightAction;
import com.example.werewolf.entity.SuspicionPoint;
import com.example.werewolf.repository.GamePlayerRepository;
import com.example.werewolf.repository.NightActionRepository;
import com.example.werewolf.repository.RoleRepository;
import com.example.werewolf.repository.SuspicionPointRepository;
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

	@Autowired
	private SuspicionPointRepository suspicionPointRepository;

	@Test
	void 情報取得のカードで人狼を占うと黒の記録が作られる() {
		Game game = gameStartService.startGame();
		GamePlayer actor = findByRoleName(game, "占い師");
		GamePlayer target = findByRoleName(game, "人狼");
		Card card = new Card("占いカード", CardEffectType.INFORMATION, "昼", 1);
		// 占いをする人として占い師の役職、占われる人として人狼の役職、そして占いのカードを用意する

		boolean handled = cardPlayService.playCard(game, actor, card, target);
		// 占いカードを使う。使えたか（true/false）が handled に入る

		assertThat(handled).isTrue();
		// 占いカードはちゃんと使えたはず（true）
		
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
		// 占いカードを使う。使えたか（true/false）が handled に入る

		assertThat(handled).isTrue();
		// 占いカードはちゃんと使えたはず（true）
		
		NightAction nightAction = findOnlyDivination(game);
		// このゲームの夜の行動の記録を取ってくる
		
		assertThat(nightAction.getTargetGamePlayerId()).isEqualTo(target.getId());
		assertThat(nightAction.getIsWerewolf()).isFalse();
		// ターゲットになった人のIDは同じなはず（村人）
		// その村人の記録は、false（人狼ではない・白）という結果になっているはず
	}

	@Test
	void 防御のカードを使うと護衛の記録が作られる() {
		Game game = gameStartService.startGame();
		GamePlayer actor = findByRoleName(game, "狩人");
		GamePlayer target = findByRoleName(game, "村人");
		Card card = new Card("護衛カード", CardEffectType.DEFENSE, "夜", 1);
		// 護衛する人として狩人、護衛される人として村人、そして護衛のカードを用意する

		boolean handled = cardPlayService.playCard(game, actor, card, target);
		// カードを使用して護衛を実行、その結果をカードが使えた(true)か・使えなかった(false)か

		assertThat(handled).isTrue();
		// 護衛カードは使えた（true）なはず

		List<NightAction> nightActions = nightActionRepository.findByGameIdAndActionType(game.getId(), "護衛");
		// 護衛の記録を用意する
		
		assertThat(nightActions).hasSize(1);
		// 記録は１件あるはず
		
		NightAction nightAction = nightActions.get(0);
		// 記録の１件目を取ってくる
		
		assertThat(nightAction.getGameId()).isEqualTo(game.getId());
		assertThat(nightAction.getDayNumber()).isEqualTo(game.getDayNumber());
		assertThat(nightAction.getActorGamePlayerId()).isEqualTo(actor.getId());
		assertThat(nightAction.getTargetGamePlayerId()).isEqualTo(target.getId());
		// ゲームのIDは同じなはず
		// 日付は同じなはず
		// 護衛を行った人のIDは同じなはず
		// 護衛をされた人のIDは同じなはず
	}

	@Test
	void 除去のカードを使うと襲撃の記録が作られる() {
		Game game = gameStartService.startGame();
		GamePlayer actor = findByRoleName(game, "人狼");
		GamePlayer target = findByRoleName(game, "村人");
		Card card = new Card("襲撃カード", CardEffectType.REMOVAL, "夜", 1);
		// 襲撃する人として人狼、襲撃される人として村人、そして襲撃のカードを用意する

		boolean handled = cardPlayService.playCard(game, actor, card, target);
		// カードを使用して襲撃を実行、その結果をカードが使えた(true)か・使えなかった(false)か

		assertThat(handled).isTrue();
		// 襲撃カードは使えた（true）なはず

		List<NightAction> nightActions = nightActionRepository.findByGameIdAndActionType(game.getId(), "襲撃");
		// 襲撃の記録を用意する
		
		assertThat(nightActions).hasSize(1);
		// 記録は１件あるはず
		
		NightAction nightAction = nightActions.get(0);
		// 記録の１件目を取ってくる
		
		assertThat(nightAction.getGameId()).isEqualTo(game.getId());
		assertThat(nightAction.getDayNumber()).isEqualTo(game.getDayNumber());
		assertThat(nightAction.getActorGamePlayerId()).isEqualTo(actor.getId());
		assertThat(nightAction.getTargetGamePlayerId()).isEqualTo(target.getId());
		// ゲームのIDは同じなはず
		// 日付は同じなはず
		// 襲撃を行った人のIDは同じなはず
		// 襲撃をされた人のIDは同じなはず
	}

	@Test
	void 疑うカードを使うと対象の被疑心が正の値で記録される() {
		Game game = gameStartService.startGame();
		GamePlayer actor = findByRoleName(game, "占い師");
		GamePlayer target = findByRoleName(game, "村人");
		Card card = new Card("疑う（弱）", CardEffectType.SUSPICION, "昼", 1);
		card.setValue(3);
		// 疑いをかける人として占い師、疑いをかけられる人として村人、
		// そして疑う（弱）カードを、疑心ポイント＋３として用意する

		boolean handled = cardPlayService.playCard(game, actor, card, target);
		// カードを使用して、その結果をカードが使えた(true)か・使えなかった(false)か

		assertThat(handled).isTrue();
		// 疑う（弱）カードは使えた（true）なはず
		
		List<SuspicionPoint> points = suspicionPointRepository.findByGameIdAndGamePlayerId(game.getId(),
				target.getId());
		// 疑いをかけられたプレイヤー（村人）の疑心ポイントの記録を取ってくる
		
		assertThat(points).hasSize(1);
		assertThat(points.get(0).getPointsDelta()).isEqualTo(3);
		assertThat(points.get(0).getGameId()).isEqualTo(game.getId());
		assertThat(points.get(0).getGamePlayerId()).isEqualTo(target.getId());
		assertThat(points.get(0).getDayNumber()).isEqualTo(game.getDayNumber());
		// その記録は１件なはず
		// 疑心ポイントは３ポイント動いたはず
		// ゲームのIDは同じなはず
		// その記録のプレイヤーIDは、疑われたプレイヤー（村人）と同じなはず
		// その記録の日付と、今のゲームの日付は同じなはず
	}

	@Test
	void 扇動カードを使うと対象と自分の被疑心が両方上がる() {
		Game game = gameStartService.startGame();
		GamePlayer actor = findByRoleName(game, "狂人");
		GamePlayer target = findByRoleName(game, "村人");
		Card card = new Card("扇動", CardEffectType.SUSPICION, "昼", 1);
		card.setValue(10);
		card.setSelfRate(0.3);
		// カードを使う人として狂人、使われる対象として村人
		// 扇動カードを疑心ポイント+10与えるものとして用意する

		boolean handled = cardPlayService.playCard(game, actor, card, target);
		// カードを使用して、その結果をカードが使えた(true)か・使えなかった(false)か

		assertThat(handled).isTrue();
		// 扇動カードは使えた（true）なはず

		List<SuspicionPoint> targetPoints = suspicionPointRepository.findByGameIdAndGamePlayerId(game.getId(),
				target.getId());
		List<SuspicionPoint> actorPoints = suspicionPointRepository.findByGameIdAndGamePlayerId(game.getId(),
				actor.getId());
		// カードを使った人（狂人）と、使われた人（村人）の疑心ポイントの記録を取ってくる

		assertThat(targetPoints).hasSize(1);
		assertThat(targetPoints.get(0).getPointsDelta()).isEqualTo(10);
		assertThat(targetPoints.get(0).getGamePlayerId()).isEqualTo(target.getId());
		assertThat(actorPoints).hasSize(1);
		assertThat(actorPoints.get(0).getPointsDelta()).isEqualTo(3);
		assertThat(actorPoints.get(0).getGamePlayerId()).isEqualTo(actor.getId());
		// そのカードを使われた人の記録は１件なはず
		// 疑心ポイントは+10動いたはず
		// その記録のプレイヤーIDは、カードを使われたプレイヤー（村人）と同じなはず
		// そのカードを使った人の記録は１件なはず
		// 疑心ポイントは+3動いたはず（扇動カードで疑いをかけた分の30%が本人に入るため）
		// その記録のプレイヤーIDは、カードを使ったプレイヤー（狂人）と同じなはず
	}

	@Test
	void 擁護カードを使うと対象の被疑心が負の値で記録される() {
		Game game = gameStartService.startGame();
		GamePlayer actor = findByRoleName(game, "占い師");
		GamePlayer target = findByRoleName(game, "村人");
		Card card = new Card("擁護（弱）", CardEffectType.SUSPICION, "昼", 1);
		card.setValue(-3);
		// 疑いを擁護する人として占い師、擁護される人として村人、
		// そして擁護（弱）カードを、疑心ポイント－３として用意する

		boolean handled = cardPlayService.playCard(game, actor, card, target);
		// カードを使用して、その結果をカードが使えた(true)か・使えなかった(false)か

		assertThat(handled).isTrue();
		// 擁護（弱）カードは使えた（true）なはず
		
		List<SuspicionPoint> points = suspicionPointRepository.findByGameIdAndGamePlayerId(game.getId(),
				target.getId());
		// 擁護をされたプレイヤー（村人）の疑心ポイントの記録を取ってくる
		
		assertThat(points).hasSize(1);
		assertThat(points.get(0).getPointsDelta()).isEqualTo(-3);
		// その記録は１件なはず
		// 疑心ポイントは－３ポイント動いたはず
	}

	@Test
	void 未実装の種類のカードでも落ちずに何も起きない() {
		Game game = gameStartService.startGame();
		GamePlayer actor = findByRoleName(game, "占い師");
		GamePlayer target = findByRoleName(game, "村人");
		Card card = new Card("報告カード", CardEffectType.REPORT, "昼", 1);

		boolean handled = cardPlayService.playCard(game, actor, card, target);

		assertThat(handled).isFalse();
		assertThat(nightActionRepository.findByGameIdAndActionType(game.getId(), "占い")).isEmpty();
		assertThat(nightActionRepository.findByGameIdAndActionType(game.getId(), "護衛")).isEmpty();
		assertThat(nightActionRepository.findByGameIdAndActionType(game.getId(), "襲撃")).isEmpty();
	}

	@Test
	void 名乗りカードで占い師が名乗ると自分の役職と現在日が記録される() {
		Game game = gameStartService.startGame();
		GamePlayer actor = findByRoleName(game, "占い師");
		Card card = new Card("名乗りカード", CardEffectType.DECLARATION, "昼", 1);

		boolean handled = cardPlayService.playCard(game, actor, card, null);

		assertThat(handled).isTrue();
		GamePlayer saved = gamePlayerRepository.findById(actor.getId()).orElseThrow();
		assertThat(saved.getClaimedRoleId()).isEqualTo(actor.getRoleId());
		assertThat(saved.getClaimedAtDay()).isEqualTo(game.getDayNumber());
	}

	@Test
	void 名乗りカードで狩人が名乗ると自分の役職と現在日が記録される() {
		Game game = gameStartService.startGame();
		GamePlayer actor = findByRoleName(game, "狩人");
		Card card = new Card("名乗りカード", CardEffectType.DECLARATION, "昼", 1);

		boolean handled = cardPlayService.playCard(game, actor, card, null);

		assertThat(handled).isTrue();
		GamePlayer saved = gamePlayerRepository.findById(actor.getId()).orElseThrow();
		assertThat(saved.getClaimedRoleId()).isEqualTo(actor.getRoleId());
		assertThat(saved.getClaimedAtDay()).isEqualTo(game.getDayNumber());
	}

	@Test
	void 名乗り済みの2回目の名乗りはfalseで内容は変わらない() {
		Game game = gameStartService.startGame();
		GamePlayer actor = findByRoleName(game, "占い師");
		Card card = new Card("名乗りカード", CardEffectType.DECLARATION, "昼", 1);
		cardPlayService.playCard(game, actor, card, null);
		Integer firstDay = actor.getClaimedAtDay();
		game.setDayNumber(game.getDayNumber() + 1);

		boolean handled = cardPlayService.playCard(game, actor, card, null);

		assertThat(handled).isFalse();
		GamePlayer saved = gamePlayerRepository.findById(actor.getId()).orElseThrow();
		assertThat(saved.getClaimedRoleId()).isEqualTo(actor.getRoleId());
		assertThat(saved.getClaimedAtDay()).isEqualTo(firstDay);
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
