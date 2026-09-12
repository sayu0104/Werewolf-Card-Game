package com.example.werewolf.repository;

import com.example.werewolf.entity.GamePlayer;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

// GamePlayerRepository ＝ game_players のデータを出し入れする倉庫番
// interface（インターフェース）＝「こういう機能があるよ」という約束だけを書くもの（中身の処理は書かない）
// extends JpaRepository<GamePlayer, Long> ＝ 万能キットを受け継ぐ。GamePlayerを、Long型のid で管理する。
// これだけで「全件取得・削除・保存」などの基本操作が自動で使えるようになる
// findByGameId(Long gameId) … ゲームIDでも探せる（ゲームIDは「複数ヒットする」からListで受ける）
// findByGameIdOrderBySeatOrderAsc … 「ゲームIDで探して、席順で昇順(1→5)に並べて返す」

public interface GamePlayerRepository extends JpaRepository<GamePlayer, Long> {
	List<GamePlayer> findByGameId(Long gameId);
	List<GamePlayer> findByGameIdOrderBySeatOrderAsc(Long gameId);
}
