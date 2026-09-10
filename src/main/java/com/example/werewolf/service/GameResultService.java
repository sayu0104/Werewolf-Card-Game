package com.example.werewolf.service;

import com.example.werewolf.entity.GamePlayer;
import com.example.werewolf.entity.Role;
import com.example.werewolf.repository.GamePlayerRepository;
import com.example.werewolf.repository.RoleRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class GameResultService { // ゲーム結果（勝利判定）

	private static final String VILLAGER_FACTION = "村人陣営";
	private static final String WEREWOLF_ROLE_NAME = "人狼";
	// 村人陣営は、VILLAGER_FACTION（村人陣営）と呼ぶことにする
	// 人狼は、WEREWOLF_ROLE_NAME（人狼役職名）と呼ぶことにする

	private final GamePlayerRepository gamePlayerRepository;
	private final RoleRepository roleRepository;

	public GameResultService(GamePlayerRepository gamePlayerRepository, RoleRepository roleRepository) {
		this.gamePlayerRepository = gamePlayerRepository;
		this.roleRepository = roleRepository;
	}

	public GameResult judge(Long gameId) {
		// ゲームID を受け取って、勝敗を判定して、GameResult（勝敗結果）を返す、judge という処理
		
		List<GamePlayer> players = gamePlayerRepository.findByGameId(gameId);
		// ゲームプレイヤーの倉庫から、ゲームIDを使って（そのゲームの）プレイヤーたちを取ってきて、players という箱に入れる

		long werewolfCount = 0;
		long villagerCount = 0;
		// 人狼の数と村人陣営の数を数える箱を作って、0を入れる

		for (GamePlayer player : players) {
			// players から、player という箱に、プレイヤー（GamePlayer）を1つずつ取り出して入れる
			// ゲームプレイヤー情報全て
			
			if (!player.getIsAlive()) {
				// もしこの人が生きていない（死んでいる）なら、continue（飛ばして次の人へ)
				// ! は「〜ではない」
				
				// if は「カッコの中が true なら、中を実行する」装置
				// ifはtrueじゃなきゃ実行しないから、! （意味を反転）を付けて、
				// 死んでる人を飛ばして、生きてる人だけ数えさせるようにした
				
				continue;
				// この人は飛ばして次へ
			}

			Role role = roleRepository.findById(player.getRoleId()).orElseThrow();
			// プレイヤーの役職IDを元に、役職の倉庫から探してきて、その役職（Role）を role に入れる。見つからなければエラー

			if (WEREWOLF_ROLE_NAME.equals(role.getName())) {
				// もしWEREWOLF_ROLE_NAME（人狼役職の名前）が、getした役職の名前と同じなら…
				
				werewolfCount++;
				// 人狼を1人数える
				
			} else if (VILLAGER_FACTION.equals(role.getFaction())) {
				// そうじゃなくて、もしVILLAGER_FACTION（村人陣営）が、getした役職の陣営（派閥）と同じなら…
				
				villagerCount++;
				// 村人陣営を1人数える
				
				// ※1の人狼でも2の村人陣営でもない＝狂人は、else が無いのでどちらも数えずスルーされる
			}
		}

		if (werewolfCount == 0) {
			return GameResult.VILLAGER_WIN;
			// もし人狼が0人なら、村人陣営の勝ち（VILLAGER_WIN）を返す
		}

		if (werewolfCount >= villagerCount) {
			return GameResult.WEREWOLF_WIN;
			// もし人狼の数が、村人陣営の数 「以上（多い、または同じ）」 なら、人狼陣営の勝ちを返す
			// 理由は、人狼と村人陣営の人数が同じだと、村人陣営は勝てないから
		}

		return GameResult.CONTINUE;
		// まだ決着がつかなかったら、CONTINUE（続行）を返す
	}
}
