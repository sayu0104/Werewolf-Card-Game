package com.example.werewolf.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * docs/database-design.md の character_dialogues テーブルに対応するエンティティ。
 */
@Entity
@Table(name = "character_dialogues")
public class CharacterDialogue {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "character_id", nullable = false)
	private Long characterId;// どのキャラのセリフか（characters の id。ただのLong）

	@Column(name = "action_type", nullable = false, length = 20)
	private String actionType;// どんな行動のときのセリフか（"占い" "襲撃" など）

	@Column(name = "condition_key", length = 50)
	private String conditionKey;// どんな状況のときか（条件のキー。空でもOK＝nullable指定なし）

	@Column(name = "dialogue_text", nullable = false, columnDefinition = "TEXT")
	private String dialogueText;// 実際のセリフ本文（TEXT型＝長い文章OK）

	// JPAが利用するための引数なしコンストラクタ
	public CharacterDialogue() {
	}

	// 「characterId という名前の、一時的な受け皿」を用意 外から渡された値が、ここに入る↓
	public CharacterDialogue(Long characterId, String actionType, String dialogueText) {
		this.characterId = characterId;
		this.actionType = actionType;
		this.dialogueText = dialogueText;
	}

	// get（外から中の値を「読み取る」ための窓口） private＝非公開のため窓口越しに見る
	public Long getId() {
		return id;
	}

	public Long getCharacterId() {
		return characterId;// どのキャラのセリフか（characters の id。ただのLong）
	}

	// set（外から中の値を「書き換える」ための窓口）
	public void setCharacterId(Long characterId) {
		this.characterId = characterId;
	}

	public String getActionType() {
		return actionType;// どんな行動のときのセリフか（"占い" "襲撃" など）
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public String getConditionKey() {
		return conditionKey;// どんな状況のときか（条件のキー。空でもOK＝nullable指定なし）
	}

	public void setConditionKey(String conditionKey) {
		this.conditionKey = conditionKey;
	}

	public String getDialogueText() {
		return dialogueText;// 実際のセリフ本文（TEXT型＝長い文章OK）
	}

	public void setDialogueText(String dialogueText) {
		this.dialogueText = dialogueText;
	}
}
