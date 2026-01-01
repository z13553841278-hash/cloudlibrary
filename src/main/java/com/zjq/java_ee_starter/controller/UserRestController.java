package com.zjq.java_ee_starter.controller;

import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.beans.BeanUtils;
import com.zjq.java_ee_starter.service.UserService;

import com.zjq.java_ee_starter.dto.UserDTO;
import com.zjq.java_ee_starter.entity.User;

@RestController
@RequestMapping(value = "/api/users")
public class UserRestController {
   private final UserService userService;

   public UserRestController(UserService userService) {
      this.userService = userService;
   }

   @PostMapping(value = "")
   public boolean createUser(@RequestBody UserDTO dto) {
      // 转换为User实体
      User user = new User();
      BeanUtils.copyProperties(dto, user);
      return userService.saveUser(user);
   }

   @GetMapping(value = "/{id}")
   public UserDTO getUser(@PathVariable("id") Integer id) {
      User user = userService.findUserbyId(id);
      UserDTO dto = new UserDTO();
      BeanUtils.copyProperties(user, dto);
      return dto;
   }

   @GetMapping(params = "name")
   public UserDTO getUserbyName(@RequestParam("name") String userName) {
      User user = userService.findUserbyName(userName);
      UserDTO dto = new UserDTO();
      BeanUtils.copyProperties(user, dto);
      return dto;
   }

   @GetMapping(value = "")
   public List<User> getUsers() {
      List<User> users = userService.findAllUsers();
      return users;
   }

   @GetMapping(params = { "userName", "email", "phone" })
   public List<User> getUsersbyMultipleCondition(User user) {
      List<User> users = userService.findUsersbyMultipleCondition(user);
      return users;
   }

   @GetMapping(params = "emails")
   public List<User> getUsersbyEmails(@RequestParam("emails") String[] emails) {
      List<User> users = userService.findUsersbyEmails(emails);
      return users;
   }

   @PutMapping(value = "/{id}")
   public boolean updateUser(@PathVariable("id") Integer id, @RequestBody UserDTO dto) {
      dto.setId(id);
      // 转换为User实体
      User user = new User();
      BeanUtils.copyProperties(dto, user);
      return userService.saveUser(user);
   }

   @DeleteMapping(value = "/{id}")
   public boolean deleteUser(@PathVariable("id") Integer id) {
      return userService.deleteUserbyId(id);
   }
}
