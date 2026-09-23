package com.example.werewolf.repository;

import com.example.werewolf.entity.NightAction;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

// 夜に行動する役職の倉庫（全件取得、削除、保存ができる）
// findByGameIdAndDayNumber … 夜に行動する役職の記録を取ってくる機能
// findByGameIdAndActionType … 夜に行動する役職の記録を、ゲームIDと行動の種類で取ってくる機能
public interface NightActionRepository extends JpaRepository<NightAction, Long> {
	List<NightAction> findByGameIdAndDayNumber(Long gameId, Integer dayNumber);
	List<NightAction> findByGameIdAndActionType(Long gameId, String actionType);
}
