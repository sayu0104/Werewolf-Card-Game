package com.example.werewolf.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.werewolf.entity.Vote;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class VoteCountServiceTest { // 投票集計ロジックのテスト

	private final VoteCountService voteCountService = new VoteCountService();

	@Test // 「これは1つのテストだよ」という目印
	void 単独最多の場合は1人だけ返る() {
		List<Vote> votes = new ArrayList<>();
		// 準備1：Vote（投票）を並べて入れる、votesという新しい空の箱を用意する
		
		votes.add(new Vote(1L, 2, 1L, 2L)); // 1番 → 2番
		votes.add(new Vote(1L, 2, 3L, 2L)); // 3番 → 2番
		votes.add(new Vote(1L, 2, 4L, 2L)); // 4番 → 2番
		votes.add(new Vote(1L, 2, 5L, 3L)); // 5番 → 3番
		// 準備2：票を何枚か入れる
		
		// 例：new Vote(1L, 2, 1L, 2L)
		// 「1L」 … gameId（試合1）
		// 「2」 … dayNumber（2日目）
		// 「1L」 … voter（1番の人が）
		// 「2L」 … target（2番の人に投票）
		
		// ※Vote(gameId, dayNumber, voterGamePlayerId, targetGamePlayerId)

		List<Long> result = voteCountService.findMostVoted(votes);
		// 実行：集計する
		// 投票集計係に、votes（票）を渡して、最多得票者を見つけてもらう
		// その結果を result に受け取る
		
		// voteCountService … 道具 (new VoteCountService() で作った「投票集計の係」)
		// .findMostVoted(...) … その係に「最多得票者を見つけて」とお願いする
		
		// find	   most	   voted
		// 見つける	最も	  投票された

		assertThat(result).containsExactly(2L); // 結果は [2] だけ
		// 確認：結果が想定通りか
	}

	@Test
	void 同数最多の場合は全員返る() {
		List<Vote> votes = new ArrayList<>();
		votes.add(new Vote(1L, 2, 1L, 2L));  // 1番 → 2番
		votes.add(new Vote(1L, 2, 3L, 2L));  // 3番 → 2番
		votes.add(new Vote(1L, 2, 4L, 3L));  // 4番 → 3番
		votes.add(new Vote(1L, 2, 5L, 3L));  // 5番 → 3番

		List<Long> result = voteCountService.findMostVoted(votes);

		assertThat(result).containsExactlyInAnyOrder(2L, 3L);
		// containsExactlyInAnyOrder(2L, 3L) … 「2, 3 を順不同できっかり」（順番は問わない）
		// InAnyOrder … in any order（どんな順でも）
		
		// ※Mapから集めた結果は、順番が保証されない。
		//  mostVotedに2番が先か3番が先か入るかは、Mapの都合で決まって予測できない
		//  そのため、両方入ってればOK
	}

	@Test
	void 票が全部同じ相手なら1人だけ返る() {
		List<Vote> votes = new ArrayList<>();
		votes.add(new Vote(1L, 2, 1L, 2L));  // 1番 → 2番
		votes.add(new Vote(1L, 2, 3L, 2L));  // 3番 → 2番
		votes.add(new Vote(1L, 2, 4L, 2L));  // 4番 → 2番

		List<Long> result = voteCountService.findMostVoted(votes);

		assertThat(result).containsExactly(2L);  // [2] だけ
		// 投票先は、1番→2番、3番→2番、4番→2番（全員2番に）
	}

	@Test
	void 票が空リストなら空リストを返す() {
		List<Vote> votes = new ArrayList<>(); // 票が1枚も無い

		List<Long> result = voteCountService.findMostVoted(votes);

		assertThat(result).isEmpty(); // 結果も空
		// isEmpty() … 空っぽか?
		
		// ※入力が空でも、エラーで落ちずに空を返す
		// 予想外の入力（空）でプログラムが壊れないか、変な入力でも安全か
	}
}
