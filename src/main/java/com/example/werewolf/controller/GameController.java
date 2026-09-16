package com.example.werewolf.controller;

import com.example.werewolf.entity.Game;
import com.example.werewolf.entity.GamePlayer;
import com.example.werewolf.entity.Role;
import com.example.werewolf.entity.Vote;
import com.example.werewolf.repository.GamePlayerRepository;
import com.example.werewolf.repository.GameRepository;
import com.example.werewolf.repository.RoleRepository;
import com.example.werewolf.repository.VoteRepository;
import com.example.werewolf.service.ExecutionResult;
import com.example.werewolf.service.ExecutionService;
import com.example.werewolf.service.FortuneTellerService;
import com.example.werewolf.service.GameResult;
import com.example.werewolf.service.GameResultService;
import com.example.werewolf.service.GameStartService;
import com.example.werewolf.service.HunterService;
import com.example.werewolf.service.PhaseService;
import com.example.werewolf.service.VoteCountService;
import com.example.werewolf.service.VotingService;
import com.example.werewolf.service.WerewolfService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller //ブラウザからの注文を受け付ける係、という目印
public class GameController { // ブラウザから指示が来たら、必要なものを画面に受け渡しする係

	private final GameStartService gameStartService;
	private final PhaseService phaseService;
	private final GameRepository gameRepository;
	private final GamePlayerRepository gamePlayerRepository;
	private final RoleRepository roleRepository;
	private final VotingService votingService;
	private final VoteCountService voteCountService;
	private final ExecutionService executionService;
	private final GameResultService gameResultService;
	private final VoteRepository voteRepository;
	private final FortuneTellerService fortuneTellerService;
	private final WerewolfService werewolfService;
	private final HunterService hunterService;
	// （画面などに）受け渡しするために、必要なデータの倉庫やシステムを用意しておく

	public GameController(GameStartService gameStartService, PhaseService phaseService, GameRepository gameRepository,
			GamePlayerRepository gamePlayerRepository, RoleRepository roleRepository, VotingService votingService,
			VoteCountService voteCountService, ExecutionService executionService, GameResultService gameResultService,
			VoteRepository voteRepository, FortuneTellerService fortuneTellerService, WerewolfService werewolfService,
			HunterService hunterService) {
		this.gameStartService = gameStartService;
		this.phaseService = phaseService;
		this.gameRepository = gameRepository;
		this.gamePlayerRepository = gamePlayerRepository;
		this.roleRepository = roleRepository;
		this.votingService = votingService;
		this.voteCountService = voteCountService;
		this.executionService = executionService;
		this.gameResultService = gameResultService;
		this.voteRepository = voteRepository;
		this.fortuneTellerService = fortuneTellerService;
		this.werewolfService = werewolfService;
		this.hunterService = hunterService;
		 // 8つの係（Service・倉庫）を受け取って、この GameController に入れる
	}

	@GetMapping("/game")
	public String showStart() {
		return "game";
		// game に GET(見せて)が来たら、game.html を返す
	}

	@PostMapping("/game/start")
	// このURLに POST（実行して）という指示が来たら、以下の処理をする
	
	public String startGame() {
		Game game = gameStartService.startGame();
		return "redirect:/game/" + game.getId();
		// gameStartService の startGame を呼んで、作られた試合(Game)を game の箱に入れる（受け取る）
		// そのゲームのIDを取り出して、URLの後ろにくっつけて、別のリンク（/game/そのID）に飛ばす
	}

	@GetMapping("/game/{id}")
	// このURLにアクセス（GET）が来たら、以下の処理をする（試合の中身を見るだけ）
	
	public String showGame(@PathVariable Long id, Model model) {
		// 試合を見せる係。URLからID、画面用のお盆(model)を受け取る
		
		Game game = gameRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("試合が見つからない: " + id));
		// さっきのIDを使って、ゲームの倉庫からその1試合を取ってきて、gameの箱に入れる
		// もし見つからなかったら「試合が見つからない: (ID)」というエラーを出す

		List<GamePlayer> gamePlayers = gamePlayerRepository.findByGameIdOrderBySeatOrderAsc(id);
		// IDでその試合のプレイヤーを探して、席順に並べる。それを複数人が入る箱(gamePlayers)に入れる

		List<PlayerView> playerViews = new ArrayList<>();
		// 画面表示用の、新しい空のリストを playerViews という箱で作る
		
		for (GamePlayer gamePlayer : gamePlayers) {
			// gamePlayers から gamePlayer に1人ずつ取り出して、全員ぶん繰り返す
			
			Role role = roleRepository.findById(gamePlayer.getRoleId())
					.orElseThrow(() -> new IllegalArgumentException("役職が見つからない: " + gamePlayer.getRoleId()));
			// その人(gamePlayer)の役職IDをゲットして、役職の倉庫から役職を探し、roleの箱に入れる
			// もし見つからなかったら「役職が見つからない: (ID)」というエラーを出す
			
			playerViews.add(new PlayerView(gamePlayer.getSeatOrder(), role.getName(), gamePlayer.getIsAlive()));
			// 画面表示用の箱(playerViews)に、一人ずつ足していく。中身は、その人の席順・役職名・生存状態
		}

