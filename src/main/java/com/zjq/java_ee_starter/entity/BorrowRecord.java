package com.zjq.java_ee_starter.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BorrowRecord {
    private Integer id;
    private Integer userId;
    private Integer bookId;
    private LocalDateTime borrowDate;
    private LocalDateTime dueDate;
    private LocalDateTime returnDate;
    private Integer status; // 0-借阅中 1-已归还 2-逾期

    // 关联对象（可选，用于查询时填充）
    private User user;
    private Book book;
}