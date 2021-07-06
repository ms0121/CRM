package com.xxxx.crm.task;

import com.xxxx.crm.service.CustomerService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author lms
 * @date 2021-07-06 - 16:14
 */
@Service
public class TaskService {

    @Resource
    private CustomerService customerService;

//    执行定时任务
//    @Scheduled(cron = "0/2 * * * * ?")
    public void job(){
        System.out.println("定时任务开始执行-->" +
                new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date()));
        customerService.updateCoutomerState();
    }

}
