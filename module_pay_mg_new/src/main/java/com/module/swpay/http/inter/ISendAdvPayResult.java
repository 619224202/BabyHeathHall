package com.module.swpay.http.inter;

import com.module.swpay.http.bean.ADVPayResultBean;

/**
 * Created by wanglu on 2019/10/8.
 */
public interface ISendAdvPayResult {
    public ADVPayResultBean doAdvPayResult(String xml);
}
