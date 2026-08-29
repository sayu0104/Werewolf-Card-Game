package com.example.werewolf.controller;

import com.example.werewolf.entity.CharacterDialogue;
import com.example.werewolf.repository.CharacterDialogueRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller // ブラウザからのアクセスを受け付ける窓口係
public class AdminCharacterDialogueController {

	private final CharacterDialogueRepository characterDialogueRepository;// 非公開の決定版の倉庫番

	public AdminCharacterDialogueController(CharacterDialogueRepository characterDialogueRepository) {// 工具箱を受け取る
		this.characterDialogueRepository = characterDialogueRepository;
	}

	@GetMapping("/admin/dialogues") // このURLにブラウザからアクセスが来たら、以下の処理を動かす
	public String listDialogues(Model model) {// この処理の名前「セリフを一覧化する」（データを乗せるお盆を受け取る）
		model.addAttribute("dialogues", characterDialogueRepository.findAll());// 道具で全セリフを取得して、"dialogues"の名札を付けてお盆にのせる
		return "dialogue-list";// 上の処理が終わったらそれを表示する
	}

	@PostMapping("/admin/dialogues") // このURLにpostで注文が来たら、以下の処理を行う

	// @RequestParam = フォームに入力された値を受け取る係 (例：文字列で nameという箱で受け取る)
	// (required = false) = この項目は入力必須じゃない（送られてこなくてもOK）

	public String createDialogue(@RequestParam Long characterId, @RequestParam String actionType,
			@RequestParam(required = false) String conditionKey, @RequestParam String dialogueText) {

		// CharacterDialogue(CharacterDialogue型の入れ物) dialogue(dialogueという名前)
		// 「CharacterDialogue型の、dialogueという名前の箱を用意する」 ← 左に新しいセリフを1つ作って入れる
		CharacterDialogue dialogue = new CharacterDialogue(characterId, actionType, dialogueText);
		dialogue.setConditionKey(conditionKey);
		characterDialogueRepository.save(dialogue);// 倉庫にこのセリフデータを保存
		return "redirect:/admin/dialogues";// 上の処理が終わったら、このURLにアクセスし直す（処理変更後の画面にするため）
	}

	@PostMapping("/admin/dialogues/{id}/delete") // このURLにpostで注文が来たら、以下の処理を行う

	// @PathVariable（"URLの"中の値（id）を取り出して、処理の中で使えるようにする（係））
	public String deleteDialogue(@PathVariable Long id) {

		// characterDialogueRepository（倉庫番）の deleteById（自動1件削除）を使い、受け取ったidのセリフを消す
		characterDialogueRepository.deleteById(id);

		return "redirect:/admin/dialogues";// 上の処理が終わったら、このURLにアクセスし直す（処理変更後の画面にするため）
	}
}
