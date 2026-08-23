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
@Entity
@Table(name = "cards")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(name = "card_type", nullable = false, length = 20)
    private String cardType;

    @Column(name = "required_role_id")
    private Long requiredRoleId;

    @Column(nullable = false)
    private Integer cost;

    @Column(name = "is_mandatory", nullable = false)
    private Boolean isMandatory = false;

    @Column(name = "is_rare", nullable = false)
    private Boolean isRare = false;

    @Column
    private Integer value;

    @Column(name = "is_usage_public", nullable = false)
    private Boolean isUsagePublic = true;

    @Column(columnDefinition = "TEXT")
    private String description;

    // JPAが利用するための引数なしコンストラクタ
    public Card() {
    }

    public Card(String name, String cardType, Integer cost) {
        this.name = name;
        this.cardType = cardType;
        this.cost = cost;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public Long getRequiredRoleId() {
        return requiredRoleId;
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
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