		GameResult gameResult = gameResultService.judge(id);
		// 今の試合が決着したか"確認するため"に、judgeで勝敗を調べる（試合は動かさない・ただ見るだけ）
		
		boolean isFinished = gameResult != GameResult.CONTINUE;
		// その結果が CONTINUE（続行）じゃなければ、決着した（isFinished = true）

		boolean hasVoted = voteRepository.countByGameIdAndDayNumber(id, game.getDayNumber()) > 0;
		// この試合の、今の日付の投票が1件以上あれば、投票済み（hasVoted = true）
		// 投票の倉庫から、ゲームIDと何日目かを使って、件数を数える
		// 0より多いなら、hasVoted に true を入れる
		// 1件でもあれば（＝誰か投票してれば）、その日はもう投票済み

		model.addAttribute("gameId", game.getId()); // 1.ゲームID
		model.addAttribute("currentPhase", game.getCurrentPhase()); // 2.今のフェーズ
		model.addAttribute("players", playerViews); // 3.表示用のプレイヤー一覧
		model.addAttribute("gameResult", gameResult);
		model.addAttribute("isFinished", isFinished);
		model.addAttribute("hasVoted", hasVoted);
		// 画面表示用のお盆(model)に、項目(名札)付きで6つ追加する

		return "game";
		// 以上の処理が終わったら、この画面(game.html)を見せる
	}

	@PostMapping("/game/{id}/next")
	// このURLに、postで指示が来たら、以下の処理をする
	
	public String nextPhase(@PathVariable Long id) {
		// 今の試合を次のフェーズに進める係。URLからIDの番号を取得する
		
		Game game = gameRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("試合が見つからない: " + id));
		// IDを使って、ゲームの倉庫からこの1試合を取ってくる。それを、gameの箱に入れる
		// もし見つからなかったら、「試合が見つからない＋（ID番号）」というエラーを出す
		
		phaseService.advancePhase(game);
		// フェーズを進める係に、この game を渡して、次のフェーズに進めてもらう
		
		return "redirect:/game/" + id;
		// 上の処理が終わったら、フェーズを進めた後のその試合の"表示画面"に戻す（今のURL＋ID番号をつけて）
	}

	@PostMapping("/game/{id}/vote")
	// このURLに、postで指示が来たら、以下の処理をする

	public String voteGame(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		// 投票→集計→処刑→勝敗判定をまとめて行う係。URLからIDの番号を取得する

		Game game = gameRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("試合が見つからない: " + id));
		// IDを使って、ゲームの倉庫からこの1試合を取ってくる。それを、gameの箱に入れる

		if (voteRepository.countByGameIdAndDayNumber(id, game.getDayNumber()) > 0) {
			// 今の日付の投票が既にあれば、二重投票なので何もせず戻す
			// その日に投票が行われているか確認して、もし行われているなら…
			
			return "redirect:/game/" + id;
			// 表示画面に戻す（ゲームIDをつけて）
			// これ以上投票する必要ないので、ここから下の投票をさせない
		}

		List<Vote> votes = votingService.vote(game);
		// そのゲームに投票メソッドを使って、votes の箱に入れる（生存者だけ、ランダムに1票）

		List<Long> mostVotedGamePlayerIds = voteCountService.findMostVoted(votes);
		// その箱から、最大票の人だけを全員集めて、別のリストの箱に入れる

		ExecutionResult executionResult = executionService.execute(mostVotedGamePlayerIds);
		// その箱の最大票の人を、処刑ロジックで実行（1人なら死亡、0か複数なら再投票）
		// （※今は全員必ず投票するので0人は実質起きないが、将来の棄権等に備えて0人も弾いてある）

		GameResult gameResult = gameResultService.judge(id);
		// 処刑後の生存者数から、勝敗を判定する

		redirectAttributes.addFlashAttribute("executionResult", executionResult);
		redirectAttributes.addFlashAttribute("gameResult", gameResult);
		// 処刑結果と勝敗結果を、画面に1回だけ映す（ずっと表示されると困るので、1回で消える）

		return "redirect:/game/" + id;
		// 上の処理が終わったら、その試合の"表示画面"に戻す
	}

	// それぞれの夜に行動する役職に、能力を使ってもらう(役職カードを使わなかった場合の設計はこれから)
	@PostMapping("/game/{id}/night1")
	public String night1(@PathVariable Long id) {
		Game game = gameRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("試合が見つからない: " + id));

		fortuneTellerService.act(game);
		werewolfService.act(game);
		hunterService.act(game);

		return "redirect:/game/" + id;
	}
}
