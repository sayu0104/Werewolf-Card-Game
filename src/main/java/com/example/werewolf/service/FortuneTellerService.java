package com.example.werewolf.service;

import com.example.werewolf.entity.Game;
import com.example.werewolf.entity.GamePlayer;
import com.example.werewolf.entity.NightAction;
import com.example.werewolf.entity.Role;
import com.example.werewolf.repository.GamePlayerRepository;
import com.example.werewolf.repository.NightActionRepository;
import com.example.werewolf.repository.RoleRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
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

		Set<Long> excludedTargetIds = alreadyDivinedTargetIds(game.getId());
		// その試合のすでに占ったことのある人を集める

		// 2.占い師を探す（＋3へ）
		List<NightAction> nightActions = new ArrayList<>();
		for (GamePlayer actor : alivePlayers) {
			Role role = roleRepository.findById(actor.getRoleId())
					.orElseThrow(() -> new IllegalArgumentException("役職が見つからない: " + actor.getRoleId()));
			if (!ROLE_NAME.equals(role.getName())) {
				continue;
			}

			// 3.占い先を決めて保存
			GamePlayer target = chooseTarget(alivePlayers, actor, excludedTargetIds);
			if (target == null) { // 占い先がないなら
				continue; // 占いはしない
			}
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
	// 1.占い先と占い師が同じにならないように、また同じ人を2回占わないように仕分ける
	private GamePlayer chooseTarget(List<GamePlayer> alivePlayers, GamePlayer actor, Set<Long> excludedTargetIds) {
		List<GamePlayer> candidates = new ArrayList<>();
		for (GamePlayer player : alivePlayers) {
			if (!player.getId().equals(actor.getId()) && !excludedTargetIds.contains(player.getId())) {
				// もし 占い先が占い師本人ではない、かつ、まだ占っていない人なら
				// 今までの占い先の中に、このプレイヤーが含まれているか？ true
				// ! で裏返すので、まだ占ってない人（含まれてない）が true ＝候補に入れる／もう占った人は false＝外す
				// .contains … （）の中身が、その箱に含まれているか、trueかfalseで返す
				
				candidates.add(player);
			}
		}

		//  2.占い候補が誰もいなかったらnull
		if (candidates.isEmpty()) {
			return null;
		}

		// 3.候補からランダムに占い先を決める（将来は頭脳的に）
		int index = new Random().nextInt(candidates.size());
		return candidates.get(index);
	}

	// その試合の占いの記録を全部集めて、占った相手のIDだけ Set に入れて返す（重複しない）
	private Set<Long> alreadyDivinedTargetIds(Long gameId) {
		Set<Long> targetIds = new HashSet<>();
		// 占い先のIDを入れるための箱を用意する
		// Set … 同じ数字が入らない箱の型
		// HashSet … Setの形式で箱の中身を用意する
		
		for (NightAction action : nightActionRepository.findByGameIdAndActionType(gameId, ACTION_TYPE)) {
			targetIds.add(action.getTargetGamePlayerId());
			// 占いの能力を使われた人を1人ずつ見て
			// 占い先の人のIDを追加していく
		}
		return targetIds;
	}
}
