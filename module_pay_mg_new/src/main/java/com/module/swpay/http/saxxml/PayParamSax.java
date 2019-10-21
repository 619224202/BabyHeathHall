package com.module.swpay.http.saxxml;

import com.migu.sdk.api.CommonInfo;
import com.migu.sdk.api.CommonPayInfo;
import com.module.swpay.http.bean.PayParamBean;
import com.module.swpay.http.inter.PaySaxXml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.InputStream;

public class PayParamSax extends PaySaxXml {
    private PayParamBean payParamBean;
    private CommonInfo commonInfo;
    private CommonPayInfo commonPayInfo;
    private String value;
    private int payInfoIndex;
    @Override
    public void startDocument() throws SAXException {
        payParamBean = new PayParamBean();
        super.startDocument();
    }

    @Override
    public void endDocument() throws SAXException {
        payParamBean.setCommonInfo(commonInfo);
        super.endDocument();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if(qName.equals("PayInfoSyncReq")){
            commonInfo = new CommonInfo();
            commonInfo.setSyn(true);
        }else if(qName.equals("PayInfo")){
            commonPayInfo = new CommonPayInfo();
            commonPayInfo.setOrderId(commonInfo.getOrderId());
//            commonPayInfo.setCustomPeriod("0");
//            commonPayInfo.setBillTimes("0");
//            commonPayInfo.setBillInterval("1");
        }
        super.startElement(uri, localName, qName, attributes);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(qName.equals("Ctype")){
            commonInfo.setcType(value);
        }else if(qName.equals("OrderId")){
            commonInfo.setOrderId(value);
        }else if(qName.equals("PayNum")){
            commonInfo.setPayNum(value);
        }else if(qName.equals("BizType")){
//            commonInfo.set
//            commonInfo.
        }else if(qName.equals("StbID")){
            commonInfo.setStbId(value);
        }else if(qName.equals("ChargePolicy")){
//            commonInfo
        }else if(qName.equals("CustomBizExpiryDate")){
            commonInfo.setCustomBizExpiryDate(value);
        }else if(qName.equals("OperCode")){
            commonInfo.setOperCode(value);
        }else if(qName.equals("Cpparam")){
            //
        }else if(qName.equals("ReserveParam")){

        }else{
            if(qName.equals("PayInfo")){
                if(payInfoIndex!=-1) {
                    payParamBean.addPayInfo(commonPayInfo, payInfoIndex);
                }else{
                    payParamBean.getPayInfos().add(commonPayInfo);
                }
                payInfoIndex = -1;
            }else if(qName.equals("Index")){
                if(value!=null && !value.isEmpty()) {
                    payInfoIndex = Integer.parseInt(value);
                }else{
                    payInfoIndex = -1;
                }
            }else if(qName.equals("IsMonthly")){
                commonPayInfo.setIsMonthly(value);
            }else if(qName.equals("CustomPeriod")){
//                if(value!=null && value.length()>0){
//                    commonPayInfo.setCustomPeriod("0");
//                }

            }else if(qName.equals("BillTimes")){
//                commonPayInfo.setBillTimes("0");
            }else if(qName.equals("BillInterval")){
//                commonPayInfo.setBillInterval("1");
            }else if(qName.equals("CampaignId")){
                commonPayInfo.setCampaignId(value);
            }else if(qName.equals("Fee")){
                commonPayInfo.setPrice(value);
            }else if(qName.equals("SpCode")){
                commonPayInfo.setSpCode(value);
            }else if(qName.equals("ServCode")){
                commonPayInfo.setServCode(value);
            }else if(qName.equals("ChannelCode")){
                commonPayInfo.setChannelId(value);
            }else if(qName.equals("CooperateCode")){
                commonPayInfo.setCpId(value);
            }else if(qName.equals("ProductCode")){
                commonPayInfo.setProductId(value);
            }else if(qName.equals("ContentCode")){
                commonPayInfo.setContentId(value);
            }else if(qName.equals("PlatForm_Code")){
                payParamBean.setPlatForm_Code(value);
//                commonPayInfo.set
            }else if(qName.equals("Cpparam")){
                payParamBean.setCpparam(value);
//                commonPayInfo.set
            }else if(qName.equals("ReserveParam")){
                payParamBean.setReserveParam(value);
            }
        }
        super.endElement(uri, localName, qName);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        value = new String(ch,start,length);
        super.characters(ch, start, length);
    }

    public PayParamBean getPayParamBean(){
        return payParamBean;
    }
}
