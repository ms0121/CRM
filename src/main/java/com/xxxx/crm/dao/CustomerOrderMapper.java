package com.xxxx.crm.dao;

import com.xxxx.crm.base.BaseMapper;
import com.xxxx.crm.vo.CustomerOrder;

import java.util.Map;

public interface CustomerOrderMapper extends BaseMapper<CustomerOrder, Integer> {

    // 根据roleId查询订单详情信息
    Map<String, Object> queryOrderDetailByRoleId(Integer roleId);
}