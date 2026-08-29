package com.example.werewolf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // ブラウザからのアクセスを受け付ける窓口係
public class LoginController {
	
	// LoginController … /login に来たら login.html を出す係（SecurityConfigと連携）

	@GetMapping("/login") // このURLにブラウザからアクセスが来たら、以下の処理を動かす
	public String login() {
		return "login"; // 上の処理が終わったらそれを表示する
	}
}
