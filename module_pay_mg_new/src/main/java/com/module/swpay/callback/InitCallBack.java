package com.module.swpay.callback;

import com.module.swpay.bean.InitBean;

/**
 * Created by wanglu on 2019/1/7.
 */
public interface InitCallBack {
    public void initSuccess(InitBean initBean);
    public void initFailed();
}
