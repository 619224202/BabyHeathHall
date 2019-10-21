package com.module.swpay.http.bean;

public class ADVPayBean {
    private String result;
    private String resultDesc;
    private String externalSeqNum;
    private String qrCode;
    private String qrCodeImgUrl;
    private String qrCodeImg;
    private String payParam;

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

    public String getExternalSeqNum() {
        return externalSeqNum;
    }

    public void setExternalSeqNum(String externalSeqNum) {
        this.externalSeqNum = externalSeqNum;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getQrCodeImgUrl() {
        return qrCodeImgUrl;
    }

    public void setQrCodeImgUrl(String qrCodeImgUrl) {
        this.qrCodeImgUrl = qrCodeImgUrl;
    }

    public String getQrCodeImg() {
        return qrCodeImg;
    }

    public void setQrCodeImg(String qrCodeImg) {
        this.qrCodeImg = qrCodeImg;
    }

    public String getPayParam() {
        return payParam;
    }

    public void setPayParam(String payParam) {
        this.payParam = payParam;
    }
}
