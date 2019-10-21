package com.module.swpay.bean;

/**
 * Created by wanglu on 2019/1/16.
 */
public class PayBean {
    //用来标识扣费的id
    private int id;
    private String payId;
    private String payName;
    private double payPrice;
    private boolean isPayCycle;
    private String tradeNo;

    public PayBean(){

    }

    public PayBean(int id, String payId, String payName, double payPrice){
        this.id = id;
        this.payId = payId;
        this.payName = payName;
        this.payPrice = payPrice;
    }

    public PayBean(int id, String payId, String payName, double payPrice, boolean isPayCycle){
        this.id = id;
        this.payId = payId;
        this.payName = payName;
        this.payPrice = payPrice;
        this.isPayCycle = isPayCycle;
    }

    public String getPayId() {
        return payId;
    }

    public void setPayId(String payId) {
        this.payId = payId;
    }

    public String getPayName() {
        return payName;
    }

    public void setPayName(String payName) {
        this.payName = payName;
    }

    public double getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(double payPrice) {
        this.payPrice = payPrice;
    }

    public boolean isPayCycle() {
        return isPayCycle;
    }

    public void setPayCycle(boolean payCycle) {
        isPayCycle = payCycle;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
