package com.xxxx.crm.service;

import com.xxxx.crm.model.UserModel;


/**
 * @author lms
 * @date 2021-05-17 - 10:56
 */
class UserServiceTest {

    public static void main(String[] args) {
        UserService userService = new UserService();
        UserModel userModel = userService.userLogin("admin", "123456");
        System.out.println("userModel = " + userModel);
    }
}