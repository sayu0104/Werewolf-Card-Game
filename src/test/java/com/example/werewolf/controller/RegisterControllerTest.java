package com.example.werewolf.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.werewolf.entity.User;
import com.example.werewolf.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest // アプリ全体を起動してテストする設定
@Transactional // テスト1つが終わるたびに、保存したデータを自動的にロールバック（元に戻す）する

// ↑これにより、このテストでDBを汚したままにする心配はない

class RegisterControllerTest { // 新規登録コントローラーのテスト

	@Autowired // Springが、必要な部品を自動でここに入れてくれる目印
	private RegisterController registerController; // 大文字＝型／小文字＝名前

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Test // 「これは1つのテストだよ」という目印
	void 登録するとユーザーが1件増える() {
		// 準備：登録前の件数を覚えておく
		long countBefore = userRepository.count();
		// 登録前の数を countBefore に覚えておく
		// count() … 件数を数える

		// 実行：登録する
		registerController.register("shinki_user", "shinki_user@example.com", "raw-password");
		// 登録処理を直接呼ぶ 3つの引数（名前・メール・パスワード）を渡してる

		// 検証：件数が1件増えている
		long countAfter = userRepository.count();
		assertThat(countAfter).isEqualTo(countBefore + 1);
		// countAfter が countBefore+1 と等しいと確かめる
		
		// assertThat(見たいもの)
		// isEqualTo（〜と等しい）
	}

	@Test
	void 登録した一般ユーザーはis_adminがfalseで保存される() {
		// 実行：登録する
		registerController.register("ippan_user", "ippan_user@example.com", "raw-password");

		// 検証：is_adminがfalseになっている
		Optional<User> found = userRepository.findByUsername("ippan_user");
		// 登録したユーザーを Username で探す
		// Optional … 中身があるかもしれないし、無いかもしれない箱
		
		// 確認
		assertThat(found).isPresent();
		// isPresent … 「存在するはず」（箱の中身がある＝ちゃんと登録された）
		
		assertThat(found.get().getIsAdmin()).isFalse();
		// この前決めた「一般ユーザー＝is_admin=false」を確かめる
		
		// found.get()＝箱から中身を取り出す
		// .getIsAdmin()＝その人のis_admin
		// .isFalse()＝「falseのはず」
	}

	@Test
	void 保存されたパスワードはハッシュ化されている() {
		// 準備：平文パスワード
		String rawPassword = "raw-password";

		// 実行：登録する
		registerController.register("hash_user", "hash_user@example.com", rawPassword);

		// 検証：保存された値が平文と異なり、BCryptで照合できる
		Optional<User> found = userRepository.findByUsername("hash_user");
		// 登録したユーザーを Username で探す
		// Optional … 中身があるかもしれないし、無いかもしれない箱
		
		assertThat(found).isPresent();
		// isPresent … 「存在するはず」（箱の中身がある＝ちゃんと登録された）
		
		// 確認：パスワードがハッシュ化してるかどうか
		assertThat(found.get().getPasswordHash()).isNotEqualTo(rawPassword);
		// found.get().getPasswordHash() … 保存されたパスワード欄の中身が
		// .isNotEqualTo( … 〜と等しくないはず
		// rawPassword ) … 平文 "raw-password" と
		
		// 確認：ハッシュ化されたものが、正しく元のパスワードのハッシュか（ハッシュが正しく機能するか）
		assertThat(passwordEncoder.matches(rawPassword, found.get().getPasswordHash())).isTrue();
		// ハッシュは戻せないので、平文をもう一度ハッシュ化して突き合わせる
		
		// passwordEncoder.matches( …　照合する（付き合わせる）
		// rawPassword, … 平文と
		// passwordHash ) … 保存されたハッシュを
	}
}
