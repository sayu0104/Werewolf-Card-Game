package com.example.werewolf.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.werewolf.entity.CharacterDialogue;
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
class CharacterDialogueRepositoryTest {

	@Autowired // Springが、必要な部品を自動でここに入れてくれる目印
	private CharacterDialogueRepository characterDialogueRepository;// CharacterDialogueRepository型の、characterDialogueRepositoryという名前の箱を用意する

	@Test // 「これは1つのテストだよ」という目印
	void セリフを1件保存すると同じ内容で取り出せる() {
		// 準備：セリフを1件作る
		CharacterDialogue dialogue = new CharacterDialogue(1L, "占い", "怪しい人がいる気がする…");

		// 実行：保存する
		CharacterDialogue saved = characterDialogueRepository.save(dialogue);

		// 検証：保存したidで取り出すと、同じ内容になっている

		// 左: Optional<CharacterDialogue> found = 「CharacterDialogueが入ってるかもしれない箱」found を用意
		// 右: 倉庫番に、savedのidで1件探してもらう → その結果をfoundに入れる

		Optional<CharacterDialogue> found = characterDialogueRepository.findById(saved.getId());
		assertThat(found).isPresent();
		// 確認する（ 取り出したセリフの各項目 が 等しい ）
		assertThat(found.get().getCharacterId()).isEqualTo(1L);
		assertThat(found.get().getActionType()).isEqualTo("占い");
		assertThat(found.get().getDialogueText()).isEqualTo("怪しい人がいる気がする…");

		// Optional … 「中身があるかもしれないし、無いかもしれない箱 （ 取り出した結果を入れる、ちょっと特殊な箱 ）」
		// assertThat … 「これを確認するよ」
		// isEqualTo … 「〜と等しい（はず）」（英語 equal to =〜に等しい）
		// isPresent … 「存在する（はず）」（found の中身があるか）
	}

	@Test
	void セリフを2件保存するとfindAllで2件増える() {
		// 準備：保存前の件数を覚えておく（他のデータが既にあっても影響を受けないように）
		long countBefore = characterDialogueRepository.count();

		// 準備：セリフを2件作る
		CharacterDialogue dialogue1 = new CharacterDialogue(1L, "占い", "怪しい人がいる気がする…");
		CharacterDialogue dialogue2 = new CharacterDialogue(2L, "襲撃", "今夜、動くぞ。");

		// 実行：2件とも保存する
		characterDialogueRepository.save(dialogue1);
		characterDialogueRepository.save(dialogue2);

		// 検証：全体の件数が2件増えている
		long countAfter = characterDialogueRepository.count();
		assertThat(countAfter).isEqualTo(countBefore + 2);
	}
}
