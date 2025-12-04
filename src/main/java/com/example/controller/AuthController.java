package com.example.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.entity.User;
import com.example.repository.UserRepository;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository; // UserRepositoryを注入して、メールアドレスの重複やユーザー検索に使用

    @Autowired
    private PasswordEncoder passwordEncoder; // パスワードのハッシュ化に使用

    @GetMapping("/login")
    public String showLoginForm() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(@ModelAttribute("user") User user) {
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@Validated @ModelAttribute("user") User user,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (userRepository.existsByEmail(user.getEmail())) { // メールアドレスの重複チェック
            result.rejectValue("email", "error.user", "このメールアドレスは既に使用されています"); // エラーメッセージを設定
        }

        if (result.hasErrors()) {
            return "auth/register";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        LocalDateTime now = LocalDateTime.now();
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        userRepository.save(user);
        redirectAttributes.addFlashAttribute("successMessage", "ユーザー登録が完了しました");

        return "redirect:/auth/login";
    }
}