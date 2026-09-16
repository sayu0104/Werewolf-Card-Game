package com.example.werewolf.repository;

import com.example.werewolf.entity.NightAction;
import org.springframework.data.jpa.repository.JpaRepository;

// 夜に行動する役職の倉庫（全件取得、削除、保存ができる）
public interface NightActionRepository extends JpaRepository<NightAction, Long> {
}
