package com.example.werewolf.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity // このクラスはDBと連携している
@Table(name = "role_guaranteed_cards") // DBでは「role_guaranteed_cards」と扱い、保存すること
public class RoleGuaranteedCard { // 役職それぞれの、毎ターン（昼）手札に保証されるカード

	@Id // 以下の変数に対してidが主キーであることを示す
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 自動的に値を生成すること（方法はDBにある自動採番機能）
	private Long id;

	@Column(name = "role_id", nullable = false) // 以下の変数に対して
	private Long roleId;

	@Column(name = "card_id", nullable = false)
	private Long cardId;

	// JPAが利用するための引数なしコンストラクタ
	public RoleGuaranteedCard() {
	}

	public RoleGuaranteedCard(Long roleId, Long cardId) {
		this.roleId = roleId;
		this.cardId = cardId;
	}

	public Long getId() {
		return id;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Long getCardId() {
		return cardId;
	}

	public void setCardId(Long cardId) {
		this.cardId = cardId;
	}
}
