package com.module.swpay.http.inter;

import com.module.swpay.http.bean.OrderBean;

/**
 * Created by wanglu on 2019/10/8.
 */
public interface ISendOrder {
    public OrderBean orderProduct(String xml);
}
