package com.example.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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

import com.example.entity.Comments;
import com.example.entity.DailyReport;
import com.example.entity.User;
import com.example.repository.CommentsRepository;
import com.example.repository.DailyReportRepository;
import com.example.repository.UserRepository;

// 日報管理を行うコントローラークラス
@Controller
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private DailyReportRepository reportRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentsRepository commentsRepository;

    // Read日報一覧表示
    @GetMapping
    public String list(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        // すべての日報を提出日の降順で取得
        model.addAttribute("reports", reportRepository.findAllByOrderBySubmissionDateDesc()); // 日報一覧をモデルに追加
        return "reports/list";
    }

    // Create日報作成フォーム表示
    @GetMapping("/create")
    public String showCreateForm(@ModelAttribute DailyReport report) {
        return "reports/create";
    }

    // Create日報作成処理
    @PostMapping("/create")
    public String create(@AuthenticationPrincipal UserDetails userDetails,
                @Validated @ModelAttribute DailyReport report,
                BindingResult result,
                Model model,
                RedirectAttributes redirectAttributes) {

        // バリデーションエラーがある場合は作成フォームに戻る
        if (result.hasErrors()) {
            return "reports/create";
        }

        User user = userRepository.findByEmail(userDetails.getUsername()).get(); // 現在のユーザー情報を取得
        report.setUser(user); // 日報にユーザーを設定

        LocalDateTime now = LocalDateTime.now(); // 現在の日時を取得
        report.setCreatedAt(now);
        report.setUpdatedAt(now);

        // 日報を保存
        reportRepository.save(report);
        redirectAttributes.addFlashAttribute("successMessage", "日報を作成しました"); // 成功メッセージがlist.htmlで表示される

        return "redirect:/reports"; // 日報一覧にリダイレクト
    }

    // Read日報詳細表示
    @GetMapping("/{id}")
    public String show(@PathVariable Long id, Model model) {

        // ① 日報1件を取得して、変数に入れる
        DailyReport report = reportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("日報が見つかりません"));
        model.addAttribute("report", report);

        // ② その日報に紐づくコメント一覧を取得
        List<Comments> comments = commentsRepository
                .findByDailyReportOrderByCreatedAtAsc(report);
        model.addAttribute("comments", comments);

        // ③ テンプレートへ
        return "reports/detail";
    }

    // Update日報編集フォーム表示
    @GetMapping("/edit/{id}")
    public String showEditForm(@AuthenticationPrincipal UserDetails userDetails, // 認証されたユーザー情報を取得
            @PathVariable Long id, // パス変数で日報IDを取得
            Model model,
            RedirectAttributes redirectAttributes) { // リダイレクト属性を使用してメッセージを渡す

        DailyReport report = reportRepository.findById(id).get(); // 日報をIDで取得

        // 投稿者本人でない場合は日報一覧にリダイレクト
        if (!report.getUser().getEmail().equals(userDetails.getUsername())) {
            redirectAttributes.addFlashAttribute("errorMessage", "他のユーザーの日報は編集できません。");
            return "redirect:/reports";
        }

        model.addAttribute("report", report);
        return "reports/edit";
    }

    // Update日報更新処理
    @PostMapping("/edit/{id}")
    public String update(@AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id,
            @Validated @ModelAttribute DailyReport report,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        // バリデーションエラーがある場合は編集フォームに戻る
        if (result.hasErrors()) {
            return "reports/edit";
        }

        // 既存の日報を取得
        DailyReport existingReport = reportRepository.findById(id).get();

        // 投稿者本人でない場合は日報一覧にリダイレクト
        if (!existingReport.getUser().getEmail().equals(userDetails.getUsername())) {
            redirectAttributes.addFlashAttribute("errorMessage", "他のユーザーの日報は編集できません。");
            return "redirect:/reports";
        }

        // 既存の日報の内容を更新
        existingReport.setTitle(report.getTitle());
        existingReport.setContent(report.getContent());
        existingReport.setSubmissionDate(report.getSubmissionDate());
        existingReport.setUpdatedAt(LocalDateTime.now());

        reportRepository.save(existingReport);
        redirectAttributes.addFlashAttribute("successMessage", "日報を更新しました");

        return "redirect:/reports";
    }

    // Delete日報削除処理
    @PostMapping("/delete/{id}") // idは日報のidのこと
    public String delete(@AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        // 既存の日報を取得
        DailyReport report = reportRepository.findById(id).get();

        // 投稿者本人でない場合は日報一覧にリダイレクト
        if (!report.getUser().getEmail().equals(userDetails.getUsername())) {
            redirectAttributes.addFlashAttribute("errorMessage", "他のユーザーの日報は削除できません。");
            return "redirect:/reports";
        }

        // 日報を削除
        reportRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "日報を削除しました");
        return "redirect:/reports";
    }
}