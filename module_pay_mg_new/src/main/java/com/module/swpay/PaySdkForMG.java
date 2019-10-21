package com.module.swpay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;

import com.migu.sdk.api.CommonInfo;
import com.migu.sdk.api.CommonPayInfo;
import com.migu.sdk.api.MiguSdk;
import com.migu.sdk.api.PayResult;
import com.module.swpay.bean.AuthBean;
import com.module.swpay.bean.InitBean;
import com.module.swpay.bean.MiGuPayBean;
import com.module.swpay.bean.PayBean;
import com.module.swpay.callback.ExitCallBack;
import com.module.swpay.callback.IAuthBack;
import com.module.swpay.callback.InitCallBack;
import com.module.swpay.callback.PayCallBack;
import com.module.swpay.callback.SdkInterface;
import com.module.swpay.http.SanxiIptvOpt;
import com.module.swpay.http.bean.ADVPayBean;
import com.module.swpay.http.bean.ADVPayResultBean;
import com.module.swpay.http.bean.AuthorizeBean;
import com.module.swpay.http.bean.OrderBean;
import com.module.swpay.http.bean.PayParamBean;
import com.module.swpay.http.bean.UserInfoBean;
import com.module.swpay.http.saxxml.PayParamSax;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.UUID;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by wanglu on 2019/7/9.
 */
public class PaySdkForMG implements SdkInterface {
    private final static String TAG = PaySdkForMG.class.getSimpleName();
    private Activity mainActivity;
    //用户信息
    private UserInfoBean userInfo;
    //鉴权返回信息
    private AuthorizeBean authorizeBean;

    public static String productCode = "8802000239";

    public String seqId = "";
    //下单返回
    private ADVPayBean advPayBean;
    //用户订购信息
    private PayParamBean payParamBean;

    private PayCallBack payCallBack;

    private boolean isCanPay = false;

    private boolean isInpay = false;
    private boolean isPayMoneySuccess = false;
    private boolean b_checkPayState = false;
    private boolean isPaySuccess = false;
    //订购展示页面
    private ThirdPayActivity payActivity;
    private boolean isThirdPay;
    //contentId
    public String contentId = "";

    public String iPayType;

    private PayBean payBean;

    private Object object = new Object();

    public static boolean isUseThirdPay = false;

    public PaySdkForMG(){

    }
    @Override
    public void InitPay(Activity activity, InitCallBack initCallBack,
                        PayCallBack payCallBack, boolean isBuyCircle, ArrayList<PayBean> propList) {
        this.mainActivity = activity;
        //System.loadLibrary("mg20pbase");

//        GameTv.init(activity);
    }

    @Override
    public void setInitBean(InitBean initBean) {

    }

    @Override
    public void InitPay(final Activity activity, final InitCallBack initCallBack, boolean isBuyCircle, ArrayList<PayBean> propList) {
        this.mainActivity = activity;
        initScales(activity);
        MiguSdk.initializeApp(activity);
        SanxiIptvOpt.getInstance().init(activity);
        //获取用户信息
        userInfo = SanxiIptvOpt.getInstance().getUserInfo();
        checkProductPrice(initCallBack);

    }

    public void initScales(Activity activity){
        DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
        PayityUnity.screenW = displayMetrics.widthPixels;
        PayityUnity.screenH = displayMetrics.heightPixels;
        PayityUnity.scaleX = PayityUnity.screenW /PayityUnity.origonScreenW;
        PayityUnity.scaleY = PayityUnity.screenH/PayityUnity.origonScreenH;
        System.out.println("scaleX="+PayityUnity.scaleX+",scaleY="+PayityUnity.scaleY);
    }

