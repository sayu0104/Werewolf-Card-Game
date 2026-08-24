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
	private Long gameId;// どのゲームか（試合開始から決着までの１ゲーム）（games の id）

	@Column(name = "user_id")
	private Long userId;// どのユーザーか（users の id）※人間プレイヤーの場合

	@Column(name = "character_id")
	private Long characterId;// どのキャラか（characters の id）※疑似プレイヤーの場合

	@Column(name = "role_id", nullable = false)
	private Long roleId;// どの役職か（roles の id）

	@Column(name = "is_alive", nullable = false)
	private Boolean isAlive = true;// 生存しているか（true/false。初期値 true＝生きている）

	@Column(name = "seat_order", nullable = false)
	private Integer seatOrder;// 座席順（Integer）

	// JPAが利用するための引数なしコンストラクタ
	public GamePlayer() {
	}

	// 「gameId という名前の、一時的な受け皿」を用意 外から渡された値が、ここに入る↓
	public GamePlayer(Long gameId, Long roleId, Integer seatOrder) {
		this.gameId = gameId;
		this.roleId = roleId;
		this.seatOrder = seatOrder;
	}

	// get（外から中の値を「読み取る」ための窓口） private＝非公開のため窓口越しに見る
	public Long getId() {
		return id;
	}

	public Long getGameId() {
		return gameId;// どのゲームか（試合開始から決着までの１ゲーム）（games の id）
	}

	// set（外から中の値を「書き換える」ための窓口）
	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}

	public Long getUserId() {
		return userId;// どのユーザーか（users の id）※人間プレイヤーの場合
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getCharacterId() {
		return characterId;// どのキャラか（characters の id）※疑似プレイヤーの場合
	}

	public void setCharacterId(Long characterId) {
		this.characterId = characterId;
	}

	public Long getRoleId() {
		return roleId;// どの役職か（roles の id）
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Boolean getIsAlive() {
		return isAlive;// 生存しているか（true/false。初期値 true＝生きている）
	}

	public void setIsAlive(Boolean isAlive) {
		this.isAlive = isAlive;
	}

	public Integer getSeatOrder() {
		return seatOrder;// 座席順（Integer）
	}

	public void setSeatOrder(Integer seatOrder) {
		this.seatOrder = seatOrder;
	}
}
