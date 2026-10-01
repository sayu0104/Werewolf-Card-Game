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

	// 扇動メソッド（相手に疑心を大きく向けるが、自分にも疑いが入る）
	// 相手に+x、自分にはその割合分（self_rate）の +y を記録する（changeSuspicionを2回使う）
	public void incite(Game game, GamePlayer actor, GamePlayer target, int x, double selfRate) {
		if (game == null || actor == null || target == null) {
			throw new IllegalArgumentException("試合・使用者・対象は必須");
		}
		int y = (int) Math.round(x * selfRate);
		// int 型の ｙ の箱に、整数にして数値を入れる
		// 相手の疑心ポイント（x）が ×selfRate（扇動なら0.3） で、30%が入る
		// 内側から計算する
		
		changeSuspicion(game, target, x);
		changeSuspicion(game, actor, y);
		// xの分とyの分を、それぞれ記録する
	}
}
