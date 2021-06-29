package com.xxxx.crm.dao;

import com.xxxx.crm.base.BaseMapper;
import com.xxxx.crm.vo.Role;

import java.util.List;
import java.util.Map;

public interface RoleMapper extends BaseMapper<Role, Integer> {

    // 查询所有的用户角色，只需要id和对应的名字即可
    // 因为数据库中有多条记录，每一条记录都是一个map，所以使用list进行存放,读取出来的数据因为是多条，所以要使用List接收
    // Mybatis返回的Map是这样的一种格式：Map<字段名称，字段值>, 一个Map<字段名，字段值>对象 代表数据库中的一行数据信息
    public List<Map<String, Object>> queryAllRoles(Integer userId);
}