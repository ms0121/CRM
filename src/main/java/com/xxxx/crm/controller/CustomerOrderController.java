package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.query.CustomerOrderQuery;
import com.xxxx.crm.service.CustomerOrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author lms
 * @date 2021-07-05 - 16:14
 */

@Controller
@RequestMapping("order")
public class CustomerOrderController extends BaseController {

    @Resource
    private CustomerOrderService customerOrderService;


    /**
     * 查询订单数据
     * @param customerOrderQuery
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> queryCustomerOrderByParams(CustomerOrderQuery customerOrderQuery){
        Map<String, Object> map = customerOrderService.queryCustomerOrderByParams(customerOrderQuery);
        return map;
    }

    /**
     * 查询订单详情信息
     * @param orderId
     * @param model
     * @return
     */
    @RequestMapping("toOrderDetailPage")
    public String toOrderDetailPage(Integer orderId, Model model){
        Map<String, Object> map = customerOrderService.queryOrderDetailByRoleId(orderId);
        // 将查询到的数据信息设置在请求域，让订单详情页面可以从请求域中的获取到订单信息
        model.addAttribute("order", map);
        return "customer/customer_order_detail";
    }

}
