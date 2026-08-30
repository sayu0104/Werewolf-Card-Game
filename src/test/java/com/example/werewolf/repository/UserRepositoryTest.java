package com.example.werewolf.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.werewolf.entity.User;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

// @SpringBootTest = アプリ全体を起動してテストする設定
// @Transactional = テスト1つが終わるたびに、保存したデータを自動的にロールバック（元に戻す）する
@SpringBootTest
@Transactional
class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;

	@Test
	void ユーザーを1件保存すると同じ内容で取り出せる() {
		// 準備：ユーザーを1件作る
		User user = new User("murabito_a", "hashed-password", "murabito_a@example.com");

		// 実行：保存する
		User saved = userRepository.save(user);

		// 検証：保存したidで取り出すと、同じ内容になっている
		Optional<User> found = userRepository.findById(saved.getId());
		assertThat(found).isPresent();
		assertThat(found.get().getUsername()).isEqualTo("murabito_a");
		assertThat(found.get().getEmail()).isEqualTo("murabito_a@example.com");
	}

	@Test
	void ユーザー名で検索すると該当するユーザーが取り出せる() {
		// 準備：ユーザーを1件保存する
		userRepository.save(new User("hunter_c", "hashed-password", "hunter_c@example.com"));

		// 実行：ユーザー名で検索する
		Optional<User> found = userRepository.findByUsername("hunter_c");

		// 検証：保存した内容と一致する
		assertThat(found).isPresent();
		assertThat(found.get().getEmail()).isEqualTo("hunter_c@example.com");
	}

	@Test
	void 存在しないユーザー名で検索すると空になる() {
		// 実行：存在しないユーザー名で検索する
		Optional<User> found = userRepository.findByUsername("no_such_user");

		// 検証：見つからない
		assertThat(found).isEmpty();
	}

	@Test
	void ユーザーを2件保存すると件数が2件増える() {
		// 準備：保存前の件数を覚えておく
		long countBefore = userRepository.count();

		// 準備：ユーザーを2件作る（username/emailは重複不可のため別々の値にする）
		User user1 = new User("murabito_a", "hashed-password-1", "murabito_a@example.com");
		User user2 = new User("jinro_b", "hashed-password-2", "jinro_b@example.com");

		// 実行：2件とも保存する
		userRepository.save(user1);
		userRepository.save(user2);

		// 検証：全体の件数が2件増えている
		long countAfter = userRepository.count();
		assertThat(countAfter).isEqualTo(countBefore + 2);
	}
}
