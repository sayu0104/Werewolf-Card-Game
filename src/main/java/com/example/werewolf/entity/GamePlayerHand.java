package com.example.werewolf.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * docs/database-design.md の game_player_hands テーブルに対応するエンティティ。
 */
@Entity
@Table(name = "game_player_hands")
public class GamePlayerHand {// プレイヤーの手札

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "game_player_id", nullable = false)
	private Long gamePlayerId;// ゲームプレイヤーId

	@Column(name = "card_id", nullable = false)
	private Long cardId;// カードId

	@Column(nullable = false, length = 20)
	private String status;// ステータス

	@Column(name = "acquired_at_day", nullable = false)
	private Integer acquiredAtDay;// このカードを何日目に獲得した（引いた）か （獲得した ～の時点 日）

	// JPAが利用するための引数なしコンストラクタ
	public GamePlayerHand() {
	}

	// 「gamePlayerId, という名前の、一時的な受け皿」を用意 外から渡された値が、ここに入る↓
	public GamePlayerHand(Long gamePlayerId, Long cardId, String status, Integer acquiredAtDay) {
		this.gamePlayerId = gamePlayerId;
		this.cardId = cardId;
		this.status = status;
		this.acquiredAtDay = acquiredAtDay;
	}

	// get（外から中の値を「読み取る」ための窓口） private＝非公開のため窓口越しに見る
	public Long getId() {
		return id;
	}

	public Long getGamePlayerId() {
		return gamePlayerId;
	}

	// set（外から中の値を「書き換える」ための窓口）
	public void setGamePlayerId(Long gamePlayerId) {
		this.gamePlayerId = gamePlayerId;
	}

	public Long getCardId() {
		return cardId;
	}

	public void setCardId(Long cardId) {
		this.cardId = cardId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getAcquiredAtDay() {
		return acquiredAtDay;
	}

	public void setAcquiredAtDay(Integer acquiredAtDay) {
		this.acquiredAtDay = acquiredAtDay;
	}
}
