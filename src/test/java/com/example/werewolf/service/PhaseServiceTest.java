package com.example.werewolf.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.werewolf.entity.Game;
import com.example.werewolf.entity.Phase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest // ＝ テスト用にアプリ一式を起動する印。本物のDBなどを使って動作を確かめられる
@Transactional // ＝ テストが終わったらDBへの変更を巻き戻す印（テストでデータが残らないようにする）
class PhaseServiceTest { // フェーズの動き方のテスト

	@Autowired // Springが、必要な部品を自動でここに入れてくれる目印
	private PhaseService phaseService;
	
	// PhaseService phaseService …　型（種類・設計図の名前） 名前（この箱の呼び名）
	// ↑ この「PhaseService型の箱」を、この係の手元に持っておく宣言
	// （中身は、コンストラクタでDIで受け取った"倉庫番"が入る）

	@Autowired
	private GameStartService gameStartService;

	@Test // 「これは1つのテストだよ」という目印
	void 朝の次は昼になる() {
		Game game = new Game("in_progress");
		game.setDayNumber(1);
		game.setCurrentPhase(Phase.MORNING);
		
		// ("in_progress") … コンストラクタ（この手順書）を使う
		
		// 準備：
		// １．手順書("in_progress")を使って、ゲームを作る
		// ２．１日目の日付を用意する
		// ３．朝のフェーズをセットする

		Game result = phaseService.advancePhase(game);
		
		// 実行；
		// Game result … 結果の箱
		// phaseServic … フェーズ管理の係（道具）
		// .advancePhase … フェーズを進める、をお願い
		// (game); … game を渡して

		assertThat(result.getCurrentPhase()).isEqualTo(Phase.DAY);
		
		// assertThat(見たいもの).○○(期待値)
		// 結果：次のフェーズは昼のはず
	}

	@Test
	void 昼の次は初日なら夜1になる() {
		Game game = new Game("in_progress");
		game.setDayNumber(1);
		game.setCurrentPhase(Phase.DAY);
		
		// 準備：
		// １．手順書("in_progress")を使って、ゲームを作る
		// ２．１日目の日付を用意する
		// ３．昼のフェーズをセットする

		Game result = phaseService.advancePhase(game);	
		// 実行：フェーズを１つ進める

		assertThat(result.getCurrentPhase()).isEqualTo(Phase.NIGHT1);
		// 結果：次のフェーズは夜１のはず（初日は投票なし）
	}

	@Test
	void 昼の次は二日目以降なら投票になる() {
		Game game = new Game("in_progress");
		game.setDayNumber(2);
		game.setCurrentPhase(Phase.DAY);
		
		// 準備：
		// １．手順書("in_progress")を使って、ゲームを作る
		// ２．２日目の日付を用意する
		// ３．昼のフェーズをセットする

		Game result = phaseService.advancePhase(game);
		// 実行：フェーズを１つ進める

		assertThat(result.getCurrentPhase()).isEqualTo(Phase.VOTE);
		// 次のフェーズは投票のはず（２日目以降は投票あり）
	}

	@Test
	void 投票の次は夜1になる() {
		Game game = new Game("in_progress");
		game.setDayNumber(2);
		game.setCurrentPhase(Phase.VOTE);
		
		// 準備：
		// １．手順書("in_progress")を使って、ゲームを作る
		// ２．２日目の日付を用意する
		// ３．投票のフェーズをセットする

		Game result = phaseService.advancePhase(game);
		// 実行：フェーズを１つ進める

		assertThat(result.getCurrentPhase()).isEqualTo(Phase.NIGHT1);
		// 結果：次のフェーズは夜１のはず
	}

	@Test
	void 夜1の次は夜2になる() {
		Game game = new Game("in_progress");
		game.setDayNumber(1);
		game.setCurrentPhase(Phase.NIGHT1);
		
		// 準備：
		// １．手順書("in_progress")を使って、ゲームを作る
		// ２．１日目の日付を用意する
		// ３．夜１のフェーズをセットする

		Game result = phaseService.advancePhase(game);
		// 実行：フェーズを１つ進める

		assertThat(result.getCurrentPhase()).isEqualTo(Phase.NIGHT2);
		// 結果：次のフェーズは夜２のはず
	}

	@Test
	void 夜2の次は朝になりdayNumberが1増える() {
		Game game = new Game("in_progress");
		game.setDayNumber(1);
		game.setCurrentPhase(Phase.NIGHT2);
		
		// 準備：
		// １．手順書("in_progress")を使って、ゲームを作る
		// ２．１日目の日付を用意する
		// ３．夜２のフェーズをセットする

		Game result = phaseService.advancePhase(game);
		// 実行：フェーズを１つ進める

		assertThat(result.getCurrentPhase()).isEqualTo(Phase.MORNING);
		assertThat(result.getDayNumber()).isEqualTo(2);
		
		// 結果：
		// 1.次のフェーズは朝のはず
		// 2.１日目から、２日目になっているはず
	}

	@Test
	void 試合開始直後のcurrentPhaseが昼になっている() {
		Game game = gameStartService.startGame();
		// 準備：ゲーム開始時を用意する

		assertThat(game.getCurrentPhase()).isEqualTo(Phase.DAY);
		// 結果：試合開始直後（１日目）のフェーズは、昼のはず
	}
}
