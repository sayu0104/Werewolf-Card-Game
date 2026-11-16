package com.example.werewolf.repository;

import com.example.werewolf.entity.RoleGuaranteedCard;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

// RoleGuaranteedCardRepository … 役職ごとに毎ターン保証されているカードのデータを管理する係（倉庫番）

// interface（インターフェース） … 「こういう機能があるよ」という約束だけを書くもの（中身の処理は書かない）
// extends JpaRepository<RoleGuaranteedCard, Long> … 万能キットを受け継ぐ
// これだけで「全件取得・削除・保存」などの基本操作が自動で使えるようになる

public interface RoleGuaranteedCardRepository extends JpaRepository<RoleGuaranteedCard, Long> {
	List<RoleGuaranteedCard> findByRoleId(Long roleId);
	// 役職IDで探して、 「RoleGuaranteedCard」 のリストの箱に入れて返す
}
