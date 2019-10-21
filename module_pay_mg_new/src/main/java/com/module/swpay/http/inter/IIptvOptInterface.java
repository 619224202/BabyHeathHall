package com.module.swpay.http.inter;

import com.module.swpay.http.bean.ADVPayBean;
import com.module.swpay.http.bean.ADVPayResultBean;
import com.module.swpay.http.bean.AuthorizeBean;
import com.module.swpay.http.bean.OmsProductBean;
import com.module.swpay.http.bean.OrderBean;


public interface IIptvOptInterface {
    /**
     * @param xmlStr 请求参数
     * 内容定价接口
     * @return 返回JsonObject
     */
    public OmsProductBean checkProductPrice(String xmlStr);


    /**
     * 鉴权询价接口
     * @param xmlStr 发送的请求参数
     * @return 返回jsonObject
     */
    public AuthorizeBean productAuthorize(String xmlStr);

    /**
     * 支付下单接口
     * @param xmlStr 发送的请求参数
     * @return 返回jsonObject
     */
    public ADVPayBean productAdvPay(String xmlStr);

    /**
     * 支付状态查询，需要轮询该接口，3分钟后无返回值则订购失败
     * @param xmlStr 发送的请求参数
     * @return 返回jsonObject
     */
    public ADVPayResultBean productAdvPayResult(String xmlStr);

    /**
     * 订购产品
     * @param xmlStr  发送的请求参数
     * @return  返回JsonObject
     */
    public OrderBean productOrder(String xmlStr);

}
