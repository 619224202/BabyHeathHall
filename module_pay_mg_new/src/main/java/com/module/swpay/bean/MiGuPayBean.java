package com.module.swpay.bean;

import android.content.Context;

import com.migu.sdk.api.CommonInfo;
import com.migu.sdk.api.CommonPayInfo;
import com.migu.sdk.api.PayCallBack;

/**
 * 咪咕魔百盒家庭应用订购参数
 * Created by wanglu on 2019/9/16.
 */
public class MiGuPayBean extends PayBean {
    //计费时传入的activity
    private Context context;
    //计费基本信息
    private CommonInfo commonInfo;
    //计费信息
    private CommonPayInfo[] payInfos;
    //透传字段
    private String cpparam;
    //预留字段
    private String reservedParam;
    //订购回调
    private PayCallBack.IPayCallback callback;

    public CommonInfo getCommonInfo() {
        return commonInfo;
    }

    public void setCommonInfo(CommonInfo commonInfo) {
        this.commonInfo = commonInfo;
    }

    public CommonPayInfo[] getPayInfos() {
        return payInfos;
    }

    public void setPayInfos(CommonPayInfo[] payInfos) {
        this.payInfos = payInfos;
    }

    public String getCpparam() {
        return cpparam;
    }

    public void setCpparam(String cpparam) {
        this.cpparam = cpparam;
    }

    public String getReservedParam() {
        return reservedParam;
    }

    public void setReservedParam(String reservedParam) {
        this.reservedParam = reservedParam;
    }

    public PayCallBack.IPayCallback getCallback() {
        return callback;
    }

    public void setCallback(PayCallBack.IPayCallback callback) {
        this.callback = callback;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
