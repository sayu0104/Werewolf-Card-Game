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

@SpringBootTest
@Transactional
class RegisterControllerTest {

	@Autowired
	private RegisterController registerController;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Test
	void 登録するとユーザーが1件増える() {
		// 準備：登録前の件数を覚えておく
		long countBefore = userRepository.count();

		// 実行：登録する
		registerController.register("shinki_user", "shinki_user@example.com", "raw-password");

		// 検証：件数が1件増えている
		long countAfter = userRepository.count();
		assertThat(countAfter).isEqualTo(countBefore + 1);
	}

	@Test
	void 登録した一般ユーザーはis_adminがfalseで保存される() {
		// 実行：登録する
		registerController.register("ippan_user", "ippan_user@example.com", "raw-password");

		// 検証：is_adminがfalseになっている
		Optional<User> found = userRepository.findByUsername("ippan_user");
		assertThat(found).isPresent();
		assertThat(found.get().getIsAdmin()).isFalse();
	}

	@Test
	void 保存されたパスワードはハッシュ化されている() {
		// 準備：平文パスワード
		String rawPassword = "raw-password";

		// 実行：登録する
		registerController.register("hash_user", "hash_user@example.com", rawPassword);

		// 検証：保存された値が平文と異なり、BCryptで照合できる
		Optional<User> found = userRepository.findByUsername("hash_user");
		assertThat(found).isPresent();
		assertThat(found.get().getPasswordHash()).isNotEqualTo(rawPassword);
		assertThat(passwordEncoder.matches(rawPassword, found.get().getPasswordHash())).isTrue();
	}
}
