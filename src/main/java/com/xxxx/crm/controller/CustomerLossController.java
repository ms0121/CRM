package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.query.CustomerLossQuery;
import com.xxxx.crm.query.CustomerOrderQuery;
import com.xxxx.crm.service.CustomerLossService;
import com.xxxx.crm.vo.CustomerLoss;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author lms
 * @date 2021-07-05 - 20:02
 */
@Controller
@RequestMapping("customer_loss")
public class CustomerLossController extends BaseController {

    @Resource
    private CustomerLossService customerLossService;

    /**
     * 打开客户流失页面
     * @return
     */
    @RequestMapping("index")
    public String index(){
        return "customerLoss/customer_loss";
    }


    /**
     * 查询客户流失的数据信息
     * @param
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> queryCustomerLossByParams(CustomerLossQuery customerLossQuery){
        Map<String, Object> map = customerLossService.queryCustomerLossByParams(customerLossQuery);
        return map;
    }

    /**
     * 打开客户流失详情页面
     * @return
     */
    @RequestMapping("customer_rep")
    public String openCustomerRep(Integer id, Model model){
        CustomerLoss customerLoss = customerLossService.selectByPrimaryKey(id);
        System.out.println("customerLoss = " + customerLoss);
        model.addAttribute("customerLoss", customerLoss);
        return "customerLoss/customer_rep";
    }


}
