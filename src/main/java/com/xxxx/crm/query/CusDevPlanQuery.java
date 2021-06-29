package com.xxxx.crm.query;

import com.xxxx.crm.base.BaseQuery;

/**
 * @author lms
 * @date 2021-06-02 - 21:51
 */
public class CusDevPlanQuery extends BaseQuery {

    // 营销机会的主键
    private Integer saleChanceId;

    public Integer getSaleChanceId() {
        return saleChanceId;
    }

    public void setSaleChanceId(Integer saleChanceId) {
        this.saleChanceId = saleChanceId;
    }
}
