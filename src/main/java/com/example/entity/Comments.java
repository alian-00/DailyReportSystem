package com.example.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comments")
public class Comments {

    // コメントID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 日報ID（外部キー）
    @ManyToOne
    @JoinColumn(name = "daily_report_id", nullable = false)
    private DailyReport dailyReport;

    // ユーザーID（外部キー）
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // コメント内容
    @NotBlank(message = "コメントを入力してください")
    @Size(max = 1000, message = "コメントは1000文字以内で入力してください")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    // 作成日時
    @Column(nullable = false)
    private LocalDateTime createdAt;

    // 更新日時
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
