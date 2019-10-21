package com.module.swpay.http;

import android.util.Xml;

import com.module.swpay.PayityUnity;
import com.module.swpay.http.bean.OrderBean;
import com.module.swpay.http.inter.ISendOrder;
import com.module.swpay.http.saxxml.OrderSax;

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
public class SendOrder implements ISendOrder {
    private boolean isInParse = false;
    private OrderBean orderBean;
    @Override
    public OrderBean orderProduct(final String xml) {
        isInParse = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                InputStream is = PayityUnity.doURL(SanxiIptvOpt.payURL,xml);
                SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
                try {
                    SAXParser saxParser = saxParserFactory.newSAXParser();
                    OrderSax orderSax = new OrderSax();
                    saxParser.parse(is,orderSax);
                    orderBean = orderSax.getOrderBean();
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
        while(!isInParse){
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return orderBean;
    }

    public OrderBean productOrder(String seqId,String userId,String accountIdentify,String terminalId,String copyRightId,
                                  String systemId,String productCode,String contentId,String copyRightContentId,String consumeLocal,
                                  String consumeScene,String consumeBehaviour,String channelId,
                                  String payType,String subType,String thirdTransID,String tokenString){
        String requestBody = orderXml(seqId, userId, accountIdentify, terminalId, copyRightId,
                systemId, productCode, contentId, copyRightContentId, consumeLocal,
                consumeScene, consumeBehaviour, channelId, payType, subType, thirdTransID, tokenString);
        return orderProduct(requestBody);
    }

    private String orderXml(String seqId,String userId,String accountIdentify,String terminalId,String copyRightId,
                           String systemId,String productCode,String contentId,String copyRightContentId,String consumeLocal,
                           String consumeScene,String consumeBehaviour,String channelId,
                           String payType,String subType,String thirdTransID,String token) {
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
            serializer.attribute(null,"command","ORDER");
            serializer.endTag(null,"header");
            //body
            serializer.startTag(null,"body");
            //advpayressult
            serializer.startTag(null,"order");

            serializer.attribute(null,"seqId",seqId);
            serializer.attribute(null,"userId",userId);
            serializer.attribute(null,"accountIdentify",accountIdentify);
            serializer.attribute(null,"terminalId",terminalId);
            serializer.attribute(null,"copyRightId",copyRightId);
            serializer.attribute(null,"systemId","0");
            serializer.attribute(null,"productCode",productCode);
            serializer.attribute(null,"contentId",contentId);
            serializer.attribute(null,"copyRightContentId",copyRightContentId);
            serializer.attribute(null,"consumeLocal",SanxiIptvOpt.userLocal);
            serializer.attribute(null,"consumeScene","01");
            serializer.attribute(null,"consumeBehaviour","02");
            serializer.attribute(null,"channelId",channelId);
            serializer.attribute(null,"payType",payType);
            serializer.attribute(null,"subType",subType);
            serializer.attribute(null,"orderTime","1");
            serializer.attribute(null,"thirdTransID",thirdTransID);
            serializer.attribute(null,"token",token);
            //end advpayressult
            serializer.endTag(null,"order");
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
