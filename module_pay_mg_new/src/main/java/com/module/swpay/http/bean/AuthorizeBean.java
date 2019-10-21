package com.module.swpay.http.bean;

import java.util.ArrayList;

public class AuthorizeBean implements ISxPayBean {
    private String result = "-1";      //0成功，1需要订购
    private String resultDesc;
    private String accountIdentifyPhone;
    private String authorizationNum;    //鉴权序列号
    private String accountIdentify; //订购使用的计费标示
    private String productCode;     //鉴权成功的产品标示
    private String noprodcutInfo;
    private ArrayList<ProductBean> productToOrderList;

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

    public String getAccountIdentifyPhone() {
        return accountIdentifyPhone;
    }

    public String getPrice(String productCode){
        if(productToOrderList==null || productToOrderList.isEmpty()){
            return null;
        }
        for(ProductBean productBean:productToOrderList){
            if(productBean.getProductCode().equals(productCode)){
                return productBean.getPrice();
            }
        }
        return null;
    }

    public void setAccountIdentifyPhone(String accountIdentifyPhone) {
        this.accountIdentifyPhone = accountIdentifyPhone;
    }

    public String getAuthorizationNum() {
        return authorizationNum;
    }

    public void setAuthorizationNum(String authorizationNum) {
        this.authorizationNum = authorizationNum;
    }

    public String getAccountIdentify() {
        return accountIdentify;
    }

    public void setAccountIdentify(String accountIdentify) {
        this.accountIdentify = accountIdentify;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getNoprodcutInfo() {
        return noprodcutInfo;
    }

    public void setNoprodcutInfo(String noprodcutInfo) {
        this.noprodcutInfo = noprodcutInfo;
    }

    public ArrayList<ProductBean> getProductToOrderList() {
        return productToOrderList;
    }

    public ProductBean getProductBean(String productCode){
        for(ProductBean productBean:productToOrderList){
            if(productBean.getProductCode().equals(productCode)){
                return productBean;
            }
        }
        return null;
    }

    public void setProductToOrderList(ArrayList<ProductBean> productToOrderList) {
        this.productToOrderList = productToOrderList;
    }

    public ProductBean createProductBean(){
        return new ProductBean();
    }

    public void addProductBean(ProductBean productBean){
        productToOrderList.add(productBean);
    }

    public SalesStrategysBean createSalesStrategysBean(){
        return new SalesStrategysBean();
    }



    public class ProductBean{
        private String productCode;
        private String orderContentId;
        private String productInfo;
        private String productPrice;
        private String unit;
        private String cycle;
        private String validstarttime;
        private String validendtime;
        private String price;
        private String bpPrice;
        private String displayPrority;
        private String paymentType;
        private String spId;
        private String combineProduct;
        private String isSalesStrategy;
        private ArrayList<SalesStrategysBean> salesStrategyList;

        public ProductBean(){

        }

        public String getProductCode() {
            return productCode;
        }

        public void setProductCode(String productCode) {
            this.productCode = productCode;
        }

        public String getOrderContentId() {
            return orderContentId;
        }

        public void setOrderContentId(String orderContentId) {
            this.orderContentId = orderContentId;
        }

        public String getProductInfo() {
            return productInfo;
        }

        public void setProductInfo(String productInfo) {
            this.productInfo = productInfo;
        }

        public String getProductPrice() {
            return productPrice;
        }

        public void setProductPrice(String productPrice) {
            this.productPrice = productPrice;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getCycle() {
            return cycle;
        }

        public void setCycle(String cycle) {
            this.cycle = cycle;
        }

        public String getValidstarttime() {
            return validstarttime;
        }

        public void setValidstarttime(String validstarttime) {
            this.validstarttime = validstarttime;
        }

        public String getValidendtime() {
            return validendtime;
        }

        public void setValidendtime(String validendtime) {
            this.validendtime = validendtime;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getBpPrice() {
            return bpPrice;
        }

        public void setBpPrice(String bpPrice) {
            this.bpPrice = bpPrice;
        }

        public String getDisplayPrority() {
            return displayPrority;
        }

        public void setDisplayPrority(String displayPrority) {
            this.displayPrority = displayPrority;
        }

        public String getPaymentType() {
            return paymentType;
        }

        public void setPaymentType(String paymentType) {
            this.paymentType = paymentType;
        }

        public String getSpId() {
            return spId;
        }

        public void setSpId(String spId) {
            this.spId = spId;
        }

        public String getCombineProduct() {
            return combineProduct;
        }

        public void setCombineProduct(String combineProduct) {
            this.combineProduct = combineProduct;
        }

        public String getIsSalesStrategy() {
            return isSalesStrategy;
        }

        public void setIsSalesStrategy(String isSalesStrategy) {
            this.isSalesStrategy = isSalesStrategy;
        }

        public ArrayList<SalesStrategysBean> getSalesStrategyList() {
            return salesStrategyList;
        }

        public void setSalesStrategyList(ArrayList<SalesStrategysBean> salesStrategyList) {
            this.salesStrategyList = salesStrategyList;
        }

        public void addSalesStrategy(SalesStrategysBean salesStrategysBean){
            salesStrategyList.add(salesStrategysBean);
        }
    }

    public class SalesStrategysBean{
        private String firstPayAmount;
        private String firstThridPayAmount;
        private String firstPayCircle;
        private String totalCircle;
        private String displayPrority;
        private String saleTransID;
        private String strategyDesc;

        public SalesStrategysBean(){};

        public String getFirstPayAmount() {
            return firstPayAmount;
        }

        public void setFirstPayAmount(String firstPayAmount) {
            this.firstPayAmount = firstPayAmount;
        }

        public String getFirstThridPayAmount() {
            return firstThridPayAmount;
        }

        public void setFirstThridPayAmount(String firstThridPayAmount) {
            this.firstThridPayAmount = firstThridPayAmount;
        }

        public String getFirstPayCircle() {
            return firstPayCircle;
        }

        public void setFirstPayCircle(String firstPayCircle) {
            this.firstPayCircle = firstPayCircle;
        }

        public String getTotalCircle() {
            return totalCircle;
        }

        public void setTotalCircle(String totalCircle) {
            this.totalCircle = totalCircle;
        }

        public String getDisplayPrority() {
            return displayPrority;
        }

        public void setDisplayPrority(String displayPrority) {
            this.displayPrority = displayPrority;
        }

        public String getSaleTransID() {
            return saleTransID;
        }

        public void setSaleTransID(String saleTransID) {
            this.saleTransID = saleTransID;
        }

        public String getStrategyDesc() {
            return strategyDesc;
        }

        public void setStrategyDesc(String strategyDesc) {
            this.strategyDesc = strategyDesc;
        }
    }
}

