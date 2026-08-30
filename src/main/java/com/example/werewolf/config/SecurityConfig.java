package com.example.werewolf.config;

import com.example.werewolf.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // 「これは設定ファイルですよ」の目印
public class SecurityConfig {

	@Bean // 「この部品を、Springに登録して使わせる」目印
	public PasswordEncoder passwordEncoder() { // 1. 暗号化担当（新）
		// Password(パスワード)+Encoder(符号化するもの)=「パスワードを暗号化するもの」。これを返す

		return new BCryptPasswordEncoder();
		// BCrypt方式の暗号化器を1個作って返す
	}

	@Bean // 「この部品を、Springに登録して使わせる」目印
	public UserDetailsService userDetailsService(UserRepository userRepository) { // 2. ユーザー情報を提供する係（新）
		return username -> userRepository.findByUsername(username)
				.filter(com.example.werewolf.entity.User::getIsAdmin)
				.map(user -> (UserDetails) User.withUsername(user.getUsername())
						.password(user.getPasswordHash())
						.roles("ADMIN")
						.build())
				.orElseThrow(() -> new UsernameNotFoundException(username));
	}

	@Bean // 「この部品を、Springに登録して使わせる」目印
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception { //  3.門番の指示書（昨日のやつ）
		http
		
		// public               … 公開
		// SecurityFilterChain  … 戻り値の型。「セキュリティのふるいの連なり」を返す
		// securityFilterChain  … メソッド名
		// (HttpSecurity http)  … 受け取る材料。「設定を書き込む台紙(http)」を受け取る
		// throws Exception     … 「エラーが起きるかも」の予告

		// http                 … 受け取った台紙に対して…

				// １．どのURLを守る/開放する
				.authorizeHttpRequests(
						auth -> auth.requestMatchers("/admin/**").authenticated().anyRequest().permitAll())

				// authorizeHttpRequests … 「リクエスト（アクセス）を、許可制にする」
				
				// -> (アロー)    … 「〜を受け取って、こう設定する」の書き方

				// requestMatchers("/admin/**") … 「/admin/ で始まるURL は」
				// .authenticated() … 「認証された人だけ（ログイン必須）」

				// anyRequest() … 「それ以外の、全てのURL は」
				// .permitAll() … 「全員許可（誰でもOK）」

				// ２．ログインの設定
				.formLogin(form -> form.loginPage("/login").defaultSuccessUrl("/admin/roles", true).permitAll())

				// formLogin … 「フォーム（入力画面）でログインする方式にする」

				// loginPage("/login") … 「ログイン画面は /login を使う」
				// defaultSuccessUrl("/admin/roles", true) … 「ログイン成功したら /admin/roles へ飛ばす」
				// permitAll() … 「ログイン画面自体は、誰でもアクセスOK」

				// ３．ログアウトの設定
				.logout(logout -> logout.permitAll());

		        // 「ログアウト機能を有効にして、誰でも使えるようにする」

		return http.build();
	}
}
