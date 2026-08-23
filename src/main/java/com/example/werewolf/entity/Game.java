package com.example.werewolf.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

/**
 * docs/database-design.md の games テーブルに対応するエンティティ。
 */
@Entity
@Table(name = "games")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(name = "current_phase", length = 20)
    private String currentPhase;

    @Column(name = "day_number", nullable = false)
    private Integer dayNumber = 1;

    @Column(name = "winner_faction", length = 20)
    private String winnerFaction;

    @Column(name = "is_single_player", nullable = false)
    private Boolean isSinglePlayer = false;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "finished_at")
    private LocalDateTime finishedAt;

    // JPAが利用するための引数なしコンストラクタ
    public Game() {
    }

    public Game(String status) {
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCurrentPhase() {
        return currentPhase;
    }

    public void setCurrentPhase(String currentPhase) {
        this.currentPhase = currentPhase;
    }

    public Integer getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(Integer dayNumber) {
        this.dayNumber = dayNumber;
    }

    public String getWinnerFaction() {
        return winnerFaction;
    }

    public void setWinnerFaction(String winnerFaction) {
        this.winnerFaction = winnerFaction;
    }

    public Boolean getIsSinglePlayer() {
        return isSinglePlayer;
    }

    public void setIsSinglePlayer(Boolean isSinglePlayer) {
        this.isSinglePlayer = isSinglePlayer;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(LocalDateTime finishedAt) {
        this.finishedAt = finishedAt;
    }
}
