package com.xxxx.crm.service;

import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.RoleMapper;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.vo.Role;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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




}
