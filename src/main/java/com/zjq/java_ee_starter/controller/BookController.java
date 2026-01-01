package com.zjq.java_ee_starter.controller;

import com.zjq.java_ee_starter.entity.Book;
import com.zjq.java_ee_starter.service.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/book")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * 显示图书管理页面（管理员）
     */
    @GetMapping("/manage")
    public String manageBooks(Model model, HttpSession session) {
        // 检查管理员权限
        Integer role = (Integer) session.getAttribute("role");
        if (role == null || role != 1) {
            return "redirect:/user/login";
        }

        List<Book> books = bookService.findAllBooks();
        model.addAttribute("books", books);
        return "book/manage";
    }

    /**
     * 显示新增图书页面
     */
    @GetMapping("/add")
    public String showAddBookForm(Model model, HttpSession session) {
        Integer role = (Integer) session.getAttribute("role");
        if (role == null || role != 1) {
            return "redirect:/user/login";
        }

        model.addAttribute("book", new Book());
        return "book/form";
    }

    /**
     * 显示编辑图书页面
     */
    @GetMapping("/edit/{id}")
    public String showEditBookForm(@PathVariable Integer id, Model model, HttpSession session) {
        Integer role = (Integer) session.getAttribute("role");
        if (role == null || role != 1) {
            return "redirect:/user/login";
        }

        Book book = bookService.findBookById(id);
        if (book == null) {
            return "redirect:/book/manage";
        }

        model.addAttribute("book", book);
        return "book/form";
    }

    /**
     * 保存图书
     */
    @PostMapping("/save")
    public String saveBook(@ModelAttribute Book book, HttpSession session) {
        Integer role = (Integer) session.getAttribute("role");
        if (role == null || role != 1) {
            return "redirect:/user/login";
        }

        bookService.saveBook(book);
        return "redirect:/book/manage";
    }

    /**
     * 删除图书
     */
    @PostMapping("/delete/{id}")
    public String deleteBook(@PathVariable Integer id, HttpSession session) {
        Integer role = (Integer) session.getAttribute("role");
        if (role == null || role != 1) {
            return "redirect:/user/login";
        }

        bookService.deleteBook(id);
        return "redirect:/book/manage";
    }

    /**
     * 查询图书（用户借阅页面）
     */
    @GetMapping("/search")
    public String searchBooks(@RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String category,
            Model model, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/user/login";
        }

        Book condition = new Book();
        condition.setTitle(title);
        condition.setAuthor(author);
        condition.setCategory(category);
        condition.setStatus(1); // 只查询可用图书

        List<Book> books = bookService.findBooksByCondition(condition);
        model.addAttribute("books", books);
        model.addAttribute("title", title);
        model.addAttribute("author", author);
        model.addAttribute("category", category);

        return "book/search";
    }
}