package com.example.werewolf.entity;//ファイルの住所

import jakarta.persistence.Column;//使う道具を他のところから持ち込む
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * docs/database-design.md の roles テーブルに対応するエンティティ。
 */
@Entity // このクラスはDBと連携している
@Table(name = "roles") // javaでは「Role」と呼ぶが、DBでは「roles」と呼ぶこと（DBでは小文字＆複数形にしたいため（設計書命名のルールに合わせるため））
public class Role {// このクラスは公開する

	@Id // フィールド（変数）に対して、IDが主キー（鍵）であることを示す
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 値は自動的に生成すること（方法＝IDENTITY（自動採番機能））
	private Long id;

	@Column(nullable = false, unique = true, length = 20) // 以下のフィールドに対して（空欄不可、重複不可、２０文字以内）
	private String name;

	@Column(nullable = false, length = 20) // （空欄不可、２０文字以内）
	private String faction;

	@Column(columnDefinition = "TEXT") // （DBの型を使う＝"TEXT"（文字数無制限の文字列の型））
	private String description;

	// JPAが利用するための引数なしコンストラクタ
	public Role() {
	}

	// ── このファイルの自分用メモ ──
//  台帳 = このオブジェクト自身の欄（フィールド、this.付き）
//  紙   = 引数として受け取った一時的な値（this.なし）

	public Role(String name, String faction, String description) {// 以下の項目を文字列で台帳に記入
		this.name = name;
		this.faction = faction;
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;// 台帳の名前欄を読み取って、外に返す（ゲッター＝読む窓口）
	}

	public void setName(String name) {
		this.name = name;// 受け取った値を台帳の名前欄に書き込む（セッター＝書く窓口）
	}

	public String getFaction() {// 派閥
		return faction;
	}

	public void setFaction(String faction) {
		this.faction = faction;
	}

	public String getDescription() {// 説明
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
