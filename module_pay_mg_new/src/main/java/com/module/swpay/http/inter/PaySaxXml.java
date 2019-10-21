package com.module.swpay.http.inter;

import com.module.swpay.http.bean.ISxPayBean;

import org.json.JSONObject;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public abstract class PaySaxXml extends DefaultHandler {

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
    }

    @Override
    public void endDocument() throws SAXException {
        System.out.println();
        super.endDocument();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        System.out.print("<"+qName+"");
        for (int i = 0; i < attributes.getLength(); i++) {
            String name = attributes.getQName(i);
            String values = attributes.getValue(name);
            System.out.print(" "+name+"=“"+values+"”");
        }
        System.out.print(">");
        super.startElement(uri, localName, qName, attributes);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        System.out.print("</"+qName+">");
        super.endElement(uri, localName, qName);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String a = new String(ch, start, length);
        System.out.print(a);
        super.characters(ch, start, length);
    }
}
