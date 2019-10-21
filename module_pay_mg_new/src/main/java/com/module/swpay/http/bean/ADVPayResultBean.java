package com.module.swpay.http.bean;

public class ADVPayResultBean {
    private String result;
    private String resultDesc;
    private String payResult;
    private String successPayMent;//支付成功的支付方式 1.支付宝 2:微信 4. 和包 8.手机话费老五套支付 16.阳光计划新六套支付
    private String externalSeqNum;  //支付下单接口返回的外部序列号

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResultDesc() {
        return resultDesc;
    }

    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
    }

    public String getPayResult() {
        return payResult;
    }

    public void setPayResult(String payResult) {
        this.payResult = payResult;
    }

    public String getSuccessPayMent() {
        return successPayMent;
    }

    public void setSuccessPayMent(String successPayMent) {
        this.successPayMent = successPayMent;
    }

    public String getExternalSeqNum() {
        return externalSeqNum;
    }

    public void setExternalSeqNum(String externalSeqNum) {
        this.externalSeqNum = externalSeqNum;
    }
}
