package com.module.swpay.http.saxxml;

import com.module.swpay.http.bean.ISxPayBean;
import com.module.swpay.http.bean.OmsProductBean;
import com.module.swpay.http.inter.PaySaxXml;

import org.json.JSONObject;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class OmsProductSax extends PaySaxXml {
    private String nodeName;
    private String value;
    private OmsProductBean omsProductBean;
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if(qName.equals("body")){
            omsProductBean = new OmsProductBean();
//            omsProductBean = (OmsProductBean) sxPayBean;
        }
       if(qName.equals("result")){

       }
        super.startElement(uri,localName,qName,attributes);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(qName.equals("result")){
            omsProductBean.setResult(value);
        }else if(qName.equals("resultDesc")){
            omsProductBean.setResultDesc(value);
        }else if(qName.equals("failContentId")){
            omsProductBean.setFailContentId(value);
        }
        super.endElement(uri, localName, qName);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        value = new String(ch,start,length);
        super.characters(ch, start, length);
    }

    public OmsProductBean getOmsProductBean() {
        return omsProductBean;
    }

    public void setOmsProductBean(OmsProductBean omsProductBean) {
        this.omsProductBean = omsProductBean;
    }
}
