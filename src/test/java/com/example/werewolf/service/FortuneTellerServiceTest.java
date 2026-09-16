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
		// 2.dayNumber が一致するか（夜は2日目から）
		// 3.占いをした人(actor)が、あの占い師"プレイヤー"のIDと一致するか
		// 4.行動タイプが "占い" か
		// 5.占い先(target)が、占い師本人と"違う"か（＝自分を占ってないか）
	}
}
