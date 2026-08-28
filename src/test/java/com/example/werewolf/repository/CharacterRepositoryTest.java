package com.example.werewolf.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.werewolf.entity.Character;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

// @SpringBootTest = アプリ全体を起動してテストする設定
// @Transactional = テスト1つが終わるたびに、保存したデータを自動的にロールバック（元に戻す）する
// これにより、このテストでDBを汚したままにする心配はない
@SpringBootTest
@Transactional
class CharacterRepositoryTest {

	@Autowired // Springが、必要な部品を自動でここに入れてくれる目印
	private CharacterRepository characterRepository;// CharacterRepository型の、characterRepositoryという名前の箱を用意する

	@Test // 「これは1つのテストだよ」という目印
	void キャラクターを1件保存すると同じ内容で取り出せる() {
		// 準備：キャラクターを1件作る
		Character character = new Character("村人A", 80, 70, 20);// 「村人A」で最適性80・一貫性70・騙す能力20。

		// 実行：保存する
		Character saved = characterRepository.save(character);

		// 検証：保存したidで取り出すと、同じ内容になっている

		// 左: Optional<Character> found = 「Characterが入ってるかもしれない箱」found を用意
		// 右: 倉庫番に、savedのidで1件探してもらう → その結果をfoundに入れる

		Optional<Character> found = characterRepository.findById(saved.getId());
		assertThat(found).isPresent();
		// 確認する（ 取り出したキャラの名前 が 等しい "村人A"と ）
		assertThat(found.get().getName()).isEqualTo("村人A");
		assertThat(found.get().getOptimality()).isEqualTo(80);
		assertThat(found.get().getConsistency()).isEqualTo(70);
		assertThat(found.get().getDeceptionSkill()).isEqualTo(20);

		// Optional … 「中身があるかもしれないし、無いかもしれない箱 （ 取り出した結果を入れる、ちょっと特殊な箱 ）」
		// assertThat … 「これを確認するよ」
		// isEqualTo … 「〜と等しい（はず）」（英語 equal to =〜に等しい）
		// isPresent … 「存在する（はず）」（found の中身があるか）
	}

	@Test
	void キャラクターを2件保存するとfindAllで2件増える() {
		// 準備：保存前の件数を覚えておく（他のデータが既にあっても影響を受けないように）
		long countBefore = characterRepository.count();

		// 準備：キャラクターを2件作る
		Character character1 = new Character("村人A", 80, 70, 20);
		Character character2 = new Character("人狼B", 60, 50, 90);

		// 実行：2件とも保存する
		characterRepository.save(character1);
		characterRepository.save(character2);

		// 検証：全体の件数が2件増えている
		long countAfter = characterRepository.count();
		assertThat(countAfter).isEqualTo(countBefore + 2);
	}
}
