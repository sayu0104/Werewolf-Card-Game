package com.example.werewolf.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "role_guaranteed_cards")
public class RoleGuaranteedCard {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "role_id", nullable = false)
	private Long roleId;

	@Column(name = "card_id", nullable = false)
	private Long cardId;

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
