package com.example.werewolf.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.werewolf.entity.Card;
import com.example.werewolf.entity.CardEffectType;
import com.example.werewolf.entity.CardTiming;
import java.util.Optional;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

// @SpringBootTest = アプリ全体を起動してテストする設定
// @Transactional = テスト1つが終わるたびに、保存したデータを自動的にロールバック（元に戻す）する
@SpringBootTest
@Transactional
class CardRepositoryTest { // カードの仕様テスト

	@Autowired
	private CardRepository cardRepository;

	@Test
	void カードを1件保存すると同じ内容で取り出せる() {
		// 準備：カードを1件作る
		Card card = new Card("テストカード", CardEffectType.SUSPICION, CardTiming.DAY, 3);

		// 実行：保存する
		Card saved = cardRepository.save(card);

		// 検証：保存したidで取り出すと、同じ内容になっている
		Optional<Card> found = cardRepository.findById(saved.getId());
		assertThat(found).isPresent();
		assertThat(found.get().getName()).isEqualTo("テストカード");
		assertThat(found.get().getEffectType()).isEqualTo(CardEffectType.SUSPICION);
		assertThat(found.get().getTiming()).isEqualTo(CardTiming.DAY);
		assertThat(found.get().getCost()).isEqualTo(3);
	}

	@Test
	void カードを2件保存すると件数が2件増える() {
		// 準備：保存前の件数を覚えておく
		long countBefore = cardRepository.count();

		// 準備：カードを2件作る
		Card card1 = new Card("テストカードA", CardEffectType.SUSPICION, CardTiming.DAY, 3);
		Card card2 = new Card("テストカードB", CardEffectType.INFORMATION, CardTiming.NIGHT, 5);

		// 実行：2件とも保存する
		cardRepository.save(card1);
		cardRepository.save(card2);

		// 検証：全体の件数が2件増えている
		long countAfter = cardRepository.count();
		assertThat(countAfter).isEqualTo(countBefore + 2);
	}

	@Test
	void カードマスタ投入後は16レコードある() {
		assertThat(cardRepository.count()).isEqualTo(16);
		// カードの合計枚数が合っているかを確かめる
	}

	@Test
	void 占いは情報取得で夜のカードである() {
		Card card = findByName("占い");

		assertThat(card.getEffectType()).isEqualTo(CardEffectType.INFORMATION);
		assertThat(card.getTiming()).isEqualTo(CardTiming.NIGHT);
		// 占いのカード効果の種類は、情報取得なはず
		// 占いのカード効果が発動するのは、夜のはず
	}

	@Test
	void 疑う弱は被疑心操作で昼のカードである() {
		Card card = findByName("疑う（弱）");

		assertThat(card.getEffectType()).isEqualTo(CardEffectType.SUSPICION);
		assertThat(card.getTiming()).isEqualTo(CardTiming.DAY);
		// 疑う（弱）のカード効果の種類は、被疑心操作なはず
		// 疑う（弱）のカード効果が発動するのは、昼のはず
	}

	@Test
	void 結果報告は報告で昼の強制使用カードである() {
		Card card = findByName("結果報告");

		assertThat(card.getEffectType()).isEqualTo(CardEffectType.REPORT);
		assertThat(card.getTiming()).isEqualTo(CardTiming.DAY);
		assertThat(card.getIsMandatory()).isTrue();
		assertThat(card.getRequiredRoleId()).isNull();
		// 結果報告のカード効果の種類は、報告なはず
		// 結果報告のカード効果が発動するのは、昼のはず
		// 結果報告のカード使用は、強制（true）であるはず
		// 結果報告のカードの役職IDは、空(null)のはず
		// （占い師と狩人の2役職で共通なので、1つの役職IDには決めない。配る側で扱う）
	}

	@Test
	void カウンターは希少カードである() {
		Card card = findByName("カウンター");

		assertThat(card.getEffectType()).isEqualTo(CardEffectType.SUSPICION);
		assertThat(card.getIsRare()).isTrue();
		// カウンターのカードの効果の種類は、被疑心操作なはず
		// カウンターのカードは、レア（希少）であるはず
	}

	private Card findByName(String name) {
		List<Card> cards = cardRepository.findAll().stream().filter(c -> c.getName().equals(name)).toList();
		assertThat(cards).hasSize(1);
		return cards.get(0);
		// カードのデータを探してくる
		// カードは１枚、１件のデータ
	}
}
