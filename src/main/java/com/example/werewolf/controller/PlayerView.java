package com.example.werewolf.controller;

public class PlayerView { // 画面表示用のプレイヤー1人の情報

	private Integer seatOrder; // 席順（数字）
	private String roleName; // 役職の名前（文字列）
	private Boolean isAlive; // 生存状態（true=生存 / false=死亡）

	public PlayerView() {
	} // 引数なしコンストラクタ（空の器を作る用。フレームワークが必要とする場合があるので用意）

	public PlayerView(Integer seatOrder, String roleName, Boolean isAlive) {
		this.seatOrder = seatOrder;
		this.roleName = roleName;
		this.isAlive = isAlive;
		// 上の ( ) の中で値を受け取ってる。
		// それで、= で「右側の seatOrder(受け取った値)」を
		// 「this の seatOrder(器の箱)」に書き込んでる。
	}

	public Integer getSeatOrder() {
		return seatOrder;
		// 席順をゲットしてきて、呼んでくれたところに渡すメソッド
	}

	public void setSeatOrder(Integer seatOrder) {
		this.seatOrder = seatOrder;
		// 席順を書き込んで、セットするためのメソッド
	}

	public String getRoleName() {
		return roleName;
		 // 役職名をゲットして、呼んでくれたところに渡す
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
		// 役職名を書き込んで、セットするためのメソッド
	}

	public Boolean getIsAlive() {
		return isAlive;
		 // 生存状態をゲットして、呼んでくれたところに渡す
	}

	public void setIsAlive(Boolean isAlive) {
		this.isAlive = isAlive;
		// 生存状態を書き込んで、セットするためのメソッド
	}
}
