package com.xxxx.crm.controller;

import com.github.pagehelper.PageInfo;
import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.query.CustomerReprieveQuery;
import com.xxxx.crm.service.CustomerReprieveService;
import com.xxxx.crm.vo.CustomerReprieve;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author lms
 * @date 2021-07-06 - 16:51
 */

@Controller
@RequestMapping("customer_rep")
public class CustomerReprieveController extends BaseController {

    @Resource
    private CustomerReprieveService customerReprieveService;

    /**
     * 根据流失客户的id查询相应的数据信息
     * @param
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> queryCustomerRepByParams(CustomerReprieveQuery customerReprieveQuery){
        return customerReprieveService.queryCustomerRepByParams(customerReprieveQuery);
    }

    /**
     * 打开添加暂缓数据表
     * @param lossId
     * @return
     */
    @RequestMapping("toAddOrUpdateCustomerReprPage")
    public String toAddOrUpdateCustomerReprPage(Integer lossId, Model model){
        CustomerReprieve customerRep = customerReprieveService.selectByPrimaryKey(lossId);
        model.addAttribute("customerRep", customerRep);
        return "customerLoss/customer_rep_add_update";
    }

    /**
     * 添加暂缓数据功能
     * @param customerReprieve
     */
    public ResultInfo addCustomerRep(CustomerReprieve customerReprieve){
        customerReprieveService.addCustomerRep(customerReprieve);
        return success("成功添加客户暂缓!");
    }


    /**
     * 更新暂缓数据功能
     * @param customerReprieve
     */
    public ResultInfo updateCustomerRep(CustomerReprieve customerReprieve){
        customerReprieveService.updateCustomerRep(customerReprieve);
        return success("成功更新客户暂缓");
    }





}
