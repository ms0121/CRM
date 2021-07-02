package com.xxxx.crm.aspect;

import com.xxxx.crm.annotation.RequiredPermission;
import com.xxxx.crm.exceptions.AuthException;
import javafx.beans.binding.ObjectExpression;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author lms
 * @date 2021-07-02 - 17:03
 * 切面里面有两个是必须进行定义： 一个是切入点， 一个是通知
 * 在这个类中使用的是环绕通知：环绕通知必须显示调用方法，和必须拥有返回值
 */
@Component
@Aspect
public class PermissionAspect {

    @Resource
    private HttpSession session;

    /**
     * 切面会拦截指定包下的指定注解
     * 即：会拦截com.xxxx.crm.annotation这个包下的RequiredPermission注解（只要代码中用了这个注解的方法
     *                                                                 或者类都会被该方法拦截）
     *
     * 在这个类中使用的是环绕通知：环绕通知必须显示调用方法，和必须拥有返回值
     * @param pjp 通过该对象可以获取到方法的名字，参数类型，参数名，注解等等信息
     * @return
     */
    @Around(value = "@annotation(com.xxxx.crm.annotation.RequiredPermission)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Object result = null;
        // 获取到当前登录用户拥有的权限(直接从session域中获取)，因为登录的时候将查询得到的权限资源设置在了session中
        List<String> permissions = (List<String>) session.getAttribute("permissions");
        // 判断用户是否拥有权限
        if (permissions == null || permissions.size() < 1){
            // 抛出异常信息
            throw new AuthException();
        }

        // 获取到对应的目标方法(即被调用执行的方法)
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        // 获取目标方法上的注解信息
        RequiredPermission requiredPermission = methodSignature.getMethod().getDeclaredAnnotation(RequiredPermission.class);
        // 判断注解上的状态码信息
        if (!(permissions.contains(requiredPermission.code()))){
            // 如果权限中不包含当前方法上注解指定的权限码，则抛出异常值
            throw new AuthException();
        }
        result = pjp.proceed();
        return result;
    }

}
