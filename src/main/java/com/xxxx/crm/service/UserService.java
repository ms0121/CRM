package com.xxxx.crm.service;


import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.UserMapper;
import com.xxxx.crm.dao.UserRoleMapper;
import com.xxxx.crm.model.UserModel;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.utils.Md5Util;
import com.xxxx.crm.utils.PhoneUtil;
import com.xxxx.crm.utils.UserIDBase64;
import com.xxxx.crm.vo.User;
import com.xxxx.crm.vo.UserRole;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author lms
 * @date 2021-05-16 - 22:26
 * 首先用户登录的实现功能
 */

@Service
public class UserService extends BaseService<User, Integer> {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserRoleMapper userRoleMapper;

    /**
     * 1、参数判断
     * 用户姓名  非空判断
     * 用户密码  非空判断
     * 2、通过用户名查询数据库中用户的记录，返回用户对象
     * 3、判断用户对象是否为空
     * 4、如果用户对象不为空，则将前台传递的用户密码和数据库中的密码进行比较
     * 5、判断密码是否正确
     * 6、如果密码正确，则登录成功，返回结果
     */
    // 判断登录用户的账号和密码是否正确
    public UserModel userLogin(String userName, String userPwd) {
        // 1、参数判断 判断用户名和密码是否为空
        checkLoginParams(userName, userPwd);
        // 2、通过用户名查询数据库中用户的记录，返回用户对象
        User user = userMapper.queryUserByName(userName);
        // System.out.println("user = " + user);
        // 3、判断用户对象是否为空
        AssertUtil.isTrue(user == null, "用户名不存在");
        // 4、如果用户对象不为空，则将前台传递的用户密码和数据库中的密码进行比较
        checkUserPwd(userPwd, user.getUserPwd());
        // 返回登录用户的部分信息的对象,
        return buildUser(user);
    }

    /**
     * 更新密码，并设置事务的操作，
         1.通过用户ID查询用户对象，并判断用户是否存在
         2.参数校验：
             判断用户是否存在
             判断原始密码是否为空
             判断原始密码是否正确（查询用户对象中的密码是否与原始密码一致）
             判断新密码是否为空
             判断新密码是否与原始密码一致
             判断确认密码是否为空
             判断新密码是否与确认密码一致
         3.设置用户的新密码
            需要将新密码通过制定的算法进行加密(md5)
         4.执行更新的操作，判断受影响的行数
     * @param userId
     * @param oldPwd
     * @param newPwd
     * @param repeatPwd
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updatePassword(Integer userId, String oldPwd, String newPwd, String repeatPwd){
        // 1.通过用户ID查询用户对象，并判断用户是否存在
        User user = userMapper.selectByPrimaryKey(userId);
        // 判断用户是否存在
        AssertUtil.isTrue(user == null, "当前用户不存在");
        // 2.参数校验：
        checkPasswordParams(user, oldPwd, newPwd, repeatPwd);
        // 3.设置用户的新密码, 需要将新密码通过制定的算法进行加密(md5)
        user.setUserPwd(Md5Util.encode(newPwd));
        //  4.执行更新的操作，判断受影响的行数
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user)<1, "修改密码失败");
    }

    /**
     * 参数校验
     * @param user
     * @param oldPwd
     * @param newPwd
     * @param repeatPwd
     */
    private void checkPasswordParams(User user, String oldPwd, String newPwd, String repeatPwd) {
        // 判断原始密码是否为空
        AssertUtil.isTrue(StringUtils.isBlank(oldPwd), "原始密码不能为空");
        // 判断原始密码是否正确（查询用户对象中的密码是否与原始密码一致）
        AssertUtil.isTrue(!user.getUserPwd().equals(Md5Util.encode(oldPwd)), "原始密码不正确");
        // 判断新密码是否为空
        AssertUtil.isTrue(StringUtils.isBlank(newPwd), "新密码不能为空");
        // 判断新密码是否与原始密码一致
        AssertUtil.isTrue(oldPwd.equals(newPwd),"新密码不能与原始密码一致");
        // 判断确认密码是否为空
        AssertUtil.isTrue(StringUtils.isBlank(repeatPwd), "确认密码不能为空");
        // 判断新密码是否与确认密码一致
        AssertUtil.isTrue(!repeatPwd.equals(newPwd), "新密码与确认密码不一致");
    }

