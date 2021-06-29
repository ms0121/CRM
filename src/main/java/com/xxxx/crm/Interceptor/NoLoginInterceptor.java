package com.xxxx.crm.Interceptor;

import com.xxxx.crm.dao.UserMapper;
import com.xxxx.crm.exceptions.NoLoginException;
import com.xxxx.crm.utils.LoginUserUtil;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 非法访问拦截
 *  继承 HandlerInterceptorAdapter 适配器
 *
 *  要想要拦截器生效，需要相对应的配置类，把拦截器加入配置类中，并告诉拦截器哪些资源要加入到
 *  拦截器中进行处理，哪些资源是可以放行的
 * @author lms
 * @date 2021-05-29 - 21:00
 */
public class NoLoginInterceptor extends HandlerInterceptorAdapter {

    @Resource
    private UserMapper userMapper;

    /**
     * 拦截用户是否是登录状态
     *    在目标方法（目标资源）执行前，（进行拦截）执行的方法
     * 方法返回Boolean类型
     *    如果返回true，表示目标方法可以被执行
     *    如果返回false，表示阻止目标方法的执行
     *
     * 判断用户是否是登录状态：
     *    1、判断cookie中是否存在用户信息(获取登录信息的UserId)
     *    2、查询数据库中是否存在指定id的用户信息
     *
     * 如果用户是登录状态，则允许目标方法执行，如果用户是非登录的状态，则抛出未登录异常（在全局异常
     *      异常中做判断，如果是未登录异常，则跳转(重定向)到登录界面）
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取cookie中的用户id
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        // 判断用户id是否为空，并且数据库中是否存在该Id的用户信息
        if (userId == null || userMapper.selectByPrimaryKey(userId) == null){
            // 抛出未登录异常
            throw new NoLoginException();
        }
        return true;
    }
}
