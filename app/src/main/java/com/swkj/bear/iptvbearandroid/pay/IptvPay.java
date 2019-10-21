package com.swkj.bear.iptvbearandroid.pay;

import android.content.Context;
import android.nfc.Tag;
import android.util.Log;

import com.migu.sdk.api.CommonInfo;
import com.migu.sdk.api.CommonPayInfo;
import com.module.swpay.PayService;
import com.module.swpay.bean.AuthBean;
import com.module.swpay.bean.InitBean;
import com.module.swpay.bean.MiGuPayBean;
import com.module.swpay.bean.PayBean;
import com.module.swpay.callback.IAuthBack;
import com.module.swpay.callback.InitCallBack;
import com.module.swpay.callback.PayCallBack;
import com.swkj.babyhall.MainActivity;


import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;



/**
 * Created by zuixian on 2018/5/11.
 */

public class IptvPay {
    public final static String TAG = IptvPay.class.getSimpleName();

    private static final String APP_ID = "swxxxz";

    private static final String APP_KEY = "swjf";

    public static String useID;

    public static String mac;

    public static String SDKVersion;

    public static MainActivity act;

    public static boolean b_payLock = false;

    public static void setMainActivity(MainActivity activity){
        act = activity;
    }


    public static void InitPay(final MainActivity activity) {
        /**
         * 初始化sdk
         * 1.由运营商提供的应用id
         * 2.由运营商提供的应用key
         * 3.版本检查结构,默认值为0
         */

        mac = getLocalMacAddress();

        PayService.getInstance().initSDK(activity, "", "", "宝宝智慧屋", false, new InitCallBack() {
            @Override
            public void initSuccess(InitBean initBean) {

            }

            @Override
            public void initFailed() {

            }
        });



        Log.i("money", "MainMidlet:145  mac:" + mac);
        /*
         * 获取SDK版本号
		 */

    }


