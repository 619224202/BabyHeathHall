package com.module.swpay.http;

import android.util.Xml;

import com.module.swpay.PayityUnity;
import com.module.swpay.http.bean.ADVPayResultBean;
import com.module.swpay.http.inter.ISendAdvPayResult;
import com.module.swpay.http.saxxml.ADVPayResultSax;

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
public class SendAdvPayResult implements ISendAdvPayResult {
    private boolean isInParse = false;
    private ADVPayResultBean advPayResultBean;
    @Override
    public ADVPayResultBean doAdvPayResult(final String xml) {
        isInParse = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                advPayResultBean = null;
                InputStream is = PayityUnity.doURL(SanxiIptvOpt.payURL,xml);
                SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
                try {
                    SAXParser saxParser = saxParserFactory.newSAXParser();
                    ADVPayResultSax advPayResultSax = new ADVPayResultSax();
                    saxParser.parse(is,advPayResultSax);
                    advPayResultBean = advPayResultSax.getAdvPayResultBean();
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
        return advPayResultBean;
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
        String requestBody = advpayresultXml(appName, terminalId, userId,
                externalSeqNum, param, token);
        return  doAdvPayResult(requestBody);
    }

    /**
     * 支付状态查询接口xml参数
     * <?xml version="1.0" encoding="UTF-8"?>
     * <message module="SCSP" version="1.1">
     * <header action="REQUEST" command="ADVPAYRESULT"/>
     * <body>
     * <advPayResult terminalId="1" appName="1"  userId="1" externalSeqNum="1" payNum="1"  param="{xxxx:a,xxxx:b}" token="1" />
     * </body>
     * </message>
     * @param appName
     * @param terminalId
     * @param userId
     * @param externalSeqNum
     * @param param
     * @param token
     * @return
     */
    public String advpayresultXml(String appName,String terminalId,String userId,
                                  String externalSeqNum,String param,String token) {
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
            serializer.attribute(null,"command","ADVPAYRESULT");
            serializer.endTag(null,"header");
            //body
            serializer.startTag(null,"body");
            //advpayressult
            serializer.startTag(null,"advPayResult");

            serializer.attribute(null,"appName",appName);
            serializer.attribute(null,"terminalId",terminalId);
            serializer.attribute(null,"userId",userId);
            serializer.attribute(null,"externalSeqNum",externalSeqNum);
            serializer.attribute(null,"param",param);
            serializer.attribute(null,"token",token);
            //end advpayressult
            serializer.endTag(null,"advPayResult");

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
