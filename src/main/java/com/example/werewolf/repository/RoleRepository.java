package com.example.werewolf.repository;

import com.example.werewolf.entity.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

// RoleRepository ＝ roles のデータを出し入れする倉庫番
// interface（インターフェース）＝「こういう機能があるよ」という約束だけを書くもの（中身の処理は書かない）
// extends JpaRepository<Role, Long> ＝ 万能キットを受け継ぐ。Roleを、Long型のid で管理する。
// これだけで「全件取得・削除・保存」などの基本操作が自動で使えるようになる

public interface RoleRepository extends JpaRepository<Role, Long> {
	Optional<Role> findByName(String name);
	// 「名前で探す」という新しい命令を、倉庫番に1つ追加した
}
