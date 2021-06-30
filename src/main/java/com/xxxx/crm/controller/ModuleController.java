package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.model.TreeModel;
import com.xxxx.crm.service.ModuleService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lms
 * @date 2021-06-30 - 20:37
 */
@Controller
@RequestMapping("module")
public class ModuleController extends BaseController {

    @Resource
    private ModuleService moduleService;

    /**
     * 查询所有的资源列表
     * @return
     */
    @GetMapping("queryAllModules")
    @ResponseBody
    public List<TreeModel> queryAllModules(){
        return moduleService.queryAllModules();
    }


    /**
     * 进入授权页面
     * @param roleId
     * @return
     */
    @GetMapping("toAddGrantPage")
    public String toAddGrantPage(Integer roleId){
        return "role/grant";
    }


}
