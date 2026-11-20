package com.example.werewolf.repository;

import com.example.werewolf.entity.DeckCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeckCardRepository extends JpaRepository<DeckCard, Long> {
}
