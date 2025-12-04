package com.example.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // セキュリティ設定クラス
@EnableWebSecurity // Webセキュリティを有効化
@EnableMethodSecurity // メソッドレベルのセキュリティを有効化
public class SecurityConfig {


    @Bean // SecurityFilterChainをBeanとして登録
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception { // HTTPセキュリティ設定
        http
            .authorizeHttpRequests((authorize) -> authorize // リクエストの認可設定
                    .requestMatchers("/css/**", "/js/**", "/images/**", "/", "/auth/**").permitAll() // 静的リソースと認証関連ページは全員アクセス可能
                    .requestMatchers("/admin/**").hasRole("ADMIN") // /admin/**はADMINロールのみアクセス可能
                    .anyRequest().authenticated() // その他のリクエストは認証が必要
            )
            .formLogin((form) -> form
                    .loginPage("/auth/login") // カスタムログインページのURL
                    .loginProcessingUrl("/auth/login") // ログイン処理のURL
                    .defaultSuccessUrl("/") // ログイン成功時のリダイレクト先
                    .failureUrl("/auth/login?error") // ログイン失敗時のリダイレクト先
                    .permitAll()
            )
            .logout((logout) -> logout
                    .logoutSuccessUrl("/auth/login?logout") // ログアウト成功時のリダイレクト先
                    .permitAll()
            );

        return http.build(); // SecurityFilterChainをビルドして返す
    }

    @Bean
    PasswordEncoder passwordEncoder() { // パスワードエンコーダーをBeanとして登録
        return new BCryptPasswordEncoder(); // BCryptアルゴリズムを使用
    }
}