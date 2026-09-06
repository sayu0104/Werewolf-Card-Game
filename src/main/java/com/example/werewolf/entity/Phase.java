package com.example.werewolf.entity;

public enum Phase { // 決まった選択肢を並べるだけの 特別なクラス
	
	// enum … 「決められた選択肢を、あらかじめ全部リストにしておく型
	// フェーズは朝・昼・投票・夜1・夜2の5つだけ、それ以外は入れられない
	
	MORNING,
	DAY,
	VOTE,
	NIGHT1,
	NIGHT2
}
