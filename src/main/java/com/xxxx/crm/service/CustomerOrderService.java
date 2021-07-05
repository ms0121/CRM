package com.xxxx.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.CustomerOrderMapper;
import com.xxxx.crm.query.CustomerOrderQuery;
import com.xxxx.crm.vo.Customer;
import com.xxxx.crm.vo.CustomerOrder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lms
 * @date 2021-07-05 - 16:14
 */
@Service
public class CustomerOrderService extends BaseService<CustomerOrder, Integer> {

    @Resource
    private CustomerOrderMapper customerOrderMapper;

    /**
     * 多条件查询用户信息，返回的数据格式必须满足layui中数据表格要求的格式(map(String,Object))
     * 否则无法在页面上进行展示
     * @param customerOrderQuery
     * @return
     */
    public Map<String, Object> queryCustomerOrderByParams(CustomerOrderQuery customerOrderQuery) {
        Map<String, Object> map = new HashMap<>();
        // 构建分页数据
        PageHelper.startPage(customerOrderQuery.getPage(), customerOrderQuery.getLimit());
        // 查询所有的customer的数据信息，按条件查询数据(数据封装在PageInfo对象中)
        List<CustomerOrder> customerOrders = customerOrderMapper.selectByParams(customerOrderQuery);
        // 将需要分页的数据列表传入到PageInfo中，得到对应的分页对象(就可以从pageInfo对象中拿取分页数，起始页等等)
        PageInfo<CustomerOrder> pageInfo = new PageInfo<>(customerOrders);

        // 设置layui表格需要的map形式的数据信息
        map.put("code", 0);
        map.put("msg", "success");
        map.put("count", pageInfo.getTotal());
        map.put("data", pageInfo.getList());

        return map;
    }

    /**
     * 根据roleId查询订单详情信息
     * @param roleId
     * @return
     */
    public Map<String, Object> queryOrderDetailByRoleId(Integer roleId) {
        return customerOrderMapper.queryOrderDetailByRoleId(roleId);
    }
}
