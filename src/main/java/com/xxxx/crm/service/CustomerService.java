package com.xxxx.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.CustomerLossMapper;
import com.xxxx.crm.dao.CustomerMapper;
import com.xxxx.crm.dao.CustomerOrderMapper;
import com.xxxx.crm.query.CustomerQuery;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.utils.PhoneUtil;
import com.xxxx.crm.vo.Customer;
import com.xxxx.crm.vo.CustomerLoss;
import com.xxxx.crm.vo.CustomerOrder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author lms
 * @date 2021-07-04 - 17:26
 */

@Service
public class CustomerService extends BaseService<Customer, Integer> {

    @Resource
    private CustomerMapper customerMapper;

    @Resource
    private CustomerOrderMapper customerOrderMapper;

    @Resource
    private CustomerLossMapper customerLossMapper;


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


    /**
     * 添加客户
     *  1.参数校验
     *      客户名称  name  非空，值唯一
     *      法人代表  fr  非空
     *      手机号码  phone 非空，格式正确
     *
     *  2.设置默认值
     *      是否有效  isValid   1
     *      创建时间  createDate  系统默认时间
     *      修改时间  updateDate  系统当前时间
     *      客户状态  state     0  正常客户   1 流失客户
     *      客户编号  khno
     *          系统生成，唯一，UUID, 时间戳
     *          格式：kh + 时间戳
     *
     *  3.执行添加操作
     * @param customer
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addCustomer(Customer customer){
        /*1.参数校验*/
        checkCustomerParams(customer.getName(), customer.getFr(), customer.getPhone());
        // 判断客户名称的唯一值
        Customer temp = customerMapper.queryCustomerByName(customer.getName());
        AssertUtil.isTrue(temp != null, "客户名称已存在，请重新输入!");

        /*2.设置默认值*/
        // 是否有效  isValid   1
        customer.setIsValid(1);
        // 创建时间  createDate  系统默认时间
        customer.setCreateDate(new Date());
        // 修改时间  updateDate  系统当前时间
        customer.setUpdateDate(new Date());
        customer.setState(0);
        // 客户编号  khno (系统生成，唯一，UUID, 时间戳), 选择格式：kh + 时间戳
        String khno = "kh" + System.currentTimeMillis();
        customer.setKhno(khno);

