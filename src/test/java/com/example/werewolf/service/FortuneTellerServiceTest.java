package com.example.werewolf.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.werewolf.entity.Game;
import com.example.werewolf.entity.GamePlayer;
import com.example.werewolf.entity.NightAction;
import com.example.werewolf.entity.Role;
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
class FortuneTellerServiceTest { // 占い師の役職のテスト

	@Autowired
	private FortuneTellerService fortuneTellerService;

	@Autowired
	private GameStartService gameStartService;

	@Autowired
	private GamePlayerRepository gamePlayerRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private NightActionRepository nightActionRepository;

	@Test
	void 生存する占い師がいると占いのNightActionが記録される() {
		// ゲームをスタートする
		Game game = gameStartService.startGame();

		// プレイヤー全員から、条件に合うやつを探して、最初の1人を取り出す（無ければエラー）
		List<GamePlayer> players = gamePlayerRepository.findByGameId(game.getId());
		GamePlayer fortuneTeller = players.stream()
				.filter(player -> "占い師".equals(roleRepository.findById(player.getRoleId()).orElseThrow().getName()))
				.findFirst()
				.orElseThrow();

		List<NightAction> nightActions = fortuneTellerService.act(game);
		// 占い師サービスに、このゲームで夜の行動（act）をさせて、その結果を、nightActions で受け取る

		assertThat(nightActions).hasSize(1);
		// 占い師が1人いるとき、act を実行したら、ちゃんと1件記録されるか?

		NightAction nightAction = nightActions.get(0);
		// 結果のリストから、0番目（＝1個目）を get で取り出して、nightAction の箱に入れてる
		
		assertThat(nightAction.getGameId()).isEqualTo(game.getId());
		assertThat(nightAction.getDayNumber()).isEqualTo(game.getDayNumber());
		assertThat(nightAction.getActorGamePlayerId()).isEqualTo(fortuneTeller.getId());
		assertThat(nightAction.getActionType()).isEqualTo("占い");
		assertThat(nightAction.getTargetGamePlayerId()).isNotEqualTo(fortuneTeller.getId());
		// 1.gameId が一致するか
		// 2.dayNumber が一致するか（夜は1日目から）
		// 3.占いをした人(actor)が、あの占い師"プレイヤー"のIDと一致するか
		// 4.行動タイプが "占い" か
		// 5.占い先(target)が、占い師本人と"違う"か（＝自分を占ってないか）
	}

	@Test
	void 対象が人狼のとき占い結果は黒で保存される() {
		NightAction nightAction = divineOnlyRole("人狼");
		// 人狼以外を全滅させて選択肢を1つにする

		assertThat(nightAction.getIsWerewolf()).isTrue();
		// 人狼を占ったので、true（人狼だった）という結果のはず
	}

	@Test
	void 対象が村人のとき占い結果は白で保存される() {
		NightAction nightAction = divineOnlyRole("村人");
		// 村人以外を全滅させて選択肢を1つにする

		assertThat(nightAction.getIsWerewolf()).isFalse();
		// 村人を占ったので、false（人狼ではなかった）という結果のはず
	}

	@Test
	void 対象が狂人のとき占い結果は白で保存される() {
		NightAction nightAction = divineOnlyRole("狂人");
		// 狂人以外を全滅させて選択肢を1つにする

		assertThat(nightAction.getIsWerewolf()).isFalse();
		// 狂人を占ったので、false（人狼ではなかった）という結果のはず
		// （狂人は人狼陣営ではあるが、人狼ではないため）
	}

	// 指定した役職を必ず占わせて、その占いの記録を返す係（テスト用の下準備）
	private NightAction divineOnlyRole(String targetRoleName) {
		
		// 1.ゲームを開始する
		Game game = gameStartService.startGame();
		
		// 2.「対象をもう1人残したか？」の旗。最初はまだなので false
		boolean targetKept = false;
		
		// 3.生きてる全員を1人ずつ確認する
		for (GamePlayer player : gamePlayerRepository.findByGameId(game.getId())) {
			String roleName = roleRepository.findById(player.getRoleId()).orElseThrow().getName();
			
			// 3-1.占い師は残す（占う人が必要なので殺さない）
			if ("占い師".equals(roleName)) {
				continue;
			}
			
			// 3-2.対象の役職で、まだ1人も残してないなら、その1人だけ残して旗を立てる
			// ※村人みたいに２人いるときは、最初の１人
			if (roleName.equals(targetRoleName) && !targetKept) {
				targetKept = true;
				continue;
			}
			
			// 3-3.それ以外は死亡させる（生存をfalseにして保存）
			player.setIsAlive(false);
			gamePlayerRepository.save(player);
		}

		// 4.生きてるのは占い師＋対象の1人だけ。この状態で占い師に占わせる
		List<NightAction> nightActions = fortuneTellerService.act(game);
		
		// 5.占いの記録はちょうど1件できてるはず
		assertThat(nightActions).hasSize(1);
		
		// 6.その1件を取り出して返す
		return nightActions.get(0);
	}
}
