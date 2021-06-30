package com.xxxx.crm.service;

import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.RoleMapper;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.vo.Role;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author lms
 * @date 2021-06-14 - 16:11
 */

@Service
public class RoleService extends BaseService<Role, Integer> {

    @Resource
    private RoleMapper roleMapper;

    /**
     * 查询所有的角色
     * @param userId
     * @return
     */
    public List<Map<String, Object>> queryAllRoles(Integer userId){
        return roleMapper.queryAllRoles(userId);
    }

    /**
     * 添加角色：
     *  1.参数校验
     *      角色名称   非空，唯一
     *  2.设置参数的默认值
     *      是否有效
     *      创建时间
     *      修改时间
     *  3.执行添加操作，判断受影响的行数
     * @param role
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addRole(Role role){
        // 参数校验
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()), "角色名称不能为空!");
        //通过角色名称查询数据库中的角色信息
        Role temp = roleMapper.selectByRoleName(role.getRoleName());
        // 判断角色是否存在数据库当中
        AssertUtil.isTrue(temp != null, "该角色名称已存在，请重试!");
        // 设置参数的默认值
        role.setIsValid(1);
        role.setCreateDate(new Date());
        role.setUpdateDate(new Date());
        AssertUtil.isTrue(roleMapper.insertSelective(role) < 1, "角色添加失败!");
    }

    /**
     * 更新角色
     *  1.参数校验
     *      角色ID   非空，并且数据唯一
     *      角色名称  非空，角色唯一
     *  2.设置参数的默认值
     *      修改时间
     *  3.执行更新操作，判断受影响的行数
     * @param role
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateRole(Role role){
        // 1. 参数校验
        AssertUtil.isTrue(role.getId() == null, "角色不存在!");
        // 根据角色的id查询数据库中的用户信息
        Role temp = roleMapper.selectByPrimaryKey(role.getId());
        AssertUtil.isTrue(temp == null, "待更新记录不存在!");
        // 判断修改后的角色名称是否存在
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()), "角色用户名称不能为空!");
        // 判断修改后的名称是否已存在
        AssertUtil.isTrue(role.getRoleName().equals(temp.getRoleName()), "角色用户名已存在，请重试!");

        // 2. 修改参数的默认值
        role.setUpdateDate(new Date());
        // 执行修改的操作
        AssertUtil.isTrue(roleMapper.updateByPrimaryKeySelective(role) < 1, "角色修改失败!");
    }
}
