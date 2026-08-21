package com.example.werewolf.controller;// このファイルの住所

import com.example.werewolf.repository.UserRepository;// 使う道具を持ち込む
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller// 以下のクラスは、ブラウザからのアクセスを受け付ける窓口係(コントローラ)
public class AdminUserController {// 公開クラス

    private final UserRepository userRepository;// 非公開の決定版（後から変えられない）の倉庫番

    public AdminUserController(UserRepository userRepository) {// ← 工具箱を受け取る（紙）
    //  ↑         ↑                    ↑
    // 公開    このクラスと同じ名前       受け取るもの
           // （＝コンストラクタの印）  （＝工具箱を1つ受け取る）
    	
        this.userRepository = userRepository;// 自分の欄(台帳)に書き写す、受け取った工具箱(紙)を
    }

    @GetMapping("/admin/users")// このURLにブラウザからアクセスが来たら、以下の処理を動かす（そのURL）
    public String listUsers(Model model) {// この処理の名前「ユーザーを一覧化する」（データを載せる"お盆"を受け取る）
        model.addAttribute("users", userRepository.findAll());// "users"の名札を付けてお盆に載せる(道具で全ユーザーを取得して)
        return "user-list";// user-listというテンプレート(お皿)で表示する
    }
}