    private static String getLocalMacAddress() {
        // TODO Auto-generated method stub
        byte[] mac = null;
        StringBuffer sb = new StringBuffer();
        try {
            Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni = netInterfaces.nextElement();
                Enumeration<InetAddress> address = ni.getInetAddresses();

                while (address.hasMoreElements()) {
                    InetAddress ip = (InetAddress) address.nextElement();
                    if (ip.isAnyLocalAddress() || !(ip instanceof Inet4Address) ||
                            ip.isLoopbackAddress()) {
                        continue;
                    }
                    if (ip.isSiteLocalAddress()) {
                        mac = ni.getHardwareAddress();
                    } else if (!ip.isLinkLocalAddress()) {
                        mac = ni.getHardwareAddress();
                        break;
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mac != null) {
            for (int i = 0; i < mac.length; i++) {
                sb.append(parseByte(mac[i]));
            }
            System.out.println("mac-mac-mac"+sb);
            return sb.substring(0, sb.length() - 1).replace("0", "");
        } else {
            return "000000FFFFFF";
        }
    }

    private static String parseByte(byte b) {
        // TODO Auto-generated method stub
        String string = "00" + Integer.toHexString(b);
        return string.substring(string.length() - 3);
    }

    /**
     * 调用外部购买界面
     *
     *
     * @return 具体做法：调用支付接口时游戏App预置一个是否正在计费的标记，然后在onResume (活动从暂停状态中恢复时调用)
     * 中判断此标记，若该标记有效则继续游戏。
     */
    public static int Pay() {
//        Log.i("money", "msg:" + payment.toString());
//        int ret = Commplatform.getInstance().uniPayExt(payment,
//                act,
//                new CallbackListener<PayResult>() {
//                    @Override
//                    public void callback(final int arg0, final PayResult arg1) {
//                        act.runOnUiThread(new Runnable() {
//                            public void run() {
//
//                                Log.v("money", "DressupObj-->748   arg0:"
//                                        + arg0);
//                                if (arg0 == ErrorCode.COM_PLATFORM_SUCCESS) {
//                                    if (arg1.getTradeStatus() == "0") {
//                                        //支付成功
//                                    } else {
//                                        //支付失败
//                                    }
//                                    Log.v("money", "DressupObj-->751");
//                                } else {
//                                    //支付失败
//                                    Log.v("money", "DressupObj-->755");
//                                }
//
//                            }
//                        });
//                    }
//                });
//        Log.i("money", "DressupObj-->763          ret:" + ret);
//        if (ret == 0) {
//            return 0;
//        } else {
//            // 返回错误,支付过程结束
//            return -1;
//        }
        return 0;
    }

    /**
     * 订购周期性产品
     * <p>
     * author zuixian
     * created 2018/5/4 上午10:58
     *
     * @return
     */
    public static int payCyclePayment(Context ctx) {
        MiGuPayBean miGuPayBean = new MiGuPayBean();
        CommonInfo commonInfo = new CommonInfo();
        //测试参数
        commonInfo.setOrderId("");
        commonInfo.setcType("5");
        commonInfo.setOperCode("4");
        commonInfo.setSyn(false);
        commonInfo.setStbId(IptvPay.getLocalMacAddress());
        commonInfo.setPayNum("");   //电话号码
//        commonInfo.setCustomBizExpiryDate();
        miGuPayBean.setCommonInfo(commonInfo);

        CommonPayInfo[] payInfos = new CommonPayInfo[1];
        payInfos[0] = new CommonPayInfo();
        payInfos[0].setOrderId("");
        payInfos[0].setIsMonthly("1");
        payInfos[0].setChannelId("06");
        payInfos[0].setCpId("");
        payInfos[0].setProductId("");       //productId必填
        payInfos[0].setPrice("0.01");
        payInfos[0].setSpCode("");          //获取spcode
        payInfos[0].setServCode("");        //包月必须
        miGuPayBean.setPayInfos(payInfos);

        b_payLock = true;
        PayService.getInstance().payCirclePays(miGuPayBean, new PayCallBack() {
            @Override
            public void paySuccess(PayBean payBean) {
                b_payLock = false;
                act.callBackPayCycle(payBean.getTradeNo(),IptvPay.useID,"1");
            }

            @Override
            public void payFailed(PayBean payBean) {
                b_payLock = false;
                act.callBackPayCycle(payBean.getTradeNo(),IptvPay.useID,"0");
            }
        });
        return 0;
    }

    /**
     * 取消订购周期性产品
     * <p>
     * author zuixian
     * created 2018/5/4 上午11:08
     *
     * @param tradeNo 要取消的订单号
     * @return
     */
    public static int cancelCyclePayment(final String tradeNo) {
//        int ret = Commplatform.getInstance().cancelCyclePay(tradeNo,
//                act,
//                new CallbackListener<PayResult>() {
//                    @Override
//                    public void callback(final int arg0, final PayResult arg1) {
//                        act.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                if (arg0 == ErrorCode.COM_PLATFORM_SUCCESS) {
//                                    Log.v("money", "取订成功");
//                                } else {
//                                    Log.v("money", "取订失败");
//                                }
//                            }
//                        });
//                    }
//                });
//        if (ret == 0) {
//            return 0;
//        } else {
//            // 返回错误,取消过程结束
//            return -1;
//        }
        return 0;
    }


    /**
     * 检查订购关系的有效性
     *
     * @param productId
     * 运营组提供的商品id
     * @param productType
     * 1:按次购买产品类型 2:周期性扣费产品类型
     */
    public static String propId = "";


//    private static String params ="";

    public static void sendAuthRequest(final String productId, String productType,String contentId,final String info) {
        PayService.getInstance().authPermission(productId, contentId, new IAuthBack() {
            @Override
            public void authSuccess(AuthBean authResult) {
                act.toMonitorCompanyList(useID,"1",productId,info);
                Log.v(TAG,"鉴权成功");
            }

            @Override
            public void authFailed(AuthBean authResult) {
                act.toMonitorCompanyList(useID,"0",productId,info);
                Log.v(TAG,"鉴权失败");
            }
        });

    }
}
