package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.model.UserModel;
import com.xxxx.crm.query.UserQuery;
import com.xxxx.crm.service.UserService;
import com.xxxx.crm.utils.LoginUserUtil;
import com.xxxx.crm.vo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author lms
 * @date 2021-05-16 - 22:27
 */
@Controller
@RequestMapping("user")
public class UserController extends BaseController {

    @Resource
    private UserService userService;

    /**
     * 接收前端传过来的参数，判读用户是否存在
     * 最重要的是控制层捕抓来自业务层的异常信息
     * 前台发送ajax请求，后台返回json数据的信息给前台用户
     * 使用try catch进行捕获登陆的异常信息，如果没有异常信息，则表示登录成功，否则登录失败
     * 默认不写表示当前的参数来源于路径
     * @param userName
     * @param userPwd
     * @return
     */
    @PostMapping("login")
    @ResponseBody
    public ResultInfo UserLogin(String userName, String userPwd) {
        // 将返回前端的信息封装在该对象中
        ResultInfo resultInfo = new ResultInfo();

        // 出现的异常将由全局异常处理类进行捕获
        UserModel userModel = userService.userLogin(userName, userPwd);
        // 将登录的部分用户信息字段封装在ResultInfo中，并将其返回到页面
        // 设置ResultInfo的result属性，然后将数据返回给登录页面的请求
        resultInfo.setResult(userModel);

        // 对可能抛出异常的代码，使用try - catch进行捕获
//        try {
//            UserModel userModel = userService.userLogin(userName, userPwd);
//            // 将登录的部分用户信息字段封装在ResultInfo中，并将其返回到页面
//            // 设置ResultInfo的result属性，然后将数据返回给登录页面的请求
//            resultInfo.setResult(userModel);
//        }catch (ParamsException p){
//            // 捕获来自业务层的异常信息，该异常信息是来自我们自定义的异常对象值
//            resultInfo.setCode(p.getCode());
//            resultInfo.setMsg(p.getMsg());
//            p.printStackTrace(); // 打印错误的信息
//        } catch (Exception e){
//            // 捕获其他的异常信息,设置异常信息码为 500
//            resultInfo.setCode(500);
//            resultInfo.setMsg("登陆失败!");
//        }

        return resultInfo;
    }

    /**
     * 修改用户密码
     * @param request
     * @param oldPwd
     * @param newPwd
     * @param repeatPwd
     */
    @PostMapping("updatePwd")
    @ResponseBody
    public ResultInfo updatePassword(HttpServletRequest request,String oldPwd, String newPwd, String repeatPwd){
        ResultInfo resultInfo = new ResultInfo();

        // 出现的异常将由全局异常处理类进行捕获
        // 从请求域的cookie中获取用户的id
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        // 调用service层的方法进行更新密码
        userService.updatePassword(userId, oldPwd, newPwd, repeatPwd);

//        try {
//            // 从请求域的cookie中获取用户的id
//            Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
//            // 调用service层的方法进行更新密码
//            userService.updatePassword(userId, oldPwd, newPwd, repeatPwd);
//        }catch (ParamsException p){
//            resultInfo.setCode(p.getCode());
//            resultInfo.setMsg(p.getMsg());
//            p.printStackTrace();
//        } catch (Exception e){
//            resultInfo.setCode(500);
//            resultInfo.setMsg("修改密码失败");
//            e.printStackTrace();
//        }

        return resultInfo;
    }


    /**
     * 密码修改页面
     * @return
     */
    @RequestMapping("/toPasswordPage")
    public String toPasswordPage(){
        return "user/password";
    }

    /**
     * 查询所有的用户信息
     * @return
     */
    @RequestMapping("queryAllSales")
    @ResponseBody
    public List<Map<String, Object>> queryAllSales(){
        return userService.queryAllSales();
    }

    /**
     * 根据输入的字段查询用户的信息
     * @param userQuery
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> queryUser(UserQuery userQuery){
        return userService.queryByParamsForTable(userQuery);
    }

    /**
     * 跳转到用户页面
     * @return
     */
    @RequestMapping("index")
    public String index(){
        return "user/index";
    }


    /**
     * 添加用户操作
     * @param user
     * @return
     */
    @PostMapping("add")
    @ResponseBody
    public ResultInfo addUser(User user){
        userService.addUser(user);
        return success("用户添加成功!");
    }

    /**
     * 弹出添加修改页面
     * @return
     */
    @GetMapping("toAddorUpdateUserPage")
    public String toAddorUpdateUserPage(Integer id, HttpServletRequest request){
        if (id != null){
            User user = userService.selectByPrimaryKey(id);
            // 将查询的数据放置在请求域中，因为如果放在session域中，请求结束查询的值都会在session域中，
            // 这不符合要求，所以将其放在请求域中
            request.setAttribute("userInfo", user);
        }
        return "user/add_update";
    }

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    @PostMapping("update")
    @ResponseBody
    public ResultInfo updateUser(User user){
        userService.updateUser(user);
        return success("用户更新成功!");
    }


    /**
     * 删除用户信息
     * @param id
     * @return
     */
    @PostMapping("delete")
    @ResponseBody
    public ResultInfo delete(Integer[] id){
        userService.deleteUser(id);
        return success("用户删除成功!");
    }


    /**
     * 查询所有的用户信息
     * @return
     */
    @RequestMapping("queryAllCustomerManagers")
    @ResponseBody
    public List<Map<String, Object>> queryAllCustomerManagers(){
        return userService.queryAllCustomerManagers();
    }
}
