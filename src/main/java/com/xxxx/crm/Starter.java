package com.xxxx.crm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author lms
 * @date 2021-05-16 - 17:29
 */
@SpringBootApplication
@MapperScan("com.xxxx.crm.dao")  // 将该包下的所有的类加入到容器当中
@EnableScheduling
public class Starter extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(Starter.class);
    }

    /*设置打包上线之后的web项目的启动入口, 重写继承父类SpringBootServletInitializer的方法configure即可*/
//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//        return builder.sources(Starter.class);
//    }
}
