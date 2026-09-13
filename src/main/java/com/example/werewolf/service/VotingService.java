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
	}

	public List<Vote> vote(Game game) {
		List<GamePlayer> alivePlayers = new ArrayList<>();
		for (GamePlayer gamePlayer : gamePlayerRepository.findByGameId(game.getId())) {
			if (gamePlayer.getIsAlive()) {
				alivePlayers.add(gamePlayer);
			}
		}

		List<Vote> votes = new ArrayList<>();
		for (GamePlayer voter : alivePlayers) {
			GamePlayer target = chooseTarget(alivePlayers, voter);
			Vote vote = new Vote(game.getId(), game.getDayNumber(), voter.getId(), target.getId());
			votes.add(voteRepository.save(vote));
		}

		return votes;
	}

	private GamePlayer chooseTarget(List<GamePlayer> alivePlayers, GamePlayer voter) {
		List<GamePlayer> candidates = new ArrayList<>();
		for (GamePlayer player : alivePlayers) {
			if (!player.getId().equals(voter.getId())) {
				candidates.add(player);
			}
		}
		if (candidates.isEmpty()) {
			candidates = alivePlayers;
		}

		int index = new Random().nextInt(candidates.size());
		return candidates.get(index);
	}
}
