package com.module.swpay.http.bean;

import com.migu.sdk.api.CommonInfo;
import com.migu.sdk.api.CommonPayInfo;

import java.util.ArrayList;

public class PayParamBean {
    private CommonInfo commonInfo;
    private ArrayList<CommonPayInfo> payInfos;
    private String Cpparam;
    private String ReserveParam;
    private String PlatForm_Code;

    public PayParamBean(){
        payInfos = new ArrayList<CommonPayInfo>();
    }

    public CommonInfo getCommonInfo() {
        return commonInfo;
    }

    public void setCommonInfo(CommonInfo commonInfo) {
        this.commonInfo = commonInfo;
    }

    public ArrayList<CommonPayInfo> getPayInfos() {
        return payInfos;
    }

    public CommonPayInfo[] getPayInfosArray(){
        int lenth = payInfos.size();
        CommonPayInfo[] payinfoArray = new CommonPayInfo[lenth];
        for(int i=0;i<payInfos.size();i++){
            payinfoArray[i] = payInfos.get(i);
            System.out.println("payInfo="+payinfoArray[i].isValid());
        }
        return payinfoArray;
    }

    public void setPayInfos(ArrayList<CommonPayInfo> payInfos) {
        this.payInfos = payInfos;
    }

    public void addPayInfo(CommonPayInfo payInfo,int index){
        if(payInfo==null){
            return;
        }
        payInfos.add(index,payInfo);
//        this.payInfos.add(payInfo);
    }

    public String getCpparam() {
        return Cpparam;
    }

    public void setCpparam(String cpparam) {
        Cpparam = cpparam;
    }

    public String getReserveParam() {
        return ReserveParam;
    }

    public void setReserveParam(String reserveParam) {
        ReserveParam = reserveParam;
    }

    public String getPlatForm_Code() {
        return PlatForm_Code;
    }

    public void setPlatForm_Code(String platForm_Code) {
        PlatForm_Code = platForm_Code;
    }
}