    // 构建需要返回前段的用户字段信息构成的对象
    private UserModel buildUser(User user) {
        UserModel userModel = new UserModel();
        // userModel.setUserIdStr(user.getId());
        userModel.setUserIdStr(UserIDBase64.encoderUserID(user.getId()));  // 给登录的用户id进行加密
        userModel.setUserName(user.getUserName());
        userModel.setTrueName(user.getTrueName());
        return userModel;
    }

    // 前台传递的用户密码和数据库中的密码进行比较
    private void checkUserPwd(String userPwd, String password) {
        //数据库中的密码是使用md5进行加密之后的结果
        String newUserPwd = Md5Util.encode(userPwd);
        // System.out.println("newUserPwd = " + newUserPwd);
        // 判断密码是否相等
        AssertUtil.isTrue(!newUserPwd.equals(password), "密码不相等，请重试");
    }

    // 判断参数是否为空
    private void checkLoginParams(String userName, String userPwd) {
        AssertUtil.isTrue(StringUtils.isBlank(userName), "用户名不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(userPwd), "用户密码不能为空");
    }

    /**
     * 查询所有的销售人员的信息
     * @return
     */
    public List<Map<String, Object>> queryAllSales(){
        return userMapper.queryAllSales();
    }

    /**
     * 添加用户的操作
     *  1.参数校验
     *      用户名userName  非空，唯一值
     *      邮箱email    非空
     *      手机号phone  非空，格式正确
     *  2.设置参数的默认值
     *      isValid       1
     *      createDate    系统当前的时间
     *      updateDate    系统当前的时间
     *      默认密码        123456 加密
     *  3.执行添加的操作
     * @param user
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addUser(User user){
        //1.参数校验
       checkUserParams(user.getUserName(), user.getEmail(), user.getPhone(), null);
        //2.设置参数的默认值
        user.setIsValid(1);
        user.setCreateDate(new Date());
        user.setUpdateDate(new Date());
        user.setUserPwd(Md5Util.encode("123456"));
        //3.执行添加的操作
        AssertUtil.isTrue(userMapper.insertSelective(user) < 1, "用户添加失败，请重试!");

        /**
         * 用户id，角色id
         * 用户的id直接通过sql语句的返回值进行获取
         */
        relationUserRole(user.getId(), user.getRoleIds());
    }

    /**
     * 用户角色关联
     *  添加操作
     *      原始角色不存在
     *          1.不添加新的角色记录    不操作用户角色表
     *          2.添加新的角色记录      给指定的用户绑定相关的角色记录
     *
     *  更新操作
     *      原始角色不存在
     *          1.不添加新的角色记录     不操作用户角色表
     *          2.添加新的角色记录       给指定用户绑定相关的角色记录
     *      原始角色存在
     *          1.添加新的角色记录       判断，已有的角色记录不添加，添加没有的角色记录
     *          2.清空所有的角色记录     删除用户绑定的角色记录
     *          3.删除部分角色记录       删除不存在的角色记录，存在的角色记录保存
     *          4.删除部分角色，添加新的角色    删除不存在的角色记录，存在的角色记录保存
     *
     *      如何进行角色分配？
     *          先判断用户的角色记录是否存在，然后将用户原有的角色记录进行删除，再重新添加新的角色记录
     *  删除操作
     *      删除指定用户锁定的角色记录
     *  @param userId  用户id
     *  @param roleIds  角色id
     */
    private void relationUserRole(Integer userId, String roleIds) {
        // 通过用户Id查询角色记录
        Integer count = userRoleMapper.countUserRoleByUserId(userId);
        // 判断角色记录是否存在
        if (count > 0){
            // 如果角色记录存在，则删除该用户对应的用户记录
            AssertUtil.isTrue(userRoleMapper.deleteUserRoleByUserId(userId) != count, "用户角色分配失败!");
        }
        // 判断用户角色是否存在，如果存在，则添加该用户对应的角色记录
        // roleIds: 添加用户的下拉框选择的角色对应的id的字符串
        if (StringUtils.isNotBlank(roleIds)){
            // 将用户角色数据添加到列表中，执行批量添加到用户角色表中
            List<UserRole> userRoleList = new ArrayList<>();
            // 将用户角色id字符串转为数组,字符串是按照 , 连接的
            String[] roleIdsArray = roleIds.split(",");
            for (String roleId : roleIdsArray) {
                UserRole userRole = new UserRole();
                // 设置用户角色对象的属性值，
                userRole.setRoleId(Integer.parseInt(roleId));
                userRole.setUserId(userId);
                userRole.setCreateDate(new Date());
                userRole.setUpdateDate(new Date());
                // 将被选择的每个用户角色封装好加入到列表中
                userRoleList.add(userRole);
            }
            // 添加用户角色记录，userRoleMapper.insertBatch执行成功返回的值是执行的行数
            AssertUtil.isTrue(userRoleMapper.insertBatch(userRoleList) != userRoleList.size(), "用户角色添加失败!");
        }
    }

