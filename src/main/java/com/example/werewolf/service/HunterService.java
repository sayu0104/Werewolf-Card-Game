package com.example.werewolf.service;

import com.example.werewolf.entity.Game;
import com.example.werewolf.entity.GamePlayer;
import com.example.werewolf.entity.NightAction;
import com.example.werewolf.entity.Role;
import com.example.werewolf.repository.GamePlayerRepository;
import com.example.werewolf.repository.NightActionRepository;
import com.example.werewolf.repository.RoleRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.springframework.stereotype.Service;

@Service
public class HunterService { // 狩人の役職について

	private static final String ROLE_NAME = "狩人";
	private static final String ACTION_TYPE = "護衛";

	private final GamePlayerRepository gamePlayerRepository;
	private final RoleRepository roleRepository;
	private final NightActionRepository nightActionRepository;

	public HunterService(GamePlayerRepository gamePlayerRepository, RoleRepository roleRepository,
			NightActionRepository nightActionRepository) {
		this.gamePlayerRepository = gamePlayerRepository;
		this.roleRepository = roleRepository;
		this.nightActionRepository = nightActionRepository;
	}

	// 試合(game)を受け取って、夜の行動を記録し、その記録（NightActionのリスト）を返す係
	// 1.生存者を集める
	public List<NightAction> act(Game game) {
		List<GamePlayer> alivePlayers = new ArrayList<>();
		for (GamePlayer gamePlayer : gamePlayerRepository.findByGameId(game.getId())) {
			if (gamePlayer.getIsAlive()) {
				alivePlayers.add(gamePlayer);
			}
		}

		// 2.狩人を探す（＋3へ）
		List<NightAction> nightActions = new ArrayList<>();
		for (GamePlayer actor : alivePlayers) {
			Role role = roleRepository.findById(actor.getRoleId())
					.orElseThrow(() -> new IllegalArgumentException("役職が見つからない: " + actor.getRoleId()));
			if (!ROLE_NAME.equals(role.getName())) {
				continue;
			}

			// 3.護衛先を決めて保存
			GamePlayer target = chooseTarget(alivePlayers, actor);
			NightAction nightAction = new NightAction(game.getId(), game.getDayNumber(), actor.getId(), ACTION_TYPE,
					target.getId());
			nightActions.add(nightActionRepository.save(nightAction));
		}

		return nightActions;
	}

	// 役職の能力発揮先を選ぶ
	// 1.護衛先と狩人が同じにならないよう仕分ける
	private GamePlayer chooseTarget(List<GamePlayer> alivePlayers, GamePlayer actor) {
		List<GamePlayer> candidates = new ArrayList<>();
		for (GamePlayer player : alivePlayers) {
			if (!player.getId().equals(actor.getId())) {
				candidates.add(player);
			}
		}
		
		// 2.護衛先が誰もいなかったら、生存者を候補にする（本人含みうるが不具合なし）
		if (candidates.isEmpty()) {
			candidates = alivePlayers;
		}

		// 3.候補からランダムに護衛先を決める（将来は頭脳的に）
		int index = new Random().nextInt(candidates.size());
		return candidates.get(index);
	}
}
