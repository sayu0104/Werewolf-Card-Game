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
public class SuspicionReason {// 疑い（疑心）の理由

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "game_id", nullable = false)
	private Long gameId;// どのゲームか（試合開始から決着までの１ゲーム）（games の id）

	@Column(name = "holder_game_player_id", nullable = false)
	private Long holderGamePlayerId;// その疑いを持っている人のid

	@Column(name = "target_game_player_id", nullable = false)
	private Long targetGamePlayerId;// 疑われている側のプレイヤーのid

	@Column(name = "reason_type", nullable = false, length = 20)
	private String reasonType;// 理由の種類

	@Column(name = "reason_text", nullable = false, columnDefinition = "TEXT")
	private String reasonText;// 理由の文章（本文）

	@Column(name = "day_number", nullable = false)
	private Integer dayNumber;// ゲーム内日付

	// JPAが利用するための引数なしコンストラクタ
	public SuspicionReason() {
	}

	// 「gameId という名前の、一時的な受け皿」を用意 外から渡された値が、ここに入る↓
	public SuspicionReason(Long gameId, Long holderGamePlayerId, Long targetGamePlayerId, String reasonType,
			String reasonText, Integer dayNumber) {
		this.gameId = gameId;
		this.holderGamePlayerId = holderGamePlayerId;
		this.targetGamePlayerId = targetGamePlayerId;
		this.reasonType = reasonType;
		this.reasonText = reasonText;
		this.dayNumber = dayNumber;
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
