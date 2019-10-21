package com.module.swpay.callback;

/**
 * Created by wanglu on 2019/1/18.
 */
public interface HttpCallBack {
    public void httpSuccess(String returnData);
    public void httpFailed(int responseCode);
}
