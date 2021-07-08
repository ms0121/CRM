package com.xxxx.crm.query;

import com.xxxx.crm.base.BaseQuery;

/**
 * @author lms
 * @date 2021-07-07 - 9:26
 */

public class CustomerServeQuery extends BaseQuery {

    private String customer;

    private Integer type;

    private String state;

    // 设置分配人
    private Integer assigner;

    public Integer getAssigner() {
        return assigner;
    }

    public void setAssigner(Integer assigner) {
        this.assigner = assigner;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
