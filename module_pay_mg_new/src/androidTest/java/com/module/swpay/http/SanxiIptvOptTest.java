package com.module.swpay.http;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SanxiIptvOptTest {

    private SanxiIptvOpt sanxiIptvOpt;
    @Test
    public void getInstance() {
    }

    @Before
    public void init() {
        sanxiIptvOpt=SanxiIptvOpt.getInstance();
    }

    @Test
    public void checkProductPrice() {
       String xml ="";
        System.out.println("xml="+xml);
        sanxiIptvOpt.checkProductPrice(xml);
    }

    @Test
    public void productAuthorize() {
    }

    @Test
    public void productAdvPay() {
    }

    @Test
    public void productAdvPayResult() {
    }

    @Test
    public void productOrder() {
    }

    @Test
    public void generateXml() {
    }
}