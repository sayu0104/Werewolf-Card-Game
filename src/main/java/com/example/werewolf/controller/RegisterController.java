package com.example.werewolf.controller;

import com.example.werewolf.entity.User;
import com.example.werewolf.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller // 以下のクラスは、ブラウザからのアクセスを受け付ける窓口係(コントローラ)
public class RegisterController { // 新規登録

	// UserRepository userRepository …　型（種類・設計図の名前） 名前（この箱の呼び名）
	// ↑ この「UserRepository型の箱」を、この係の手元に持っておく宣言
	// （中身は、コンストラクタでDIで受け取った"倉庫番"が入る）
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public RegisterController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		//                          ↑ユーザーの倉庫番（道具）       ↑パスワードをハッシュ化する道具
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	// このURLにブラウザからアクセスが来たら、以下の処理を動かす
	@GetMapping("/register")
	public String showRegisterForm() {
		return "register";
	}

	// このURLにpostで注文が来たら、以下の処理を行う
	@PostMapping("/register")
	public String register(
			
			// フォームに入力された値を受け取る係　文字列で　usernameという箱で受け取る
			@RequestParam String username,
			@RequestParam String email,
			@RequestParam String password) {
		
		// user倉庫に保存する（新しいユーザー（ユーザーネーム、パスワード、メールアドレス））
		userRepository.save(new User(username, passwordEncoder.encode(password), email));
		
		// passwordEncoder.encode(password) … パスワードをハッシュ化する
		// passwordEncoder を使って → .encode(password)（ハッシュ化を実行）
		// なぜ生パスワードをそのまま保存しないのか＝漏れても元が分からないように一方通行に変換
		
		return "redirect:/login";
	}
}
