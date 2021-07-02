package com.xxxx.crm;

import com.alibaba.fastjson.JSON;
import com.sun.corba.se.spi.ior.IdentifiableFactory;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.exceptions.AuthException;
import com.xxxx.crm.exceptions.NoLoginException;
import com.xxxx.crm.exceptions.ParamsException;
import jdk.nashorn.internal.ir.IfNode;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 全局异常统一处理
 * 处理异常程序解析器
 * @author lms
 * @date 2021-05-29 - 19:33
 */
// 将全局异常处理解析器交给IOC管理
@Component
public class GlobalExceptionResolver implements HandlerExceptionResolver {

    /**
     * 异常处理方法:
     * controller方法的返回值：
     * 1.返回视图
     * 2.返回数据(JSON数据)
     * <p>
     * 如何判断方法的返回值？
     * 通过方法上是否声明 @ResponseBody 注解
     * 如果声明了则表示返回的是数据
     * 如果没有声明该注解，表示返回的是视图
     *
     * @param request  请求对象
     * @param response 响应对象
     * @param handler  方法对象
     * @param ex       异常对象
     * @return
     */
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response,
                                         Object handler, Exception ex) {
        /**
         * 非法拦截请求
         *      判断是否抛出未登录的异常信息
         *          如果抛出未登录的异常信息，则要求用户登录，重定向跳转到登录页面
         */
        if (ex instanceof NoLoginException){
            // 重定向到登录页面
            ModelAndView modelAndView = new ModelAndView("redirect:/index");
            return modelAndView;
        }


        /**
         * 设置默认的异常处理（返回视图）
         */
        ModelAndView modelAndView = new ModelAndView();
        // 设置异常信息
        modelAndView.addObject("code", 500);
        modelAndView.addObject("msg", "系统异常,请重试!");

        // 判断HandlerMethod，
        // 拦截器中handler强转成HandlerMethod，获取controller中api接口的注解。
        if (handler instanceof HandlerMethod) {
            // 类型转换
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            // 获取方法上声明的 @ResponseBody 注解对象
            ResponseBody responseBody = handlerMethod.getMethod().getDeclaredAnnotation(ResponseBody.class);

            // 判断responseBody对象是否为空，如果对象为空，则表示返回的是视图，否则返回的是数据
            if (responseBody == null) {
                // 返回的是视图
                // 判断视图返回的异常类型
                if (ex instanceof ParamsException){
                    ParamsException p = (ParamsException) ex;
                    // 设置视图异常的信息
                    modelAndView.addObject("code", p.getCode());
                    modelAndView.addObject("msg",p.getMsg());
                }else if (ex instanceof AuthException){   // 认证异常信息
                    AuthException p = (AuthException) ex;
                    // 设置视图异常的信息
                    modelAndView.addObject("code", p.getCode());
                    modelAndView.addObject("msg",p.getMsg());
                }

                // 将ModelAndView进行返回
                return modelAndView;

            } else {
                // 返回的是数据
                // 设置默认的异常处理,并将其封装在ResultInfo中，json格式进行返回
                ResultInfo resultInfo = new ResultInfo();
                resultInfo.setCode(500);
                resultInfo.setMsg("系统异常，请重试！");

                // 判断异常类型是否是自定义类型
                if (ex instanceof ParamsException){
                    ParamsException p = (ParamsException) ex;
                    // 设置数据异常的信息
                    resultInfo.setCode(p.getCode());
                    resultInfo.setMsg(p.getMsg());
                } else if (ex instanceof AuthException){  // 认证异常
                    AuthException p = (AuthException) ex;
                    // 设置数据异常的信息
                    resultInfo.setCode(p.getCode());
                    resultInfo.setMsg(p.getMsg());
                }

                // 设置响应类型以及编码格式(响应JSON格式的数据)
                response.setContentType("application/json;charset=utf-8");
                // 得到字符输出流
                PrintWriter out = null;
                try {
                    // 得到输出流
                    out = response.getWriter();
                    // 将需要返回的对象resultInfo数据转为json的格式
                    String json = JSON.toJSONString(resultInfo);
                    // 将json数据进行输出
                    out.write(json);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    // 关闭输出流对象
                    if (out != null){
                        out.close();
                    }
                }
                // 因为返回的是json数据，不是视图
            return null;
            }
        }
        return modelAndView;
    }
}
