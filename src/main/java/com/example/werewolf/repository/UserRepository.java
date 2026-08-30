package com.example.werewolf.repository;//このファイルの住所（repository＝データ出し入れ係の置き場）

import com.example.werewolf.entity.User;//扱う対象の User を持ち込む
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;//データ出し入れの万能キットを持ち込む

//UserRepository ＝ users のデータを出し入れする倉庫番
//interface（インターフェース）＝「こういう機能があるよ」という約束だけを書くもの（中身の処理は書かない）
//extends JpaRepository<User, Long> ＝ 万能キットを受け継ぐ。Userを、Long型のid で管理する。
//これだけで「全件取得・削除・保存」などの基本操作が自動で使えるようになる

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByUsername(String username);
	
	//  「見つかるかも」    ユーザー名で探す（受け取ったusernameで）
	// の箱で<User>を返す
}
