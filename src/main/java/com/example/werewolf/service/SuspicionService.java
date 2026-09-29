package com.example.werewolf.service;

import com.example.werewolf.entity.Game;
import com.example.werewolf.entity.GamePlayer;
import com.example.werewolf.entity.SuspicionPoint;
import com.example.werewolf.repository.SuspicionPointRepository;
import org.springframework.stereotype.Service;

@Service
public class SuspicionService { // 疑心ポイントの変化を処理するクラス

	private final SuspicionPointRepository suspicionPointRepository;

	public SuspicionService(SuspicionPointRepository suspicionPointRepository) {
		this.suspicionPointRepository = suspicionPointRepository;
	}

	// 疑心ポイントの変化を1件記録するメソッド（足し引き＝プラスで上げ、マイナスで下げ）
	public SuspicionPoint changeSuspicion(Game game, GamePlayer target, int delta) {
		if (game == null || target == null) {
			throw new IllegalArgumentException("試合・対象は必須");
		}
		SuspicionPoint suspicionPoint = new SuspicionPoint(game.getId(), target.getId(), game.getDayNumber(), delta);
		return suspicionPointRepository.save(suspicionPoint);
		// どのプレイヤーが、何日目に、何ポイント動いた（増減した）かを1件記録する
	}
}
