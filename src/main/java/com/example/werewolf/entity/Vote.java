package com.example.werewolf.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * docs/database-design.md の votes テーブルに対応するエンティティ。
 */
@Entity
@Table(name = "votes")
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "game_id", nullable = false)
    private Long gameId;

    @Column(name = "day_number", nullable = false)
    private Integer dayNumber;

    @Column(nullable = false)
    private Integer round = 1;

    @Column(name = "voter_game_player_id", nullable = false)
    private Long voterGamePlayerId;

    @Column(name = "target_game_player_id", nullable = false)
    private Long targetGamePlayerId;

    // JPAが利用するための引数なしコンストラクタ
    public Vote() {
    }

    public Vote(Long gameId, Integer dayNumber, Long voterGamePlayerId, Long targetGamePlayerId) {
        this.gameId = gameId;
        this.dayNumber = dayNumber;
        this.voterGamePlayerId = voterGamePlayerId;
        this.targetGamePlayerId = targetGamePlayerId;
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

    public Integer getRound() {
        return round;
    }

    public void setRound(Integer round) {
        this.round = round;
    }

    public Long getVoterGamePlayerId() {
        return voterGamePlayerId;
    }

    public void setVoterGamePlayerId(Long voterGamePlayerId) {
        this.voterGamePlayerId = voterGamePlayerId;
    }

    public Long getTargetGamePlayerId() {
        return targetGamePlayerId;
    }

    public void setTargetGamePlayerId(Long targetGamePlayerId) {
        this.targetGamePlayerId = targetGamePlayerId;
    }
}
