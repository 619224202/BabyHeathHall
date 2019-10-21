package com.module.swpay.http;

import android.util.Xml;

import com.module.swpay.PayityUnity;
import com.module.swpay.http.bean.AuthorizeBean;
import com.module.swpay.http.inter.ISendAuthProduct;
import com.module.swpay.http.saxxml.AuthorizeSax;

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
public class SendAuthProduct implements ISendAuthProduct {
    private boolean isInParse = false;
    private AuthorizeBean authorizeBean;
    @Override
    public AuthorizeBean productAuthorize(final String xml) {
        isInParse = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                InputStream is = PayityUnity.doURL(SanxiIptvOpt.payURL,xml);
                System.out.println("is="+is);
                SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
//                AuthorizeBean authorizeBean = null;
                try {
                    SAXParser saxParser = saxParserFactory.newSAXParser();
                    AuthorizeSax authorizeSax = new AuthorizeSax();
                    saxParser.parse(is,authorizeSax);
                    authorizeBean = authorizeSax.getAuthorizeBean();
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
        while(!isInParse){
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return authorizeBean;
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
        String requestBody = authoXml(userId, terminalId, copyRightId, contentId, token);
        return productAuthorize(requestBody);
    }

    /*
    * <?xml version="1.0" encoding="UTF-8"?>
        <message module="SCSP" version="1.1">
        <header action="REQUEST" command="AUTHORIZE"/>
        <body>
        <authorize userId="123" terminalId="123" contentId="" subContentId="" systemId="1"
        consumeLocal="02" consumeScene="01" consumeBehaviour="02"  path=""  preview="0" channelId="" productId="" token=""/>
        </body>
        </message>

    *
    *
    * 鉴权询价接口调试
    *
    * */

    public String authoXml(String userId,String terminalId,String copyRightId,String contentId,String token) {
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
            serializer.attribute(null,"command","AUTHORIZE");
            serializer.endTag(null,"header");
            //body
            serializer.startTag(null,"body");
            //authorize
            serializer.startTag(null,"authorize");
            serializer.attribute(null,"userId",userId);
            serializer.attribute(null,"terminalId",terminalId);
            serializer.attribute(null,"contentId",contentId);
            serializer.attribute(null,"copyRightId",copyRightId);
            serializer.attribute(null,"subContentId","");
            serializer.attribute(null,"systemId","0");
            serializer.attribute(null,"consumeLocal",SanxiIptvOpt.userLocal);
            serializer.attribute(null,"consumeScene","01");
            serializer.attribute(null,"consumeBehaviour","02");
            serializer.attribute(null,"path","");
            serializer.attribute(null,"channelId","");
            serializer.attribute(null,"productId","");
            serializer.attribute(null,"token",token);

            //end authorize
            serializer.endTag(null,"authorize");

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
