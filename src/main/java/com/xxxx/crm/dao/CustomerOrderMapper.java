package com.xxxx.crm.dao;

import com.xxxx.crm.vo.CustomerOrder;

public interface CustomerOrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CustomerOrder record);

    int insertSelective(CustomerOrder record);

    CustomerOrder selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CustomerOrder record);

    int updateByPrimaryKey(CustomerOrder record);
}