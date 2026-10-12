package com.example.werewolf.repository;

import com.example.werewolf.entity.CardUsageLog;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardUsageLogRepository extends JpaRepository<CardUsageLog, Long> {
	List<CardUsageLog> findByGameId(Long gameId);
}
