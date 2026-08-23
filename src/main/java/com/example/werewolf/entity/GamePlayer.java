package com.example.werewolf.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * docs/database-design.md の game_players テーブルに対応するエンティティ。
 */
@Entity
@Table(name = "game_players")
public class GamePlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "game_id", nullable = false)
    private Long gameId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "character_id")
    private Long characterId;

    @Column(name = "role_id", nullable = false)
    private Long roleId;

    @Column(name = "is_alive", nullable = false)
    private Boolean isAlive = true;

    @Column(name = "seat_order", nullable = false)
    private Integer seatOrder;

    // JPAが利用するための引数なしコンストラクタ
    public GamePlayer() {
    }

    public GamePlayer(Long gameId, Long roleId, Integer seatOrder) {
        this.gameId = gameId;
        this.roleId = roleId;
        this.seatOrder = seatOrder;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCharacterId() {
        return characterId;
    }

    public void setCharacterId(Long characterId) {
        this.characterId = characterId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Boolean getIsAlive() {
        return isAlive;
    }

    public void setIsAlive(Boolean isAlive) {
        this.isAlive = isAlive;
    }

    public Integer getSeatOrder() {
        return seatOrder;
    }

    public void setSeatOrder(Integer seatOrder) {
        this.seatOrder = seatOrder;
    }
}
