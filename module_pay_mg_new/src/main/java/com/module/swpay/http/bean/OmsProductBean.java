package com.module.swpay.http.bean;

public class OmsProductBean implements  ISxPayBean{
    private String result;
    private String resultDesc;
    private String failContentId;

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

    public String getFailContentId() {
        return failContentId;
    }

    public void setFailContentId(String failContentId) {
        this.failContentId = failContentId;
    }
}