        /*3.执行添加操作*/
        AssertUtil.isTrue(customerMapper.insertSelective(customer) < 1, "客户添加失败!");
    }


    /**
     * 更新客户
     *  1.参数校验
     *      客户id    非空，并且数据存在
     *      客户名称  name  非空，值唯一
     *      法人代表  fr  非空
     *      手机号码  phone 非空，格式正确
     *
     *  2.设置默认值
     *      修改时间  updateDate  系统当前时间
     *  3.执行更新操作
     * @param customer
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateCustomer(Customer customer){
        /*1.参数校验*/
        AssertUtil.isTrue(customer.getId() == null, "待更新记录不存在!");
        // 根据id查询客户对象信息
        Customer temp = customerMapper.selectByPrimaryKey(customer.getId());
        AssertUtil.isTrue(temp == null, "待更新记录不存在!");
        checkCustomerParams(customer.getName(), customer.getFr(), customer.getPhone());
        //  客户名称  name 值唯一
        temp = customerMapper.queryCustomerByName(customer.getName());
        AssertUtil.isTrue(temp != null && !(temp.getId().equals(customer.getId())), "客户名称已存在!");

        /*2.设置默认值*/
        customer.setUpdateDate(new Date());

        /*3.执行更新操作*/
        AssertUtil.isTrue(customerMapper.updateByPrimaryKeySelective(customer) < 1, "客户信息更新失败!");
    }


    /**
     * 删除（实际上为更新的操作）客户信息
     * 1.参数校验
     *      id 非空， 数据存在
     * 2.设置默认值
     *
     * 3.执行删除(修改操作)
     *
     * @param id
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteCustomer(Integer id){
        // 1. 参数校验
        AssertUtil.isTrue(id == null, "待删除记录不存在!");
        Customer temp = customerMapper.selectByPrimaryKey(id);
        AssertUtil.isTrue(temp == null, "待删除记录不存在!");

        // 2. 设置默认值
        temp.setIsValid(0);
        temp.setUpdateDate(new Date());

        // 3.执行删除的操作
        AssertUtil.isTrue(customerMapper.updateByPrimaryKeySelective(temp) < 1, "删除客户失败!");
    }


    /**
     * 参数校验
     *      客户名称  name  非空，值唯一
     *      法人代表  fr  非空
     *      手机号码  phone 非空，格式正确
     * @param name
     * @param fr
     * @param phone
     */
    private void checkCustomerParams(String name, String fr, String phone) {
        AssertUtil.isTrue(StringUtils.isBlank(name), "用户名不能为空!");
        AssertUtil.isTrue(StringUtils.isBlank(fr), "法人不能为空!");
        AssertUtil.isTrue(StringUtils.isBlank(phone), "手机号码不能为空!");
        AssertUtil.isTrue(!PhoneUtil.isMobile(phone), "手机号码格式不正确!");
    }


    /**
     * 更新客户的流失状态
     *  1.查询待流失客户数据
     *  2.将流失客户数据批量添加到客户流失表中
     *  3.批量更新客户的流失状态   0 正常客户  1 流失客户
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateCoutomerState(){
        /* 1. 查询待流失的客户数据 */
        List<Customer> lossCustomerList = customerMapper.queryLossCustomers();

        /* 2. 将流失客户数据批量添加到客户流失表中 */
        // 判断流失客户数据是否存在
        if (lossCustomerList != null && lossCustomerList.size() > 0) {
            // 定义集合 用来接收流失客户的ID
            List<Integer> lossCustomerIds = new ArrayList<>();
            // 定义流失客户的列表
            List<CustomerLoss> customerLossList = new ArrayList<>();
            // 遍历查询到的流失客户的数据
            lossCustomerList.forEach(customer -> {
                // 定义流失客户对象
                CustomerLoss customerLoss = new CustomerLoss();
                // 创建时间  系统当前时间
                customerLoss.setCreateDate(new Date());
                // 客户经理
                customerLoss.setCusManager(customer.getCusManager());
                // 客户名称
                customerLoss.setCusName(customer.getName());
                // 客户编号
                customerLoss.setCusNo(customer.getKhno());
                // 是否有效  1=有效
                customerLoss.setIsValid(1);
                // 修改时间  系统当前时间
                customerLoss.setUpdateDate(new Date());
                // 客户流失状态   0=暂缓流失状态  1=确认流失状态
                customerLoss.setState(0);
                // 客户最后下单时间
                // 通过客户ID查询最后的订单记录（最后一条订单记录）
                CustomerOrder customerOrder = customerOrderMapper.queryLossCustomerOrderByCustomerId(customer.getId());
                // 判断客户订单是否存在，如果存在，则设置最后下单时间
                if (customerOrder != null) {
                    customerLoss.setLastOrderTime(customerOrder.getOrderDate());
                }
                // 将流失客户对象设置到对应的集合中
                customerLossList.add(customerLoss);

                // 将流失客户的ID设置到对应的集合中
                lossCustomerIds.add(customer.getId());
            });
            // 批量添加流失客户记录
            AssertUtil.isTrue(customerLossMapper.insertBatch(customerLossList) != customerLossList.size(), "客户流失数据转移失败！");

            /* 3. 批量更新客户的流失状态 */
            AssertUtil.isTrue(customerMapper.updateCustomerStateByIds(lossCustomerIds) != lossCustomerIds.size(), "客户流失数据转移失败！");
        }
    }

    /**
     * 查询客户的贡献度
     * @param customerQuery
     * @return
     */
    public Map<String, Object> queryCustomerContributionByParams(CustomerQuery customerQuery){
        HashMap<String, Object> map = new HashMap<>();
        // 开启分页(设置起始页和每页的size)
        PageHelper.startPage(customerQuery.getPage(), customerQuery.getLimit());
        // 查询所有的customer的数据信息，按条件查询数据(数据封装在PageInfo对象中)
        List<Map<String, Object>> maps = customerMapper.queryCustomerContributionByParams(customerQuery);
        // 将需要分页的数据列表传入到PageInfo中，得到对应的分页对象(就可以从pageInfo对象中拿取分页数，起始页等等)
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(maps);

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
