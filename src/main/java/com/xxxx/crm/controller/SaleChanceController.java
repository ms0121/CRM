package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.dao.SaleChanceMapper;
import com.xxxx.crm.enums.StateStatus;
import com.xxxx.crm.query.SaleChanceQuery;
import com.xxxx.crm.service.SaleChanceService;
import com.xxxx.crm.utils.CookieUtil;
import com.xxxx.crm.utils.LoginUserUtil;
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
 * @date 2021-05-30 - 14:21
 */
@Controller
@RequestMapping("sale_chance")
public class SaleChanceController extends BaseController {

    @Resource
    private SaleChanceService saleChanceService;

    /**
     * 营销机会数据查询，分页多条件查询
     * 要求：如果flag的值不为空，并且值为1，则表示当前查询的是客户开发机会，
     *      否则查询 营销机会数据
     * @param saleChanceQuery
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> querySaleChanceByParams(SaleChanceQuery saleChanceQuery,
                                                       Integer flag, HttpServletRequest request){
        // 判断flag 的值
        if (flag != null && flag == 1){
            // 表示当前执行的是：查询客户开发计划，那么就要查询匹配已经分配的人员，
            // 并且指派人为当前的登录用户
            // 设置分配状态
            saleChanceQuery.setState(StateStatus.STATED.getType());
            // 设置指派人(当前登录用户的ID)
            // 从cookie中获取当前登录用户的id
            int userId = LoginUserUtil.releaseUserIdFromCookie(request);
            saleChanceQuery.setAssignMan(userId);
        }
        return saleChanceService.querySaleChanceByParams(saleChanceQuery);
    }

    @RequestMapping("index")
    public String index(){
        return "saleChance/sale_chance";
    }

    /**
     * 添加营销机会,添加成功之后返回json数据信息
     * @param saleChance
     * @param request
     * @return
     */
    @PostMapping("add")
    @ResponseBody
    public ResultInfo addSaleChance(SaleChance saleChance, HttpServletRequest request){
        // 直接从request中的cookie里面获取登录用户的用户名
        String userName = CookieUtil.getCookieValue(request, "userName");
        // 设置SaleChance的创建人属性值
        saleChance.setCreateMan(userName);
        // 执行插入的操作
        saleChanceService.addSaleChanceParams(saleChance);
        return success("营销机会数据添加成功!");
    }

    /**
     * 添加/编辑的机会营销管理界面
     * @param id  被编辑用户的id
     * @param request  将查询的数据对象放到请求中,实现数据的共享
     * @return 跳转到指定的页面
     */
    @RequestMapping("toSaleChance")
    public String toSaleChance(Integer id, HttpServletRequest request){
        // 数据表格的编辑操作
        if (id != null){
            // 根据id从数据库中查询用对象
            SaleChance saleChance = saleChanceService.selectByPrimaryKey(id);
            // 将查询到的数据对象放入请求域中，这样子前端页面就可以使用el表达式直接从请求域中拿到该对象数据信息
            request.setAttribute("saleChance", saleChance);
        }
        return "saleChance/add_update";
    }


    /**
     * 更新营销机会,更新成功之后返回json数据信息
     * @param saleChance
     * @return
     */
    @PostMapping("update")
    @ResponseBody
    public ResultInfo updateSaleChance(SaleChance saleChance){
        // 执行插入的操作
        // System.out.println("我已经进入了当前的页面..........");
        saleChanceService.updateSaleChance(saleChance);
        return success("营销机会数据更新成功!");
    }

    /**
     * 营销机会数据删除操作
     * @param ids
     * @return
     */
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteSaleChance(Integer[] ids){
        saleChanceService.deleteSaleChance(ids);
        return success("营销机会数据删除成功!");
    }


    @PostMapping("updateSaleChanceDevResult")
    @ResponseBody
    public ResultInfo updateSaleChanceDevResult(Integer id, Integer devResult){
       saleChanceService.updateSaleChanceDevResult(id, devResult);
        return success("开发状态更新成功!");
    }
}















