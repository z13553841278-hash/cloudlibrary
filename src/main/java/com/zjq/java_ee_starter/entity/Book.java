package com.zjq.java_ee_starter.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Book {
    private Integer id;
    private String title;
    private String author;
    private String isbn;
    private String category;
    private String description;
    private Integer totalCopies;
    private Integer availableCopies;
    private Integer status; // 1-可用 0-不可用
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}