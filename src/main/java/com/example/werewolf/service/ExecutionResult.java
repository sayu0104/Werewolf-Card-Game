package com.example.werewolf.service;

public enum ExecutionResult { // 決まった選択肢を並べるだけの 特別なクラス

	// enum … 決められた選択肢を、あらかじめ全部リストにしておく型
	// 処刑処理の結果は、この2つのどれかだけ、それ以外は入れられない
	
	EXECUTED, // 処刑した
	NEEDS_REVOTE // 再投票が必要
}
