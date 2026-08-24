package com.example.werewolf.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * docs/database-design.md の night_actions テーブルに対応するエンティティ。
 */
@Entity
@Table(name = "night_actions")
public class NightAction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "game_id", nullable = false)
    private Long gameId;

    @Column(name = "day_number", nullable = false)
    private Integer dayNumber;

    @Column(name = "actor_game_player_id", nullable = false)
    private Long actorGamePlayerId;

    @Column(name = "action_type", nullable = false, length = 20)
    private String actionType;

    @Column(name = "target_game_player_id", nullable = false)
    private Long targetGamePlayerId;

    @Column(name = "is_successful")
    private Boolean isSuccessful;

    // JPAが利用するための引数なしコンストラクタ
    public NightAction() {
    }

    public NightAction(Long gameId, Integer dayNumber, Long actorGamePlayerId, String actionType, Long targetGamePlayerId) {
        this.gameId = gameId;
        this.dayNumber = dayNumber;
        this.actorGamePlayerId = actorGamePlayerId;
        this.actionType = actionType;
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

    public Long getActorGamePlayerId() {
        return actorGamePlayerId;
    }

    public void setActorGamePlayerId(Long actorGamePlayerId) {
        this.actorGamePlayerId = actorGamePlayerId;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public Long getTargetGamePlayerId() {
        return targetGamePlayerId;
    }

    public void setTargetGamePlayerId(Long targetGamePlayerId) {
        this.targetGamePlayerId = targetGamePlayerId;
    }

    public Boolean getIsSuccessful() {
        return isSuccessful;
    }

    public void setIsSuccessful(Boolean isSuccessful) {
        this.isSuccessful = isSuccessful;
    }
}
