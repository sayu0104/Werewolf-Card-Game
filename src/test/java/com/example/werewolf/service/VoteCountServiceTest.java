package com.example.werewolf.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.werewolf.entity.Vote;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class VoteCountServiceTest {

	private final VoteCountService voteCountService = new VoteCountService();

	@Test
	void 単独最多の場合は1人だけ返る() {
		List<Vote> votes = new ArrayList<>();
		votes.add(new Vote(1L, 2, 1L, 2L));
		votes.add(new Vote(1L, 2, 3L, 2L));
		votes.add(new Vote(1L, 2, 4L, 2L));
		votes.add(new Vote(1L, 2, 5L, 3L));

		List<Long> result = voteCountService.findMostVoted(votes);

		assertThat(result).containsExactly(2L);
	}

	@Test
	void 同数最多の場合は全員返る() {
		List<Vote> votes = new ArrayList<>();
		votes.add(new Vote(1L, 2, 1L, 2L));
		votes.add(new Vote(1L, 2, 3L, 2L));
		votes.add(new Vote(1L, 2, 4L, 3L));
		votes.add(new Vote(1L, 2, 5L, 3L));

		List<Long> result = voteCountService.findMostVoted(votes);

		assertThat(result).containsExactlyInAnyOrder(2L, 3L);
	}

	@Test
	void 票が全部同じ相手なら1人だけ返る() {
		List<Vote> votes = new ArrayList<>();
		votes.add(new Vote(1L, 2, 1L, 2L));
		votes.add(new Vote(1L, 2, 3L, 2L));
		votes.add(new Vote(1L, 2, 4L, 2L));

		List<Long> result = voteCountService.findMostVoted(votes);

		assertThat(result).containsExactly(2L);
	}

	@Test
	void 票が空リストなら空リストを返す() {
		List<Vote> votes = new ArrayList<>();

		List<Long> result = voteCountService.findMostVoted(votes);

		assertThat(result).isEmpty();
	}
}
