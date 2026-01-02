package com.zjq.java_ee_starter.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

import com.zjq.java_ee_starter.entity.*;

@Mapper
public interface UserMapper {

    // 插入用户
    @Insert("""
                INSERT INTO users (
                    user_name,
                    password,
                    email,
                    phone,
                    role,
                    status
                ) VALUES (
                    #{userName},
                    #{password},
                    #{email},
                    #{phone},
                    #{role},
                    #{status}
                )
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertUser(User user);

    // 根据ID查询用户
    @Select("select * from users where id = #{id}")
    User findUserbyId(@Param("id") Integer id);

    @Select("select * from users where email = #{email}")
    User findUserbyEmail(@Param("email") String email);

    @Select("select * from users where user_name = #{userName}")
    User findUserbyName(@Param("userName") String userName);

    // 根据所有用户
    @Select("SELECT * FROM users ORDER BY create_time DESC")
    List<User> findAllUsers();

    @Update("UPDATE users SET user_name=#{userName}, password=#{password}, email=#{email}, "
            + "phone=#{phone}, role=#{role}, status=#{status} WHERE id=#{id}")
    int updateUserbyId(User user);

    @Delete("DELETE FROM users WHERE id = #{id}")
    int deleteUserbyId(@Param("id") Integer id);

    @Delete("DELETE FROM users")
    int deleteAllUsers();

    // 统计用户数
    @Select("SELECT COUNT(*) FROM users")
    int countUsers();

    // 多条件查询
    List<User> selectUsersXml(User user);

    // 动态更新
    int updateUserbyIdXml(@Param("id") Long id,
            @Param("userName") String userName,
            @Param("password") String password,
            @Param("email") String email,
            @Param("phone") String phone);

    // 动态排序
    List<User> selectUserswithOrderXml(@Param("orderby") String orderby,
            @Param("sortOrder") String sortOrder);

    // 多值查询
    List<User> selectUsersByEmailListXml(@Param("emailList") String[] emailList);

    // 批量插入
    int batchInsertUsersXml(@Param("userList") List<User> userList);

    // 批量更新不同字段
    int batchUpdateUsersXml(@Param("updateList") List<Map<String, Object>> updateList);

}
