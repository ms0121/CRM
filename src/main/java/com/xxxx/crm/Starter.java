package com.xxxx.crm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author lms
 * @date 2021-05-16 - 17:29
 */
@SpringBootApplication
@MapperScan("com.xxxx.crm.dao")  // 将该包下的所有的类加入到容器当中
public class Starter {
    public static void main(String[] args) {
        SpringApplication.run(Starter.class);
    }
}
