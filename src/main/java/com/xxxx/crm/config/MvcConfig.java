package com.xxxx.crm.config;

import com.xxxx.crm.Interceptor.NoLoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author lms
 * @date 2021-05-29 - 21:25
 */

@Configuration  // 配置类
public class MvcConfig extends WebMvcConfigurerAdapter {

    // 将拦截器注入到IOC容器中
    @Bean // 讲方法的返回值交给IOC进行维护
    public NoLoginInterceptor noLoginInterceptor(){
        return new NoLoginInterceptor();
    }

    /**
     * 添加拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 需要一个实现了拦截器功能的实例对象，这里使用的是noLoginInterceptor
        registry.addInterceptor(noLoginInterceptor())
                .addPathPatterns("/**") // 默认拦截所有的请求
                .excludePathPatterns("/css/**", "/images/**", "/js/**", "/lib/**",
                        "/index","/user/login"); // 设置不需要拦截的请求
    }
}
