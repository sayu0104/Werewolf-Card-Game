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
@Entity // このクラスはDBと連携している
@Table(name = "games") // javaでは「Game」、DBでは「games」と扱い、保存する
public class Game {

	@Id // 以下の変数に対してidが主キーであることを示す
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 自動的に値を生成すること（方法＝DBにある自動採番機能）
	private Long id;

	@Column(nullable = false, length = 20) // 以下の変数に対して（）
	private String status;// ステータス

	@Column(name = "current_phase", length = 20)
	private String currentPhase;// 現在のフェーズ（段階）

	@Column(name = "day_number", nullable = false)
	private Integer dayNumber = 1;// ゲーム内日付（ゲームは1日目から始まる）

	@Column(name = "winner_faction", length = 20)
	private String winnerFaction;// 勝利陣営（派閥）

	@Column(name = "is_single_player", nullable = false)
	private Boolean isSinglePlayer = false;// シングルプレイヤーか？＝違う

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;// リアルの作成日時

	@Column(name = "finished_at")
	private LocalDateTime finishedAt;// リアルの終了日時

	// JPAが利用するための引数なしコンストラクタ
	public Game() {
	}

	public Game(String status) {// 新規作成に最低限必要な変数だけ受け取る
		this.status = status;// ステータス
		this.createdAt = LocalDateTime.now();// 作成日時だけは、作った瞬間の時刻を自動で入れる
	}

	// get（外から中の値を「読み取る」ための窓口） private＝非公開なので窓口越しに読む
	public Long getId() {
		return id;// idの値を返す（教える）
	}

	public String getStatus() {
		return status;// ステータス
	}

	// set（外から中の値を「書き換える」ための窓口）
	public void setStatus(String status) {
		this.status = status;// 台帳と、記入する（紙）
	}

	public String getCurrentPhase() {
		return currentPhase;// 現在のフェーズ
	}

	public void setCurrentPhase(String currentPhase) {
		this.currentPhase = currentPhase;
	}

	public Integer getDayNumber() {
		return dayNumber;// ゲーム内日付
	}

	public void setDayNumber(Integer dayNumber) {
		this.dayNumber = dayNumber;
	}

	public String getWinnerFaction() {
		return winnerFaction;// 勝利陣営（派閥）
	}

	public void setWinnerFaction(String winnerFaction) {
		this.winnerFaction = winnerFaction;
	}

	public Boolean getIsSinglePlayer() {// はい or いいえ
		return isSinglePlayer;// シングルプレイヤーか？
	}

	public void setIsSinglePlayer(Boolean isSinglePlayer) {
		this.isSinglePlayer = isSinglePlayer;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;// リアルの作成日時
	}

	public LocalDateTime getFinishedAt() {
		return finishedAt;// リアルの終了日時
	}

	public void setFinishedAt(LocalDateTime finishedAt) {
		this.finishedAt = finishedAt;
	}
}
