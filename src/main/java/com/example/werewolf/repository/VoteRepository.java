package com.example.werewolf.repository;

import com.example.werewolf.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

//VoteRepository ＝ votes のデータを出し入れする倉庫番
//interface（インターフェース）＝「こういう機能があるよ」という約束だけを書くもの（中身の処理は書かない）
//extends JpaRepository<Vote, Long> ＝ 万能キットを受け継ぐ。Voteを、Long型のid で管理する。
//これだけで「全件取得・削除・保存」などの基本操作が自動で使えるようになる
// countByGameIdAndDayNumber(gameId, dayNumber)
// ＝そのゲームID かつ その日付」の投票（Vote）が、何件あるかを数える
// 「その日もう投票したか」を数えて、1日1回だけにする（二重投票防止）

public interface VoteRepository extends JpaRepository<Vote, Long> {

	long countByGameIdAndDayNumber(Long gameId, Integer dayNumber);
}
