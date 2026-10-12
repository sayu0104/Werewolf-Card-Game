package com.example.werewolf.repository;

import com.example.werewolf.entity.NightAction;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

// 夜に行動する役職の倉庫（全件取得、削除、保存ができる）
// findByGameIdAndDayNumber … 夜に行動する役職の記録を取ってくる機能
// findByGameIdAndActionType … 夜に行動する役職の記録を、ゲームIDと行動の種類で取ってくる機能
// findFirstByGameIdAndActorGamePlayerIdAndActionTypeAndDayNumber …
// … 条件で探して、最初の1件を取る。無いかもしれない(ゲームID、カード使用者のID、行動の種類、そのゲーム内の日付)

public interface NightActionRepository extends JpaRepository<NightAction, Long> {
	List<NightAction> findByGameIdAndDayNumber(Long gameId, Integer dayNumber);
	List<NightAction> findByGameIdAndActionType(Long gameId, String actionType);
	Optional<NightAction> findFirstByGameIdAndActorGamePlayerIdAndActionTypeAndDayNumber(Long gameId,
			Long actorGamePlayerId, String actionType, Integer dayNumber);
}
