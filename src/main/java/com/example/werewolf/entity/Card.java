package com.example.werewolf.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * docs/database-design.md の cards テーブルに対応するエンティティ。
 */
@Entity // このクラスはDBと連携している
@Table(name = "cards") // javaでは「Card」、DBでは「cards」と扱い、保存すること
public class Card {

	@Id // 以下の変数に対してidが主キーであることを示す
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 自動的に値を生成すること（方法はDBにある自動採番機能）
	private Long id;

	@Column(nullable = false, length = 50) // 以下の変数に対して
	private String name;

	@Column(name = "card_type", nullable = false, length = 20)
	private String cardType;// カードタイプ（種類）

	@Column(name = "required_role_id")
	private Long requiredRoleId;// 必要な役職id

	@Column(nullable = false)
	private Integer cost;// コスト（カード使用時に消費）

	@Column(name = "is_mandatory", nullable = false)
	private Boolean isMandatory = false;// 強制使用カードか？＝いいえ

	@Column(name = "is_rare", nullable = false)
	private Boolean isRare = false;// 希少カード（レア）か？＝いいえ

	@Column
	private Integer value;// カードが持つ価値（値）

	@Column(name = "is_usage_public", nullable = false)
	private Boolean isUsagePublic = true;// カードを使用したか、他の人に見えるか？＝はい

	@Column(columnDefinition = "TEXT") // DBではこの列をこの型にして（TEXT型(長い文章が入る型)）
	private String description;// カード説明

	// JPAが利用するための引数なしコンストラクタ
	public Card() {
	}

	public Card(String name, String cardType, Integer cost) {// 新規作成時に最低限必要な項目
		this.name = name;
		this.cardType = cardType;// カードタイプ
		this.cost = cost;// カードコスト
	}

	// get（外から中の値を「読み取る」ための窓口） private＝非公開のため窓口越しに見る
	public Long getId() {
		return id;// idの値を返す（教える）
	}

	public String getName() {
		return name;
	}

	// set（外から中の値を「書き換える」ための窓口）
	public void setName(String name) {
		this.name = name;// 台帳と、それに記入する（紙）
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public Long getRequiredRoleId() {
		return requiredRoleId;// 必要な役職id
	}

	public void setRequiredRoleId(Long requiredRoleId) {
		this.requiredRoleId = requiredRoleId;
	}

	public Integer getCost() {
		return cost;
	}

	public void setCost(Integer cost) {
		this.cost = cost;
	}

	public Boolean getIsMandatory() {
		return isMandatory;
	}

	public void setIsMandatory(Boolean isMandatory) {
		this.isMandatory = isMandatory;
	}

	public Boolean getIsRare() {
		return isRare;
	}

	public void setIsRare(Boolean isRare) {
		this.isRare = isRare;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public Boolean getIsUsagePublic() {
		return isUsagePublic;
	}

	public void setIsUsagePublic(Boolean isUsagePublic) {
		this.isUsagePublic = isUsagePublic;
	}

	public String getDescription() {
		return description;// カード説明
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
