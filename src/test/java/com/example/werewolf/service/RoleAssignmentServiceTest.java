package com.example.werewolf.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.werewolf.entity.GamePlayer;
import com.example.werewolf.entity.Role;
import com.example.werewolf.repository.RoleRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class RoleAssignmentServiceTest {

	@Autowired
	private RoleAssignmentService roleAssignmentService;

	@Autowired
	private RoleRepository roleRepository;

	private List<GamePlayer> createSixPlayers() {
		List<GamePlayer> players = new ArrayList<>();
		for (int i = 1; i <= 6; i++) {
			players.add(new GamePlayer(1L, null, i));
		}
		return players;
	}

	@Test
	void 六人渡すと全員に役職が割り当てられる() {
		List<GamePlayer> players = createSixPlayers();

		List<GamePlayer> result = roleAssignmentService.assignRoles(players);

		assertThat(result).hasSize(6);
		for (GamePlayer player : result) {
			assertThat(player.getRoleId()).isNotNull();
		}
	}

	@Test
	void 役職の内訳が想定通りになる() {
		List<GamePlayer> players = createSixPlayers();

		List<GamePlayer> result = roleAssignmentService.assignRoles(players);

		Map<String, Long> countByName = new HashMap<>();
		for (GamePlayer player : result) {
			Role role = roleRepository.findById(player.getRoleId()).orElseThrow();
			countByName.merge(role.getName(), 1L, Long::sum);
		}

		assertThat(countByName.get("人狼")).isEqualTo(1);
		assertThat(countByName.get("占い師")).isEqualTo(1);
		assertThat(countByName.get("狩人")).isEqualTo(1);
		assertThat(countByName.get("狂人")).isEqualTo(1);
		assertThat(countByName.get("村人")).isEqualTo(2);
	}

	@Test
	void 複数回実行すると割り当てが固定ではない() {
		List<Long> firstResult = null;
		boolean foundDifferentOrder = false;

		for (int i = 0; i < 20; i++) {
			List<GamePlayer> players = createSixPlayers();
			List<GamePlayer> result = roleAssignmentService.assignRoles(players);
			List<Long> roleIds = result.stream().map(GamePlayer::getRoleId).toList();

			if (firstResult == null) {
				firstResult = roleIds;
			} else if (!firstResult.equals(roleIds)) {
				foundDifferentOrder = true;
				break;
			}
		}

		assertThat(foundDifferentOrder).isTrue();
	}
}
