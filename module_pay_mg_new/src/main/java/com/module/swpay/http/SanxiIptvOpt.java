package com.module.swpay.http;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Xml;

import com.module.swpay.PayityUnity;
import com.module.swpay.http.bean.ADVPayBean;
import com.module.swpay.http.bean.ADVPayResultBean;
import com.module.swpay.http.bean.AuthorizeBean;
import com.module.swpay.http.bean.OmsProductBean;
import com.module.swpay.http.bean.OrderBean;
import com.module.swpay.http.bean.UserInfoBean;
import com.module.swpay.http.inter.IIptvOptInterface;
import com.module.swpay.http.inter.ISendAdvPay;
import com.module.swpay.http.inter.ISendAdvPayResult;
import com.module.swpay.http.inter.ISendAuthProduct;
import com.module.swpay.http.inter.ISendOrder;
import com.module.swpay.http.inter.ISendProductPrice;
import com.module.swpay.http.inter.ISendUserInfo;
import com.module.swpay.http.saxxml.ADVPayResultSax;
import com.module.swpay.http.saxxml.ADVPaySax;
import com.module.swpay.http.saxxml.AuthorizeSax;
import com.module.swpay.http.saxxml.OmsProductSax;
import com.module.swpay.http.saxxml.OrderSax;
import com.module.swpay.http.saxxml.UserInfoSax;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlSerializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SanxiIptvOpt{
    //URL配置地址
    private Properties properties;
    private final String filePath = "paydata/payUrl.properties";
    public static String priceURL = "";
    public static String payURL = "";

    private String priceJson = "";

    private String account;
    private String password;
    private String stbId;
    private String cpId;



    //获取用户信息
    private ISendUserInfo sendUserInfo;
    //定价接口
    private ISendProductPrice sendProductPrice;
    //鉴权接口
    private ISendAuthProduct sendAuthProduct;
    //下单接口
    private ISendAdvPay sendAdvPay;
    //轮询订购状态
    private ISendAdvPayResult sendAdvPayResult;
    //订购接口
    private ISendOrder sendOrder;

    private Context context;

    public static String userLocal = "06";



    private static class SanXiIptvOptImpl{
        public static final SanxiIptvOpt instance= new SanxiIptvOpt();
    }

    private SanxiIptvOpt(){
    }

    public static final SanxiIptvOpt getInstance(){
        return SanXiIptvOptImpl.instance;
    }

    public void init(Context context){
        this.context = context;
        properties = PayityUnity.loadProperties(context,filePath);
        priceURL = properties.getProperty("priceURL");
        payURL = properties.getProperty("payURL");
        if(priceURL==null || priceURL.length()==0){
            priceURL = "http://183.192.162.165:30060/oms-api/OmsProductManageServlet";
        }
        if(payURL==null || payURL.length()==0){
            payURL = "http://183.192.162.103:80/scspProxy";
//            http://183.192.162.103:80/scspProxy
        }


        account = properties.getProperty("account");
        password = properties.getProperty("password");
        stbId = properties.getProperty("stbId");
        cpId = properties.getProperty("cpId");
        priceJson = properties.getProperty("priceCode");

        sendUserInfo = new SendUserInfo();
        sendProductPrice = new SendProductPrice();
        sendAuthProduct = new SendAuthProduct();
        sendAdvPay = new SendAdvPay();
        sendAdvPayResult = new SendAdvPayResult();
        sendOrder = new SendOrder();


        System.out.println("account="+account+",password="+password+",stbId="+stbId);
        System.out.println("priceJson="+priceJson);
    }




    /**
     * 内容定价
     * @param platform
     * @param contentId
     * @param strategyCode
     * @return
     */
    public OmsProductBean checkProductPrice(String platform,String contentId,String copyRightId,String copyRightContentId,String strategyCode){
//        String requestBody = priceXml(platform,contentId,copyRightId,copyRightContentId,strategyCode);
//        return checkProductPrice(requestBody);

        return ((SendProductPrice) sendProductPrice).checkProductPrice(platform, contentId,
                copyRightId, copyRightContentId, strategyCode);
    }

    /**
     * 鉴权
     * @param userId
     * @param terminalId
     * @param copyRightId
     * @param contentId
     * @param token
     * @return
     */
    public AuthorizeBean productAuthorize(String userId,String terminalId,String copyRightId,String contentId,String token){
        return ((SendAuthProduct) sendAuthProduct).productAuthorize(userId, terminalId, copyRightId, contentId, token);

    }

    /**
     * 下单
     * @param seqId
     * @param userId
     * @param terminalId
     * @param copyRightId
     * @param productCode
     * @param amount
     * @param contentId
     * @param channelId
     * @param payType
     * @param accountIdentify
     * @param token
     * @return
     */
    public ADVPayBean productAdvPay(String seqId,String userId,String terminalId,String copyRightId,String appName,
                                    String productCode,String amount,String contentId,
                                    String channelId,String payType,String accountIdentify,String token){
        return ((SendAdvPay)sendAdvPay).productAdvPay(seqId, userId, terminalId, copyRightId,
                appName, productCode, amount, contentId, channelId, payType, accountIdentify, token);
    }

    /**
     * 支付状态查询
     * @param appName
     * @param terminalId
     * @param userId
     * @param externalSeqNum
     * @param param
     * @param token
     * @return
     */
    public ADVPayResultBean productAdvPayResult(String appName,String terminalId,String userId,
                                                String externalSeqNum,String param,String token){
        return ((SendAdvPayResult)sendAdvPayResult).productAdvPayResult(appName, terminalId, userId,
                externalSeqNum, param, token);

    }

    /**
     * 订购
     * @param seqId
     * @param userId
     * @param accountIdentify
     * @param terminalId
     * @param copyRightId
     * @param systemId
     * @param productCode
     * @param contentId
     * @param copyRightContentId
     * @param consumeLocal
     * @param consumeScene
     * @param consumeBehaviour
     * @param channelId
     * @param payType
     * @param subType
     * @param thirdTransID
     * @param tokenString
     * @return
     */
    public OrderBean productOrder(String seqId,String userId,String accountIdentify,String terminalId,String copyRightId,
                                  String systemId,String productCode,String contentId,String copyRightContentId,String consumeLocal,
                                  String consumeScene,String consumeBehaviour,String channelId,
                                  String payType,String subType,String thirdTransID,String tokenString){

        return ((SendOrder)sendOrder).productOrder(seqId, userId, accountIdentify,
                terminalId, copyRightId, systemId, productCode, contentId,
                copyRightContentId, consumeLocal, consumeScene, consumeBehaviour, channelId,
                payType, subType, thirdTransID, tokenString);
       // return productOrder(requestBody);
    }


    public UserInfoBean getUserInfo(){
        return ((SendUserInfo)sendUserInfo).getTestUserInfo(account,password,stbId);
//        return sendUserInfo.getUserInfo(context);
    }


    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStbId() {
        return stbId;
    }

    public void setStbId(String stbId) {
        this.stbId = stbId;
    }

    public String getCpId() {
        return cpId;
    }

    public void setCpId(String cpId) {
        this.cpId = cpId;
    }

    public String getPriceJson() {
        return priceJson;
    }

    public void setPriceJson(String priceJson) {
        this.priceJson = priceJson;
    }

    public String getLocalPrice(String productCode){
        try {
            JSONArray jsonArray = new JSONArray(priceJson);
            JSONObject jobj = null;
            for(int i=0;i<jsonArray.length();i++){
                jobj = jsonArray.getJSONObject(i);
                if(jobj.getString("productCode").equals(productCode)){
                    return jobj.getString("price");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "0";
    }
}
