package com.module.swpay.callback;


import com.module.swpay.bean.PayBean;

/**
 * Created by wanglu on 2019/1/16.
 */
public interface PayCallBack {
    /**
     *
     * @param payBean
     */
    public void paySuccess(PayBean payBean);

    /**
     *
     * @param payBean
     */
    public void payFailed(PayBean payBean);
}
