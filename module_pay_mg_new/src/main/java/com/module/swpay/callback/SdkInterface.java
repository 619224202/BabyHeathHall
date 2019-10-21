package com.module.swpay.callback;

import android.app.Activity;

import com.module.swpay.bean.InitBean;
import com.module.swpay.bean.PayBean;

import java.util.ArrayList;


/**
 * Created by wanglu on 2019/1/17.
 */
public interface SdkInterface {
    public void InitPay(final Activity activity, final InitCallBack initCallBack, PayCallBack payCallBack,
                        boolean isBuyCircle, ArrayList<PayBean> propList);
    public void setInitBean(InitBean initBean);
    public void InitPay(final Activity activity,final InitCallBack initCallBack);
    public void InitPay(final Activity activity, final InitCallBack initCallBack, boolean isBuyCircle, ArrayList<PayBean> propList);
    public  int Pay(final PayBean payBean, final PayCallBack payCallBack);
    public  int Pay(final int id, final PayCallBack payCallBack);
    public void checkTradeNo(final Activity main, final InitCallBack initCallBack);
    public void checkTradeNo(final Activity main, final InitCallBack initCallBack, PayCallBack payCallBack);
    public  int payCyclePayment(final PayBean payBean, final Activity ctx, final PayCallBack payCallBack);
    public  int cancelCyclePayment(final String tradeNo);
    public void sendAuthRequest(final String productId, String productType, String contentId, final IAuthBack authBack);

    public void exit(ExitCallBack callBack);
}
