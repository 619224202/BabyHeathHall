package com.module.swpay.http.saxxml;

import com.module.swpay.http.SanxiIptvOpt;
import com.module.swpay.http.bean.UserInfoBean;
import com.module.swpay.http.inter.PaySaxXml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class UserInfoSax extends PaySaxXml {
    private UserInfoBean userInfoBean;
    private UserInfoBean.BussinessInfoBean bussinessInfoBean;
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
        if (qName.equals("loginAuth")){
            userInfoBean = new UserInfoBean();
            userInfoBean.setResult(attributes.getValue("result"));
            userInfoBean.setResultDesc(attributes.getValue("resultDesc"));
            userInfoBean.setMessageAddress(attributes.getValue("messageAddress"));
            userInfoBean.setMessageJID(attributes.getValue("messageJID"));
            userInfoBean.setMessagePassword(attributes.getValue("messagePassword"));
            userInfoBean.setToken(attributes.getValue("token"));
            userInfoBean.setUserId(attributes.getValue("userId"));
        }else if(qName.equals("businessInfo")){
            bussinessInfoBean = userInfoBean.createBusinessInfoBean();
            bussinessInfoBean.setAccountIdentity(attributes.getValue("accountIdentity"));
            bussinessInfoBean.setAuthCode(attributes.getValue("authCode"));
            bussinessInfoBean.setUserLocal(attributes.getValue("userLocal"));
            SanxiIptvOpt.userLocal = bussinessInfoBean.getUserLocal();
//            SanxiIptvOpt.userLocal = "16";
            bussinessInfoBean.setUserGroup(attributes.getValue("userGroup"));
            bussinessInfoBean.setRightChange(attributes.getValue("rightChange"));
            bussinessInfoBean.setECCoporationCode(attributes.getValue("ECCoporationCode"));
            bussinessInfoBean.setECCode(attributes.getValue("ECCode"));
            bussinessInfoBean.setCpCode(attributes.getValue("cpCode"));
//            bussinessInfoBean.setCopyrightId(attributes.getValue("copyrighteId"));
            bussinessInfoBean.setCopyrightId(SanxiIptvOpt.getInstance().getCpId());
            bussinessInfoBean.setCity(attributes.getValue("city"));
            bussinessInfoBean.setChangeTerminalModel(attributes.getValue("changeTerminalModel"));
            userInfoBean.setBussinessInfoBean(bussinessInfoBean);
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

    public UserInfoBean getUserInfoBean(){
        return userInfoBean;
    }
}
