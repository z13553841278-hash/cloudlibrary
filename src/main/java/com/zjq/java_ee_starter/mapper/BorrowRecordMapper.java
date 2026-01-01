package com.zjq.java_ee_starter.mapper;

import org.apache.ibatis.annotations.*;
import java.util.List;
import com.zjq.java_ee_starter.entity.BorrowRecord;

@Mapper
public interface BorrowRecordMapper {

    @Insert("INSERT INTO borrow_records (user_id, book_id, borrow_date, due_date, status) " +
            "VALUES (#{userId}, #{bookId}, #{borrowDate}, #{dueDate}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertBorrowRecord(BorrowRecord record);

    @Select("SELECT * FROM borrow_records WHERE id = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "bookId", column = "book_id"),
            @Result(property = "borrowDate", column = "borrow_date"),
            @Result(property = "dueDate", column = "due_date"),
            @Result(property = "returnDate", column = "return_date"),
            @Result(property = "status", column = "status"),
            @Result(property = "user", column = "user_id", one = @One(select = "com.zjq.java_ee_starter.mapper.UserMapper.findUserbyId")),
            @Result(property = "book", column = "book_id", one = @One(select = "com.zjq.java_ee_starter.mapper.BookMapper.findBookById"))
    })
    BorrowRecord findBorrowRecordById(@Param("id") Integer id);

    @Select("SELECT br.*, u.user_name, b.title, b.author FROM borrow_records br " +
            "LEFT JOIN users u ON br.user_id = u.id " +
            "LEFT JOIN books b ON br.book_id = b.id " +
            "WHERE br.user_id = #{userId} AND br.status = 0 ORDER BY br.borrow_date DESC")
    List<BorrowRecord> findCurrentBorrowRecordsByUserId(@Param("userId") Integer userId);

    @Select("SELECT br.*, u.user_name, b.title, b.author FROM borrow_records br " +
            "LEFT JOIN users u ON br.user_id = u.id " +
            "LEFT JOIN books b ON br.book_id = b.id " +
            "WHERE br.user_id = #{userId} ORDER BY br.borrow_date DESC")
    List<BorrowRecord> findAllBorrowRecordsByUserId(@Param("userId") Integer userId);

    @Select("SELECT br.*, u.user_name, b.title, b.author FROM borrow_records br " +
            "LEFT JOIN users u ON br.user_id = u.id " +
            "LEFT JOIN books b ON br.book_id = b.id " +
            "ORDER BY br.borrow_date DESC")
    List<BorrowRecord> findAllBorrowRecords();

    @Update("UPDATE borrow_records SET return_date = #{returnDate}, status = #{status} WHERE id = #{id}")
    int updateReturnDate(@Param("id") Integer id, @Param("returnDate") java.time.LocalDateTime returnDate,
            @Param("status") Integer status);

    @Select("SELECT COUNT(*) FROM borrow_records WHERE user_id = #{userId} AND book_id = #{bookId} AND status = 0")
    int countUnreturnedByUserAndBook(@Param("userId") Integer userId, @Param("bookId") Integer bookId);

    @Select("SELECT COUNT(*) FROM borrow_records WHERE book_id = #{bookId} AND status = 0")
    int countBorrowedByBookId(@Param("bookId") Integer bookId);
}