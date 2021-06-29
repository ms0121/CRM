package com.xxxx.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.SaleChanceMapper;
import com.xxxx.crm.enums.DevResult;
import com.xxxx.crm.enums.StateStatus;
import com.xxxx.crm.query.SaleChanceQuery;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.utils.PhoneUtil;
import com.xxxx.crm.vo.SaleChance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lms
 * @date 2021-05-30 - 14:19
 */

@Service
public class SaleChanceService extends BaseService<SaleChance, Integer> {

    @Resource
    private SaleChanceMapper saleChanceMapper;

    /**
     * 多条件分页查询营销机会，返回的数据格式必须满足layui中数据表格要求的格式(map(String,Object))
     * 否则无法在页面上进行展示
     * @param saleChanceQuery
     * @return
     */
    public Map<String, Object> querySaleChanceByParams(SaleChanceQuery saleChanceQuery){
        Map<String, Object> map = new HashMap<>();
        // 开启分页（设置起始页和每页的size）
        PageHelper.startPage(saleChanceQuery.getPage(), saleChanceQuery.getLimit());
        // 查询所有的SaleChance数据信息,按条件查询数据(数据封装在对象中)
        List<SaleChance> saleChances = saleChanceMapper.selectByParams(saleChanceQuery);
        // 将需要分页的数据列表传入到PageInfo中，得到对应的分页对象
        PageInfo<SaleChance> pageInfo = new PageInfo<>(saleChances);

        // 设置满足layui表单数据的map形式的对象数据给controller
        map.put("code", 0);
        map.put("msg", "success");
        // 数据的总数
        map.put("count",pageInfo.getTotal());
        // 设置分页好的列表
        map.put("data",pageInfo.getList());
        return map;
    }

    /**
     * 添加营销机会思路：
     *    1.参数校验
     *      customerName 客户名称      非空
     *      linkMan 联系人             非空
     *      linkPhone 联系手机号       非空，手机号码格式正确
     *
     *    2.设置相关字段的默认值
     *      createMan 创建人     当前的登录用户（从controller中获取）
     *      assignMan 指派人
     *          如果未设置指派人（默认）
     *              state分配状态   (0未分配 1已分配)
     *                  0 = 未分配
     *              assignTime指派时间
     *                  设置为null
     *              devResult开发状态 0未开发  1开发中  2开发成功  3开发失败
     *                  0 未开发 默认
     *          如果设置了指派人
     *              state分配状态   (0未分配 1已分配)
*                         1 = 已分配
*                   assignTime指派时间
*                        设置为系统当前时间
*                   devResult开发状态 0未开发  1开发中  2开发成功  3开发失败
*                         1 = 开发中
     *
     *       isValid 是否有效  0无效   1 有效
     *             设置为有效 1 = 有效
     *       createDate  创建时间
     *              默认是使用系统当前的时间
     *       updateDate  更新时间
     *              默认是使用系统当前的时间
     *
     *    3.执行添加操作，判断受到影响的行数
     */
    // 给添加操作执行事务的操作
    @Transactional(propagation = Propagation.REQUIRED)
    public void addSaleChanceParams(SaleChance saleChance){
        // 1. 参数校验
        checkSaleChanceParams(saleChance.getCustomerName(), saleChance.getLinkMan(), saleChance.getLinkPhone());
        // 2. 设置相关字段的默认值
        // isValid 设置是否有效
        saleChance.setIsValid(1);
        //  createDate  创建时间
        saleChance.setCreateDate(new Date());
        // updateDate  更新时间
        saleChance.setUpdateDate(new Date());

        // 判断是否设置了指派人
        if (StringUtils.isBlank(saleChance.getAssignMan())){
            // 如果为空，表示没设置了指派人
            // 设置分配状态( 0 = 未分配)
            saleChance.setState(StateStatus.UNSTATE.getType());
            // 设置指派的时间
            saleChance.setAssignTime(null);
            // 设置开发的状态：   0 未开发 默认
            saleChance.setDevResult(DevResult.UNDEV.getStatus());
        }else {
            // 设置了指派人
            // 设置分配状态( 1 = 已分配)
            saleChance.setState(StateStatus.STATED.getType());
            // 设置指派的时间（当前系统的时间）
            saleChance.setAssignTime(new Date());
            // 设置开发的状态：  1 = 开发中
            saleChance.setDevResult(DevResult.DEVING.getStatus());
        }
        // 3.执行添加操作，判断受到影响的行数
        AssertUtil.isTrue(saleChanceMapper.insertSelective(saleChance) < 1, "营销机会添加失败!");
    }


