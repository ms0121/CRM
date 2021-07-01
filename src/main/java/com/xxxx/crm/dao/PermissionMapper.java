package com.xxxx.crm.dao;

import com.xxxx.crm.base.BaseMapper;
import com.xxxx.crm.vo.Permission;

import java.util.List;

public interface PermissionMapper extends BaseMapper<Permission, Integer> {

    // 查询当前角色用户的权限记录
    Integer countPermissionByRoleId(Integer roleId);

    // 删除当前角色拥有的权限记录
    void deletePermissionByRoleId(Integer roleId);

    // 查询当前的roleId拥有的资源id
    List<Integer> queryRoleHasModuleIdsByRoleId(Integer roleId);
}