package com.zjq.java_ee_starter.mapper;

import org.apache.ibatis.annotations.*;
import java.util.List;
import com.zjq.java_ee_starter.entity.Book;

@Mapper
public interface BookMapper {

    @Insert("INSERT INTO books (title, author, isbn, category, description, total_copies, available_copies, status) " +
            "VALUES (#{title}, #{author}, #{isbn}, #{category}, #{description}, #{totalCopies}, #{availableCopies}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertBook(Book book);

    @Select("SELECT * FROM books WHERE id = #{id}")
    Book findBookById(@Param("id") Integer id);

    @Select("SELECT * FROM books WHERE isbn = #{isbn}")
    Book findBookByIsbn(@Param("isbn") String isbn);

    @Select("SELECT * FROM books ORDER BY create_time DESC")
    List<Book> findAllBooks();

    @Select("SELECT * FROM books WHERE status = 1 ORDER BY create_time DESC")
    List<Book> findAvailableBooks();

    @Update("UPDATE books SET title=#{title}, author=#{author}, isbn=#{isbn}, category=#{category}, " +
            "description=#{description}, total_copies=#{totalCopies}, available_copies=#{availableCopies}, " +
            "status=#{status} WHERE id=#{id}")
    int updateBook(Book book);

    @Update("UPDATE books SET available_copies = available_copies - 1 WHERE id = #{id} AND available_copies > 0")
    int decreaseAvailableCopies(@Param("id") Integer id);

    @Update("UPDATE books SET available_copies = available_copies + 1 WHERE id = #{id}")
    int increaseAvailableCopies(@Param("id") Integer id);

    @Delete("DELETE FROM books WHERE id = #{id}")
    int deleteBook(@Param("id") Integer id);

    @Select("SELECT COUNT(*) FROM books")
    int countBooks();

    List<Book> findBooksByCondition(Book book);
}