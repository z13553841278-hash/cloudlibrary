-- 1. 创建数据库：强制指定 utf8mb4 编码+排序规则，指定存储引擎（兼容所有MySQL版本）
CREATE DATABASE IF NOT EXISTS book_management DEFAULT CHARACTER SET utf8mb4 DEFAULT COLLATE utf8mb4_general_ci;

-- 强制切换到目标库（核心兜底：无论命令是否指定库，脚本内自行切换）
USE book_management;

-- 用户表：显式指定编码，优化字段长度/约束，增加存储引擎+字符集显式声明
CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    user_name VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    email VARCHAR(100) NOT NULL COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '手机号',
    role TINYINT DEFAULT 0 COMMENT '0-用户 1-管理员',
    status TINYINT DEFAULT 1 COMMENT '1-正常 0-禁用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '用户信息表';

-- 图书表：显式指定编码，适配长标题/描述
CREATE TABLE IF NOT EXISTS books (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '图书ID',
    title VARCHAR(200) NOT NULL COMMENT '图书标题',
    author VARCHAR(100) NOT NULL COMMENT '作者',
    isbn VARCHAR(20) UNIQUE COMMENT 'ISBN编号',
    category VARCHAR(50) COMMENT '图书分类',
    description TEXT COMMENT '图书描述',
    total_copies INT DEFAULT 1 COMMENT '总库存',
    available_copies INT DEFAULT 1 COMMENT '可借阅库存',
    status TINYINT DEFAULT 1 COMMENT '1-可用 0-不可用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '图书信息表';

-- 借阅记录表：显式指定编码，优化外键约束（ON DELETE CASCADE 级联删除更合理）
CREATE TABLE IF NOT EXISTS borrow_records (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '借阅记录ID',
    user_id INT NOT NULL COMMENT '借阅人ID',
    book_id INT NOT NULL COMMENT '图书ID',
    borrow_date DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '借阅时间',
    due_date DATETIME COMMENT '应归还时间',
    return_date DATETIME NULL COMMENT '实际归还时间',
    status TINYINT DEFAULT 0 COMMENT '0-借阅中 1-已归还 2-逾期',
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES books (id) ON DELETE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '图书借阅记录表';

-- 插入测试数据（增加 IGNORE 关键字，重复执行不报错）
INSERT IGNORE INTO
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
        'user@example.com',
        '13800138002',
        0
    ),
    (
        'zjq',
        '23053003',
        'zjq@example.com',
        '13800138001',
        0
    ),
    (
        'clx',
        '23053012',
        'clx@example.com',
        '13800138002',
        0
    );

INSERT IGNORE INTO
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

INSERT IGNORE INTO
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