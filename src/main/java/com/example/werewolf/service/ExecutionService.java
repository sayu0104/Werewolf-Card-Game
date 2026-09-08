package com.example.werewolf.service;

import com.example.werewolf.entity.GamePlayer;
import com.example.werewolf.repository.GamePlayerRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service // 以下のクラスが「処理担当の係」であることをSpringに知らせる印
public class ExecutionService { // 処刑ロジック

	private final GamePlayerRepository gamePlayerRepository;
	
	// GamePlayerRepository gamePlayerRepository …　型（種類・設計図の名前） 名前（この箱の呼び名）
	// ↑ この「GamePlayerRepository型の箱」を、この係の手元に持っておく宣言
	// （中身は、コンストラクタでDIで受け取った"倉庫番"が入る）

	//                      　　　　　　 ↓ゲームプレイヤーの倉庫番（道具）
	public ExecutionService(GamePlayerRepository gamePlayerRepository) {
		this.gamePlayerRepository = gamePlayerRepository;
	}

	public ExecutionResult execute(List<Long> mostVotedGamePlayerIds) {
		// 処刑結果を実行する
		// 最多得票者のIdリスト　の中から　Idを取り出して
		
		// 1.人数チェック（1人じゃなければ再投票）
		if (mostVotedGamePlayerIds.size() != 1) {
			// もし　最多得票者のIdリスト　の　人数が1人ではない　なら…
			
			// mostVotedGamePlayerIds … 最多得票者のIdリスト
			// .size() != 1 … 個数が 1ではない なら
			// != … 等しくない
			
			// ※「1人じゃない」＝「0人（空）or 複数（同数）」をまとめて弾く
			
			return ExecutionResult.NEEDS_REVOTE;
			// 処刑結果を、「再投票が必要」として返す
			// ※処刑はしない
		}

		//  2.1人なら処刑する
		Long targetId = mostVotedGamePlayerIds.get(0);
		// 最多得票者のIdリスト　の中から　0番目　のIdを取り出して
		// ※0番目（＝唯一の1人）
		
		GamePlayer target = gamePlayerRepository.findById(targetId).orElseThrow();
		
		// gamePlayerRepository　…　ゲームプレイヤー倉庫から
		// .findById(targetId) … Idで探す
		// .orElseThrow() … なければエラー
		
		target.setIsAlive(false);
		// target.setIsAlive …　その人の isAlive（生存フラグ）をsetterで書き込む
		// (false) … false（死亡）にして
		
		gamePlayerRepository.save(target);
		// 死亡させた状態を保存（DBに反映）

		return ExecutionResult.EXECUTED;
		// 「処刑した」を返す
	}
}
