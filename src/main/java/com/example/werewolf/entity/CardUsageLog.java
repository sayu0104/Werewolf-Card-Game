package com.example.werewolf.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

/**
 * docs/database-design.md の card_usage_logs テーブルに対応するエンティティ。
 */
@Entity
@Table(name = "card_usage_logs")
public class CardUsageLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "game_id", nullable = false)
    private Long gameId;

    @Column(name = "day_number", nullable = false)
    private Integer dayNumber;

    @Column(nullable = false, length = 20)
    private String phase;

    @Column(name = "game_player_id", nullable = false)
    private Long gamePlayerId;

    @Column(name = "card_id", nullable = false)
    private Long cardId;

    @Column(name = "target_game_player_id")
    private Long targetGamePlayerId;

    @Column(name = "declared_result", length = 10)
    private String declaredResult;

    @Column(name = "used_at", nullable = false)
    private LocalDateTime usedAt;

    // JPAが利用するための引数なしコンストラクタ
    public CardUsageLog() {
    }

    public CardUsageLog(Long gameId, Integer dayNumber, String phase, Long gamePlayerId, Long cardId) {
        this.gameId = gameId;
        this.dayNumber = dayNumber;
        this.phase = phase;
        this.gamePlayerId = gamePlayerId;
        this.cardId = cardId;
        this.usedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public Integer getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(Integer dayNumber) {
        this.dayNumber = dayNumber;
    }

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
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

    public Long getTargetGamePlayerId() {
        return targetGamePlayerId;
    }

    public void setTargetGamePlayerId(Long targetGamePlayerId) {
        this.targetGamePlayerId = targetGamePlayerId;
    }

    public String getDeclaredResult() {
        return declaredResult;
    }

    public void setDeclaredResult(String declaredResult) {
        this.declaredResult = declaredResult;
    }

    public LocalDateTime getUsedAt() {
        return usedAt;
    }
}
