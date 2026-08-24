package com.example.werewolf.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * docs/database-design.md の suspicion_reasons テーブルに対応するエンティティ。
 */
@Entity
@Table(name = "suspicion_reasons")
public class SuspicionReason {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "game_id", nullable = false)
    private Long gameId;

    @Column(name = "holder_game_player_id", nullable = false)
    private Long holderGamePlayerId;

    @Column(name = "target_game_player_id", nullable = false)
    private Long targetGamePlayerId;

    @Column(name = "reason_type", nullable = false, length = 20)
    private String reasonType;

    @Column(name = "reason_text", nullable = false, columnDefinition = "TEXT")
    private String reasonText;

    @Column(name = "day_number", nullable = false)
    private Integer dayNumber;

    // JPAが利用するための引数なしコンストラクタ
    public SuspicionReason() {
    }

    public SuspicionReason(Long gameId, Long holderGamePlayerId, Long targetGamePlayerId,
            String reasonType, String reasonText, Integer dayNumber) {
        this.gameId = gameId;
        this.holderGamePlayerId = holderGamePlayerId;
        this.targetGamePlayerId = targetGamePlayerId;
        this.reasonType = reasonType;
        this.reasonText = reasonText;
        this.dayNumber = dayNumber;
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

    public Long getHolderGamePlayerId() {
        return holderGamePlayerId;
    }

    public void setHolderGamePlayerId(Long holderGamePlayerId) {
        this.holderGamePlayerId = holderGamePlayerId;
    }

    public Long getTargetGamePlayerId() {
        return targetGamePlayerId;
    }

    public void setTargetGamePlayerId(Long targetGamePlayerId) {
        this.targetGamePlayerId = targetGamePlayerId;
    }

    public String getReasonType() {
        return reasonType;
    }

    public void setReasonType(String reasonType) {
        this.reasonType = reasonType;
    }

    public String getReasonText() {
        return reasonText;
    }

    public void setReasonText(String reasonText) {
        this.reasonText = reasonText;
    }

    public Integer getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(Integer dayNumber) {
        this.dayNumber = dayNumber;
    }
}
