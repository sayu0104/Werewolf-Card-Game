package com.example.werewolf.controller;

import com.example.werewolf.entity.Character;
import com.example.werewolf.repository.CharacterRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller // ブラウザからのアクセスを受け付ける窓口係
public class AdminCharacterController {

	private final CharacterRepository characterRepository;// 非公開の決定版の倉庫番

	public AdminCharacterController(CharacterRepository characterRepository) {// 工具箱を受け取る
		this.characterRepository = characterRepository;
	}

	@GetMapping("/admin/characters") // このURLにブラウザからアクセスが来たら、以下の処理を動かす
	public String listCharacters(Model model) {// この処理の名前「キャラクターを一覧化する」（データを乗せるお盆を受け取る）
		model.addAttribute("characters", characterRepository.findAll());// 道具で全キャラクターを取得して、"characters"の名札を付けてお盆にのせる
		return "character-list";// 上の処理が終わったらそれを表示する
	}

	@PostMapping("/admin/characters") // このURLにpostで注文が来たら、以下の処理を行う

	// @RequestParam = フォームに入力された値を受け取る係 (例：文字列で nameという箱で受け取る)
	// (required = false) = この項目は入力必須じゃない（送られてこなくてもOK）

	public String createCharacter(@RequestParam String name, @RequestParam(required = false) String personality,
			@RequestParam Integer optimality, @RequestParam Integer consistency, @RequestParam Integer deceptionSkill) {

		// Character(Character型の入れ物) character(characterという名前)
		// 「Character型の、characterという名前の箱を用意する」 ← 左に新しいキャラクターを1つ作って入れる
		Character character = new Character();
		character.setName(name);
		character.setPersonality(personality);
		character.setOptimality(optimality);
		character.setConsistency(consistency);
		character.setDeceptionSkill(deceptionSkill);
		characterRepository.save(character);// 倉庫にこのキャラクターデータを保存
		return "redirect:/admin/characters";// 上の処理が終わったら、このURLにアクセスし直す（処理変更後の画面にするため）
	}

	@PostMapping("/admin/characters/{id}/delete") // このURLにpostで注文が来たら、以下の処理を行う

	// @PathVariable（"URLの"中の値（id）を取り出して、処理の中で使えるようにする（係））
	public String deleteCharacter(@PathVariable Long id) {

		// characterRepository（倉庫番）の deleteById（自動1件削除）を使い、受け取ったidのキャラクターを消す
		characterRepository.deleteById(id);

		return "redirect:/admin/characters";// 上の処理が終わったら、このURLにアクセスし直す（処理変更後の画面にするため）
	}
}
