package com.xxxx.crm.dao;

import com.xxxx.crm.base.BaseMapper;
import com.xxxx.crm.vo.Customer;

public interface CustomerMapper extends BaseMapper<Customer, Integer> {

    // 根据客户名称查询用户对象
    Customer queryCustomerByName(String name);
}