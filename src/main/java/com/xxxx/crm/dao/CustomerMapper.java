package com.xxxx.crm.dao;

import com.xxxx.crm.base.BaseMapper;
import com.xxxx.crm.query.CustomerQuery;
import com.xxxx.crm.vo.Customer;

import java.util.List;
import java.util.Map;

public interface CustomerMapper extends BaseMapper<Customer, Integer> {

    // 根据客户名称查询用户对象
    Customer queryCustomerByName(String name);

    // 查询所有的流失客户
    public List<Customer> queryLossCustomers();

    // 通过客户id批量更新客户流失状态
    int updateCustomerStateByIds(List<Integer> lossCustomerIds);

    // 查询客户贡献度
    List<Map<String, Object>> queryCustomerContributionByParams(CustomerQuery customerQuery);
}