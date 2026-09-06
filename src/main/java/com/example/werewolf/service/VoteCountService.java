package com.example.werewolf.service;

import com.example.werewolf.entity.Vote;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class VoteCountService { // 投票集計ロジック

	public List<Long> findMostVoted(List<Vote> votes) {
		
		// １．票を数える
		Map<Long, Long> countByTarget = new HashMap<>();
		
		// Map<Long, Long> countByTarget … 「投票された人のId → 票数」の対応表（辞書）
		// Map … 対応表（辞書） 名前と値をペアで持つ箱
		// <Long, Long> … 探すキー　キーもLong（プレイヤーId）、値もLong（票数）
		
		for (Vote vote : votes) {
			// for (Vote vote : votes) … 票を1枚ずつ見る（:のfor）
			
			countByTarget.merge(vote.getTargetGamePlayerId(), 1L, Long::sum);
			
			// .merge … 数え上げる道具(マージ ＝ 合体・統合)
			// .getTargetGamePlayerId() … その票の投票された人（target）に
			// 1L … Long型の1
			// Long::sum … 足し算する（1ずつ足してる）
			
			// 例：「5番に3票、8番に1票」なら {5→3, 8→1}という対応表ができる
		}

		// ２．最大の票数を探す
		long maxCount = 0;
		// long maxCount = 0 … 「今までの最大票数」を覚える箱。最初は0
		
		for (long count : countByTarget.values()) {
			//┗━━受け皿━━┛    ┗━━取り出す元━━┛
			
			// 票数を1個ずつ count に入れながら、くり返す
			
			// long count … 取り出した票数を受け取る箱
			// : …　「〜の中から、1個ずつ」（英語の for each 〜 in の "in"）
			// countByTarget.values() … 対応表から値（票数）だけを取り出す（誰が、は今いらないから）
			
			if (count > maxCount) {
				maxCount = count;
				// if (count > maxCount) { … もし今見ている票数が、記録より大きかったら…
				// maxCount = count …　記録を更新
			}
		}

		// ３．最大票の人を全員集める
		List<Long> mostVoted = new ArrayList<>();
		// Long（数字）を並べて入れる、mostVotedという新しい空の箱を用意する
		
		// List<Long> … Long（数字）を並べて入れる箱
		// mostVoted … 名前（箱の呼び名） 「最も投票された」
		// = new ArrayList<>() … 新しい空っぽの箱を作る
		
		for (Map.Entry<Long, Long> entry : countByTarget.entrySet()) {
			//    ┗━━━━ 受け皿 ━━━━┛             ┗━━ 取り出す元 ━━┛
			
			// 対応表のペアを、1組ずつ entry に入れながら回す
			
			// entry（エントリー） … 1件のデータ、1組
			// Map.Entry …　「Mapのペア1組」を表す型
			//  : …　〜の中から、1個ずつ
			// countByTarget.entrySet() … ペア（キーと値　両方）
			
			// 対応表から「ペア（キー＋値）」を取り出す　（"誰が"も"何票か"も両方要るから）
			
			if (entry.getValue() == maxCount) {
				mostVoted.add(entry.getKey());
				
				// if (entry.getValue() == maxCount) … この人の票数が、最大票数（例：3）と同じか?
				// .add(entry.getKey()) … 同じなら、その人のId を箱に追加
				
				// entry.getValue() … そのペアの値（票数）を取り出す
				// == maxCount … 最大票数と同じか？
				// mostVoted.add … なら、箱に追加する
				// entry.getKey() … そのペアのキー（人のId）だけを取り出して
			}
		}

		return mostVoted;
		// 詰め終わった箱を返す
	}
}
