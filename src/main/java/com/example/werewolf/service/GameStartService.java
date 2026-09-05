package com.example.werewolf.service;

import com.example.werewolf.entity.Game;
import com.example.werewolf.entity.GamePlayer;
import com.example.werewolf.entity.Phase;
import com.example.werewolf.repository.GameRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service // 以下のクラスが「処理担当の係」であることをSpringに知らせる印
public class GameStartService { // ゲームの試合開始処理クラス

	private static final int PLAYER_COUNT = 6;
	// ゲームをプレイする人数 = ６人
	// 人数や内訳を変えたいときはこの1行を書き換える
	// static final = 実行中は動かない共有の定数（開発中に手で書き換えるのはOK）
	
	private final GameRepository gameRepository;
	private final RoleAssignmentService roleAssignmentService;
	// GameRepository gameRepository …　型（種類・設計図の名前） 名前（この箱の呼び名）
	// ↑ この「GameRepository型の箱」を、この係の手元に持っておく宣言
	// （中身は、コンストラクタでDIで受け取った"倉庫番"が入る）

	//                          ↓ゲームの倉庫番（道具）       　　　　　　↓役職を割り振りする道具
	public GameStartService(GameRepository gameRepository, RoleAssignmentService roleAssignmentService) {
		this.gameRepository = gameRepository;
		this.roleAssignmentService = roleAssignmentService;
	}

	public Game startGame() {
		Game game = new Game("in_progress");
		// ("in_progress") … コンストラクタ（この手順書）を使う
		// 1.作る瞬間 → status と createdAt が入る（コンストラクタ）
		
		game.setDayNumber(1); // 1日目
		// 2.作った後 → dayNumber欄に 1 を書き込む（setter）

		game.setCurrentPhase(Phase.DAY);

		game.setIsSinglePlayer(true); // 1人プレイか？ (はい)
		// 3.作った後 → isSinglePlayer欄に true を書き込む（setter）
		
		game = gameRepository.save(game);
		// Gameをnewで作った直後はidが空
		// saveするとDBがidを自動で振り、id付きのGameが返ってくる
		// これをgameに入れ直すことで、下のgetId()が使えるようになる
		// 保存してはじめてidがわかる → それを受け取る

        //	   (型)       (名前)     (機能：空の箱を作る)
		List<GamePlayer> players = new ArrayList<>();
		// GamePlayerを並べる箱を、players という名前で、空っぽで新しく用意する
		
		// < > … この箱・この道具が扱うのは"どの種類"か　中身の"種類"を指定する
		// List<GamePlayer>（GamePlayerを入れる箱）
		
		for (int seatOrder = 1; seatOrder <= PLAYER_COUNT; seatOrder++) {
			players.add(new GamePlayer(game.getId(), null, seatOrder));
			// int seatOrder = 1; … seatOrderを１から始める
			// seatOrder <= PLAYER_COUNT; … seatOrderがPLAYER_COUNT（＝6）以下のあいだ、くり返し続ける（条件）
			// seatOrder++ … １回繰り返すたびに、seatOrder を1増やす（1.2.3.…）
			
			// players.add(...) … 作った人を、playersに追加
			// new GamePlayer(...) … gameId, roleId=null, 席順=今の番号
		}

		//     1.道具         2.お願いする命令(3.渡す材料)
		roleAssignmentService.assignRoles(players);
		// 役職を配る係に、この6人への割り振りを任せる
		
		// roleAssignmentService … DIで受け取っておいた道具
		// .assignRoles(...) … 役職を配る係さん、役職を割り振る仕事をやって
		// (players) … forで作った6人に配る
		
		// ※assignRolesは中で保存まで済ませるので、戻り値は受け取らず呼ぶだけでよい

		return game;
		// 出来上がった Game（id入り・進行中）を、呼んだ相手に返す
	}
}
