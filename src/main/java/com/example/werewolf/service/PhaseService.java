package com.example.werewolf.service;

import com.example.werewolf.entity.Game;
import com.example.werewolf.entity.Phase;
import com.example.werewolf.repository.GameRepository;
import org.springframework.stereotype.Service;

@Service // 以下のクラスが「処理担当の係」であることをSpringに知らせる印
public class PhaseService {

	private final GameRepository gameRepository;
	// GameRepository gameRepository …　型（種類・設計図の名前） 名前（この箱の呼び名）
	// ↑ この「GameRepository型の箱」を、この係の手元に持っておく宣言
	// （中身は、コンストラクタでDIで受け取った"倉庫番"が入る）

	//                          ↓ゲームの倉庫番（道具）
	public PhaseService(GameRepository gameRepository) {
		this.gameRepository = gameRepository;
	}

	public Game advancePhase(Game game) { // フェーズを進める
		Phase currentPhase = game.getCurrentPhase();
		// Phase … 型（朝・昼・投票・夜1・夜２）
		// currentPhase … 名前
		// = game.getCurrentPhase() … 右辺：game から今のフェーズを取り出す
		// （getterで読み取る）→ それを左の箱に入れる
		// カッコ有り＝動き、無し＝名前
		
		// ※箱に入れるのは、この後 switch で使い回すため（毎回長く書かずに済む）

		switch (currentPhase) {
		// 今のフェーズ（currentPhase）を見て、下の case のどれに当てはまるか振り分ける
		// switch … 「今の値によって、行き先を振り分ける」
		
		// case … 今のフェーズが…
			case MORNING -> game.setCurrentPhase(Phase.DAY);
			// MORNING（朝なら） → 昼（DAY）にする
			
			case DAY -> {
				if (game.getDayNumber() == 1) {
					// もし、初日（1日目）なら…
					
					game.setCurrentPhase(Phase.NIGHT1);
					// 夜1へ（投票を飛ばす）
					
				} else {
					// 2日目以降なら…
					
					game.setCurrentPhase(Phase.VOTE);
					// 投票へ（通常）
				}
			}
			case VOTE -> game.setCurrentPhase(Phase.NIGHT1);
			// VOTE(投票なら) → 夜1（NIGHT1）にする
			
			case NIGHT1 -> game.setCurrentPhase(Phase.NIGHT2);
			// NIGHT1(夜１なら) →	夜2（NIGHT2）にする
			
			case NIGHT2 -> {
				game.setCurrentPhase(Phase.MORNING);
				game.setDayNumber(game.getDayNumber() + 1);
				// NIGHT2（夜２なら） →	朝（MORNING）にして、dayNumberを+1
			}
		}

		return gameRepository.save(game);
		// フェーズを書き換えたGameを保存して返す
	}
}
