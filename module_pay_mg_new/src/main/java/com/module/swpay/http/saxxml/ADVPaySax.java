package com.module.swpay.http.saxxml;

import com.module.swpay.http.bean.ADVPayBean;
import com.module.swpay.http.inter.PaySaxXml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class ADVPaySax extends PaySaxXml {
    private ADVPayBean advPayBean;
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
            advPayBean = new ADVPayBean();
        }else if(qName.equals("advPay")){
            advPayBean.setResultDesc(attributes.getValue("resultDesc"));
            advPayBean.setResult(attributes.getValue("result"));
            advPayBean.setExternalSeqNum(attributes.getValue("externalSeqNum"));
            advPayBean.setPayParam(attributes.getValue("payParam"));
            advPayBean.setQrCode(attributes.getValue("qrCode"));
            advPayBean.setQrCodeImg(attributes.getValue("qrCodeImg"));
            advPayBean.setQrCodeImgUrl(attributes.getValue("qrCodeImgUrl"));
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

    public ADVPayBean getAdvPayBean() {
        return advPayBean;
    }

    public void setAdvPayBean(ADVPayBean advPayBean) {
        this.advPayBean = advPayBean;
    }
}
