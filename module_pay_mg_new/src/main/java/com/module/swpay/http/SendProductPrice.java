package com.module.swpay.http;

import com.module.swpay.PayityUnity;
import com.module.swpay.http.SanxiIptvOpt;
import com.module.swpay.http.bean.OmsProductBean;
import com.module.swpay.http.inter.ISendProductPrice;
import com.module.swpay.http.saxxml.OmsProductSax;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by wanglu on 2019/10/8.
 */
public class SendProductPrice implements ISendProductPrice {
    private boolean isInParse = false;
    private OmsProductBean omsProductBean;

    @Override
    public OmsProductBean checkProductPrice(final String xml) {
        isInParse = false;

        new Thread(new Runnable() {
            @Override
            public void run() {
                InputStream is = PayityUnity.doURL(SanxiIptvOpt.priceURL, xml);
                SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
                try {
                    SAXParser saxParser = saxParserFactory.newSAXParser();
                    OmsProductSax productSax = new OmsProductSax();
                    saxParser.parse(is, productSax);
                    omsProductBean = productSax.getOmsProductBean();
                    isInParse = true;
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }  finally {
                    isInParse = true;
                    if(is!=null){
                        try {
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    is = null;
//                        context.getApplicationContext().notify();
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
        return omsProductBean;
    }

    /**
     * 内容定价
     * @param platform
     * @param contentId
     * @param strategyCode
     * @return
     */
    public OmsProductBean checkProductPrice(String platform,String contentId,String copyRightId,String copyRightContentId,String strategyCode){
        String requestBody = priceXml(platform,contentId,copyRightId,copyRightContentId,strategyCode);
        return checkProductPrice(requestBody);
    }

    /**
     *
     * <?xml version="1.0" encoding="UTF-8"?>
     <message module="SCSP" version="1.1">
     <header action="REQUEST" command="PRO_TO_CONTNET"/>
     <body>
     <proToContent operation="2">
     <platform>wasu</platform>
     <contentId><![CDATA[1234;12345;43343;66666;222]]></contentId>
     <strategyCode><![CDATA[华数基本包]]></strategyCode >
     </proToContent>
     </body>
     </message>
     *
     *
     * 内容定价接口传入数据
     *
     *
     * */
    public String priceXml(String platform,String contentId,String copyRightId,String copyRightContentId,String strategyCode) {
        String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
                "<message module=\"SCSP\" version=\"1.1\">" +
                "<header action=\"REQUEST\" command=\"PRO_TO_CONTNET\"/>" +
                "<body>" +
                "<proToContent operation=\"2\">" +
                "<platform>"+platform+"</platform>" +
                "<contentId><![CDATA["+contentId+"]]></contentId>" +
                "<strategyCode><![CDATA["+strategyCode+"]]></strategyCode>" +
                "</proToContent>" +
                "</body>" +
                "</message>";
        return xml;

    }
}
