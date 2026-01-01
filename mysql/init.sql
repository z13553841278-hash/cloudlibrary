CREATE DATABASE IF NOT EXISTS book_management;

USE book_management;

-- 用户表
CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_name VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    role INT DEFAULT 0 COMMENT '0-用户 1-管理员',
    status INT DEFAULT 1 COMMENT '1-正常 0-禁用',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 图书表
CREATE TABLE IF NOT EXISTS books (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL,
    author VARCHAR(100) NOT NULL,
    isbn VARCHAR(20) UNIQUE,
    category VARCHAR(50),
    description TEXT,
    total_copies INT DEFAULT 1,
    available_copies INT DEFAULT 1,
    status INT DEFAULT 1 COMMENT '1-可用 0-不可用',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 借阅记录表
CREATE TABLE IF NOT EXISTS borrow_records (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    book_id INT NOT NULL,
    borrow_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    due_date TIMESTAMP,
    return_date TIMESTAMP NULL,
    status INT DEFAULT 0 COMMENT '0-借阅中 1-已归还 2-逾期',
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (book_id) REFERENCES books (id)
);

-- 插入测试数据
INSERT INTO
    users (
        user_name,
        password,
        email,
        phone,
        role
    )
VALUES (
        'admin',
        '123456',
        'admin@example.com',
        '13800138000',
        1
    ),
    (
        'user1',
        '123456',
        'user1@example.com',
        '13800138001',
        0
    ),
    (
        'user2',
        '123456',
        'user2@example.com',
        '13800138002',
        0
    );

-- 插入图书测试数据
INSERT INTO
    books (
        title,
        author,
        isbn,
        category,
        description,
        total_copies,
        available_copies
    )
VALUES (
        'Java编程思想',
        'Bruce Eckel',
        '9787111213826',
        '编程',
        '经典的Java入门书籍',
        5,
        5
    ),
    (
        'Spring实战',
        'Craig Walls',
        '9787121150297',
        '框架',
        'Spring框架深度指南',
        3,
        3
    ),
    (
        '数据库系统概念',
        'Abraham Silberschatz',
        '9787115328651',
        '数据库',
        '数据库理论基础',
        2,
        2
    ),
    (
        '算法导论',
        'Thomas H. Cormen',
        '9787111407010',
        '算法',
        '算法经典教材',
        4,
        4
    );

-- 插入借阅记录测试数据
INSERT INTO
    borrow_records (
        user_id,
        book_id,
        borrow_date,
        due_date,
        return_date,
        status
    )
VALUES (
        2,
        1,
        '2023-01-01 10:00:00',
        '2023-01-15 10:00:00',
        NULL,
        0
    ),
    (
        2,
        2,
        '2023-01-05 14:30:00',
        '2023-01-19 14:30:00',
        '2023-01-10 16:00:00',
        1
    );