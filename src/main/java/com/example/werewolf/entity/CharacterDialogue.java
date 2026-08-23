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
    private Long characterId;

    @Column(name = "action_type", nullable = false, length = 20)
    private String actionType;

    @Column(name = "condition_key", length = 50)
    private String conditionKey;

    @Column(name = "dialogue_text", nullable = false, columnDefinition = "TEXT")
    private String dialogueText;

    // JPAが利用するための引数なしコンストラクタ
    public CharacterDialogue() {
    }

    public CharacterDialogue(Long characterId, String actionType, String dialogueText) {
        this.characterId = characterId;
        this.actionType = actionType;
        this.dialogueText = dialogueText;
    }

    public Long getId() {
        return id;
    }

    public Long getCharacterId() {
        return characterId;
    }

    public void setCharacterId(Long characterId) {
        this.characterId = characterId;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getConditionKey() {
        return conditionKey;
    }

    public void setConditionKey(String conditionKey) {
        this.conditionKey = conditionKey;
    }

    public String getDialogueText() {
        return dialogueText;
    }

    public void setDialogueText(String dialogueText) {
        this.dialogueText = dialogueText;
    }
}
