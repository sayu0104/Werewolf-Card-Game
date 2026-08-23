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
@Entity
@Table(name = "characters")
public class Character {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String personality;

    @Column(nullable = false)
    private Integer optimality;

    @Column(nullable = false)
    private Integer consistency;

    @Column(name = "deception_skill", nullable = false)
    private Integer deceptionSkill;

    // JPAが利用するための引数なしコンストラクタ
    public Character() {
    }

    public Character(String name, Integer optimality, Integer consistency, Integer deceptionSkill) {
        this.name = name;
        this.optimality = optimality;
        this.consistency = consistency;
        this.deceptionSkill = deceptionSkill;
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

    public String getPersonality() {
        return personality;
    }

    public void setPersonality(String personality) {
        this.personality = personality;
    }

    public Integer getOptimality() {
        return optimality;
    }

    public void setOptimality(Integer optimality) {
        this.optimality = optimality;
    }

    public Integer getConsistency() {
        return consistency;
    }

    public void setConsistency(Integer consistency) {
        this.consistency = consistency;
    }

    public Integer getDeceptionSkill() {
        return deceptionSkill;
    }

    public void setDeceptionSkill(Integer deceptionSkill) {
        this.deceptionSkill = deceptionSkill;
    }
}
