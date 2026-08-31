package com.example.werewolf.repository;

import com.example.werewolf.entity.GamePlayer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GamePlayerRepository extends JpaRepository<GamePlayer, Long> {
}
