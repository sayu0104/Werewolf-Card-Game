package com.example.werewolf.repository;

import com.example.werewolf.entity.DeckCard;
import org.springframework.data.jpa.repository.JpaRepository;

// DeckCardRepository … 山札構成（deck_cards）のデータを管理する係

public interface DeckCardRepository extends JpaRepository<DeckCard, Long> {
}
