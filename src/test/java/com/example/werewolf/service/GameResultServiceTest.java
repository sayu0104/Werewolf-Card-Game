package com.example.werewolf.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.werewolf.entity.GamePlayer;
import com.example.werewolf.entity.Role;
import com.example.werewolf.repository.GamePlayerRepository;
import com.example.werewolf.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class GameResultServiceTest { // ゲーム結果（勝利判定）のテスト

	@Autowired
	private GameResultService gameResultService;

	@Autowired
	private GamePlayerRepository gamePlayerRepository;

	@Autowired
	private RoleRepository roleRepository;

	private Long werewolfRoleId() {
		return roleRepository.findByName("人狼").orElseThrow().getId();
		// 役職の倉庫から人狼という名前で探して、なければエラー、（見つけた人狼の）IDをゲットして
		// それを（呼び出し元に）返す
		// 人狼のIDを持ってきてくれる係(メソッド)。呼ぶと return でIDを渡す
	}

	private Long villagerRoleId() {
		return roleRepository.findByName("村人").orElseThrow().getId();
		// 役職の倉庫から村人という名前で探して、なければエラー、（見つけた村人の）IDをゲットして
		// それを（呼び出し元に）返す
	}

	private Long madmanRoleId() {
		return roleRepository.findByName("狂人").orElseThrow().getId();
		// 役職の倉庫から狂人という名前で探して、なければエラー、（見つけた狂人の）IDをゲットして
		// それを（呼び出し元に）返す
	}

	@Test
	void 人狼が0人なら村人陣営の勝ち() {
		long gameId = System.nanoTime();
		// ゲームIDの箱に、毎回違う数字（の時刻）を入れる
		// テストどうしのプレイヤーが混ざらない
		
		gamePlayerRepository.save(new GamePlayer(gameId, villagerRoleId(), 1));
		gamePlayerRepository.save(new GamePlayer(gameId, villagerRoleId(), 2));
		// この試合（gameId）に、村人を1人（〇番席に）追加する（2人分）
		// gameId（さっき作ったかぶらないID）

		GameResult result = gameResultService.judge(gameId);
		// 勝敗判定の係（gameResultService）に、gameId を渡して judge（判定）を実行してもらい、その結果を result という箱に入れる

		assertThat(result).isEqualTo(GameResult.VILLAGER_WIN);
		// 結果の箱の中身（result）が、村人陣営の勝利（VILLAGER_WIN）と同じかどうか確かめる
	}

	@Test
	void 人狼と村人が同数なら人狼陣営の勝ち() {
		long gameId = System.nanoTime();
		gamePlayerRepository.save(new GamePlayer(gameId, werewolfRoleId(), 1));
		gamePlayerRepository.save(new GamePlayer(gameId, werewolfRoleId(), 2));
		gamePlayerRepository.save(new GamePlayer(gameId, villagerRoleId(), 3));
		gamePlayerRepository.save(new GamePlayer(gameId, villagerRoleId(), 4));

		GameResult result = gameResultService.judge(gameId);

		assertThat(result).isEqualTo(GameResult.WEREWOLF_WIN);
	}

	@Test
	void 人狼が村人陣営より少なければ続行() {
		// 条件に対して、ゲームがちゃんと続くかどうかを確かめてる
		// 勝敗が"決まらない"ケースも確かめている
		
		long gameId = System.nanoTime();
		gamePlayerRepository.save(new GamePlayer(gameId, werewolfRoleId(), 1));
		gamePlayerRepository.save(new GamePlayer(gameId, villagerRoleId(), 2));
		gamePlayerRepository.save(new GamePlayer(gameId, villagerRoleId(), 3));
		gamePlayerRepository.save(new GamePlayer(gameId, villagerRoleId(), 4));

		GameResult result = gameResultService.judge(gameId);

		assertThat(result).isEqualTo(GameResult.CONTINUE);
	}

	@Test
	void 死亡したプレイヤーは数に入れない() {
		long gameId = System.nanoTime();
		GamePlayer deadWerewolf = gamePlayerRepository.save(new GamePlayer(gameId, werewolfRoleId(), 1));
		// ゲームプレイヤー倉庫に新しいゲームプレイヤーを保存する(中身はゲームID、人狼役職のID、席順1)
		// それを deadWerewolf の箱に入れる
		
		deadWerewolf.setIsAlive(false);
		// deadWerewolf の中身（プレイヤー）の生存状態を false にして、そのプレイヤーを死亡にする
		
		gamePlayerRepository.save(deadWerewolf);
		// ゲームプレイヤー倉庫に deadWerewolf を保存する
		// 死亡状態になった deadWerewolf が、DBに保存（更新）される
		
		gamePlayerRepository.save(new GamePlayer(gameId, villagerRoleId(), 2));
		// ゲームプレイヤー倉庫に保存／ゲームID・村人役職ID取得・席順2
		// 現状では死んでいる人狼1人と、生存している村人1人が保存されている

		GameResult result = gameResultService.judge(gameId);
		// ゲーム結果のクラスから、ゲームIDを使って勝利判定をして、結果の箱に入れる
		// judge は死んでるプレイヤーを省くので、生存している村人1人だけの状態
		// 死んでる人狼は数えないので、数えられるのは生きてる村人1人だけ

		assertThat(result).isEqualTo(GameResult.VILLAGER_WIN);
		// 結果、村人の勝利という結果になるはず
	}

	@Test
	void 狂人は人狼として数えない() {
		long gameId = System.nanoTime();
		gamePlayerRepository.save(new GamePlayer(gameId, werewolfRoleId(), 1));
		gamePlayerRepository.save(new GamePlayer(gameId, madmanRoleId(), 2));
		gamePlayerRepository.save(new GamePlayer(gameId, villagerRoleId(), 3));
		gamePlayerRepository.save(new GamePlayer(gameId, villagerRoleId(), 4));
		// 人狼1・狂人1・村人2 を作る

		GameResult result = gameResultService.judge(gameId);
		// judge を実行
		// 人狼陣営は2人（人狼1＋狂人1）だが、judge は狂人を人狼として数えないのでスルー

		assertThat(result).isEqualTo(GameResult.CONTINUE);
		// 実際に数えるのは 人狼1・村人2 → 人狼1 < 村人2 なので決着せず CONTINUE（続行）
	}

	@Test
	void 狂人が生き残っても人狼が全滅なら村人の勝ち() {
		long gameId = System.nanoTime();
		GamePlayer deadWerewolf = gamePlayerRepository.save(new GamePlayer(gameId, werewolfRoleId(), 1));
		// 人狼役職のプレイヤーを1人用意して、倉庫に保存し、deadWerewolfの箱に入れる
		
		deadWerewolf.setIsAlive(false);
		// 箱の中身の、人狼プレイヤーの生存をfalse（死亡）にする
		
		// なんで一回 deadWerewolf の箱に入れてから false にするのか
		// （＝保存したモノを受け取っておかないと、後で setIsAlive したり save し直したりできない）
		
		gamePlayerRepository.save(deadWerewolf);
		// 倉庫に、箱の中身の状態を保存（更新）する

		gamePlayerRepository.save(new GamePlayer(gameId, madmanRoleId(), 2));
		gamePlayerRepository.save(new GamePlayer(gameId, villagerRoleId(), 3));
		// ゲームプレイヤーを二人用意する。一人は狂人で、もう一人は村人。 その状態を倉庫に保存する

		GameResult result = gameResultService.judge(gameId);
		// 現時点でのゲームの勝利判定をジャッジで実行する

		assertThat(result).isEqualTo(GameResult.VILLAGER_WIN);
		// 人狼陣営1人と村人陣営1人だが、狂人は「人狼（役職）」に含まれず、スルーされるため、村人陣営の勝利
	}
}
