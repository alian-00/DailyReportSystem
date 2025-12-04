package com.example.controller.admin;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.entity.User;
import com.example.repository.UserRepository;

@Controller
@RequestMapping("/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Readユーザー一覧表示
    @GetMapping
    public String list(Model model) {
        model.addAttribute("users", userRepository.findAll()); // すべてのユーザーを取得してモデルに追加
        return "admin/users/list";
    }

    // Createユーザー作成フォーム表示
    @GetMapping("/create")
    public String showCreateForm(@ModelAttribute("user") User user) {
        return "admin/users/create"; // ユーザー作成フォームを表示
    }

    // Createユーザー作成処理
    @PostMapping("/create")
    public String create(@Validated @ModelAttribute("user") User user,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (userRepository.existsByEmail(user.getEmail())) {
            result.rejectValue("email", "error.user", "このメールアドレスは既に使用されています");
        }

        if (result.hasErrors()) {
            return "admin/users/create";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword())); // パスワードをエンコード
        LocalDateTime now = LocalDateTime.now(); // 現在日時を取得
        user.setCreatedAt(now); // 作成日時を設定
        user.setUpdatedAt(now); // 更新日時を設定

        userRepository.save(user); // ユーザーを保存
        redirectAttributes.addFlashAttribute("successMessage", "ユーザーを登録しました");

        return "redirect:/admin/users";
    }

    // Deleteユーザー削除処理
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        // 管理者は削除できないようにする
        if (userRepository.findById(id).get().getRole().equals("ROLE_ADMIN")) {
            redirectAttributes.addFlashAttribute("errorMessage", "管理者は削除できません。");
            return "redirect:/admin/users";
        }

        userRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "ユーザーを削除しました。");
        return "redirect:/admin/users";
    }
}