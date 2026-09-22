package com.example.werewolf.service;

import com.example.werewolf.entity.Game;
import com.example.werewolf.entity.GamePlayer;
import com.example.werewolf.entity.NightAction;
import com.example.werewolf.repository.GamePlayerRepository;
import com.example.werewolf.repository.GameRepository;
import com.example.werewolf.repository.NightActionRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class NightResultService { // 夜2の結果処理をする係(役職の能力の交差)

	private static final String ATTACK_ACTION_TYPE = "襲撃";
	private static final String GUARD_ACTION_TYPE = "護衛";

	private final NightActionRepository nightActionRepository;
	private final GamePlayerRepository gamePlayerRepository;
	private final GameRepository gameRepository;

	public NightResultService(NightActionRepository nightActionRepository, GamePlayerRepository gamePlayerRepository,
			GameRepository gameRepository) {
		this.nightActionRepository = nightActionRepository;
		this.gamePlayerRepository = gamePlayerRepository;
		this.gameRepository = gameRepository;
	}

	public void resolve(Game game) {
		List<NightAction> nightActions = nightActionRepository.findByGameIdAndDayNumber(game.getId(), game.getDayNumber());
		// 倉庫から、その日の夜の行動の記録を全部集める

		List<NightAction> attacks = new ArrayList<>();
		List<NightAction> guards = new ArrayList<>();
		// 襲撃の記録・護衛の記録を入れる、空の箱を2つ作る
		
		for (NightAction nightAction : nightActions) {
			// 記録を1つずつ見て
			
			if (ATTACK_ACTION_TYPE.equals(nightAction.getActionType())) {
				attacks.add(nightAction); // 襲撃なら襲撃の箱に
				
			} else if (GUARD_ACTION_TYPE.equals(nightAction.getActionType())) {
				guards.add(nightAction); // 護衛なら護衛の箱に
				// それ以外（占いなど）はスルー
			}
		}

		if (attacks.isEmpty()) {
			markResolved(game); 
			return;
			// 襲撃が無いなら、夜2の処理を終えた日付を記入して終わる
		}

		for (NightAction attack : attacks) {
			NightAction matchedGuard = null;
			// 「襲撃先を守ってる護衛」を入れる箱。最初は null（まだ見つかってない）
			
			for (NightAction guard : guards) {
				// 護衛を1つずつ見て
				
				if (guard.getTargetGamePlayerId().equals(attack.getTargetGamePlayerId())) {
					// もし護衛先と襲撃先が同じなら（＝その護衛が、この襲撃を防いでる）
					
					matchedGuard = guard;
					// その護衛を箱に入れる
					
					break;
					// 見つかったので、探すのをやめる
				}
			}

			if (matchedGuard != null) {
				// 護衛が見つかった（守られてる）なら
				
				attack.setIsSuccessful(false); // 襲撃は失敗
				matchedGuard.setIsSuccessful(true); // 護衛は成功
				nightActionRepository.save(matchedGuard);// 護衛の成否を保存
				
			} else {
				// 護衛が無い（守られてない）なら
				attack.setIsSuccessful(true); // 襲撃は成功
				GamePlayer target = gamePlayerRepository.findById(attack.getTargetGamePlayerId())
						.orElseThrow(() -> new IllegalArgumentException("プレイヤーが見つからない: " + attack.getTargetGamePlayerId()));
				// 襲撃先のプレイヤーを倉庫から探す（いなければエラー）
				
				target.setIsAlive(false); // その人を死亡に
				gamePlayerRepository.save(target); // プレイヤー倉庫に保存
			}
			nightActionRepository.save(attack);
			// 襲撃の成否（成功/失敗）を、役職の倉庫に保存する（護衛あり/なし どちらでも）
		}

		for (NightAction guard : guards) {
			// 護衛の記録を1つずつ見て
			
			if (guard.getIsSuccessful() == null) {
				// まだ成否が決まってない護衛（＝護衛先が襲われず、出番が無かった）なら
				
				guard.setIsSuccessful(false); // 失敗（成功してない）として記録する
				nightActionRepository.save(guard);
				// この護衛の成否（成功/失敗）を、夜の行動の記録の倉庫に保存する
			}
		}

		markResolved(game);
		// 夜2の処理が終わったことを保存する
	}

	// 夜2の処理が終わったことを保存する（今の日付を取ってきて、夜2の処理を終えた日付として記録する）
	private void markResolved(Game game) {
		game.setNightResolvedDay(game.getDayNumber());
		gameRepository.save(game);
	}
}
