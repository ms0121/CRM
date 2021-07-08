package com.xxxx.crm.query;

import com.xxxx.crm.base.BaseQuery;

/**
 * @author lms
 * @date 2021-07-04 - 17:52
 */

public class CustomerQuery extends BaseQuery {

    private String customerName;
    private String customerNo;
    private String level;

    private String time;
    private Integer type;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
