package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.service.UserRoleService;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * @author lms
 * @date 2021-06-14 - 23:26
 */
@Controller
public class UserRoleController extends BaseController {

    @Resource
    private UserRoleService userRoleService;

}
