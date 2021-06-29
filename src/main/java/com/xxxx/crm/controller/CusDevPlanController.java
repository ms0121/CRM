package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.query.CusDevPlanQuery;
import com.xxxx.crm.service.CusDevPlanService;
import com.xxxx.crm.service.SaleChanceService;
import com.xxxx.crm.vo.CusDevPlan;
import com.xxxx.crm.vo.SaleChance;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author lms
 * @date 2021-06-02 - 20:06
 */
@Controller
@RequestMapping("cus_dev_plan")
public class CusDevPlanController extends BaseController {

    @Resource
    private SaleChanceService saleChanceService;

    @Resource
    private CusDevPlanService cusDevPlanService;


    @RequestMapping("index")
    public String index(){
        return "cusDevPlan/cus_dev_plan";
    }

    @RequestMapping("toCusDevPlanPage")
    public String toCusDevPlanPage(Integer id, HttpServletRequest request){
        System.out.println("=============================================");
        // 根据被点击用户的userId查询数据库中对应的用户信息
        SaleChance saleChance = saleChanceService.selectByPrimaryKey(id);
        // 将查询到的用户数据放到请求域中，供前端页面进行获取
        request.setAttribute("saleChance", saleChance);
        System.out.println("=============================================");
        return "cusDevPlan/cus_dev_plan_data";
    }


    /**
     * 客户开发机会数据查询(分页多条件查询)
     * @param cusDevPlanQuery
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> queryCusDevPlanByParams(CusDevPlanQuery cusDevPlanQuery){
        return cusDevPlanService.queryCusDevPlanByParams(cusDevPlanQuery);
    }

    /**
     * 添加计划项数据
     * @param cusDevPlan
     * @return
     */
    @PostMapping("add")
    @ResponseBody
    public ResultInfo addCusDevPlan(CusDevPlan cusDevPlan){
        cusDevPlanService.addCusDevService(cusDevPlan);
        return success("计划项数据添加成功");
    }


    @RequestMapping("toAddOrUpdateCusDevPlanPage")
    public String toAddOrUpdateCusDevPlanPage(HttpServletRequest request, Integer sId, Integer id){
        // 将营销机会id设置到请求域中，给计划页面进行获取
        request.setAttribute("sid", sId);
        // 根据传入的id查询用户的信息
        CusDevPlan cusDevPlan = cusDevPlanService.selectByPrimaryKey(id);
        request.setAttribute("cusDevPlan", cusDevPlan);
        // 跳转到添加/修改的页面
        return "cusDevPlan/add_update";
    }


    /**
     * 更新计划项数据
     * @param cusDevPlan
     * @return
     */
    @PostMapping("update")
    @ResponseBody
    public ResultInfo updateCusDevPlan(CusDevPlan cusDevPlan){
        cusDevPlanService.updateCusDevService(cusDevPlan);
        return success("计划项数据更新成功");
    }


    /**
     * 删除计划项数据
     * @param id
     * @return
     */
    @PostMapping("delete")
    @ResponseBody
    public ResultInfo deleteCusDevPlan(Integer id){
        cusDevPlanService.deleteCusDevPlan(id);
        return success("计划项数据删除成功");
    }

}
















