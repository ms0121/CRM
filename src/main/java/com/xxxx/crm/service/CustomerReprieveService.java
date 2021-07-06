package com.xxxx.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.CustomerLossMapper;
import com.xxxx.crm.dao.CustomerReprieveMapper;
import com.xxxx.crm.query.CustomerReprieveQuery;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.vo.CustomerLoss;
import com.xxxx.crm.vo.CustomerReprieve;
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
 * @date 2021-07-06 - 16:50
 */

@Service
public class CustomerReprieveService extends BaseService<CustomerReprieve, Integer> {

    @Resource
    private CustomerReprieveMapper customerReprieveMapper;

    @Resource
    private CustomerLossMapper customerLossMapper;

    /**
     * 根据流失客户Id查询用户的详细信息
     * @param customerReprieveQuery
     * @return
     */
    public Map<String, Object> queryCustomerRepByParams(CustomerReprieveQuery customerReprieveQuery) {
        Map<String, Object> map = new HashMap<>();

        // 构建分页数据
        PageHelper.startPage(customerReprieveQuery.getPage(), customerReprieveQuery.getLimit());
        // 查询所有的customer的数据信息，按条件查询数据(数据封装在PageInfo对象中)
        List<CustomerReprieve> customerReprieves = customerReprieveMapper.selectByParams(customerReprieveQuery);
        // 将需要分页的数据列表传入到PageInfo中，得到对应的分页对象(就可以从pageInfo对象中拿取分页数，起始页等等)
        PageInfo<CustomerReprieve> pageInfo = new PageInfo<>(customerReprieves);

        // 设置layui表格需要的map形式的数据信息
        map.put("code", 0);
        map.put("msg", "success");
        map.put("count", pageInfo.getTotal());
        map.put("data", pageInfo.getList());

        return map;
    }

    /**
     * 添加暂缓数据功能
     *  1.参数校验
     *      流失客户id(lossId)    非空，数据存在
     *      暂缓措施内容   measure  非空
     *  2.设置默认值
     *      是否有效   默认有效  1
     *      创建时间   系统当前时间
     *      修改时间   系统当前时间
     *  3.执行添加操作，判断受影响的行数
     * @param customerReprieve
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addCustomerRep(CustomerReprieve customerReprieve) {
        /*1.参数校验*/
        checkParams(customerReprieve.getLossId(), customerReprieve.getMeasure());

        /*2.设置默认值*/
        customerReprieve.setIsValid(1);
        customerReprieve.setCreateDate(new Date());
        customerReprieve.setUpdateDate(new Date());

        /*3.执行添加操作，判断受影响的行数*/
        AssertUtil.isTrue(customerReprieveMapper.insertSelective(customerReprieve) < 1,
                "暂缓数据添加失败!");
    }

    // 参数校验
    public void checkParams(Integer lossId, String measure) {
        /*1.参数校验*/
        // 流失客户id(lossId)    非空
        AssertUtil.isTrue(lossId == null, "数据不存在!");
        // 根据lossId查询流失客户的数据，判断数据是否存在
        CustomerLoss customerLoss = customerLossMapper.selectByPrimaryKey(lossId);
        AssertUtil.isTrue(customerLoss == null, "流失客户数据不存在!");
        // 暂缓措施内容
        AssertUtil.isTrue(measure == null, "暂缓措施内容不允许为空!");
    }

    /**
     * 更新暂缓数据功能
     *  1.参数校验
     *      暂缓数据id  非空，并且暂缓数据存在
     *      流失客户id  lossId  非空，数据存在
     *      暂缓措施内容   measure  非空
     *  2.设置默认值
     *      修改时间   系统当前时间
     *  3.执行修改操作，判断受影响的行数
     * @param customerReprieve
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateCustomerRep(CustomerReprieve customerReprieve) {
        /*1.参数校验*/
        AssertUtil.isTrue(customerReprieve.getId() == null, "暂缓数据不存在!");
        CustomerReprieve temp = customerReprieveMapper.selectByPrimaryKey(customerReprieve.getId());
        AssertUtil.isTrue(temp == null, "暂缓数据不存在!");
        checkParams(customerReprieve.getLossId(), customerReprieve.getMeasure());

        /*2.设置默认值*/
        customerReprieve.setUpdateDate(new Date());

        /*3.执行修改操作，判断受影响的行数*/
        AssertUtil.isTrue(customerReprieveMapper.updateByPrimaryKeySelective(customerReprieve)<1,
                "暂缓数据更新失败!");

    }

    /**
     * 删除暂缓数据
     *  1.参数检验，判断id是否为空，且数据存在
     *  2.设置isValid为0,以及更新的时间
     *  3.执行更新操作，判断受影响的行数
     * @param id
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteCustomerRep(Integer id) {
        /* 1.参数检验，判断id是否为空，且数据存在*/
        AssertUtil.isTrue(id == null, "待删除暂缓数据不存在!");
        CustomerReprieve customerReprieve = customerReprieveMapper.selectByPrimaryKey(id);
        AssertUtil.isTrue(customerReprieve == null, "待删除暂缓数据不存在!");

        /*2.设置isValid为0*/
        customerReprieve.setIsValid(0);
        customerReprieve.setUpdateDate(new Date());

        /* 3.执行更新操作，判断受影响的行数*/
        AssertUtil.isTrue(customerReprieveMapper.updateByPrimaryKeySelective(customerReprieve) < 1,
                "删除暂缓数据失败!");
    }

}