    /**
     更新营销机会
     *    1.参数校验
     *      用户ID  非空，数据库中对应的记录必须存在
     *      customerName 客户名称      非空
     *      linkMan 联系人             非空
     *      linkPhone 联系手机号        非空，手机号码格式正确
     *
     *   2.设置相关参数的默认值
     *      updateDate更新时间，设置为系统当前的时间
     *      assignMan 指派人
     *          原始数据未设置
     *              修改后未设置
     *                  不需要操作
     *              修改后已设置
     *                  assignTime 指派时间  设置为系统当前的时间
     *                  分配状态  1=已分配
     *                  开发状态  1=开发中
     *
     *          原始数据已设置
     *              修改后设置
     *                  修改后未设置
*                           assignTime 指派时间  设置为系统当前的时间
                            分配状态  0=未分配
                            开发状态  0=未发中
 *                    修改后已设置
     *                      判断修改前后是否是一个指派人
     *                          如果是，则不要操作
     *                          如果不是，则需要更新assignTime指派时间，设置为当前系统时间
     *
     *   3.执行更新操作，判断受到影响的行数
     *
     * @param saleChance
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateSaleChance(SaleChance saleChance) {
        //1.参数校验
        //  用户ID  非空，数据库中对应的记录必须存在
        AssertUtil.isTrue(saleChance.getId() == null, "待更新记录不存在");
        SaleChance temp = saleChanceMapper.selectByPrimaryKey(saleChance.getId());
        AssertUtil.isTrue(temp==null, "待更新记录不存在");
        // 参数校验
        checkSaleChanceParams(saleChance.getCustomerName(), saleChance.getLinkMan(), saleChance.getLinkPhone());

        //2.设置相关参数的默认值
        //  updateDate更新时间，设置为系统当前的时间
        saleChance.setUpdateDate(new Date());
        //  assignMan 指派人
        // 判断原始数据是否存在
        if (StringUtils.isBlank(temp.getAssignMan())){ //不存在
            //  判断修改后的值是否存在
            if (!StringUtils.isBlank(saleChance.getAssignMan())){
                // 修改前为空，修改后有值
                //  assignTime 指派时间  设置为系统当前的时间
                saleChance.setAssignTime(new Date());
                //  分配状态  1=已分配
                saleChance.setState(StateStatus.STATED.getType());
                //  开发状态  1=开发中
                saleChance.setDevResult(DevResult.DEVING.getStatus());
            }
        }else { // 存在
            // 判断修改后的值是否存在
            if (StringUtils.isBlank(saleChance.getAssignMan())){
                // 修改前有值，修改后无值
                // assignTime 指派时间  设置为系统当前的时间
                saleChance.setAssignTime(new Date());
                // 分配状态  0=未分配
                saleChance.setState(StateStatus.UNSTATE.getType());
                // 开发状态  0=未发中
                saleChance.setDevResult(DevResult.UNDEV.getStatus());
            }else {
                // 修改前有值，修改后也有值
                // 判断修改前后是否是同一个用户
                if (!saleChance.getAssignMan().equals(temp.getAssignMan())){
                    // 如果指派前后是同一个人，则修改指派时间
                    saleChance.setAssignTime(new Date());
                }else {
                    // 设置指派时间为修改前的时间
                    saleChance.setAssignTime(temp.getAssignTime());
                }
            }
        }

        // 3.执行更新操作，判断受到影响的行数
        AssertUtil.isTrue(saleChanceMapper.updateByPrimaryKeySelective(saleChance) < 1, "营销机会更新失败!");
    }


    /**
     1.参数校验
     *      customerName 客户名称      非空
     *      linkMan 联系人             非空
     *      linkPhone 联系手机号        非空，手机号码格式正确
     * @param customerName
     * @param linkMan
     * @param linkPhone
     */
    private void checkSaleChanceParams(String customerName, String linkMan, String linkPhone) {
        AssertUtil.isTrue(StringUtils.isBlank(customerName), "用户名不能为空!");
        AssertUtil.isTrue(StringUtils.isBlank(linkMan), "联系人不能为空!");
        AssertUtil.isTrue(StringUtils.isBlank(linkPhone), "联系手机号码不能为空!");
        //  linkPhone 联系手机号        手机号码格式验证
        AssertUtil.isTrue(!PhoneUtil.isMobile(linkPhone), "手机号码格式不正确！");
    }


    /**
     * 删除营销机会的操作
     * @param ids
     */
    public void deleteSaleChance(Integer[] ids){
        // 判断id是否为空
        AssertUtil.isTrue(ids==null || ids.length < 1, "待删除记录不存在");
        // 执行删除的操作，判断受影响的行数
        AssertUtil.isTrue(saleChanceMapper.deleteBatch(ids) != ids.length, "营销机会数据删除失败");

    }

    /**
     * 修改营销计划的开发状态
     * @param id 用户id
     * @param devResult 用户的开发状态
     */
    public void updateSaleChanceDevResult(Integer id, Integer devResult) {
        AssertUtil.isTrue(id == null, "该数据不存在!");
        SaleChance saleChance = saleChanceMapper.selectByPrimaryKey(id);
        AssertUtil.isTrue(saleChance == null, "该数据不存在!");
        // 设置当前用户的开发状态
        saleChance.setDevResult(devResult);
        // 更新用户的开发状态信息
        AssertUtil.isTrue(saleChanceMapper.updateByPrimaryKeySelective(saleChance) < 1, "开发状态更新失败!");
    }
}
















