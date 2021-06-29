package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.service.UserService;
import com.xxxx.crm.utils.LoginUserUtil;
import com.xxxx.crm.vo.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author lms
 * @date 2021-05-16 - 17:18
 */
@Controller
public class IndexController extends BaseController {

    @Resource
    private UserService userService;

    /**
     * 系统登录页面
     */
    @RequestMapping("index")
    public String index() {
        return "index";
    }

    /**
     * 系统界面欢迎页
     */
    @RequestMapping("welcome")
    public String welcome(Model model) {
        return "welcome";
    }

    /**
     * 后端管理主页面
     * 通过request于获取到保存在session中的登录用户的id信息
     */
    @RequestMapping("main")
    public String main(HttpServletRequest request) {
        // 从request中获取到key = "userIdStr"的值对应的用户id
        Integer id = LoginUserUtil.releaseUserIdFromCookie(request);
        // 根据登录用户的id获取到用户的信息，并把用户信息保存到session中
        User user = userService.selectByPrimaryKey(id);
        request.getSession().setAttribute("user", user);
        return "main";
    }
}
