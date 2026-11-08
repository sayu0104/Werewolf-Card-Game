package com.example.werewolf.service;

import com.example.werewolf.entity.Card;
import com.example.werewolf.entity.CardUsageLog;
import com.example.werewolf.entity.Game;
import com.example.werewolf.entity.GamePlayer;
import com.example.werewolf.entity.NightAction;
import com.example.werewolf.entity.Role;
import com.example.werewolf.repository.CardRepository;
import com.example.werewolf.repository.CardUsageLogRepository;
import com.example.werewolf.repository.GamePlayerRepository;
import com.example.werewolf.repository.NightActionRepository;
import com.example.werewolf.repository.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class ReportService { // 報告（占い結果を card_usage_logs に記録する）専門の係

	private final CardRepository cardRepository;
	private final CardUsageLogRepository cardUsageLogRepository;
	private final NightActionRepository nightActionRepository;
	private final RoleRepository roleRepository;
	private final GamePlayerRepository gamePlayerRepository;

	private static final String VILLAGER_FACTION = "村人陣営";
	private static final String FORTUNE_TELLER = "占い師";
	private static final String REPORT_CARD_NAME = "結果報告";

	public ReportService(CardRepository cardRepository, CardUsageLogRepository cardUsageLogRepository,
			NightActionRepository nightActionRepository, RoleRepository roleRepository,
			GamePlayerRepository gamePlayerRepository) {
		this.cardRepository = cardRepository;
		this.cardUsageLogRepository = cardUsageLogRepository;
		this.nightActionRepository = nightActionRepository;
		this.roleRepository = roleRepository;
		this.gamePlayerRepository = gamePlayerRepository;
	}

	// 本物の占い師の1日分の報告を１件作る
	public boolean recordVillageReport(Game game, GamePlayer player, int day) {
		if (game == null || player == null) {
			throw new IllegalArgumentException("試合と使用者は必須");
			// もし、試合がない または プレイヤーが空 なら、エラー
		}
		if (!isVillageFortuneTeller(player)) {
			return false;
			// 村側の占い師（村人陣営かつ占い師）ではないなら、false で中止
		}
		NightAction divination = nightActionRepository
				.findFirstByGameIdAndActorGamePlayerIdAndActionTypeAndDayNumber(game.getId(), player.getId(),
						"占い", day)
				.orElse(null);
		// このゲームで・この使用者が・『占い』という行動を・この日付にした記録を、1件探してくる。無ければ null。

		if (divination == null || divination.getIsWerewolf() == null) {
			return false;
			// 記録が無い、または 占い結果（白黒）がまだ入ってないなら、false で中止
		}
		if (cardUsageLogRepository.existsByGameIdAndGamePlayerIdAndDayNumber(game.getId(), player.getId(), day)) {
			return true;
			// もし、この試合の、この人の、この日の報告がすでにあるなら、
			// true（報告がすでにある）を返す
		}
		Card reportCard = cardRepository.findFirstByName(REPORT_CARD_NAME)
				.orElseThrow(() -> new IllegalStateException("結果報告カードが存在しない"));
		// カードを使わず自動で報告を作るので、結果報告カードを名前で探してidを得る（無ければエラー）
		
		CardUsageLog reportLog = new CardUsageLog(game.getId(), day, "昼", player.getId(), reportCard.getId());
		reportLog.setTargetGamePlayerId(divination.getTargetGamePlayerId());
		reportLog.setDeclaredResult(divination.getIsWerewolf() ? "黒" : "白");
		cardUsageLogRepository.save(reportLog);
		return true;
		// 報告の記録を新しく作る（ゲーム・日付・昼・使用者・カードを入れる）
		// 占った相手を対象にセット
		// 占い結果が人狼なら"黒"、違えば"白"をセット
		// 記録を保存して、true（報告できた）を返す
	}

	// 後から名乗った占い師に、1日目から今日まで全部の報告をまとめて作る
	public void backfillPastReports(Game game, GamePlayer player) {
		if (game == null || player == null) {
			throw new IllegalArgumentException("試合と使用者は必須");
			// もし、この試合が空、または、プレイヤーが空なら、エラー
		}
		for (int day = 1; day <= game.getDayNumber(); day++) {
			recordVillageReport(game, player, day);
			// 今の試合まで何日あるかを1日目から数える
			// その日数分、1日ぶんの報告を作る（既にある日は中でスキップ）
		}
	}

	public void recordAutoReports(Game game) {
		if (game == null) {
			throw new IllegalArgumentException("試合は必須");
		}
		for (GamePlayer player : gamePlayerRepository.findByGameId(game.getId())) {
			if (player.getClaimedRoleId() != null) {
				backfillPastReports(game, player);
			}
		}
	}

	// 村人陣営の占い師かを true か false で判定する
	private boolean isVillageFortuneTeller(GamePlayer player) {
		Role role = roleRepository.findById(player.getRoleId())
				.orElseThrow(() -> new IllegalArgumentException("使用者の役職が存在しない"));
		// そのプレイヤーの役職IDで探して、その役職がなければエラー
		
		return VILLAGER_FACTION.equals(role.getFaction()) && FORTUNE_TELLER.equals(role.getName());
		// 村人陣営 かつ 占い師 なら true の結果を返す
	}
}
