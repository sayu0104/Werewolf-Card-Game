package com.example.werewolf.controller;

public class PlayerView {

	private Integer seatOrder;
	private String roleName;
	private Boolean isAlive;

	public PlayerView() {
	}

	public PlayerView(Integer seatOrder, String roleName, Boolean isAlive) {
		this.seatOrder = seatOrder;
		this.roleName = roleName;
		this.isAlive = isAlive;
	}

	public Integer getSeatOrder() {
		return seatOrder;
	}

	public void setSeatOrder(Integer seatOrder) {
		this.seatOrder = seatOrder;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Boolean getIsAlive() {
		return isAlive;
	}

	public void setIsAlive(Boolean isAlive) {
		this.isAlive = isAlive;
	}
}
