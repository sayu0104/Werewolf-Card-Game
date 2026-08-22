package com.example.werewolf.controller;//このファイルの住所

import com.example.werewolf.entity.Role;
import com.example.werewolf.repository.RoleRepository;//使うための道具を持ち込む
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller // ブラウザからのアクセスを受け付ける窓口係
public class AdminRoleController {

	private final RoleRepository roleRepository;// 非公開の決定版の倉庫番

	public AdminRoleController(RoleRepository roleRepository) {// 工具箱を受け取る
		this.roleRepository = roleRepository;// 台帳＝それに記入する紙
	}

	@GetMapping("/admin/roles") // このURLにブラウザからアクセスが来たら、以下の処理を動かす
	public String listRoles(Model model) {// この処理の名前「役職を一覧化する」（データを乗せるお盆を受け取る）
		model.addAttribute("roles", roleRepository.findAll());// 道具で全役職を取得して、"roles"の名札を付けてお盆にのせる
		return "role-list";// 上の処理が終わったらそれを表示する
	}

	@PostMapping("/admin/roles")// このURLにpostで注文が来たら、以下の処理を行う
	public String createRole(
			@RequestParam String name,// フォームに入力された値を受け取る係　文字列で　nameという箱で受け取る
			@RequestParam String faction,
			@RequestParam String description) {
		roleRepository.save(new Role(name, faction, description));// 役職の倉庫番に.保存させる（受け取った3つの値で、新しいRoleを作る（名前、派閥、説明））
		return "redirect:/admin/roles";// 上の処理が終わったら、「/admin/roles」にアクセスし直す（処理変更後の画面にするため）
	}

	@PostMapping("/admin/roles/{id}/delete")// このURLにpostで注文が来たら、以下の処理を行う
	public String deleteRole(@PathVariable Long id) {// @PathVariable（"URLの"中の値（id）を取り出して、処理の中で使えるようにする（係））
		roleRepository.deleteById(id);// 倉庫番に.万能キット（JpaRepository）にある「deleteById（自動一件削除機能）」を使うと指示する（受け取ったidの役職を消す）
		return "redirect:/admin/roles";// 上の処理が終わったら、「/admin/roles」にアクセスし直す（処理変更後の画面にするため）
	}
}
