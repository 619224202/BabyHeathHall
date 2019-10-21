package com.module.swpay.http;

import android.content.Context;
import android.util.Xml;

import com.module.swpay.PayityUnity;
import com.module.swpay.http.bean.UserInfoBean;
import com.module.swpay.http.inter.ISendUserInfo;
import com.module.swpay.http.saxxml.UserInfoSax;
import com.shcmcc.tools.GetSysInfo;
import com.shcmcc.tools.UserAccountInfo;

import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlSerializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by wanglu on 2019/10/8.
 *
 */
public class SendUserInfo implements ISendUserInfo {
    private boolean isInParse = false;
    private UserInfoBean userInfoBean = null;
    /**
     * 从盒子中获取用户信息
     * @param context
     * @return
     */
    @Override
    public UserInfoBean getUserInfo(Context context) {
        userInfoBean = new UserInfoBean();
        UserInfoBean.BussinessInfoBean bussinessInfoBean = userInfoBean.createBusinessInfoBean();
        GetSysInfo info = GetSysInfo.getInstance("10086","",context);
        //stbId存于SanxiIptvOpt中
        SanxiIptvOpt.getInstance().setStbId(info.getSnNum());
        userInfoBean.setUserId(info.getEpgUserId());
        userInfoBean.setToken(info.getEpgToken());
        bussinessInfoBean.setECCode(info.getEpgEccode());
        bussinessInfoBean.setCopyrightId(info.getEpgCopyrightId());
        bussinessInfoBean.setECCoporationCode(info.getEpgEccoporationCode());
        bussinessInfoBean.setCity(info.getEpgCityCode());
        SanxiIptvOpt.userLocal = info.getEpgProvince();
        bussinessInfoBean.setCpCode(info.getEpgCpCode());
        bussinessInfoBean.setUserGroup(info.getEpgUserGroup());
        userInfoBean.setBussinessInfoBean(bussinessInfoBean);

        UserAccountInfo userAccountInfo = UserAccountInfo.getInstance(context);
        SanxiIptvOpt.getInstance().setAccount(userAccountInfo.getLoginAcount());
        SanxiIptvOpt.getInstance().setPassword(userAccountInfo.getLoginPassword());
        return userInfoBean;
    }

    public UserInfoBean getTestUserInfo(final String userId,final String passWord,final String stbId){
        isInParse = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                String userXml = userInfoXml(userId, passWord, stbId);
                InputStream is = PayityUnity.doURL(SanxiIptvOpt.payURL,userXml);
                SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();

                try {
                    SAXParser saxParser = saxParserFactory.newSAXParser();
                    UserInfoSax userInfoSax = new UserInfoSax();
                    saxParser.parse(is,userInfoSax);
                    userInfoBean = userInfoSax.getUserInfoBean();
                    System.out.println("userInfo>>>"+userInfoBean.toString());
                    isInParse = true;
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    isInParse = true;
                    if(is!=null){
                        try {
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    is = null;
                }
            }
        }).start();
        while (!isInParse){
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return userInfoBean;
    }

    private String userInfoXml(String account,String password,String stbId) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            XmlSerializer serializer = Xml.newSerializer();
            serializer.setOutput(outputStream, "UTF-8");
            serializer.startDocument("UTF-8", true);
            //messsage
            serializer.startTag(null, "message");
            serializer.attribute(null,"module","SCSP");
            serializer.attribute(null,"version","1.1");
            //header
            serializer.startTag(null,"header");
            serializer.attribute(null,"action","REQUEST");
            serializer.attribute(null,"command","LOGINAUTH");

            serializer.endTag(null,"header");
            //body
            serializer.startTag(null,"body");
            //loginAuth
            serializer.startTag(null,"loginAuth");
            serializer.attribute(null,"loginType","5");
            serializer.attribute(null,"account",account);
            serializer.attribute(null,"password",password);
            serializer.attribute(null,"stbId",stbId);

            //end loginAuth
            serializer.endTag(null,"loginAuth");

            //end body
            serializer.endTag(null,"body");
            //end Message
            serializer.endTag(null,"message");
            serializer.endDocument();
            outputStream.flush();
            outputStream.close();
            return outputStream.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
