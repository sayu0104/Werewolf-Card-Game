package com.example.werewolf.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.werewolf.entity.Role;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

// @SpringBootTest = アプリ全体を起動してテストする設定
// @Transactional = テスト1つが終わるたびに、保存したデータを自動的にロールバック（元に戻す）する
@SpringBootTest
@Transactional
class RoleRepositoryTest {

	@Autowired
	private RoleRepository roleRepository;

	@Test
	void 役職を1件保存すると同じ内容で取り出せる() {
		// 準備：役職を1件作る
		Role role = new Role("テスト村人", "村人陣営", "テスト用の役職");

		// 実行：保存する
		Role saved = roleRepository.save(role);

		// 検証：保存したidで取り出すと、同じ内容になっている
		Optional<Role> found = roleRepository.findById(saved.getId());
		assertThat(found).isPresent();
		assertThat(found.get().getName()).isEqualTo("テスト村人");
		assertThat(found.get().getFaction()).isEqualTo("村人陣営");
		assertThat(found.get().getDescription()).isEqualTo("テスト用の役職");
	}

	@Test
	void 役職を2件保存すると件数が2件増える() {
		// 準備：保存前の件数を覚えておく
		long countBefore = roleRepository.count();

		// 準備：役職を2件作る（nameは重複不可のため別々の値にする）
		Role role1 = new Role("テスト村人", "村人陣営", "テスト用の役職1");
		Role role2 = new Role("テスト人狼", "人狼陣営", "テスト用の役職2");

		// 実行：2件とも保存する
		roleRepository.save(role1);
		roleRepository.save(role2);

		// 検証：全体の件数が2件増えている
		long countAfter = roleRepository.count();
		assertThat(countAfter).isEqualTo(countBefore + 2);
	}
}
