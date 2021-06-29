package com.xxxx.crm.dao;

import com.xxxx.crm.base.BaseMapper;
import com.xxxx.crm.vo.SaleChance;

public interface SaleChanceMapper extends BaseMapper<SaleChance, Integer> {

    // public List<T> selectByParams(BaseQuery baseQuery) throws DataAccessException;
    // 父类 BaseMapper里面已经包括了多条件查询的方法
    // 多条件查询的接口不需要单独的定义，由于多个模块涉及多条件查询操作，所以将对应多条件查询的
    // 功能直接定义在父接口中


    // 父接口中已经有默认的插入对象的方法

}