package com.xxxx.crm.service;

import ch.qos.logback.core.pattern.ConverterUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.CustomerMapper;
import com.xxxx.crm.dao.CustomerServeMapper;
import com.xxxx.crm.dao.UserMapper;
import com.xxxx.crm.enums.CustomerServeStatus;
import com.xxxx.crm.query.CustomerServeQuery;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.vo.Customer;
import com.xxxx.crm.vo.CustomerReprieve;
import com.xxxx.crm.vo.CustomerServe;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
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

    @Resource
    private CustomerMapper customerMapper;

    @Resource
    private UserMapper userMapper;


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

    /**
     * 添加客户服务操作
     *  1.参数校验
     *      客户名  customer 非空  客户表中存在客户记录
     *      服务类型  serveType  非空
     *      服务请求内容   serviceRequest  非空
     *  2.设置参数的默认值
     *      服务状态
     *          服务创建状态   fw_001
     *      是否有效
     *      创建时间
     *      修改时间
     *      创建人   createPeople  （前端页面中通过cookie获取，传递到后台）(已经设置在customerServe对象中)
     *  3.执行添加操作
     * @param customerServe
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addCustomerServe(CustomerServe customerServe) {
        /*1.参数校验*/
        AssertUtil.isTrue(StringUtils.isBlank(customerServe.getCustomer()), "客户名不能为空!");
        // 要求客户表中存在记录信息
        Customer customer = customerMapper.queryCustomerByName(customerServe.getCustomer());
        AssertUtil.isTrue(customer == null, "客户信息不存在!");
        AssertUtil.isTrue(StringUtils.isBlank(customerServe.getServeType()), "服务类型不能为空!");
        AssertUtil.isTrue(StringUtils.isBlank(customerServe.getServiceRequest()), "服务请求不能为空!");

        /*2.设置参数的默认值*/
        customerServe.setServeType(CustomerServeStatus.CREATED.getState());
        customerServe.setIsValid(1);
        customerServe.setCreateDate(new Date());
        customerServe.setUpdateDate(new Date());

        /*3.执行添加操作*/
        AssertUtil.isTrue(customerServeMapper.insertSelective(customerServe) < 1, "服务添加失败!");
    }

    /**
     * 服务分配/服务处理/服务反馈
     *  1.参数校验与设置参数的默认值
     *      客户服务ID
     *          非空， 记录必须存在
     *      客户服务状态
     *          如果服务状态为  服务分配状态   fw_002
     *              分配人     非空，分配用户记录存在
     *              分配时间   系统当前时间
     *              更新时间   系统当前时间
     *
     *          如果服务状态为  服务处理状态   fw_003
     *              服务处理内容  非空
     *              服务处理时间    系统当前时间
     *              更新时间     系统当前时间
     *
     *          如果服务状态为  服务反馈状态   fw_004
     *              服务反馈内容  非空
     *              服务满意度   非空
     *              更新时间    系统当前时间
     *              服务状态    设置为服务归档装填 fw_005
     *
     *  2.执行更新操作。判断受影响的行数
     * @param customerServe
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateCustomerServeState(CustomerServe customerServe){
        /* 客户服务ID 非空， 记录必须存在*/
        AssertUtil.isTrue(customerServe.getId() == null ||
                customerServeMapper.selectByPrimaryKey(customerServe.getId()) == null, "待更新记录不存在!");

        /*判断客户的服务状态*/
        if (CustomerServeStatus.ASSIGNED.getState().equals(customerServe.getState())) {
            // 服务分配状态
            // 分配人     非空，分配用户(当前登录用户)记录存在
            AssertUtil.isTrue(customerServe.getAssigner() == null, "待分配用户不能为空!");
            AssertUtil.isTrue(userMapper.selectByPrimaryKey(
                    Integer.parseInt(customerServe.getAssigner())) == null, "待分配用户不存在!");
            // 分配时间   系统当前时间
            customerServe.setAssignTime(new Date());

        }else if (CustomerServeStatus.PROCED.getState().equals(customerServe.getState())){
            //  服务处理状态
            //  服务处理内容  非空
            AssertUtil.isTrue(StringUtils.isBlank(customerServe.getServiceProce()), "服务处理内容不能为空!");
            //  服务处理时间    系统当前时间
            customerServe.setServiceProceTime(new Date());

        }else if (CustomerServeStatus.FEED_BACK.getState().equals(customerServe.getState())){
            // 服务反馈状态
            // 服务反馈内容  非空
            AssertUtil.isTrue(StringUtils.isBlank(customerServe.getServiceProceResult()), "服务反馈内容不能为空!");
            // 服务满意度   非空
            AssertUtil.isTrue(StringUtils.isBlank(customerServe.getMyd()), "服务满意度不能为空!");
            // 服务状态    设置为服务归档装填 fw_005
            customerServe.setState(CustomerServeStatus.ARCHIVED.getState());
        }
        // 更新时间   系统当前时间
        customerServe.setUpdateDate(new Date());

        /*2.执行更新操作。判断受影响的行数*/
        AssertUtil.isTrue(customerServeMapper.updateByPrimaryKeySelective(customerServe) < 1, "服务更新失败!");
    }
}
