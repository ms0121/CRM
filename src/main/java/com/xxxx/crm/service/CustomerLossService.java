package com.xxxx.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.CustomerLossMapper;
import com.xxxx.crm.query.CustomerLossQuery;
import com.xxxx.crm.vo.CustomerLoss;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lms
 * @date 2021-07-05 - 20:00
 */
@Service
public class CustomerLossService extends BaseService<CustomerLoss, Integer> {

    @Resource
    private CustomerLossMapper customerLossMapper;

    /**
     * 查询流失客户数据信息
     * @param customerLossQuery
     * @return
     */
    public Map<String, Object> queryCustomerLossByParams(CustomerLossQuery customerLossQuery) {
        Map<String, Object> map = new HashMap<>();

        // 构建分页数据
        PageHelper.startPage(customerLossQuery.getPage(), customerLossQuery.getLimit());
        // 查询所有的customer的数据信息，按条件查询数据(数据封装在PageInfo对象中)
        List<CustomerLoss> customerLosses = customerLossMapper.selectByParams(customerLossQuery);
        // 将需要分页的数据列表传入到PageInfo中，得到对应的分页对象(就可以从pageInfo对象中拿取分页数，起始页等等)
        PageInfo<CustomerLoss> pageInfo = new PageInfo<>(customerLosses);

        // 设置layui表格需要的map形式的数据信息
        map.put("code", 0);
        map.put("msg", "success");
        map.put("count", pageInfo.getTotal());
        map.put("data", pageInfo.getList());

        return map;

    }
}
