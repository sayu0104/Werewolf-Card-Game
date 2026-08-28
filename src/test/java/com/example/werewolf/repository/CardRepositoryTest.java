package com.example.werewolf.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.werewolf.entity.Card;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

// @SpringBootTest = アプリ全体を起動してテストする設定
// @Transactional = テスト1つが終わるたびに、保存したデータを自動的にロールバック（元に戻す）する
@SpringBootTest
@Transactional
class CardRepositoryTest {

	@Autowired
	private CardRepository cardRepository;

	@Test
	void カードを1件保存すると同じ内容で取り出せる() {
		// 準備：カードを1件作る
		Card card = new Card("テストカード", "基本", 3);

		// 実行：保存する
		Card saved = cardRepository.save(card);

		// 検証：保存したidで取り出すと、同じ内容になっている
		Optional<Card> found = cardRepository.findById(saved.getId());
		assertThat(found).isPresent();
		assertThat(found.get().getName()).isEqualTo("テストカード");
		assertThat(found.get().getCardType()).isEqualTo("基本");
		assertThat(found.get().getCost()).isEqualTo(3);
	}

	@Test
	void カードを2件保存すると件数が2件増える() {
		// 準備：保存前の件数を覚えておく
		long countBefore = cardRepository.count();

		// 準備：カードを2件作る
		Card card1 = new Card("テストカードA", "基本", 3);
		Card card2 = new Card("テストカードB", "役職", 5);

		// 実行：2件とも保存する
		cardRepository.save(card1);
		cardRepository.save(card2);

		// 検証：全体の件数が2件増えている
		long countAfter = cardRepository.count();
		assertThat(countAfter).isEqualTo(countBefore + 2);
	}
}
