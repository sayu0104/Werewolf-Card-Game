package com.example.werewolf.repository;

import com.example.werewolf.entity.CardUsageLog;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

// カードの使用記録を保存する倉庫
// CardUsageLog（カード使用記録）を、Long型のidで出し入れする倉庫。基本の保存・取得・削除が自動で付く
// そのゲームIDの、カード使用記録を全部リストで取ってくる

public interface CardUsageLogRepository extends JpaRepository<CardUsageLog, Long> {
	List<CardUsageLog> findByGameId(Long gameId);
}
