package com.xxxx.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.CusDevPlanMapper;
import com.xxxx.crm.dao.SaleChanceMapper;
import com.xxxx.crm.query.CusDevPlanQuery;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.vo.CusDevPlan;
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
 * @date 2021-06-02 - 21:37
 */

@Service
public class CusDevPlanService extends BaseService<CusDevPlan, Integer> {

    @Resource
    private CusDevPlanMapper cusDevPlanMapper;

    @Resource
    private SaleChanceMapper saleChanceMapper;


    /**
     * 多条件分页查询营销开发计划，返回的数据格式必须满足layui中数据表格要求的格式(map(String,Object))
     * 否则无法在页面上进行展示
     * @param cusDevPlanQuery
     * @return
     */
    public Map<String, Object> queryCusDevPlanByParams(CusDevPlanQuery cusDevPlanQuery){
        Map<String, Object> map = new HashMap<>();
        // 开启分页（设置起始页和每页的size）
        PageHelper.startPage(cusDevPlanQuery.getPage(), cusDevPlanQuery.getLimit());
        // 查询所有的SaleChance数据信息,按条件查询数据(数据封装在对象中)
        List<CusDevPlan> cusDevPlans = cusDevPlanMapper.selectByParams(cusDevPlanQuery);
        // 将需要分页的数据列表传入到PageInfo中，得到对应的分页对象
        PageInfo<CusDevPlan> pageInfo = new PageInfo<>(cusDevPlans);

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
     * 添加客户开发计划数据
     * 1.参数校验
     *      营销机会id  非空，数据存在
     *      计划项内容   非空
     *      计划时间    非空
     * 2.设置参数的默认值
     *      是否有效    默认有效
     *      创建时间    系统当前时间
     *      修改时间    系统当前的时间
     * 3.执行添加操作，判断受影响的行数
     * @param cusDevPlan
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addCusDevService(CusDevPlan cusDevPlan){
        // 1.参数校验
        checkCusDevPlanParams(cusDevPlan);
        // 2.设置参数的默认值
        // 是否有效    默认有效
        cusDevPlan.setIsValid(1);
        // 创建时间    系统当前时间
        cusDevPlan.setCreateDate(new Date());
        // 修改时间    系统当前的时间
        cusDevPlan.setUpdateDate(new Date());
        AssertUtil.isTrue(cusDevPlanMapper.insertSelective(cusDevPlan) < 1, "计划项数据添加失败");
    }

    /**
     * 1.参数校验
     *    营销机会id  非空，数据存在
     *    计划项内容   非空
     *    计划时间    非空
     * @param cusDevPlan
     */
    private void checkCusDevPlanParams(CusDevPlan cusDevPlan) {
        // 营销机会id  非空，数据存在
        Integer saleChanceId = cusDevPlan.getSaleChanceId();
        AssertUtil.isTrue(saleChanceId==null || saleChanceMapper.selectByPrimaryKey(saleChanceId)==null,
                "数据异常，请重试!");
        // 计划项内容   非空
        AssertUtil.isTrue(StringUtils.isBlank(cusDevPlan.getPlanItem()), "计划项内容不为空");
        // 计划时间    非空
        AssertUtil.isTrue(cusDevPlan.getPlanDate()== null, "计划时间不能为空");
    }


    /**
     * 更新计划项
     *  1.参数校验
     *      id  非空 记录存在
     *      营销机会id 非空 记录必须存在
     *      计划项内容  非空
     *      计划项时间 非空
     *  2.参数默认值设置
     *      updateDate
     *  3.执行更新  判断结果
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateCusDevService(CusDevPlan cusDevPlan){
        // 1.参数校验
        //   id  非空 记录存在
        AssertUtil.isTrue(cusDevPlan.getId() == null, "数据异常，请重试");
        // 营销机会id 非空 记录必须存在
        // 计划项内容  非空
        // 计划项时间 非空
        checkCusDevPlanParams(cusDevPlan);
        // 2.参数默认值设置 updateDate
        cusDevPlan.setUpdateDate(new Date());
        // 3.执行更新  判断结果
        AssertUtil.isTrue(cusDevPlanMapper.updateByPrimaryKeySelective(cusDevPlan) < 1, "计划项数据更新失败");
    }


    /**
     * 根据用户的id进行删除对应的用户开发计划,将对应的用户信息的 isvalid 属性更新为0
     * @param id
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteCusDevPlan(Integer id){
        AssertUtil.isTrue(id == null, "用户信息不存在!");
        CusDevPlan cusDevPlan = cusDevPlanMapper.selectByPrimaryKey(id);
        AssertUtil.isTrue(cusDevPlan == null, "用户信息不存在!");
        // 设置IsValid为0
        cusDevPlan.setIsValid(0);
        // 更新用户的信息
        Integer flag = cusDevPlanMapper.updateByPrimaryKeySelective(cusDevPlan);
        AssertUtil.isTrue(flag < 1, "删除失败，请重试!");
    }
}









