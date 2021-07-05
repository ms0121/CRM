package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.query.CustomerQuery;
import com.xxxx.crm.service.CustomerService;
import com.xxxx.crm.vo.Customer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author lms
 * @date 2021-07-04 - 17:27
 */

@Controller
@RequestMapping("customer")
public class CustomerController extends BaseController {

    @Resource
    private CustomerService customerService;

    /**
     * 打开客户管理页面
     * @return
     */
    @GetMapping("index")
    public String index(){
        return "customer/customer";
    }


    /**
     * 多条件查询客户信息
     * @param customerQuery
     * @return
     */
    @GetMapping("list")
    @ResponseBody
    public Map<String, Object> queryCustomerByParams(CustomerQuery customerQuery){
        return customerService.queryCustomerByParams(customerQuery);
    }

    /**
     * 打开添加/修改客户页面
     * @return
     */
    @GetMapping("toCustomer")
    public String toCustomer(Integer id, HttpServletRequest request){
        // 根据点击修改的用户的id来数据库查询当前的用户数据，然后将用户数据信息设置在请求域中(一次有效)，
        // 从而传到修改页面
        Customer customer = customerService.selectByPrimaryKey(id);
        request.setAttribute("customer", customer);
        return "customer/add_update";
    }

    /**
     * 添加客户信息
     * @param customer
     * @return
     */
    @PostMapping("add")
    @ResponseBody
    public ResultInfo addCustomer(Customer customer){
        customerService.addCustomer(customer);
        return success("客户添加成功!");
    }

    /**
     * 更新客户信息
     * @param customer
     * @return
     */
    @PostMapping("update")
    @ResponseBody
    public ResultInfo updateCustomer(Customer customer){
        customerService.updateCustomer(customer);
        return success("客户更新成功!");
    }

    /**
     * 删除客户信息
     * @param id
     * @return
     */
    @PostMapping("delete")
    @ResponseBody
    public ResultInfo deleteCustomer(Integer id){
        System.out.println("id = " + id);
        customerService.deleteCustomer(id);
        return success("客户删除成功!");
    }

    /**
     * 打开订单页面
     * @param id
     * @param request
     * @return
     */
    @RequestMapping("toCustomerOrderPage")
    public String toCustomerOrderPage(Integer id, HttpServletRequest request){
        Customer customer = customerService.selectByPrimaryKey(id);
        request.setAttribute("customer", customer);
        return "customer/customer_order";
    }


}
