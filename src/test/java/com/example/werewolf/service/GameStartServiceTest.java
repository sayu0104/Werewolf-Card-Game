package com.example.werewolf.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.werewolf.entity.Game;
import com.example.werewolf.entity.GamePlayer;
import com.example.werewolf.entity.Role;
import com.example.werewolf.repository.GamePlayerRepository;
import com.example.werewolf.repository.RoleRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest // ＝ テスト用にアプリ一式を起動する印。本物のDBなどを使って動作を確かめられる
@Transactional // ＝ テストが終わったらDBへの変更を巻き戻す印（テストでデータが残らないようにする）
class GameStartServiceTest {

	@Autowired // Springが、必要な部品を自動でここに入れてくれる目印
	private GameStartService gameStartService;
	
	// GameStartService gameStartService …　型（種類・設計図の名前） 名前（この箱の呼び名）
	// ↑ この「GameStartService型の箱」を、この係の手元に持っておく宣言
	// （中身は、コンストラクタでDIで受け取った"倉庫番"が入る）

	@Autowired
	private GamePlayerRepository gamePlayerRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Test // 「これは1つのテストだよ」という目印
	void 試合を開始するとGameが1件保存される() {
		
		// 実行：試合開始を呼ぶ
		Game game = gameStartService.startGame();
		// startGame() を呼んで、返ってきた Game を受け取る

		// 確認：1.2.3.4
		assertThat(game.getId()).isNotNull();
		assertThat(game.getStatus()).isEqualTo("in_progress");
		assertThat(game.getDayNumber()).isEqualTo(1);
		assertThat(game.getIsSinglePlayer()).isTrue();
		
		// assertThat(見たいもの).○○(期待値)
		// 1.idは　空じゃない？　（空じゃない＝ちゃんと保存された（DBがid振った））
		// 2.ステータスは "進行中" になってる？（"in_progress" … 進行中）
		// 3.ゲーム内の日付は、1日？
		// 4.1人でも遊べる（シングルプレイ）モード？
	}

	@Test
	void 試合を開始するとGamePlayerが6人保存される() {
		Game game = gameStartService.startGame();
		// startGame() を呼んで、返ってきた Game を受け取る

		List<GamePlayer> players = gamePlayerRepository.findAll().stream()
				.filter(player -> player.getGameId().equals(game.getId()))
				.toList();

		assertThat(players).hasSize(6);
		// ゲームプレイヤーの人数は、6人？　（ここで確認しているのは「人数（数）」だけ）
		
		List<Integer> seatOrders = players.stream().map(GamePlayer::getSeatOrder).sorted().toList();
		assertThat(seatOrders).containsExactly(1, 2, 3, 4, 5, 6);
		// 席番号は、「1,2,3,4,5,6」の順番で合っている？　（ちゃんと「順番通り」で、被りも、抜けもない）
		
		// players           … 6人が入った箱（スタート地点）
		// .stream()       … これから流れ作業をするよ
		// .map(GamePlayer::getSeatOrder)  … 各人を「席番号」に変える　（"人"を"席番号"に）
		// .sorted()       … 小さい順に並べる
		// .toList()       … 結果を箱（リスト）に戻す
		
		// containsExactly … これで全部、余計なものは無い
	}

	@Test
	void 六人全員に役職が割り当てられ内訳が想定通りになる() {
		Game game = gameStartService.startGame();

		List<GamePlayer> players = gamePlayerRepository.findAll().stream()
				.filter(player -> player.getGameId().equals(game.getId()))
				.toList();

		Map<String, Long> countByName = new HashMap<>();
		// 6人の役職を数えて、内訳（人狼何人・村人何人…）を集計したい
		
		// Map … 対応表（辞書） 名前と値をペアで持つ箱
		// <String, Long> … 探すキー　String（役職名の文字）、値がLong（人数の数字）
		
		for (GamePlayer player : players) {
			// 1個ずつ取り出す(6人を1人ずつ見ていく)
			
			assertThat(player.getRoleId()).isNotNull();
			// その人の役職のIdは、空じゃない？（その人に役職はあるはず）
			
			Role role = roleRepository.findById(player.getRoleId()).orElseThrow();
			
			// Role role = roleRepository …
			// .findById … Idで探す
			// (player.getRoleId())… その人のroleIdから、役職(Role)を引く
			// .orElseThrow() … なければエラー
			
			countByName.merge(role.getName(), 1L, Long::sum);
			//その役職名の欄に、1 を足す（すでにあれば合算、無ければ新規で1）
			
			// merge … 数え上げる道具(マージ ＝ 合体・統合)
			// role.getName() … 役職の名前
			// 1L … Long型の1
			// Long::sum … 足し算する（1ずつ足してる）
		}

		assertThat(countByName.get("人狼")).isEqualTo(1);
		assertThat(countByName.get("占い師")).isEqualTo(1);
		assertThat(countByName.get("狩人")).isEqualTo(1);
		assertThat(countByName.get("狂人")).isEqualTo(1);
		assertThat(countByName.get("村人")).isEqualTo(2);
		
		// 例：役職名（人狼）で探して、その役職の人数は、1人で合ってる？
		// Map(辞書)　から　キーと値　で探す
	}
}
