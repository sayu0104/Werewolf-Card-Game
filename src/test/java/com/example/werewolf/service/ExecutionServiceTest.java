package com.example.werewolf.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.werewolf.entity.GamePlayer;
import com.example.werewolf.repository.GamePlayerRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest // ＝ テスト用にアプリ一式を起動する印。本物のDBなどを使って動作を確かめられる
@Transactional // ＝ テストが終わったらDBへの変更を巻き戻す印（テストでデータが残らないようにする）
class ExecutionServiceTest {

	@Autowired // Springが、必要な部品を自動でここに入れてくれる目印
	private ExecutionService executionService;

	@Autowired
	private GamePlayerRepository gamePlayerRepository;

	@Test // 「これは1つのテストだよ」という目印
	void 最多得票者が1人なら処刑されてEXECUTEDが返る() {
		GamePlayer player = gamePlayerRepository.save(new GamePlayer(1L, 1L, 1));
		// 準備：倉庫に ゲームプレイヤーを保存して、**その結果（id入り）**を ゲームプレイヤーの箱に入れる
		// プレイヤー → 「isAlive=true（生きてる）」
		// （gameId（試合1）, roleId（役職1）, seatOrder（1番席））

		ExecutionResult result = executionService.execute(List.of(player.getId()));
		// 実行：プレイヤーIDをリストにして、処刑処理を実行し、結果を箱に受け取る
		// List.of(player.getId()) は「この人1人だけのリスト」＝最多得票者が1人、という状況を作ってる

		GamePlayer updated = gamePlayerRepository.findById(player.getId()).orElseThrow();
		// 処刑した人の ID を倉庫から探してきて、なければエラーを出して、 あれば、 アップデートの箱に入れる
		
		assertThat(updated.getIsAlive()).isFalse();
		assertThat(result).isEqualTo(ExecutionResult.EXECUTED);
		// 確認１：生存しているか（true/false）を読み取る」で、isFalse() が「それが false（＝生存していない）か確かめる」
		// 確認２：result（処刑結果）が、EXECUTED（処刑した）と等しいか確かめる
	}

	@Test
	void 最多得票者が複数なら誰も処刑されずNEEDS_REVOTEが返る() {
		GamePlayer player1 = gamePlayerRepository.save(new GamePlayer(1L, 1L, 1));
		GamePlayer player2 = gamePlayerRepository.save(new GamePlayer(1L, 1L, 2));
		// 1.ゲームプレイヤーの倉庫に新しいゲームプレイヤーを保存する(試合〇、役職〇、席順〇番)
		// 2.そのプレイヤーを、プレイヤー1の箱に入れる

		ExecutionResult result = executionService.execute(List.of(player1.getId(), player2.getId()));
		// プレイヤー1のIDとプレイヤー2のIDをゲットして、その2つをリストに入れて、処刑処理ロジックを実行する。それを結果の箱に入れる

		GamePlayer updated1 = gamePlayerRepository.findById(player1.getId()).orElseThrow();
		GamePlayer updated2 = gamePlayerRepository.findById(player2.getId()).orElseThrow();
		// プレイヤー〇のIDを使って、倉庫からその人を探してきて、なければエラー、あれば updated〇 の箱に入れる
		
		assertThat(updated1.getIsAlive()).isTrue();
		assertThat(updated2.getIsAlive()).isTrue();
		// アップデートの箱から、プレイヤー1と2を取り出して、生存しているかの確認をする、trueで生きているか確かめる
		
		assertThat(result).isEqualTo(ExecutionResult.NEEDS_REVOTE);
		// result（結果）が、NEEDS_REVOTE（再投票が必要）と等しいか確かめる
	}

	@Test
	void 最多得票者が空リストならNEEDS_REVOTEが返る() {
		ExecutionResult result = executionService.execute(List.of());
		// 空のリストを処刑処理に渡して実行し、結果を result に受け取る

		assertThat(result).isEqualTo(ExecutionResult.NEEDS_REVOTE);
		// 結果の箱の中身が、再投票と同じになっているか確かめる
		// 「1じゃなければ弾く」＝「0人（空）でも、2人以上（同数）でも、まとめて NEEDS_REVOTE」
	}
}