    /**
     * 定价检测
     * @param initCallBack
     */
    public void checkProductPrice(InitCallBack initCallBack){
        StringBuffer strBuf = new StringBuffer();
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(SanxiIptvOpt.getInstance().getPriceJson());
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jobj = jsonArray.getJSONObject(i);
                strBuf.append(jobj.getString("productCode"));
                if(i<jsonArray.length()-1){
                    strBuf.append(";");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String platform = SanxiIptvOpt.getInstance().getCpId();
        contentId = String.valueOf(System.currentTimeMillis());
        String copyRightId = userInfo.getBussinessInfoBean().getCopyrightId();
        String copyRightContentId = String.valueOf(System.currentTimeMillis());;
        String strategyCode = strBuf.toString();
        SanxiIptvOpt.getInstance().checkProductPrice(platform,contentId,copyRightId,copyRightContentId,strategyCode);
        InitBean initBean = new InitBean();
        initCallBack.initSuccess(initBean);
    }

    /**
     * 初始化Pay信息
     * @param activity
     * @param initCallBack
     */
    @Override
    public void InitPay(final Activity activity, final InitCallBack initCallBack) {
        InitPay(activity,initCallBack,false,null);
    }

    /**
     * 项目调用订购包里的pay方法
     * @param payBean
     * @param payCallBack
     * @return
     */
    @Override
    public int Pay(final PayBean payBean, final PayCallBack payCallBack) {
        //下单处理，获取订购信息
        this.payCallBack = payCallBack;
        this.payBean = payBean;
        //下单处理，获取订购信息
        doAdvPay();

        final MiGuPayBean miGuPayBean = (MiGuPayBean) payBean;
        final CommonInfo commonInfo = payParamBean.getCommonInfo();
        System.out.println("common="+commonInfo.validation());
        final CommonPayInfo[] commonPayInfos = payParamBean.getPayInfosArray();
        isInpay = true;
        isPayMoneySuccess = false;
        MiguSdk.pay(mainActivity, commonInfo, commonPayInfos,
                "", "", new com.migu.sdk.api.PayCallBack.IPayCallback() {
                    @Override
                    public void onResult(int resultCode, String stateCode, String result) {
                        System.out.println("resultCode="+resultCode+",stateCode="+stateCode+",result="+result);
                        if(resultCode== PayResult.SUCCESS){
                            System.out.println("计费成功，开始订购");
                            toCheckPayState();
                            isInpay = false;
                            isPayMoneySuccess = true;

//                            payActivity.showPaySuccess();
                        }else{
                            isInpay = false;
                            System.out.println("计费失败");
                            isPayMoneySuccess = false;
                            payCallBack.payFailed(payBean);
                            payActivity.showPayFailed(result);
                        }
                    }
                });

        return 0;
    }

    /**
     * 轮询检测订购状态，每秒检测一次
     */
    public  void toCheckPayState(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (object) {
                    String appName = mainActivity.getPackageName();
                    String terminalId = SanxiIptvOpt.getInstance().getStbId();
                    String userId = userInfo.getUserId();
                    String externalSeqNum = advPayBean.getExternalSeqNum();
                    String param = "";
                    String token = userInfo.getToken();
                    b_checkPayState = true;
                    ADVPayResultBean resultBean = null;
                    while (b_checkPayState) {
                        resultBean = SanxiIptvOpt.getInstance().productAdvPayResult(appName, terminalId,
                                userId, externalSeqNum, param, token);
                        if (resultBean.getResult().equals("0")) {
                            if (resultBean.getPayResult().equals("0")) {
                                b_checkPayState = false;
                                isPaySuccess = true;
                            } else if (resultBean.getPayResult().equals("1")) {
                                b_checkPayState = false;
                                isPaySuccess = false;
                            }
                        } else if (resultBean.getResult().equals("1")) {
                            b_checkPayState = false;
                            isPaySuccess = false;
                        }
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if (isPaySuccess) {
                        order();
                    } else {
//            payCallBack.payFailed(new PayBean());
                        if(resultBean!=null && resultBean.getResult().equals("1")) {
                            payCallBack.payFailed(payBean);
                            payActivity.showPayFailed("轮询查询失败");
//                            doPayEnd(false);
                        }else{
                            //doPayEnd(false);
                        }
                    }
                }
            }
        }).start();

    }

    /**
     * 订购产品,
     */
    private void order(){
        AuthorizeBean.ProductBean productBean = authorizeBean.getProductBean(productCode);
        String seqId = this.seqId;
        String userId = userInfo.getUserId();
        String accountIdentify = authorizeBean.getAccountIdentify();
        String terminalId = SanxiIptvOpt.getInstance().getStbId();
        String copyRightId = userInfo.getBussinessInfoBean().getCopyrightId();
        String systemId = "0";
        String productCode = PaySdkForMG.productCode;
        String contentId = this.contentId;
        String coypRightContentId = "";
        String consumeLocal = SanxiIptvOpt.userLocal;
        String consumeScene = "01";
        String consumeBehaviour = "02";
        String channelId = "00001";
        String payType = productBean.getPaymentType();
        if((Integer.parseInt(payType)&16)!=0){
            channelId = "00001";
        }
        payType = iPayType;
        String subType = "03";
        String thirdTransID = advPayBean.getExternalSeqNum();
        String tokenString = userInfo.getToken();
        OrderBean orderBean = SanxiIptvOpt.getInstance().productOrder(seqId,userId,accountIdentify,terminalId,copyRightId,systemId,
                productCode,contentId,coypRightContentId,consumeLocal,consumeScene,
                consumeBehaviour,channelId,payType,subType,thirdTransID,tokenString);
        if(orderBean.getResult().equals("0")){
            payActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    payActivity.hideLoading();
                }
            });
            payCallBack.paySuccess(payBean);
           payActivity.showPaySuccess();
        }else{
            payActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    payActivity.hideLoading();
                }
            });
           payCallBack.payFailed(payBean);
           payActivity.showPayFailed(orderBean.getResultDesc());
        }
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

    /**
     * 订购包月产品
     * @param payBean
     * @param ctx
     * @param payCallBack
     * @return
     */
    @Override
    public int payCyclePayment(final PayBean payBean, final Activity ctx, final PayCallBack payCallBack) {
        this.payCallBack = payCallBack;
        this.mainActivity = ctx;
        this.payBean = payBean;
        //下单处理，获取订购信息
        showPayView(null);
        return 0;
    }

    public void doPay(){
        payActivity.showLoading();
        boolean isThirdPay = doAdvPay();
        payActivity.hideLoading();
        System.out.println("isThirdPay="+isThirdPay);
        if(!isThirdPay){
            createQuickPay(payBean,mainActivity);
        }else{
            createThirdPay(mainActivity);
        }
    }

    /**
     * 显示sdk中的订购，别名为快速订购方式
     * @param payBean
     * @param ctx
     */
    public void createQuickPay(final PayBean payBean,final Context ctx){
        final MiGuPayBean miGuPayBean = (MiGuPayBean) payBean;
        final CommonInfo commonInfo = payParamBean.getCommonInfo();
        System.out.println("common="+commonInfo.validation());
        final CommonPayInfo[] commonPayInfos = payParamBean.getPayInfosArray();
        isInpay = true;
        isPayMoneySuccess = false;
        MiguSdk.pay(payActivity, commonInfo, commonPayInfos,
            "", "", new com.migu.sdk.api.PayCallBack.IPayCallback() {
                @Override
                public void onResult(int resultCode, String stateCode, String result) {
                    System.out.println("resultCode="+resultCode+",stateCode="+stateCode+",result="+result);
                    if(resultCode== PayResult.SUCCESS){
                        System.out.println("计费成功，开始订购");
                        payActivity.showLoading();
                        toCheckPayState();
                        //isInpay = false;
                        isPayMoneySuccess = true;
//                        payActivity.showPaySuccess();
                    }else if(resultCode==PayResult.CANCELLED){
                        //isInpay = false;
                        isPayMoneySuccess = false;
                        payCallBack.payFailed(payBean);
//                        payActivity.showPayFailed(result);
                    }else{
                        System.out.println("计费失败");
                        isPayMoneySuccess = false;
                        payCallBack.payFailed(payBean);
                        payActivity.showPayFailed(result);
                    }
                }
        });
    }

    /**
     * 显示第三方订购信息及开始检测
     * @param ctx
     */
    private void createThirdPay(Context ctx){
//       showPayView(advPayBean.getQrCodeImg());
        this.payActivity.showQRView();
       toCheckPayState();
    }

    /**
     * 显示第三方订购信息
     * @param imgData
     */
    private void showPayView(String imgData){
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(mainActivity,ThirdPayActivity.class);
                mainActivity.startActivity(intent);
            }
        });

    }


    /**
     * 获取主activity
     * @return
     */
    public Activity getMainActivity(){
        return mainActivity;
    }

    /**
     * 取消订购，无用
     * @param tradeNo
     * @return
     */
    @Override
    public int cancelCyclePayment(String tradeNo) {
        return 0;
    }

    /**
     * 订购下单处理，获取订单信息，如果为第三方订购，则返回true，否则返回false，交给sdk处理
     * 如果为第三方订购，则需要显示二维码界面
     */
    private boolean doAdvPay(){
//        boolean isThirdPay = false;
        synchronized (object) {
            String UUIDs = UUID.randomUUID().toString().replaceAll("-", "");
            System.out.println("UUID=" + UUIDs);
            seqId = UUIDs.substring(14, 30);
            String userId = SanxiIptvOpt.getInstance().getAccount();
            String terminalId = SanxiIptvOpt.getInstance().getStbId();
            String copyRightId = userInfo.getBussinessInfoBean().getCopyrightId();
            String appName = mainActivity.getPackageName();
            String productCode = PaySdkForMG.productCode;
            AuthorizeBean.ProductBean productBean = authorizeBean.getProductBean(productCode);
            String amount = productBean.getPrice();
            String contentId = productBean.getOrderContentId();
            String channelId = "00001";
            String payType = productBean.getPaymentType();
            //test
//        payType="02";
            //test//
//            int iPayType = (Integer.parseInt(payType) ^ 16);
            int iPayType = 16;
            if(!isUseThirdPay) {
                iPayType = (Integer.parseInt(payType) & 16);
                System.out.println("iPayType=" + iPayType);
                if (iPayType != 0) {
                    payType = String.valueOf(iPayType);
                    isThirdPay = false;
                }else{
                    isThirdPay = true;
                }
            }else{
                iPayType = (Integer.parseInt(payType) ^ 16);
                System.out.println("iPayType=" + iPayType);
                if (iPayType != 0) {
                    payType = String.valueOf(iPayType);
                    isThirdPay = true;
                }else{
                    isThirdPay = false;
                }
            }
            this.iPayType = payType;
            String accountIdentify = authorizeBean.getAccountIdentify();
            String token = userInfo.getToken();

            advPayBean = SanxiIptvOpt.getInstance().productAdvPay(seqId, userId, terminalId, copyRightId,
                    appName, productCode, amount, contentId,
                    channelId, payType, accountIdentify, token);
            //base64位解密，然后解析
            if (!isThirdPay) {
                String payParam = advPayBean.getPayParam();
                parsePayParam(payParam);
                return false;
            } else {
                return true;
            }
        }

    }

    /**
     * 解析payType值为16时返回的payParam
     * @param payParam
     */
    private void parsePayParam(String payParam){
        byte[] paramBytes = Base64.decode(payParam,Base64.DEFAULT);
        InputStream inputStream = new ByteArrayInputStream(paramBytes);
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        try {
            SAXParser saxParser = saxParserFactory.newSAXParser();
            PayParamSax payParamSax = new PayParamSax();
            saxParser.parse(inputStream,payParamSax);
            payParamBean = payParamSax.getPayParamBean();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 根据productCode获得本地的道具价格。
     * @param productCode
     * @return
     */
    private String getProductPrice(String productCode){
        String price = authorizeBean.getPrice(productCode);
        if(price==null || price.length()==0){
            price = SanxiIptvOpt.getInstance().getLocalPrice(productCode);
        }
        if(price==null){
            return "0.01";
        }
        return price;
    }

    /**
     * 鉴权
     * @param productId
     * @param productType
     * @param contentId
     * @param authBack
     */
    @Override
    public void sendAuthRequest(String productId, String productType, String contentId, final IAuthBack authBack) {
        Log.v(TAG,"鉴权开始");
        String userId = userInfo.getUserId();
        String terminalId = SanxiIptvOpt.getInstance().getStbId();
        String copyRightId = userInfo.getBussinessInfoBean().getCopyrightId();
        String contentIds = this.contentId;
        String token = userInfo.getToken();
        authorizeBean = SanxiIptvOpt.getInstance().productAuthorize(userId,terminalId,copyRightId,contentIds,token);
        int resultCode = Integer.parseInt(authorizeBean.getResult());
        if(resultCode==0){
            authBack.authSuccess(new AuthBean());
        }else{
            authBack.authFailed(new AuthBean());
            isCanPay = (resultCode==1||resultCode==20||resultCode==50);
        }
    }

    /**
     * 退出产品时调用
     * @param callBack
     */
    @Override
    public void exit(final ExitCallBack callBack){

        MiguSdk.exitApp(mainActivity);
    }

    /**
     * 设置订购显示的activity
     * @param payActivity
     */
    protected void setPayActivity(ThirdPayActivity payActivity){
        this.payActivity = payActivity;
    }


    /**
     * 退出产品时调用
     * @param context
     */
    public static void exit(Context context){
        MiguSdk.exitApp(context);
    }

    /**
     * 订购结束后调用回调函数
     *
     */
    public void doPayEnd(boolean isPaySuccess){
        if( payActivity!=null){
            payActivity.endActivity(mainActivity);
        }
        if(isPaySuccess){
            payCallBack.paySuccess(payBean);
        }else{
            payCallBack.payFailed(payBean);
        }
    }

    public void endPayActivity(){
        if( payActivity!=null){
            payActivity.endActivity(mainActivity);
        }
    }

    public String getImgData(){
        return advPayBean.getQrCodeImg();
    }

    protected String getImgUrl(){
        return advPayBean.getQrCodeImgUrl();
    }

    //停止检测
    public void stopCheck(){
        b_checkPayState = false;
//        payActivity.showLoading();
        boolean isPay = doAdvPay();
//        payActivity.hideLoading();
        createThirdPay(mainActivity);
//        doPayEnd(false);
    }


    public void back(){
        b_checkPayState = false;
        payActivity.endActivity(mainActivity);
    }

    public ArrayList<AuthorizeBean.ProductBean> getProductBeanList(){
        return authorizeBean.getProductToOrderList();
    }

    public AuthorizeBean.ProductBean getProductBean(){
        return authorizeBean.getProductBean(productCode);
    }

    public String getPhone(){
        return authorizeBean.getAccountIdentify();
    }

    public boolean isUseThirdPay(){
        return isUseThirdPay;
    }

    public void setUseThirdPay(boolean isUseThirdPay){
        this.isUseThirdPay = isUseThirdPay;
    }
}
