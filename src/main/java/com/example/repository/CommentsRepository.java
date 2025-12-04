package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Comments;
import com.example.entity.DailyReport;

@Repository
public interface CommentsRepository extends JpaRepository<Comments, Long> {
    // 指定した日報のコメントを作成日の昇順で取得
    List<Comments> findByDailyReportOrderByCreatedAtAsc(DailyReport dailyReport);

}