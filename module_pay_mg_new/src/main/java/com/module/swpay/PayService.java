package com.module.swpay;

import android.app.Activity;
import android.util.Log;

import com.module.swpay.bean.InitBean;
import com.module.swpay.bean.PayBean;
import com.module.swpay.callback.ExitCallBack;
import com.module.swpay.callback.IAuthBack;
import com.module.swpay.callback.InitCallBack;
import com.module.swpay.callback.PayCallBack;
import com.module.swpay.callback.SdkInterface;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by wanglu on 2019/1/17.
 */
public class PayService {
    private static PayService instance;
    private SdkInterface paySdk;
    private InitBean initBean;
    private Activity ctx;

    public static PayService getInstance(){
        if(instance==null){
            instance = new PayService();
        }
        return instance;
    }

    public void initSDK(Activity ctx, String app_id, String app_key,
                        String app_name, boolean isCircle,  InitCallBack initCallBack){
        //设置全局变量appid
        PayityUnity.app_id = app_id;
        //设置全局变量app_key
        PayityUnity.app_key = app_key;
        //容器类
        this.ctx = ctx;
        initBean = new InitBean();
        initBean.setApp_id(app_id);
        initBean.setApp_key(app_key);
        initBean.setGameName(app_name);
        paySdk = createSdk();
        //Log.v("paySdk>>>>>>>>>>",paySdk==null?"null":paySdk.toString());
        switch(PayConstant.payUnion){
            case PayConstant.PAY_TJLT:
                paySdk.setInitBean(initBean);
                break;
            case PayConstant.PAY_MG:
                break;
        }

        paySdk.InitPay(ctx,initCallBack);
    }

    private SdkInterface createSdk(){
        String className = "";

        switch(PayConstant.payUnion){
            case PayConstant.PAY_TJLT:
                className = "com.module.swpay.PaySdkForTJ";
                break;
            case PayConstant.PAY_HX:
                className = "com.module.swpay.PaySdkForHX";
                break;
            case PayConstant.PAY_MG:
                className = "com.module.swpay.PaySdkForMG";
                break;
        }
        if(PayConstant.isDebug){
            className = "com.module.swpay.PaySdkForNormal";
        }
        try {
            Class sdkClass = Class.forName(className);
            SdkInterface sdk =(SdkInterface)(sdkClass.newInstance());
            return sdk;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return new PaySdkForNormal();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 购买一次性道具接口
     * @param payBean
     * @param payCallBack
     */
    public void payUnitPays(PayBean payBean, PayCallBack payCallBack){
        paySdk.Pay(payBean,payCallBack);
    }

    /**
     *
     * @param payId
     * @param payCallBack
     */
    public void payUnitPays(int payId, PayCallBack payCallBack){
        paySdk.Pay(payId,payCallBack);
    }

    public void payCirclePays(PayBean payBean,PayCallBack payCallBack){
        paySdk.payCyclePayment(payBean,ctx,payCallBack);
    }
    //一般用于包月产品
    public void authPermission(String productId, String contentId, IAuthBack authBack){
        paySdk.sendAuthRequest(productId,"2",contentId,authBack);
    }

    public void exitGame(ExitCallBack callBack){
        paySdk.exit(callBack);
    }

    public String getTVMacs(){
        return PayityUnity.getLocalMacAddressFromBusybox();
    }

    protected SdkInterface getPaySdk(){
        return paySdk;
    }
}
