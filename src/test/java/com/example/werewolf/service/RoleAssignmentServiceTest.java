package com.example.werewolf.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.werewolf.entity.GamePlayer;
import com.example.werewolf.entity.Role;
import com.example.werewolf.repository.RoleRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

// @SpringBootTest ＝ テスト用にアプリ一式を起動する印。本物のDBなどを使って動作を確かめられる
// @Transactional ＝ テストが終わったらDBへの変更を巻き戻す印（テストでデータが残らないようにする）
@SpringBootTest
@Transactional
class RoleAssignmentServiceTest {

	// @Autowired ＝ 必要な部品（係）をSpringが自動でセットしてくれる印（自分でnewしない＝DI）
	// roleAssignmentService ＝ 今回テストする本体（役職を割り振る処理係）
	@Autowired
	private RoleAssignmentService roleAssignmentService;

	// roleRepository ＝ 確認のために使う倉庫番（roleIdから役職を引き直すのに使う）
	@Autowired
	private RoleRepository roleRepository;

	// createSixPlayers ＝ テスト用の「6人」を用意するお手伝い（3つのテストで使い回す）
	private List<GamePlayer> createSixPlayers() {
		// List<GamePlayer>(GamePlayerの並び) players(playersという名前)
		// 「GamePlayerが並ぶplayersという空の箱を、新しく用意する」
		List<GamePlayer> players = new ArrayList<>();
		// 番号iを1から6まで、1つずつ増やしながら6回くり返す（i++ ＝ iを1増やす）
		for (int i = 1; i <= 6; i++) {
			// 新しいGamePlayerを1人作って、箱に加える（add ＝ 加える）
			players.add(new GamePlayer(1L, null, i));
		}
		// 作った6人を、呼び出し元に返す
		return players;
	}

	// @Test ＝ 「これはテスト項目ですよ」の印（JUnitがこのメソッドを実行して合否を出す）
	// メソッド名（日本語）＝ このテストで何を確かめるか
	@Test
	void 六人渡すと全員に役職が割り当てられる() {
		// ①準備：テスト用の6人を用意する
		List<GamePlayer> players = createSixPlayers();

		// ②実行：本体の assignRoles を呼んで役職を配らせ、結果を result で受け取る
		List<GamePlayer> result = roleAssignmentService.assignRoles(players);

		// ③確認：assertThat(見たいもの).○○(期待する値) ＝「〜が、○○であると確かめる」
		// assertThat(result)(resultが) .hasSize(6)(サイズ6だと確かめる)
		// 「result が 6人いる ことを確かめる」
		assertThat(result).hasSize(6);
		// result の中の人を1人ずつ取り出して（: のfor＝1個ずつ）、全員に同じ確認をする
		for (GamePlayer player : result) {
			// player.getRoleId()(その人の役職IDが) .isNotNull()(空でないと確かめる)
			// 「全員ちゃんと役職IDがついている（空っぽの人がいない）ことを確かめる」
			assertThat(player.getRoleId()).isNotNull();
		}
	}

	@Test
	void 役職の内訳が想定通りになる() {
		// ①準備：6人を用意
		List<GamePlayer> players = createSixPlayers();

		// ②実行：役職を配らせて結果を受け取る
		List<GamePlayer> result = roleAssignmentService.assignRoles(players);

		// ③確認の下ごしらえ：役職名ごとに「何人いるか」を数える箱を用意する
		// Map<String, Long> ＝「名前(String)→個数(Long)」を対にして持つ入れ物（＝countByName）
		Map<String, Long> countByName = new HashMap<>();
		// result の人を1人ずつ見て（: のfor）、その人の役職を数える
		for (GamePlayer player : result) {
			// その人のroleIdから、役職(Role)を引き直す（findById＋orElseThrow＝無ければエラー）
			Role role = roleRepository.findById(player.getRoleId()).orElseThrow();
			// merge ＝ その役職名の個数に1を足す（初めての名前なら1、2回目以降は+1）
			countByName.merge(role.getName(), 1L, Long::sum);
		}

		// ③確認：数えた結果が、想定の内訳（人狼1・占い師1・狩人1・狂人1・村人2）と一致するか
		// assertThat(...get("人狼"))(人狼の数が) .isEqualTo(1)(1と等しいと確かめる)
		assertThat(countByName.get("人狼")).isEqualTo(1);
		assertThat(countByName.get("占い師")).isEqualTo(1);
		assertThat(countByName.get("狩人")).isEqualTo(1);
		assertThat(countByName.get("狂人")).isEqualTo(1);
		assertThat(countByName.get("村人")).isEqualTo(2);
	}

	@Test
	void 複数回実行すると割り当てが固定ではない() {
		// firstResult ＝ 1回目の結果を覚えておく箱（最初は空＝null）
		List<Long> firstResult = null;
		// foundDifferentOrder ＝「1回目と違う並びが見つかったか」の旗（boolean＝true/falseの2択。最初はfalse）
		boolean foundDifferentOrder = false;

		// 最大20回くり返して、「毎回同じ並びではない（＝ランダムだ）」を確かめる
		for (int i = 0; i < 20; i++) {
			// 毎回、新しく6人を用意して役職を配らせる
			List<GamePlayer> players = createSixPlayers();
			List<GamePlayer> result = roleAssignmentService.assignRoles(players);
			// 配られた役職IDだけを順番に取り出して、並び（リスト）にする
			List<Long> roleIds = result.stream().map(GamePlayer::getRoleId).toList();

			if (firstResult == null) {
				// まだ1回目 → この並びを「基準」として覚えておく
				firstResult = roleIds;
			} else if (!firstResult.equals(roleIds)) {
				// 1回目と並びが違った（! ＝ ではない）→ ランダムだと確認できたので旗を立てて終了
				foundDifferentOrder = true;
				break;
			}
		}

		// ③確認：foundDifferentOrder(違う並びが見つかったが) .isTrue()(本当(true)だと確かめる)
		// 「20回のうちに、少なくとも1回は違う並びが出た＝配り方がランダムだ」を確かめる
		assertThat(foundDifferentOrder).isTrue();
	}
}
