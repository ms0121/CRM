package com.xxxx.crm.controller;

import com.github.pagehelper.PageInfo;
import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.query.CustomerReprieveQuery;
import com.xxxx.crm.service.CustomerReprieveService;
import com.xxxx.crm.vo.CustomerReprieve;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
     * @param lossId 将客户流失的id号设置到添加更新的页面中
     * @return
     */
    @RequestMapping("toAddOrUpdateCustomerReprPage")
    public String toAddOrUpdateCustomerReprPage(Integer lossId, Model model, Integer id){
        model.addAttribute("lossId", lossId);
        if (id != null){
            CustomerReprieve customerReprieve = customerReprieveService.selectByPrimaryKey(id);
            model.addAttribute("customerRep", customerReprieve);
        }
        return "customerLoss/customer_rep_add_update";
    }

    /**
     * 添加暂缓数据功能
     * @param customerReprieve
     */
    @RequestMapping("add")
    @ResponseBody
    public ResultInfo addCustomerRep(CustomerReprieve customerReprieve){
        customerReprieveService.addCustomerRep(customerReprieve);
        return success("成功添加暂缓数据!");
    }

    /**
     * 更新暂缓数据功能
     * @param customerReprieve
     */
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo updateCustomerRep(CustomerReprieve customerReprieve){
        customerReprieveService.updateCustomerRep(customerReprieve);
        return success("成功更新暂缓数据!");
    }

    /**
     * 删除暂缓数据信息
     * @param id
     * @return
     */
    @PostMapping("delete")
    @ResponseBody
    public ResultInfo deleteCustomerRep(Integer id){
        customerReprieveService.deleteCustomerRep(id);
        return success("成功删除暂缓数据!");
    }
}
