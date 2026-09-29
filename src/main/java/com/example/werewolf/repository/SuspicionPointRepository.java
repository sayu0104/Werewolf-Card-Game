package com.example.werewolf.repository;

import com.example.werewolf.entity.SuspicionPoint;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

// 疑心ポイントについて記録するための倉庫
// そのゲームの疑心ポイントの記録を、指定したプレイヤー別に取ってくるためのメソッド

public interface SuspicionPointRepository extends JpaRepository<SuspicionPoint, Long> {
	List<SuspicionPoint> findByGameIdAndGamePlayerId(Long gameId, Long gamePlayerId);
}
