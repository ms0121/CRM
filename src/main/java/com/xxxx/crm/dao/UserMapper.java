package com.xxxx.crm.dao;

import com.xxxx.crm.base.BaseMapper;
import com.xxxx.crm.vo.User;

import java.util.List;
import java.util.Map;

//需要将该组件添加到容器当中

public interface UserMapper extends BaseMapper<User, Integer> {
    // 根据用户名查询用户信息
    public User queryUserByName(String username);

    // 查询所有的销售人员
    List<Map<String, Object>> queryAllSales();

    // 查询的所有的客户经理
    List<Map<String, Object>> queryAllCustomerManagers();


}