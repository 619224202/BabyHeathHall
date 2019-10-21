package com.module.swpay.callback;

import com.module.swpay.bean.AuthBean;

/**
 * Created by wanglu on 2019/4/12.
 */
public interface IAuthBack {
    public void authSuccess(AuthBean authResult);
    public void authFailed(AuthBean authResult);
}
