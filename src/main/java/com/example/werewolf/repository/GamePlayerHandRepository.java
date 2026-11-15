package com.example.werewolf.repository;

import com.example.werewolf.entity.GamePlayerHand;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GamePlayerHandRepository extends JpaRepository<GamePlayerHand, Long> {
	boolean existsByGamePlayerIdAndCardIdAndStatus(Long gamePlayerId, Long cardId, String status);

	List<GamePlayerHand> findByGamePlayerIdAndStatus(Long gamePlayerId, String status);
}
