package com.xxxx.crm.query;

import com.xxxx.crm.base.BaseQuery;

/**
 * @author lms
 * @date 2021-07-05 - 18:04
 */

public class OrderDetailsQuery extends BaseQuery {

    private Integer orderId;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }
}
