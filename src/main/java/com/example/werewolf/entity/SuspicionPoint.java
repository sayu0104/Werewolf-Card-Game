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
public class SuspicionPoint {// 疑い（疑心）ポイント

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "game_id", nullable = false)
	private Long gameId;// どのゲームか（試合開始から決着までの１ゲーム）（games の id）

	@Column(name = "game_player_id", nullable = false)
	private Long gamePlayerId;

	@Column(name = "day_number", nullable = false)
	private Integer dayNumber;// ゲーム内日付

	@Column(name = "points_delta", nullable = false)
	private Integer pointsDelta;// 疑心によるポイントの増減量　（「いつ・何で・どれだけ動いたか」を1件ずつ残せる）

	@Column(length = 50)
	private String reason;// （ポイントが動いた）理由

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;// この記録が作られた日時（リアル）

	// JPAが利用するための引数なしコンストラクタ
	public SuspicionPoint() {
	}

	// 「gameId という名前の、一時的な受け皿」を用意 外から渡された値が、ここに入る↓
	public SuspicionPoint(Long gameId, Long gamePlayerId, Integer dayNumber, Integer pointsDelta) {
		this.gameId = gameId;
		this.gamePlayerId = gamePlayerId;
		this.dayNumber = dayNumber;
		this.pointsDelta = pointsDelta;
		this.createdAt = LocalDateTime.now();
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
