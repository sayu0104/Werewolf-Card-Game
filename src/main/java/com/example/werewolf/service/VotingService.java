package com.example.werewolf.service;

import com.example.werewolf.entity.Game;
import com.example.werewolf.entity.GamePlayer;
import com.example.werewolf.entity.Vote;
import com.example.werewolf.repository.GamePlayerRepository;
import com.example.werewolf.repository.VoteRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.springframework.stereotype.Service;

@Service
public class VotingService { // 投票ロジック（投票先の選び方は今はランダム。後で差し替え予定）

	private final GamePlayerRepository gamePlayerRepository;
	private final VoteRepository voteRepository;

	public VotingService(GamePlayerRepository gamePlayerRepository, VoteRepository voteRepository) {
		this.gamePlayerRepository = gamePlayerRepository;
		this.voteRepository = voteRepository;
		// 必要な係（倉庫）を受け取って、VotingServiceに入れる
	}

	public List<Vote> vote(Game game) {
		// 1つのゲームを渡して投票メソッドを実行
		
		List<GamePlayer> alivePlayers = new ArrayList<>();
		// 生存しているプレイヤーだけが入る、新しいリストを作る
		
		for (GamePlayer gamePlayer : gamePlayerRepository.findByGameId(game.getId())) {
			// その試合の全プレイヤーを、1人ずつ、gamePlayer（1人用の箱）に取り出して
			
			if (gamePlayer.getIsAlive()) {
				// もし生きていたら
				
				alivePlayers.add(gamePlayer);
				// alivePlayers に足す（死んでる人は足さない＝スルー）
			}
		}

		List<Vote> votes = new ArrayList<>();
		// 全員の投票結果をまとめる新しいリストを作る
		
		for (GamePlayer voter : alivePlayers) {
			// 生存プレイヤーを1人ずつ、voter（投票する人）に取り出す
			
			GamePlayer target = chooseTarget(alivePlayers, voter);
			// 生存プレイヤーと投票する側を使って、ターゲットを選ばせるメソッドで、ターゲットの箱に入れる
			
			Vote vote = new Vote(game.getId(), game.getDayNumber(), voter.getId(), target.getId());
			// 必要なもの（ゲームID・日付・投票する人ID・される人ID）を使って、新たに投票をする。その状態を箱に入れる
			
			votes.add(voteRepository.save(vote));
			// その1人分の投票を、倉庫にセーブして、まとめるリストに追加
		}
		
		return votes;
		// 生存プレイヤー全員分繰り返す
	}

	private GamePlayer chooseTarget(List<GamePlayer> alivePlayers, GamePlayer voter) {
		// 投票先を選ぶ（今はランダム。将来は「賢い判断」に差し替える）
		// 自分以外を候補にして、ランダムに1人選んで返す
		
		List<GamePlayer> candidates = new ArrayList<>();
		for (GamePlayer player : alivePlayers) {
			if (!player.getId().equals(voter.getId())) {
				// もし player が voter と別人なら（＝自分自身でなければ）
				
				candidates.add(player);
				// 候補に足す（自分は候補に入れない）
			}
		}
		if (candidates.isEmpty()) { // もし候補が空なら（自分1人だけの保険）
			candidates = alivePlayers; // しかたなく、自分含む全員を候補にする
		}

		int index = new Random().nextInt(candidates.size()); // ランダムな番号を1個出す（0〜候補数-1）
		return candidates.get(index); // その番号の候補を返す
	}
}
