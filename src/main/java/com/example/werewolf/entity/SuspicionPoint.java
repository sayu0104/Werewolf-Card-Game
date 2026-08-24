package com.example.werewolf.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

/**
 * docs/database-design.md の suspicion_points テーブルに対応するエンティティ。
 */
@Entity
@Table(name = "suspicion_points")
public class SuspicionPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "game_id", nullable = false)
    private Long gameId;

    @Column(name = "game_player_id", nullable = false)
    private Long gamePlayerId;

    @Column(name = "day_number", nullable = false)
    private Integer dayNumber;

    @Column(name = "points_delta", nullable = false)
    private Integer pointsDelta;

    @Column(length = 50)
    private String reason;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    // JPAが利用するための引数なしコンストラクタ
    public SuspicionPoint() {
    }

    public SuspicionPoint(Long gameId, Long gamePlayerId, Integer dayNumber, Integer pointsDelta) {
        this.gameId = gameId;
        this.gamePlayerId = gamePlayerId;
        this.dayNumber = dayNumber;
        this.pointsDelta = pointsDelta;
        this.createdAt = LocalDateTime.now();
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

    public Long getGamePlayerId() {
        return gamePlayerId;
    }

    public void setGamePlayerId(Long gamePlayerId) {
        this.gamePlayerId = gamePlayerId;
    }

    public Integer getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(Integer dayNumber) {
        this.dayNumber = dayNumber;
    }

    public Integer getPointsDelta() {
        return pointsDelta;
    }

    public void setPointsDelta(Integer pointsDelta) {
        this.pointsDelta = pointsDelta;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
