package com.zjq.java_ee_starter.controller;

import com.zjq.java_ee_starter.service.BorrowRecordService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import com.zjq.java_ee_starter.entity.BorrowRecord;

@Controller
@RequestMapping("/borrow")
public class BorrowRecordController {

    private final BorrowRecordService borrowRecordService;

    public BorrowRecordController(BorrowRecordService borrowRecordService) {
        this.borrowRecordService = borrowRecordService;
    }

    /**
     * 显示当前借阅页面（用户）
     */
    @GetMapping("/current")
    public String showCurrentBorrows(Model model, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/user/login";
        }

        List<BorrowRecord> records = borrowRecordService.findCurrentBorrowRecordsByUserId(userId);
        model.addAttribute("borrowRecords", records);
        return "borrow/current";
    }

    /**
     * 显示借阅记录页面（用户）
     */
    @GetMapping("/history")
    public String showBorrowHistory(Model model, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/user/login";
        }

        List<BorrowRecord> records = borrowRecordService.findAllBorrowRecordsByUserId(userId);
        model.addAttribute("borrowRecords", records);
        return "borrow/history";
    }

    /**
     * 显示所有借阅记录（管理员）
     */
    @GetMapping("/manage")
    public String manageBorrowRecords(Model model, HttpSession session) {
        Integer role = (Integer) session.getAttribute("role");
        if (role == null || role != 1) {
            return "redirect:/user/login";
        }

        List<BorrowRecord> records = borrowRecordService.findAllBorrowRecords();
        model.addAttribute("borrowRecords", records);
        return "borrow/manage";
    }

    /**
     * 借阅图书
     */
    @PostMapping("/borrow/{bookId}")
    public String borrowBook(@PathVariable Integer bookId, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/user/login";
        }

        try {
            borrowRecordService.borrowBook(userId, bookId);
        } catch (Exception e) {
            // 可以添加错误提示
            System.err.println("借阅失败: " + e.getMessage());
        }

        return "redirect:/book/search";
    }

    /**
     * 用户归还图书
     */
    @PostMapping("/return/{recordId}")
    public String returnBook(@PathVariable Integer recordId, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/user/login";
        }

        borrowRecordService.returnBook(recordId);
        return "redirect:/borrow/current";
    }

    /**
     * 管理员确认归还图书
     */
    @PostMapping("/confirm/{recordId}")
    public String confirmReturn(@PathVariable Integer recordId, HttpSession session) {
        Integer role = (Integer) session.getAttribute("role");
        if (role == null || role != 1) {
            return "redirect:/user/login";
        }

        borrowRecordService.confirmReturn(recordId);
        return "redirect:/borrow/manage";
    }

    /**
     * 新增借阅记录（管理员）
     */
    @PostMapping("/add")
    public String addBorrowRecord(@RequestParam Integer userId,
            @RequestParam Integer bookId,
            HttpSession session) {
        Integer role = (Integer) session.getAttribute("role");
        if (role == null || role != 1) {
            return "redirect:/user/login";
        }

        try {
            borrowRecordService.borrowBook(userId, bookId);
        } catch (Exception e) {
            // 可以添加错误提示
            System.err.println("新增借阅记录失败: " + e.getMessage());
        }

        return "redirect:/borrow/manage";
    }
}