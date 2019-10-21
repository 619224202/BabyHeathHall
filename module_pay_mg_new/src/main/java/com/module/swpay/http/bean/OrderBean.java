package com.module.swpay.http.bean;

public class OrderBean {
    private String result;
    private String resultDesc;
    private String authorizationNum;
    private String orderSeq;

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

    public String getAuthorizationNum() {
        return authorizationNum;
    }

    public void setAuthorizationNum(String authorizationNum) {
        this.authorizationNum = authorizationNum;
    }

    public String getOrderSeq() {
        return orderSeq;
    }

    public void setOrderSeq(String orderSeq) {
        this.orderSeq = orderSeq;
    }
}
