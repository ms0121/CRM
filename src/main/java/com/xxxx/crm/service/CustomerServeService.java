package com.xxxx.crm.service;

import ch.qos.logback.core.pattern.ConverterUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.CustomerServeMapper;
import com.xxxx.crm.query.CustomerServeQuery;
import com.xxxx.crm.vo.CustomerReprieve;
import com.xxxx.crm.vo.CustomerServe;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lms
 * @date 2021-07-07 - 9:14
 */
@Service
public class CustomerServeService extends BaseService<CustomerServe, Integer> {

    @Resource
    private CustomerServeMapper customerServeMapper;


    /**
     * 查询客户服务的数据信息
     * @param customerServeQuery
     * @return
     */
    public Map<String, Object> queryCustomerServeByParams(CustomerServeQuery customerServeQuery){
        Map<String, Object> map = new HashMap<>();

        // 构建分页数据
        PageHelper.startPage(customerServeQuery.getPage(), customerServeQuery.getLimit());
        // 查询所有的customer的数据信息，按条件查询数据(数据封装在PageInfo对象中)
        List<CustomerServe> customerServes = customerServeMapper.selectByParams(customerServeQuery);
        // 将需要分页的数据列表传入到PageInfo中，得到对应的分页对象(就可以从pageInfo对象中拿取分页数，起始页等等)
        PageInfo<CustomerServe> pageInfo = new PageInfo<>(customerServes);

        // 设置layui表格需要的map形式的数据信息
        map.put("code", 0);
        map.put("msg", "success");
        map.put("count", pageInfo.getTotal());
        map.put("data", pageInfo.getList());

        return map;
    }

}
