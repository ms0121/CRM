package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author lms
 * @date 2021-07-08 - 9:52
 */
@Controller
public class CustomerReportController extends BaseController {

    @RequestMapping("report/{type}")
    public String index(@PathVariable Integer type){
        if (type != null){
            if (type == 0){
                return "report/customer_contri";
            }else if (type == 1){
                return "report/customer_make";
            }else if (type == 3){
                return "report/customer_loss";
            }else {
                return "";
            }
        }else {
            return "";
        }
    }
}
