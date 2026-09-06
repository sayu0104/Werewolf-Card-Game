package com.example.werewolf.service;

import com.example.werewolf.entity.Vote;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class VoteCountService {

	public List<Long> findMostVoted(List<Vote> votes) {
		Map<Long, Long> countByTarget = new HashMap<>();
		for (Vote vote : votes) {
			countByTarget.merge(vote.getTargetGamePlayerId(), 1L, Long::sum);
		}

		long maxCount = 0;
		for (long count : countByTarget.values()) {
			if (count > maxCount) {
				maxCount = count;
			}
		}

		List<Long> mostVoted = new ArrayList<>();
		for (Map.Entry<Long, Long> entry : countByTarget.entrySet()) {
			if (entry.getValue() == maxCount) {
				mostVoted.add(entry.getKey());
			}
		}

		return mostVoted;
	}
}
