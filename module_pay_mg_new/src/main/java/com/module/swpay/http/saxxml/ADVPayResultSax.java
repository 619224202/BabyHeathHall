package com.module.swpay.http.saxxml;

import com.module.swpay.http.bean.ADVPayResultBean;
import com.module.swpay.http.inter.PaySaxXml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ADVPayResultSax extends PaySaxXml {
    private ADVPayResultBean advPayResultBean;
    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if(qName.equals("body")){
            advPayResultBean = new ADVPayResultBean();
        }else if(qName.equals("advPayResult")){
            advPayResultBean.setResult(attributes.getValue("result"));
            advPayResultBean.setResultDesc(attributes.getValue("resultDesc"));
            advPayResultBean.setExternalSeqNum(attributes.getValue("externalSeqNum"));
            advPayResultBean.setPayResult(attributes.getValue("payResult"));
            advPayResultBean.setSuccessPayMent(attributes.getValue("successPayMent"));
        }
        super.startElement(uri, localName, qName, attributes);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
    }

    public ADVPayResultBean getAdvPayResultBean() {
        return advPayResultBean;
    }

    public void setAdvPayResultBean(ADVPayResultBean advPayResultBean) {
        this.advPayResultBean = advPayResultBean;
    }
}
