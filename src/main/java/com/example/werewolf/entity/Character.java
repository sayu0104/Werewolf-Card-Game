package com.example.werewolf.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * docs/database-design.md の characters テーブルに対応するエンティティ。
 */
@Entity // このクラスはDBと連携している
@Table(name = "characters") // Javaでは「Character」と呼ぶが、DBでは「characters」というテーブル名で保存すること（設計書の命名ルールに合わせるため）
public class Character {

	@Id // 以下の変数に対してidが主キー
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 自動的に値を生成すること（方法はDBにある自動採番機能と使う）
	private Long id;

	@Column(nullable = false, length = 50) // 以下の変数に対して、DBの列の項目（空欄×、文字数５０文字以内）
	private String name;

	@Column(columnDefinition = "TEXT") // DBではこの列をこの型にして（TEXT型(長い文章が入る型)）
	private String personality;// 性格

	@Column(nullable = false)
	private Integer optimality;// 判断の最適性(Integer 0~100)

	@Column(nullable = false)
	private Integer consistency;// 一貫性(Integer 0~100)

	@Column(name = "deception_skill", nullable = false) // DBではこの列を「deception_skill」という名前にする
	private Integer deceptionSkill;// 騙す能力

	// JPAが利用するための引数なしコンストラクタ
	public Character() {
	}

	// ↓（）の中 「nameという名前の一時的な受け皿を用意」外から渡された値がここに入る（受け取った値を一時的に持っておく"紙"）
	public Character(String name, Integer optimality, Integer consistency, Integer deceptionSkill) {// 新規作成に最低限必要な変数だけ受け取る
		this.name = name;
		this.optimality = optimality;// 判断の最適性
		this.consistency = consistency;// 一貫性
		this.deceptionSkill = deceptionSkill;// 騙す能力
	}

	// 外から中の値を「読み取るため」の窓口（フィールドはprivate＝非公開なので、この窓口ごしに読む）（フィールドはprivate、窓口はpublic）
	public Long getId() {
		return id;// idの値を返す（＝教える）
	}

	public String getName() {
		return name;
	}

	// 外から中の値を「書き換える」窓口
	public void setName(String name) {
		this.name = name;// 名前欄（台帳）に、受け取った新しい名前に書き換える
	}

	public String getPersonality() {
		return personality;// 性格
	}

	public void setPersonality(String personality) {
		this.personality = personality;
	}

	public Integer getOptimality() {
		return optimality;// 判断の最適性
	}

	public void setOptimality(Integer optimality) {
		this.optimality = optimality;
	}

	public Integer getConsistency() {
		return consistency;// 一貫性
	}

	public void setConsistency(Integer consistency) {
		this.consistency = consistency;
	}

	public Integer getDeceptionSkill() {
		return deceptionSkill;// 騙す能力
	}

	public void setDeceptionSkill(Integer deceptionSkill) {
		this.deceptionSkill = deceptionSkill;
	}
}
