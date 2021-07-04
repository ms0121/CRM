package com.xxxx.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.CustomerMapper;
import com.xxxx.crm.query.CustomerQuery;
import com.xxxx.crm.vo.Customer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lms
 * @date 2021-07-04 - 17:26
 */

@Service
public class CustomerService extends BaseService<Customer, Integer> {

    @Resource
    private CustomerMapper customerMapper;


    /**
     * 多条件查询用户信息，返回的数据格式必须满足layui中数据表格要求的格式(map(String,Object))
     * 否则无法在页面上进行展示
     * @param customerQuery
     * @return
     */
    public Map<String, Object> queryCustomerByParams(CustomerQuery customerQuery){

        HashMap<String, Object> map = new HashMap<>();
        // 开启分页(设置起始页和每页的size)
        PageHelper.startPage(customerQuery.getPage(), customerQuery.getLimit());
        // 查询所有的customer的数据信息，按条件查询数据(数据封装在PageInfo对象中)
        List<Customer> customers = customerMapper.selectByParams(customerQuery);
        // 将需要分页的数据列表传入到PageInfo中，得到对应的分页对象(就可以从pageInfo对象中拿取分页数，起始页等等)
        PageInfo<Customer> pageInfo = new PageInfo<>(customers);

        // 设置layui表单数据需要后台传过来的是map形式的数据
        map.put("code", 0);
        map.put("msg", "success");
        // 数据的总条数
        map.put("count", pageInfo.getTotal());
        // 设置分页好的列表
        map.put("data", pageInfo.getList());
        return map;
    }
}
