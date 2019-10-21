package com.module.swpay.http.inter;

import com.module.swpay.http.bean.OmsProductBean;

/**
 * Created by wanglu on 2019/10/8.
 * 定价接口
 */
public interface ISendProductPrice {
    //定价
    public OmsProductBean checkProductPrice(String xml);
}
