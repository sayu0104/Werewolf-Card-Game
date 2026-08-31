package com.example.werewolf.service;

import com.example.werewolf.entity.GamePlayer;
import com.example.werewolf.entity.Role;
import com.example.werewolf.repository.GamePlayerRepository;
import com.example.werewolf.repository.RoleRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RoleAssignmentService {

	private static final List<String> ROLE_NAMES = List.of("人狼", "占い師", "狩人", "狂人", "村人", "村人");

	private final RoleRepository roleRepository;
	private final GamePlayerRepository gamePlayerRepository;

	public RoleAssignmentService(RoleRepository roleRepository, GamePlayerRepository gamePlayerRepository) {
		this.roleRepository = roleRepository;
		this.gamePlayerRepository = gamePlayerRepository;
	}

	public List<GamePlayer> assignRoles(List<GamePlayer> players) {
		if (players.size() != ROLE_NAMES.size()) {
			throw new IllegalArgumentException("プレイヤー人数が想定と異なる: " + players.size());
		}

		List<Long> roleIds = new ArrayList<>();
		for (String roleName : ROLE_NAMES) {
			Role role = roleRepository.findByName(roleName)
					.orElseThrow(() -> new IllegalStateException("役職が見つからない: " + roleName));
			roleIds.add(role.getId());
		}
		Collections.shuffle(roleIds);

		for (int i = 0; i < players.size(); i++) {
			players.get(i).setRoleId(roleIds.get(i));
		}

		return gamePlayerRepository.saveAll(players);
	}
}
