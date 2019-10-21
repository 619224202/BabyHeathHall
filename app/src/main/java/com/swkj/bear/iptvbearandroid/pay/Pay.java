package com.swkj.bear.iptvbearandroid.pay;

import android.content.Context;


/**
 * Created by zuixian on 2018/5/11.
 */

public class Pay {

    private static String notifyURL = "http://202.99.114.28:2380/IptvBearAndroid/order!orderMsg.action";

    public static void payOne(String tradeNo,String productId,String subject,Double amount,boolean isDebug){


        IptvPay.Pay();
    }

    public static void payOne(boolean isDebug){

        IptvPay.Pay();
    }

    public static void payCycle(String tradeNo, String productId, boolean isDebug, Context ctx){


        IptvPay.payCyclePayment(ctx);
    }

    public static void sendAuthRequest(boolean isDebug,String info){
        String cid = "swxxxzby015";
        String pid = "swxxxzby015";
        String productType = "2";//周期性产品
        IptvPay.sendAuthRequest(pid,productType,cid,info);
    }

    public static void cancelPayCycle(String tradeNo){
        IptvPay.cancelCyclePayment(tradeNo);
    }
}