    /**
     * 参数校验
     *    用户名userName  非空，唯一值
     *    邮箱email    非空
     *    手机号phone  非空，格式正确
     * @param userName
     * @param email
     * @param phone
     */
    private void checkUserParams(String userName, String email, String phone, Integer userId) {
        //  用户名userName  非空，唯一值
        AssertUtil.isTrue(StringUtils.isBlank(userName), "用户名不能为空!");
        // 判断唯一值,数据库中找到的用户
        User user = userMapper.queryUserByName(userName);
        // 修改的时候，查询得到的用户id等于自身没问题（就是修改当前记录但是不修改名字），但是添加的时候不允许重名
        AssertUtil.isTrue((user != null) && !(user.getId().equals(userId)), "用户名已存在，请重试!");
        //  邮箱email    非空
        AssertUtil.isTrue(StringUtils.isBlank(email), "邮箱不能为空!");
        //  手机号phone  非空，格式正确
        AssertUtil.isTrue(StringUtils.isBlank(phone), "手机号不能为空!");
        // 手机号格式判断
        AssertUtil.isTrue(!PhoneUtil.isMobile(phone), "手机号格式不正确!");

    }

    /**
     * 更新用户
     *  1. 参数校验
     *     id  非空  记录必须存在
     *     用户名 非空  唯一性
     *     email 非空
     *     手机号 非空 格式合法
     *  2. 设置默认参数
     *     updateDate
     *  3. 执行更新，判断结果
     * @param user
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUser(User user){
        // id  非空  记录必须存在
        AssertUtil.isTrue(user.getId() == null, "待更新记录不存在！");
        User user1 = userMapper.selectByPrimaryKey(user.getId());
        AssertUtil.isTrue(user1 == null, "待更新记录不存在！");
        checkUserParams(user.getUserName(), user.getEmail(), user.getPhone(), user.getId());
        // 设置更新时间
        user.setUpdateDate(new Date());
        // 执行更新的操作
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user) != 1, "用户更新失败，请重试!");
        relationUserRole(user.getId(), user.getRoleIds());
    }

    /**
     * 删除用户
     * @param ids
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteUser(Integer[] ids){
        AssertUtil.isTrue(ids == null || ids.length == 0, "用户不存在");
        AssertUtil.isTrue(deleteBatch(ids) != ids.length, "用户删除失败，请重试!");
        // 遍历删除的用户id
        for (Integer userId : ids) {
            // 查询该用户id对应的用户拥有的角色
            Integer count = userRoleMapper.countUserRoleByUserId(userId);
            if (count > 0){
                // 删除所有的对应的用户角色
                userRoleMapper.deleteUserRoleByUserId(userId);
            }
        }
    }
}

