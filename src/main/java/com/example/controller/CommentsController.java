package com.example.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.entity.Comments;
import com.example.entity.DailyReport;
import com.example.entity.User;
import com.example.repository.CommentsRepository;
import com.example.repository.DailyReportRepository;
import com.example.repository.UserRepository;

// コメント用のコントローラークラス
@Controller
@RequestMapping("/comments")
public class CommentsController {

    @Autowired
    private CommentsRepository commentsRepository;

    @Autowired
    private DailyReportRepository reportRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/add/{reportId}")
    public String add(
            @PathVariable Long reportId,
            @RequestParam("content") String content,
            @AuthenticationPrincipal UserDetails userDetails) {

        // コメント追加のロジックをここに実装

        // 1. reportId から日報を取得
        DailyReport report = reportRepository.findById(reportId).get();

        // 2. ログインユーザーを取得
        User user = userRepository.findByEmail(userDetails.getUsername()).get();

        // 3. Comments を new して各フィールドをセット
        Comments comment = new Comments();
        comment.setDailyReport(report);
        comment.setUser(user);
        comment.setContent(content);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());

        // 4. commentsRepository.save(comment);
        commentsRepository.save(comment);

        // 5. 日報詳細にリダイレクト

        return "redirect:/reports/" + reportId; // コメント追加後に日報詳細にリダイレクト
    }
}
