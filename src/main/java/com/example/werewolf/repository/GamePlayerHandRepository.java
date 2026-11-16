package com.example.werewolf.repository;

import com.example.werewolf.entity.GamePlayerHand;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

// GamePlayerHandRepository … プレイヤーの手札のデータを管理する係（倉庫番）

// interface（インターフェース） … 「こういう機能があるよ」という約束だけを書くもの（中身の処理は書かない）
// extends JpaRepository<GamePlayerHand, Long> … 万能キットを受け継ぐ
// これだけで「全件取得・削除・保存」などの基本操作が自動で使えるようになる

public interface GamePlayerHandRepository extends JpaRepository<GamePlayerHand, Long> {
	boolean existsByGamePlayerIdAndCardIdAndStatus(Long gamePlayerId, Long cardId, String status);
	// 「このプレイヤーの このカードが この状態で 存在するか？」 を true か false で返す

	List<GamePlayerHand> findByGamePlayerIdAndStatus(Long gamePlayerId, String status);
	// 「このプレイヤーの この状態の手札」 を探して、その結果を GamePlayerHand のリストの箱に入れて返す
}
