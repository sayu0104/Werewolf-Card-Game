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
public class NightAction {// 夜の行動

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "game_id", nullable = false)
	private Long gameId;// どのゲームか（試合開始から決着までの１ゲーム）（games の id）

	@Column(name = "day_number", nullable = false)
	private Integer dayNumber;// ゲーム内日付

	@Column(name = "actor_game_player_id", nullable = false)
	private Long actorGamePlayerId;// 行動を起こしたプレイヤーのid

	@Column(name = "action_type", nullable = false, length = 20)
	private String actionType;// 行動の種類

	@Column(name = "target_game_player_id", nullable = false)
	private Long targetGamePlayerId;// 対象にしたプレイヤーのid

	@Column(name = "is_successful")
	private Boolean isSuccessful;// 成功したか？判定

	// JPAが利用するための引数なしコンストラクタ
	public NightAction() {
	}

	// 「gameId という名前の、一時的な受け皿」を用意 外から渡された値が、ここに入る↓
	public NightAction(Long gameId, Integer dayNumber, Long actorGamePlayerId, String actionType,
			Long targetGamePlayerId) {
		this.gameId = gameId;
		this.dayNumber = dayNumber;
		this.actorGamePlayerId = actorGamePlayerId;
		this.actionType = actionType;
		this.targetGamePlayerId = targetGamePlayerId;
	}

	// get（外から中の値を「読み取る」ための窓口） private＝非公開のため窓口越しに見る
	public Long getId() {
		return id;
	}

	public Long getGameId() {
		return gameId;
	}

	// set（外から中の値を「書き換える」ための窓口）
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
