package com.example.werewolf.repository;

import com.example.werewolf.entity.RoleGuaranteedCard;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleGuaranteedCardRepository extends JpaRepository<RoleGuaranteedCard, Long> {
	List<RoleGuaranteedCard> findByRoleId(Long roleId);
}
