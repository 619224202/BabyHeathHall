package com.module.swpay;

import android.app.Activity;

import com.module.swpay.bean.InitBean;
import com.module.swpay.bean.PayBean;
import com.module.swpay.callback.ExitCallBack;
import com.module.swpay.callback.IAuthBack;
import com.module.swpay.callback.InitCallBack;
import com.module.swpay.callback.PayCallBack;
import com.module.swpay.callback.SdkInterface;

import java.util.ArrayList;


/**
 * Created by wanglu on 2019/7/8.
 */
public class PaySdkForNormal implements SdkInterface {
    @Override
    public void InitPay(Activity activity, InitCallBack initCallBack, PayCallBack payCallBack, boolean isBuyCircle, ArrayList<PayBean> propList) {

    }

    @Override
    public void setInitBean(InitBean initBean) {

    }

    @Override
    public void InitPay(Activity activity, InitCallBack initCallBack) {

    }

    @Override
    public void InitPay(Activity activity, InitCallBack initCallBack, boolean isBuyCircle, ArrayList<PayBean> propList) {

    }

    @Override
    public int Pay(PayBean payBean, PayCallBack payCallBack) {
        return 0;
    }

    @Override
    public int Pay(int id, PayCallBack payCallBack) {
        return 0;
    }

    @Override
    public void checkTradeNo(Activity main, InitCallBack initCallBack) {

    }

    @Override
    public void checkTradeNo(Activity main, InitCallBack initCallBack, PayCallBack payCallBack) {

    }

    @Override
    public int payCyclePayment(PayBean payBean, Activity ctx, PayCallBack payCallBack) {
        return 0;
    }

    @Override
    public int cancelCyclePayment(String tradeNo) {
        return 0;
    }

    @Override
    public void sendAuthRequest(String productId, String productType, String contentId, IAuthBack authBack) {

    }

    @Override
    public void exit(ExitCallBack callBack) {

    }


}
