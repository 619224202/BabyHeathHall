package com.module.swpay;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.module.swpay.http.bean.AuthorizeBean;
import com.module.swpay.view.FailedView;
import com.module.swpay.view.PayConfirmView;
import com.module.swpay.view.ProductSelectView;
import com.module.swpay.view.QRCodeView;
import com.module.swpay.view.SecondPayView;
import com.module.swpay.view.SpinnerHub;
import com.module.swpay.view.SuccessView;
import com.swkj.module_pay_mg_new.R;

public class ThirdPayActivity extends AppCompatActivity {
    private PaySdkForMG paySdkForMG;
    //二次确认框
    private SecondPayView secondPayView;
    //确认框
    private PayConfirmView payConfirmView;
    //二维码页面
    private QRCodeView qrCodeView;

    private SuccessView successView;
    private FailedView failedView;


    private FrameLayout payLayout;

    private ProductSelectView productSelectView;

    //显示加载的loading
    public SpinnerHub loadingHub;

    private int payState = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_layout);
        payLayout = findViewById(R.id.payLayout);
        paySdkForMG = (PaySdkForMG) PayService.getInstance().getPaySdk();
        paySdkForMG.setPayActivity(this);
        showProductInfoPay();

    }

    public void endActivity(Activity activity){
        Intent intent = new Intent(ThirdPayActivity.this,activity.getClass());
        startActivity(intent);
        paySdkForMG.setPayActivity(null);
        this.paySdkForMG = null;
        this.finish();
    }

    public void doEnd(){
        paySdkForMG.endPayActivity();
    }

    public void showSecondPayView(){
        AuthorizeBean.ProductBean productBean = paySdkForMG.getProductBean();
        secondPayView = new SecondPayView(this,productBean.getPrice(),paySdkForMG.getPhone());
        payLayout.addView(secondPayView);
        setState(0);
    }

    public void showPayConfirm(){
//        secondPayView.setFocusable(false);
        AuthorizeBean.ProductBean productBean = paySdkForMG.getProductBean();
        payConfirmView = new PayConfirmView(this,productBean.getPrice(),paySdkForMG.getPhone());
        payConfirmView.setId(View.generateViewId());
        payLayout.removeView(productSelectView);
        productSelectView = null;
        payLayout.addView(payConfirmView);
        setState(1);
    }

    public void showQRView(){
//        payLayout.removeView(secondPayView);
        payLayout.removeAllViews();
        if(payConfirmView!=null) {
            payConfirmView.setFocusable(false);
            payLayout.removeView(payConfirmView);
            payConfirmView = null;
        }
        qrCodeView = null;
        qrCodeView = new QRCodeView(this);
        payLayout.addView(qrCodeView);
        setState(2);
    }


    public void showPaySuccess(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                successView = new SuccessView(ThirdPayActivity.this);
                payLayout.removeView(payConfirmView);
                payConfirmView = null;
                payLayout.addView(successView);
                setState(3);
            }
        });
    }

    public void showPayFailed(final String desc){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                failedView = new FailedView(ThirdPayActivity.this,desc);
                payLayout.removeView(payConfirmView);
                payConfirmView = null;
                payLayout.addView(failedView);
                setState(4);
            }
        });

    }

    public void showProductInfoPay(){
        productSelectView = new ProductSelectView(ThirdPayActivity.this,paySdkForMG.getProductBeanList());
        payLayout.addView(productSelectView);
        setState(0);
    }

    public void setState(int state){
        this.payState = state;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch(payState){
            case 0:
                return productSelectView.doPressed(keyCode,event);
//                return secondPayView.doPressed(keyCode,event);
            case 1:
                return payConfirmView.doPressed(keyCode, event);
            case 2:
                return qrCodeView.doPressed(keyCode, event);
            case 3:
                return successView.doPressed(keyCode, event);
            case 4:
                return failedView.doPressed(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }
    public PaySdkForMG getPaySdkForMG(){
        return paySdkForMG;
    }

    public void showLoading(){

            if(loadingHub==null){
                loadingHub=SpinnerHub.show(this,"加载中",true,false,null);
            }


    }

    public void hideLoading(){
        if(loadingHub!=null){
            loadingHub.hide();
            loadingHub.cancel();
            loadingHub = null;
        }
    }
}
