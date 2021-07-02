package com.xxxx.crm.service;

import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.PermissionMapper;
import com.xxxx.crm.vo.Permission;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lms
 * @date 2021-07-01 - 14:40
 */
@Service
public class PermissionService extends BaseService<Permission, Integer> {

    @Resource
    private PermissionMapper permissionMapper;

    /**
     * 通过查询用户拥有的角色，角色拥有的资源，得到拥有的资源列表(资源权限码)
     * 通过当前登录的用户id查询当前登录用户所拥有的资源列表(查询对应资源的授权码)
     * @param id
     * @return
     */
    public List<String> queryUserHasPermissionByUserId(Integer id) {
        return permissionMapper.queryUserHasPermissionByUserId(id);
    }
}
