package com.module.swpay.http.inter;

import android.content.Context;

import com.module.swpay.http.bean.UserInfoBean;

/**
 * Created by wanglu on 2019/10/8.
 */
public interface ISendUserInfo {
    public UserInfoBean getUserInfo(Context context);
}
