package com.module.swpay.http.inter;

import com.module.swpay.http.bean.ADVPayBean;

/**
 * Created by wanglu on 2019/10/8.
 */
public interface ISendAdvPay {
    //下单处理
    public ADVPayBean advPay(String xml);
}
