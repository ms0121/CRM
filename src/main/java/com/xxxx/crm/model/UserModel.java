package com.xxxx.crm.model;

/**
 * @author lms
 * @date 2021-05-16 - 23:19
 * 用于封装需要返回到页面中的user的部分属性信息（将需要的用户信息进行返回到控制层）
 */
public class UserModel {
//    private Integer userId;
    private String userIdStr;  // 将用户的Id进行加密，防止泄露
    private String userName;
    private String trueName;

//    public Integer getUserId() {
//        return userId;
//    }
//
//    public void setUserId(Integer userId) {
//        this.userId = userId;
//    }


    public String getUserIdStr() {
        return userIdStr;
    }

    public void setUserIdStr(String userIdStr) {
        this.userIdStr = userIdStr;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }
}
