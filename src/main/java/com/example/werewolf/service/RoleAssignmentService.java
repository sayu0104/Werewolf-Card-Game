package com.example.werewolf.service;

import com.example.werewolf.entity.GamePlayer;
import com.example.werewolf.entity.Role;
import com.example.werewolf.repository.GamePlayerRepository;
import com.example.werewolf.repository.RoleRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Service;

@Service // 以下のクラスが「処理担当の係」であることをSpringに知らせる印
public class RoleAssignmentService { // 役職割り振りの処理を受け持つクラス

	private static final List<String> ROLE_NAMES = List.of("人狼", "占い師", "狩人", "狂人", "村人", "村人");

	// 配る役職の内訳（人狼1・占い師1・狩人1・狂人1・村人2＝6人分）
	// 人数や内訳を変えたいときはこの1行を書き換える
	// static final = 実行中は動かない共有の定数（開発中に手で書き換えるのはOK）

	// RoleRepository(RoleRepository型の入れ物) roleRepository(roleRepositoryという名前)
	// 「RoleRepository型の、roleRepositoryという名前の置き場を用意する」 ← ここに、渡された倉庫番をしまう
	// 使う倉庫番（Repository）を外から受け取って、この係の手元に持っておく（DI＝依存性注入）
	// Springが自動で渡してくれるので、自分で作らなくてよい

	private final RoleRepository roleRepository;
	private final GamePlayerRepository gamePlayerRepository;

	// ↓受け取り口。「RoleRepository型の紙(引数)を roleRepository という名前で受け取る」
	public RoleAssignmentService(RoleRepository roleRepository, GamePlayerRepository gamePlayerRepository) {
		this.roleRepository = roleRepository;
		this.gamePlayerRepository = gamePlayerRepository;
	}

	// １．ゲーム開始・N人を受け取る(人数が正しいかもチェック)
	public List<GamePlayer> assignRoles(List<GamePlayer> players) {
		// assignRoles = 役職を割り振る（処理名）
		// (List<GamePlayer> players)

		if (players.size() != ROLE_NAMES.size()) {
			// 渡された人数が、用意した役職の数と合っている？もし違うなら…
			// != … 「等しくない」

			throw new IllegalArgumentException("プレイヤー人数が想定と異なる: " + players.size());
			// 処理を止め、理由を明確にして、エラーメッセージを出す
			// Illegal(不正な)＋Argument(引数＝渡された紙)＋Exception(例外) ＝「渡された紙がおかしいよ、という例外」
		}

		// ２．役職名のリストから、実際に配るための"役職IDの山"を組み立てる
		List<Long> roleIds = new ArrayList<>();
		// Long型（数字）が並ぶ、roleIdsという名前の箱を、空っぽで新しく用意する

		for (String roleName : ROLE_NAMES) {
			// くり返せ String型のroleNameという名前で 〜の中から1個ずつ ROLE_NAMESから
			// ROLE_NAMES の中身を1個ずつ roleName に入れながら、中括弧の中を繰り返せ

			Role role = roleRepository.findByName(roleName)
					// この名前(例:"人狼")のRoleを探して

					.orElseThrow(() -> new IllegalStateException("役職が見つからない: " + roleName));
			// 箱に中身があればそれを取り出す。もし空だったら、代わりにエラーを投げる

			roleIds.add(role.getId());
			// roleIdsの箱に追加する roleのID（背番号）を取り出して
		}

		// ３．シャッフルして配る
		Collections.shuffle(roleIds);
		// Collections = リストみたいな"集まり"を操作する道具が入った箱

		for (int i = 0; i < players.size(); i++) {
			// 1.開始 2.続ける条件 3.毎周の変化

			// int i = 0 = 番号 i を 0 からスタート（プログラムは0から数える）
			// i < players.size() = 「i が人数(6)未満のあいだ」回し続ける → i=0,1,2,3,4,5 の6回
			// i++ = 1周ごとに i を +1 する（++＝1増やす）

			players.get(i).setRoleId(roleIds.get(i));
			// players.get(i) … プレイヤーの並びから i番目の人を取り出す（get＝取り出す、さっきの三点セットの実戦！）
			// roleIds.get(i) … 混ぜた役職IDの山から i番目のIDを取り出す
			// .setRoleId(...) … その人に、その役職IDをセットする（書き込む）
		}

		return gamePlayerRepository.saveAll(players);
		// saveAll＝配り終えた6人をまとめて保存して返す
	}
}
