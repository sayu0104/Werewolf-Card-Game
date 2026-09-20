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
public class FortuneTellerService { // 占い師の役職について

	private static final String ROLE_NAME = "占い師";
	private static final String ACTION_TYPE = "占い";
	private static final String WEREWOLF_ROLE_NAME = "人狼";

	private final GamePlayerRepository gamePlayerRepository;
	private final RoleRepository roleRepository;
	private final NightActionRepository nightActionRepository;

	public FortuneTellerService(GamePlayerRepository gamePlayerRepository, RoleRepository roleRepository,
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

		// 2.占い師を探す（＋3へ）
		List<NightAction> nightActions = new ArrayList<>();
		for (GamePlayer actor : alivePlayers) {
			Role role = roleRepository.findById(actor.getRoleId())
					.orElseThrow(() -> new IllegalArgumentException("役職が見つからない: " + actor.getRoleId()));
			if (!ROLE_NAME.equals(role.getName())) {
				continue;
			}

			// 3.占い先を決めて保存
			GamePlayer target = chooseTarget(alivePlayers, actor);
			NightAction nightAction = new NightAction(game.getId(), game.getDayNumber(), actor.getId(), ACTION_TYPE,
					target.getId());
			Role targetRole = roleRepository.findById(target.getRoleId())
					.orElseThrow(() -> new IllegalArgumentException("役職が見つからない: " + target.getRoleId()));
			nightAction.setIsWerewolf(WEREWOLF_ROLE_NAME.equals(targetRole.getName()));
			// 占った人の役職名が「人狼」なら、trueという結果を出す
			
			nightActions.add(nightActionRepository.save(nightAction));
		}

		return nightActions;
	}

	// 役職の能力発揮先を選ぶ
	// 1.占い先と占い師が同じにならないよう仕分ける
	private GamePlayer chooseTarget(List<GamePlayer> alivePlayers, GamePlayer actor) {
		List<GamePlayer> candidates = new ArrayList<>();
		for (GamePlayer player : alivePlayers) {
			if (!player.getId().equals(actor.getId())) {
				candidates.add(player);
			}
		}
		
		// 2.占い先が誰もいなかったら、生存者を候補にする（本人含みうるが不具合なし）
		if (candidates.isEmpty()) {
			candidates = alivePlayers;
		}

		// 3.候補からランダムに占い先を決める（将来は頭脳的に）
		int index = new Random().nextInt(candidates.size());
		return candidates.get(index);
	}
}
