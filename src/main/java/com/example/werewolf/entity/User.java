package com.example.werewolf.entity;//このファイルの住所

import jakarta.persistence.Column;//使う道具を他のところから持ち込む
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

/**
 * docs/database-design.md の users テーブルに対応するエンティティ。
 */

//★このクラス = 「会員カードの設計図」。
//new User(...) で、実物のカードを1枚ずつ発行する（実物ごとに名前やメールは別の値）。
//this = 今まさに処理されている、その実物カード1枚自身のこと。

@Entity//このクラスはデータベースのテーブルと連携している
@Table(name = "users")//この世界（java）では「User」と呼ぶが、データベースの世界では「users」というテーブル名で保存すること（DBでは小文字＆複数形の users にする（設計書の命名ルールに合わせるため））
public class User {//このクラス（User）を他のパッケージからでもアクセスできるように公開する

    @Id//フィールド（変数）に対して、IDが主キー（鍵）であることを示す
    @GeneratedValue(strategy = GenerationType.IDENTITY)//自動的に値を生成すること（方法＝IDENTITY（データベースにある自動採番機能））
    private Long id;//非公開、数値専用の型（nullを使える）、変数名はid

    @Column(nullable = false, unique = true, length = 50)//以下のフィールド対して、データベースの列の項目（名前は空欄不可、重複不可、５０文字以内）
    private String username;

    @Column(name = "password_hash", nullable = false, length = 255)//（名前は"password_hash"、空欄不可、２５５文字以内）
    private String passwordHash;

    @Column(unique = true, length = 255)//（重複不可、２５５文字以内）
    private String email;

    @Column(name = "is_admin", nullable = false)
    private Boolean isAdmin = false;

    @Column(name = "created_at", nullable = false)//（名前は"created_at"、空欄不可）
    private LocalDateTime createdAt;

    // JPAが利用するための引数なしコンストラクタ
    public User() {
    }//空っぽ

 // 新しいUser（実物カード）を1枚作る手順。受け取った値を、この実物自身の欄に書き写す
    public User(String username, String passwordHash, String email) {
        this.username = username;// 受け取った名前を、この実物の名前欄に入れる
        this.passwordHash = passwordHash;
        this.email = email;
        this.createdAt = LocalDateTime.now();//　作成日時だけは、作った瞬間の時刻を自動で入れる
    }

 // 中の値を外から「読み取る」窓口（フィールドはprivate＝非公開なので、この窓口ごしに読む）
    public Long getId() {
        return id;//　idの値を返す（＝教える）
    }

    public String getUsername() {
        return username;
    }

 // 中の値を外から「書き換える」窓口
    public void setUsername(String username) {
        this.username = username;//　この実物の名前欄を、受け取った新しい名前に書き換える
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;//
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

 // ※ id と createdAt には「書き換え窓口（setter）」をあえて作っていない
//  → DBが決める値／作った瞬間に確定する値なので、後から書き換えさせないため
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
