package com.example.werewolf.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "deck_cards")
public class DeckCard { // 山札用のカード（保証枠のカードは含まない）

	@Id // 以下の変数に対してidが主キーであることを示す
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 自動的に値を生成すること（方法はDBにある自動採番機能）
	private Long id;

	@Column(name = "card_id", nullable = false)
	private Long cardId;

	@Column(nullable = false)
	private Integer copies; // ※基本カード（疑う/擁護）をコピーして増やし、配るためのもの

	// JPAが利用するための引数なしコンストラクタ
	public DeckCard() {
	}

	public DeckCard(Long cardId, Integer copies) {
		this.cardId = cardId;
		this.copies = copies;
	}

	public Long getId() {
		return id;
	}

	public Long getCardId() {
		return cardId;
	}

	public void setCardId(Long cardId) {
		this.cardId = cardId;
	}

	public Integer getCopies() {
		return copies;
	}

	public void setCopies(Integer copies) {
		this.copies = copies;
	}
}
