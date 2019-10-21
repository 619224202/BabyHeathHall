package com.module.swpay.http.inter;

import com.module.swpay.http.bean.AuthorizeBean;

/**
 * Created by wanglu on 2019/10/8.
 */
public interface ISendAuthProduct {
    public AuthorizeBean productAuthorize(String xml);
}
