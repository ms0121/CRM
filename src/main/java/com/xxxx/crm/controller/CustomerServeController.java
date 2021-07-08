package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.query.CustomerServeQuery;
import com.xxxx.crm.service.CustomerServeService;
import com.xxxx.crm.utils.LoginUserUtil;
import com.xxxx.crm.vo.CustomerServe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author lms
 * @date 2021-07-07 - 9:15
 */
@Controller
@RequestMapping("customer_serve")
public class CustomerServeController extends BaseController {

    @Resource
    private CustomerServeService customerServeService;

    /**
     * 跳转页面
     * @param type
     * @return
     */
    @RequestMapping("index/{type}")
    public String index(@PathVariable("type") Integer type){
        if (type == 1){
            return "customerServe/customer_serve";
        }else if (type == 2){
            return "customerServe/customer_serve_assign";
        }else if (type == 3){
            return "customerServe/customer_serve_proce";
        }else if (type == 4){
            return "customerServe/customer_serve_feed_back";
        }else if (type == 5){
            return "customerServe/customer_serve_archive";
        }else {
            return "";
        }
    }

    /**
     * 查询客户服务数据信息，构建数据表格
     * @param customerServeQuery
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> queryCustomerServeByParams(CustomerServeQuery customerServeQuery, Integer flag,
                                                          HttpServletRequest request){
        if (flag != null && flag == 1){
            // 从请求中获取当前的登录用户编号
            customerServeQuery.setAssigner(LoginUserUtil.releaseUserIdFromCookie(request));
        }
        return customerServeService.queryCustomerServeByParams(customerServeQuery);
    }


    /**
     * 打开客户服务添加的页面
     * @return
     */
    @RequestMapping("toAddCustomerServe")
    public String toAddCustomerServe(){
        return "customerServe/customer_serve_add";
    }


    /**
     * 添加客户服务
     * @param customerServe
     * @return
     */
    @RequestMapping("add")
    @ResponseBody
    public ResultInfo addCustomerServe(CustomerServe customerServe){
        customerServeService.addCustomerServe(customerServe);
        return success("添加客户服务成功!");
    }


    /**
     * 服务更新操作
     * @param customerServe
     * @return
     */
    @PostMapping("update")
    @ResponseBody
    public ResultInfo updateCustomerServeState(CustomerServe customerServe){
        customerServeService.updateCustomerServeState(customerServe);
        return success("服务更新成功!");
    }


    /**
     * 跳转至分配页面
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("toCustomerServeAssignPage")
    public String toCustomerServeAssignPage(Integer id, Model model){
        CustomerServe customerServe = customerServeService.selectByPrimaryKey(id);
        model.addAttribute("customerServe", customerServe);
        return "customerServe/customer_serve_assign_add";
    }


    /**
     * 打开服务处理的对话框
     * @param id
     * @return
     */
    @RequestMapping("toCustomerServeProcePage")
    public String toCustomerServeProcePage(Integer id, Model model){
        CustomerServe customerServe = customerServeService.selectByPrimaryKey(id);
        model.addAttribute("customerServe", customerServe);
        return "customerServe/customer_serve_proce_add";
    }

    /**
     * 打开服务反馈的对话框
     * @param id
     * @return
     */
    @RequestMapping("toCustomerServeFeedBackPage")
    public String toCustomerServeFeedBackPage(Integer id, Model model){
        CustomerServe customerServe = customerServeService.selectByPrimaryKey(id);
        model.addAttribute("customerServe", customerServe);
        return "customerServe/customer_serve_feed_back_add";
    }



}
