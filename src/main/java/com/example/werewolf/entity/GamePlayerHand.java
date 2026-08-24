package com.example.werewolf.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * docs/database-design.md の game_player_hands テーブルに対応するエンティティ。
 */
@Entity
@Table(name = "game_player_hands")
public class GamePlayerHand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "game_player_id", nullable = false)
    private Long gamePlayerId;

    @Column(name = "card_id", nullable = false)
    private Long cardId;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(name = "acquired_at_day", nullable = false)
    private Integer acquiredAtDay;

    // JPAが利用するための引数なしコンストラクタ
    public GamePlayerHand() {
    }

    public GamePlayerHand(Long gamePlayerId, Long cardId, String status, Integer acquiredAtDay) {
        this.gamePlayerId = gamePlayerId;
        this.cardId = cardId;
        this.status = status;
        this.acquiredAtDay = acquiredAtDay;
    }

    public Long getId() {
        return id;
    }

    public Long getGamePlayerId() {
        return gamePlayerId;
    }

    public void setGamePlayerId(Long gamePlayerId) {
        this.gamePlayerId = gamePlayerId;
    }

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getAcquiredAtDay() {
        return acquiredAtDay;
    }

    public void setAcquiredAtDay(Integer acquiredAtDay) {
        this.acquiredAtDay = acquiredAtDay;
    }
}
