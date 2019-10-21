package com.module.swpay.http;

import android.util.Xml;

import com.module.swpay.PayityUnity;
import com.module.swpay.http.bean.ADVPayBean;
import com.module.swpay.http.inter.ISendAdvPay;
import com.module.swpay.http.saxxml.ADVPaySax;

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
 */
public class SendAdvPay implements ISendAdvPay {
    private boolean isInParse;
    private ADVPayBean advPayBean;
    @Override
    public ADVPayBean advPay(final String xml) {
        isInParse = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                InputStream is = PayityUnity.doURL(SanxiIptvOpt.payURL,xml);
                SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
                try {
                    SAXParser saxParser = saxParserFactory.newSAXParser();
                    ADVPaySax advPaySax = new ADVPaySax();
                    saxParser.parse(is,advPaySax);
                    advPayBean = advPaySax.getAdvPayBean();
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
        return advPayBean;
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
        String requestBody = advpayXml(seqId, userId, terminalId, copyRightId,appName, productCode,
                amount, contentId, channelId, payType, accountIdentify, token);
        return advPay(requestBody);
    }

    /**
     * 支付下单接口
     * <?xml version="1.0" encoding="UTF-8"?>
     * <message module="SCSP" version="1.1">
     * <header action="REQUEST" command="ADVPAY"/>
     * <body>
     * <advPay seqId="123" userId="123" accountIdentify="123"
     * terminalId="123"  appName=”” productCode="3344"   contentId="3344"
     * copyRightContentId=”” consumeLocal="02" consumeScene="01" consumeBehaviour="02"
     * channelId="" path="" payType="1"   subType=””saleTransID=""  token="" />
     * </body>
     * </message>
     * @param seqId
     * @param userId
     * @param productCode
     * @param amount
     * @param contentId
     * @param channelId
     * @param payType
     * @param token
     * @return
     */
    public String advpayXml(String seqId,String userId,String terminalId,String copyRightId,String appName,
                            String productCode,String amount,String contentId,
                            String channelId,String payType,String accountIdentify,String token) {
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
            serializer.attribute(null,"command","ADVPAY");
            serializer.endTag(null,"header");
            //body
            serializer.startTag(null,"body");
            //advPay
            serializer.startTag(null,"advPay");

            serializer.attribute(null,"seqId",seqId);
            serializer.attribute(null,"userId",userId);
            serializer.attribute(null,"terminalId",terminalId);
            serializer.attribute(null,"copyRightId",copyRightId);
            serializer.attribute(null,"appName",appName);
            serializer.attribute(null,"productCode",productCode);
            serializer.attribute(null,"amount",amount);
            serializer.attribute(null,"contentId",contentId);
            serializer.attribute(null,"copyRightContentId","");
            serializer.attribute(null,"consumeLocal",SanxiIptvOpt.userLocal);
            serializer.attribute(null,"consumeScene","01");
            serializer.attribute(null,"consumeBehaviour","02");
            serializer.attribute(null,"channelId",channelId);
            serializer.attribute(null,"payType",payType);   //一般传入16
            serializer.attribute(null,"accountIdentify",accountIdentify);
            serializer.attribute(null,"token",token);
            //end advPay
            serializer.endTag(null,"advPay");

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
