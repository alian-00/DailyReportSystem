package com.example.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.entity.User;
import com.example.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    // findByEmailメソッドを使用してユーザー情報を取得し、Spring SecurityのUserDetailsオブジェクトに変換するクラス
    @Autowired
    private UserRepository userRepository; // UserRepositoryを注入

    @Override // UserDetailsServiceのメソッドを実装
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException { // メールアドレスでユーザーを検索
        User user = userRepository.findByEmail(email) // UserRepositoryを使用してユーザーを検索
                .orElseThrow(() -> new UsernameNotFoundException("ユーザーが見つかりません: " + email)); // ユーザーが見つからない場合は例外をスロー

        return org.springframework.security.core.userdetails.User // UserDetailsオブジェクトを作成して返す
                .withUsername(user.getEmail()) // ユーザー名にメールアドレスを設定
                .password(user.getPassword()) // パスワードを設定
                .roles(user.getRole().replace("ROLE_", ""))
                .build();
    }
}