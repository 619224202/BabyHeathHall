package com.module.swpay.http.saxxml;

import com.module.swpay.http.bean.AuthorizeBean;
import com.module.swpay.http.inter.PaySaxXml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public class AuthorizeSax extends PaySaxXml {
    //鉴权返回对象
    private AuthorizeBean authorizeBean;
    //当前层的product
    private AuthorizeBean.ProductBean productBean;
    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
    }

    @Override
    public void endDocument() throws SAXException {
        AuthorizeBean.ProductBean buf = productBean;
        productBean = authorizeBean.createProductBean();
        productBean.setProductCode(buf.getProductCode());
        productBean.setBpPrice(buf.getBpPrice());
        productBean.setCombineProduct(buf.getCombineProduct());
        productBean.setCycle(buf.getCycle());

        productBean.setPaymentType(buf.getPaymentType());
//            productBean.setPaymentType("16");
        productBean.setPrice(buf.getPrice());
        productBean.setProductInfo(buf.getProductInfo());
        productBean.setProductPrice(buf.getProductPrice());
        productBean.setSpId(buf.getSpId());
        productBean.setUnit(buf.getUnit());
        authorizeBean.addProductBean(productBean);
        super.endDocument();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        if(qName.equals("authorize")){
            authorizeBean = new AuthorizeBean();
            authorizeBean.setResult(attributes.getValue("result"));
            authorizeBean.setResultDesc(attributes.getType("resultDesc"));
            authorizeBean.setAccountIdentify(attributes.getValue("accountIdentify"));
            authorizeBean.setAccountIdentifyPhone(attributes.getValue("accountIdentifyPhone",""));
            authorizeBean.setAuthorizationNum(attributes.getValue("authorizationNum"));
            authorizeBean.setProductCode(attributes.getValue("productCode",""));
            authorizeBean.setNoprodcutInfo(attributes.getValue("noproductInfo",""));
        }else if(qName.equals("productToOrderList")){
            authorizeBean.setProductToOrderList(new ArrayList<AuthorizeBean.ProductBean>());
        }else if(qName.equals("Product")){
            productBean = authorizeBean.createProductBean();
            productBean.setProductCode(attributes.getValue("productCode"));
            productBean.setBpPrice(attributes.getValue("bpPrice"));
            productBean.setCombineProduct(attributes.getValue("combineProduct"));
            productBean.setCycle(attributes.getValue("cycle"));
            productBean.setDisplayPrority(attributes.getValue("displayPrority"));
            productBean.setIsSalesStrategy(attributes.getValue("isSalesStrategy"));
            productBean.setOrderContentId(attributes.getValue("orderContentId"));
            productBean.setPaymentType(attributes.getValue("paymentType"));
//            productBean.setPaymentType("16");
            productBean.setPrice(attributes.getValue("price"));
            productBean.setProductInfo(attributes.getValue("productInfo"));
            productBean.setProductPrice(attributes.getValue("productPrice"));
            productBean.setSpId(attributes.getValue("spId"));
            productBean.setUnit(attributes.getValue("unit"));
            productBean.setValidendtime(attributes.getValue("validendtime"));
            productBean.setValidstarttime(attributes.getValue("validstarttime"));
            authorizeBean.addProductBean(productBean);
        }else if(qName.equals("salesStrategys")){
            productBean.setSalesStrategyList(new ArrayList<AuthorizeBean.SalesStrategysBean>());
        }else if(qName.equals("salesStrategy")){
            AuthorizeBean.SalesStrategysBean salesStrategysBean = authorizeBean.createSalesStrategysBean();
            salesStrategysBean.setDisplayPrority(attributes.getValue("displayPrority"));
            salesStrategysBean.setFirstPayAmount(attributes.getValue("firstPayAmount"));
            salesStrategysBean.setFirstPayCircle(attributes.getValue("firstPayCircle"));
            salesStrategysBean.setFirstThridPayAmount(attributes.getValue("firstThridPayAmount"));
            salesStrategysBean.setSaleTransID(attributes.getValue("saleTransID"));
            salesStrategysBean.setStrategyDesc(attributes.getValue("strategyDesc"));
            salesStrategysBean.setTotalCircle(attributes.getValue("totalCircle"));
            productBean.addSalesStrategy(salesStrategysBean);
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

    public AuthorizeBean getAuthorizeBean() {
        return authorizeBean;
    }

    public void setAuthorizeBean(AuthorizeBean authorizeBean) {
        this.authorizeBean = authorizeBean;
    }
}
