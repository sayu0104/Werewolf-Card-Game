package com.example.werewolf.repository;

import com.example.werewolf.entity.Character;
import org.springframework.data.jpa.repository.JpaRepository;

//CharacterRepository ＝ characters のデータを出し入れする倉庫番
//interface（インターフェース）＝「こういう機能があるよ」という約束だけを書くもの（中身の処理は書かない）
//extends JpaRepository<Character, Long> ＝ 万能キットを受け継ぐ。Characterを、Long型のid で管理する。
//これだけで「全件取得・削除・保存」などの基本操作が自動で使えるようになる

public interface CharacterRepository extends JpaRepository<Character, Long> {
}
