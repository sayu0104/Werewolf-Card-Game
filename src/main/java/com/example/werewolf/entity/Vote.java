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
public class Vote {// 投票

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "game_id", nullable = false)
	private Long gameId;// どのゲームか（試合開始から決着までの１ゲーム）（games の id）

	@Column(name = "day_number", nullable = false)
	private Integer dayNumber;// ゲーム内日付

	@Column(nullable = false)
	private Integer round = 1;// 投票の何回戦目か（同票で再投票なら2以上） 初期値1

	@Column(name = "voter_game_player_id", nullable = false)
	private Long voterGamePlayerId;// 投票した人のプレイヤーId

	@Column(name = "target_game_player_id", nullable = false)
	private Long targetGamePlayerId;// 投票された側のプレイヤーのid

	// JPAが利用するための引数なしコンストラクタ
	public Vote() {
	}

	// 「gameId という名前の、一時的な受け皿」を用意 外から渡された値が、ここに入る↓
	public Vote(Long gameId, Integer dayNumber, Long voterGamePlayerId, Long targetGamePlayerId) {
		this.gameId = gameId;
		this.dayNumber = dayNumber;
		this.voterGamePlayerId = voterGamePlayerId;
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
