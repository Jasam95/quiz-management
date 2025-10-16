package com.example.quiz_management.service;

import com.example.quiz_management.dto.UserDto;
import com.example.quiz_management.entity.User;

import java.util.List;

public interface UserLoginService {

        void createUser(UserDto userDto , String roles);

        UserDto findUserByEmail(String email);

        List<UserDto> findAllUsers();


    User findByEmail(String name);


    long countByRoleName(String participant);
}
