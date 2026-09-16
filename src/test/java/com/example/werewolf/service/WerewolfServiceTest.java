package com.example.werewolf.service;

import static org.assertj.core.api.Assertions.assertThat;

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
class WerewolfServiceTest { // 人狼の役職のテスト

	@Autowired
	private WerewolfService werewolfService;

	@Autowired
	private GameStartService gameStartService;

	@Autowired
	private GamePlayerRepository gamePlayerRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private NightActionRepository nightActionRepository;

	@Test
	void 生存する人狼がいると襲撃のNightActionが記録される() {
		// ゲームをスタートする
		Game game = gameStartService.startGame();

		// プレイヤー全員から、条件に合うやつを探して、最初の1人を取り出す（無ければエラー）
		List<GamePlayer> players = gamePlayerRepository.findByGameId(game.getId());
		GamePlayer werewolf = players.stream()
				.filter(player -> "人狼".equals(roleRepository.findById(player.getRoleId()).orElseThrow().getName()))
				.findFirst()
				.orElseThrow();

		List<NightAction> nightActions = werewolfService.act(game);
		// 人狼サービスに、このゲームで夜の行動（act）をさせて、その結果を、nightActions で受け取る

		assertThat(nightActions).hasSize(1);
		// 人狼が1人いるとき、act を実行したら、ちゃんと1件記録されるか?

		NightAction nightAction = nightActions.get(0);
		// 結果のリストから、0番目（＝1個目）を get で取り出して、nightAction の箱に入れてる
		
		assertThat(nightAction.getGameId()).isEqualTo(game.getId());
		assertThat(nightAction.getDayNumber()).isEqualTo(game.getDayNumber());
		assertThat(nightAction.getActorGamePlayerId()).isEqualTo(werewolf.getId());
		assertThat(nightAction.getActionType()).isEqualTo("襲撃");
		assertThat(nightAction.getTargetGamePlayerId()).isNotEqualTo(werewolf.getId());
		// 1.gameId が一致するか
		// 2.dayNumber が一致するか（夜は2日目から）
		// 3.襲撃をした人(actor)が、あの人狼"プレイヤー"のIDと一致するか
		// 4.行動タイプが "襲撃" か
		// 5.襲撃先(target)が、人狼本人と"違う"か（＝自分を襲撃してないか）
	}
}
