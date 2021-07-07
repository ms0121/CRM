package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.query.CustomerServeQuery;
import com.xxxx.crm.service.CustomerServeService;
import com.xxxx.crm.vo.CustomerServe;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
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

    @RequestMapping("index/{type}")
    public String index(@PathVariable("type") Integer type){
        if (type == 1){
            return "customerServe/customer_serve";
        }else if (type == 2){
            return "customerServe/customer_serve";
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
    public Map<String, Object> queryCustomerServeByParams(CustomerServeQuery customerServeQuery){
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


}
