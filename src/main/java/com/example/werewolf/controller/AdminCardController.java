package com.example.werewolf.controller;

import com.example.werewolf.entity.Card;
import com.example.werewolf.repository.CardRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller // ブラウザからのアクセスを受け付ける窓口係
public class AdminCardController {

	private final CardRepository cardRepository;// 非公開の決定版の倉庫番

	public AdminCardController(CardRepository cardRepository) {// 工具箱を受け取る
		this.cardRepository = cardRepository;
	}

	@GetMapping("/admin/cards") // このURLにブラウザからアクセスが来たら、以下の処理を動かす
	public String listCards(Model model) {// この処理の名前「カードを一覧化する」（データを乗せるお盆を受け取る）
		model.addAttribute("cards", cardRepository.findAll());// 道具で全カードを取得して、"cards"の名札を付けてお盆にのせる
		return "card-list";// 上の処理が終わったらそれを表示する
	}

	@PostMapping("/admin/cards") // このURLにpostで注文が来たら、以下の処理を行う

	// @RequestParam = フォームに入力された値を受け取る係 (例：文字列で nameという箱で受け取る)
	// (required = false) = この項目は入力必須じゃない（送られてこなくてもOK）

	public String createCard(@RequestParam String name, @RequestParam String cardType,
			@RequestParam(required = false) Long requiredRoleId, @RequestParam Integer cost,
			@RequestParam(required = false, defaultValue = "false") Boolean isMandatory,
			@RequestParam(required = false, defaultValue = "false") Boolean isRare,
			@RequestParam(required = false) Integer value,
			@RequestParam(required = false, defaultValue = "false") Boolean isUsagePublic,
			@RequestParam(required = false) String description) {

		// Card(カード型の入れ物) card(cardという名前) 「Card型の、cardという名前の箱を用意する」 ← 左に新しいカードを1つ作って入れる
		Card card = new Card();
		card.setName(name);
		card.setCardType(cardType);
		card.setRequiredRoleId(requiredRoleId);
		card.setCost(cost);
		card.setIsMandatory(isMandatory);
		card.setIsRare(isRare);
		card.setValue(value);
		card.setIsUsagePublic(isUsagePublic);
		card.setDescription(description);
		cardRepository.save(card);// 倉庫にこのカードデータを保存
		return "redirect:/admin/cards";// 上の処理が終わったら、「/admin/cards」にアクセスし直す（処理変更後の画面にするため）
	}

	@PostMapping("/admin/cards/{id}/delete") // このURLにpostで注文が来たら、以下の処理を行う

	// @PathVariable（"URLの"中の値（id）を取り出して、処理の中で使えるようにする（係））
	public String deleteCard(@PathVariable Long id) {

		// cardRepository（倉庫番）の deleteById（自動1件削除）を使い、受け取ったidのカードを消す
		cardRepository.deleteById(id);

		return "redirect:/admin/cards";// 上の処理が終わったら、「/admin/cards」にアクセスし直す（処理変更後の画面にするため）
	}
}
