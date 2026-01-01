package com.zjq.java_ee_starter.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import com.zjq.java_ee_starter.mapper.BorrowRecordMapper;
import com.zjq.java_ee_starter.entity.BorrowRecord;

@Service
public class BorrowRecordService {
    private final BorrowRecordMapper borrowRecordMapper;
    private final BookService bookService;

    public BorrowRecordService(BorrowRecordMapper borrowRecordMapper, BookService bookService) {
        this.borrowRecordMapper = borrowRecordMapper;
        this.bookService = bookService;
    }

    public List<BorrowRecord> findAllBorrowRecords() {
        return borrowRecordMapper.findAllBorrowRecords();
    }

    public List<BorrowRecord> findCurrentBorrowRecordsByUserId(Integer userId) {
        return borrowRecordMapper.findCurrentBorrowRecordsByUserId(userId);
    }

    public List<BorrowRecord> findAllBorrowRecordsByUserId(Integer userId) {
        return borrowRecordMapper.findAllBorrowRecordsByUserId(userId);
    }

    public BorrowRecord findBorrowRecordById(Integer id) {
        return borrowRecordMapper.findBorrowRecordById(id);
    }

    @Transactional
    public boolean borrowBook(Integer userId, Integer bookId) {
        // 检查用户是否已借阅此书且未归还
        if (borrowRecordMapper.countUnreturnedByUserAndBook(userId, bookId) > 0) {
            throw new RuntimeException("您已借阅此书且未归还");
        }

        // 尝试借书（减少图书可用数量）
        if (!bookService.borrowBook(bookId)) {
            throw new RuntimeException("图书不可借阅");
        }

        // 创建借阅记录
        BorrowRecord record = new BorrowRecord();
        record.setUserId(userId);
        record.setBookId(bookId);
        record.setBorrowDate(LocalDateTime.now());
        record.setDueDate(LocalDateTime.now().plusDays(14)); // 借阅期限14天
        record.setStatus(0); // 借阅中

        return borrowRecordMapper.insertBorrowRecord(record) > 0;
    }

    @Transactional
    public boolean returnBook(Integer recordId) {
        BorrowRecord record = findBorrowRecordById(recordId);
        if (record == null || record.getStatus() != 0) {
            return false;
        }

        // 更新借阅记录
        LocalDateTime now = LocalDateTime.now();
        int status = now.isAfter(record.getDueDate()) ? 2 : 1; // 1-已归还 2-逾期归还
        borrowRecordMapper.updateReturnDate(recordId, now, status);

        // 增加图书可用数量
        bookService.returnBook(record.getBookId());

        return true;
    }

    public boolean confirmReturn(Integer recordId) {
        // 管理员确认归还（将状态从2改为1）
        BorrowRecord record = findBorrowRecordById(recordId);
        if (record != null && record.getStatus() == 2) {
            return borrowRecordMapper.updateReturnDate(recordId, record.getReturnDate(), 1) > 0;
        }
        return false;
    }

    public boolean saveBorrowRecord(BorrowRecord record) {
        return borrowRecordMapper.insertBorrowRecord(record) > 0;
    }
}