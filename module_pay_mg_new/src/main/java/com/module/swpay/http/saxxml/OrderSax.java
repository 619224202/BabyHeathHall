package com.module.swpay.http.saxxml;

import com.module.swpay.http.bean.OrderBean;
import com.module.swpay.http.inter.PaySaxXml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class OrderSax extends PaySaxXml {
    private OrderBean orderBean;
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
            orderBean = new OrderBean();
        }else if(qName.equals("order")){
            orderBean.setResult(attributes.getValue("result"));
            orderBean.setResultDesc(attributes.getValue("resultDesc"));
            orderBean.setAuthorizationNum(attributes.getValue("authorizationNum"));
            orderBean.setOrderSeq(attributes.getValue("orderSeq"));

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

    public OrderBean getOrderBean() {
        return orderBean;
    }

    public void setOrderBean(OrderBean orderBean) {
        this.orderBean = orderBean;
    }
}
