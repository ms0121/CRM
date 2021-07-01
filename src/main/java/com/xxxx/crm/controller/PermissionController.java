package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.service.PermissionService;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;


/**
 * @author lms
 * @date 2021-07-01 - 14:41
 */
@Controller
public class PermissionController extends BaseController {

    @Resource
    private PermissionService permissionService;

}
