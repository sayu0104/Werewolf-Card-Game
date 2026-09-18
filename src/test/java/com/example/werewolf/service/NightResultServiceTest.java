package com.example.werewolf.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.werewolf.entity.Game;
import com.example.werewolf.entity.GamePlayer;
import com.example.werewolf.entity.NightAction;
import com.example.werewolf.repository.GamePlayerRepository;
import com.example.werewolf.repository.NightActionRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class NightResultServiceTest {

	@Autowired
	private NightResultService nightResultService;

	@Autowired
	private GameStartService gameStartService;

	@Autowired
	private GamePlayerRepository gamePlayerRepository;

	@Autowired
	private NightActionRepository nightActionRepository;

	@Test
	void 襲撃対象と護衛対象が違うと襲撃対象が死亡する() {
		Game game = gameStartService.startGame();
		List<GamePlayer> players = gamePlayerRepository.findByGameId(game.getId());

		// 準備１：襲撃される人、護衛される人、襲撃する人、護衛する人の4人を用意する
		GamePlayer attackTarget = players.get(0); // 襲撃される人
		GamePlayer guardTarget = players.get(1); // 護衛される人（別人）
		GamePlayer attacker = players.get(2); // 襲撃する人
		GamePlayer guarder = players.get(3); // 護衛する人

		// 準備2：行動の記録を保存する
		nightActionRepository.save(
				new NightAction(game.getId(), game.getDayNumber(), attacker.getId(), "襲撃", attackTarget.getId()));
		nightActionRepository.save(
				new NightAction(game.getId(), game.getDayNumber(), guarder.getId(), "護衛", guardTarget.getId()));

		nightResultService.resolve(game);
		// 実行：resolve で「決着させる」（襲撃と護衛をさせる）

		GamePlayer resultTarget = gamePlayerRepository.findById(attackTarget.getId()).orElseThrow();
		assertThat(resultTarget.getIsAlive()).isFalse();
		// 結果：襲撃された人を取ってきて（なければエラー）、生存が false（死亡） か確かめる
	}

	@Test
	void 襲撃対象と護衛対象が同じだと襲撃対象は死亡しない() {
		Game game = gameStartService.startGame();
		List<GamePlayer> players = gamePlayerRepository.findByGameId(game.getId());

		// 準備1：襲撃と護衛の両方をされる人、襲撃する人、護衛する人の3人を用意する
		GamePlayer target = players.get(0);
		GamePlayer attacker = players.get(1);
		GamePlayer guarder = players.get(2);

		// 準備2：行動の記録を保存する
		nightActionRepository
				.save(new NightAction(game.getId(), game.getDayNumber(), attacker.getId(), "襲撃", target.getId()));
		nightActionRepository
				.save(new NightAction(game.getId(), game.getDayNumber(), guarder.getId(), "護衛", target.getId()));

		nightResultService.resolve(game);
		// 実行：resolve で「決着させる」（襲撃と護衛をさせる）

		GamePlayer resultTarget = gamePlayerRepository.findById(target.getId()).orElseThrow();
		assertThat(resultTarget.getIsAlive()).isTrue();
		// 結果：襲撃された人のデータを倉庫からとってきて（なければエラー）、生存が true（生存） か確かめる
		// 襲撃先と護衛先が同じな場合、護衛が優先され、プレイヤーは生存している
	}

	@Test
	void 護衛が無いと襲撃対象が死亡する() {
		Game game = gameStartService.startGame();
		List<GamePlayer> players = gamePlayerRepository.findByGameId(game.getId());

		// 準備1：襲撃される人、襲撃する人の2人を用意する
		GamePlayer target = players.get(0);
		GamePlayer attacker = players.get(1);

		// 準備2：行動の記録を保存する
		nightActionRepository
				.save(new NightAction(game.getId(), game.getDayNumber(), attacker.getId(), "襲撃", target.getId()));

		nightResultService.resolve(game);
		// 実行：resolve で「決着させる」（襲撃のみ、させる）

		GamePlayer resultTarget = gamePlayerRepository.findById(target.getId()).orElseThrow();
		assertThat(resultTarget.getIsAlive()).isFalse();
		// 結果：襲撃された人のデータを倉庫からとってきて（なければエラー）、生存が false（死亡） か確かめる
	}

	@Test
	void 襲撃が無いと誰も死なない() {
		Game game = gameStartService.startGame();
		List<GamePlayer> players = gamePlayerRepository.findByGameId(game.getId());

		// 準備1：護衛される人、護衛する人の2人を用意する
		GamePlayer guardTarget = players.get(0);
		GamePlayer guarder = players.get(1);

		nightActionRepository
				.save(new NightAction(game.getId(), game.getDayNumber(), guarder.getId(), "護衛", guardTarget.getId()));
		// 準備2：行動の記録を保存する

		nightResultService.resolve(game);
		// 実行：resolve で「決着させる」（護衛のみ、させる）

		List<GamePlayer> resultPlayers = gamePlayerRepository.findByGameId(game.getId());
		// 結果：その試合のプレイヤー全員のデータを倉庫からとってきて（なければエラー）、
		
		for (GamePlayer player : resultPlayers) {
			assertThat(player.getIsAlive()).isTrue();
			// プレイヤー全員の生存が true（生存） かどうかを、1人ずつ確かめる
			// 襲撃は行われていないので、全員生存しているはず
		}
	}
}
