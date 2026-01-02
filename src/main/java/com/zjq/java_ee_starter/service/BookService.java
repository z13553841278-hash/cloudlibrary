package com.zjq.java_ee_starter.service;

import org.springframework.stereotype.Service;
import java.util.List;
import com.zjq.java_ee_starter.mapper.BookMapper;
import com.zjq.java_ee_starter.entity.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class BookService {
    private static final Logger logger = LoggerFactory.getLogger(BookService.class);
    private final BookMapper bookMapper;

    public BookService(BookMapper bookMapper) {
        this.bookMapper = bookMapper;
    }

    public List<Book> findAllBooks() {
        List<Book> books = bookMapper.findAllBooks();
        for (Book book : books) {
            logger.debug("Book title: {}, bytes: {}", book.getTitle(), book.getTitle().getBytes());
        }
        return books;
    }

    public List<Book> findAvailableBooks() {
        return bookMapper.findAvailableBooks();
    }

    public Book findBookById(Integer id) {
        return bookMapper.findBookById(id);
    }

    public Book findBookByIsbn(String isbn) {
        return bookMapper.findBookByIsbn(isbn);
    }

    public List<Book> findBooksByCondition(Book book) {
        List<Book> books = bookMapper.findBooksByCondition(book);
        for (Book b : books) {
            logger.debug("Book title: {}, bytes: {}", b.getTitle(), b.getTitle().getBytes());
        }
        return books;
    }

    public boolean saveBook(Book book) {
        if (book.getId() == null) {
            // 新增图书
            return bookMapper.insertBook(book) > 0;
        } else {
            // 更新图书
            return bookMapper.updateBook(book) > 0;
        }
    }

    public boolean deleteBook(Integer id) {
        return bookMapper.deleteBook(id) > 0;
    }

    public boolean borrowBook(Integer bookId) {
        // 检查图书是否可借
        Book book = findBookById(bookId);
        if (book == null || book.getAvailableCopies() <= 0 || book.getStatus() != 1) {
            return false;
        }
        // 减少可用数量
        return bookMapper.decreaseAvailableCopies(bookId) > 0;
    }

    public boolean returnBook(Integer bookId) {
        // 增加可用数量
        return bookMapper.increaseAvailableCopies(bookId) > 0;
    }

    public int countBooks() {
        return bookMapper.countBooks();
    }
}