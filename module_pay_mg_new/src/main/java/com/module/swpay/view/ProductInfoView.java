package com.module.swpay.view;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.module.swpay.http.bean.AuthorizeBean;
import com.swkj.module_pay_mg_new.R;

/**
 * Created by wanglu on 2019/10/14.
 */
public class ProductInfoView extends View {

    private AuthorizeBean.ProductBean productBean;
    private Bitmap bgBit;
    private Paint paint;
    private boolean isSelect;
    private int beginX,beginY;
    private float f_price;
    private String unit;
    private String desc;

    private boolean isPhonePay = false;
    private boolean isQRPay = false;

    private boolean b_selPayType = false;

    private String payMess = "";

    private final String msg = "按【确定】键查看详情";
    public ProductInfoView(Context context, AuthorizeBean.ProductBean productBean) {
        super(context);
        this.productBean = productBean;
        init();
    }

    public ProductInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void init(){
        bgBit = BitmapFactory.decodeResource(getResources(),R.drawable.product_bg);
        setPivotX(bgBit.getWidth()/2);
        setPivotY(bgBit.getHeight()/2);
        System.out.println("w="+bgBit.getWidth()+",h="+bgBit.getHeight());
        this.f_price = Integer.parseInt(productBean.getPrice())/100.0f;
        int circle = 0;
        if(productBean.getCycle()!=null && productBean.getCycle().length()>0){
            circle = Integer.parseInt(productBean.getCycle());
        }
        this.unit = "元/"+getUnitStr(Integer.parseInt(productBean.getUnit()),circle);
        if(this.unit.contains("月")){
            desc = "包月会员";
        }else if(this.unit.contains("季")){
            desc = "季度会员";
        }else if(this.unit.contains("年")){
            desc = "年度会员";
        }else if(this.unit.contains("次")){
            desc = "按次订购";
        }
        setProductBean(productBean);
//        checkPayType();
    }



    /**
     * 1.天、2.连续包月、3.单月、4.年、5.季、6.固定时长、7.按次
     */

    private String getUnitStr(int unit_type,int circle){
        switch (unit_type){
            case 1:
                if(circle==1||circle==0) {
                    return "天";
                }else{
                    return circle+"天";
                }
            case 2:
                payMess = "连续包月，长期有效";
                return "连续包月";
            case 3:
                if(circle==1||circle==0) {
                    payMess = "连续包月，长期有效";
                    return "月";
                }else{
                    payMess = "连续包"+circle+"月，长期有效";
                    return circle+"月";
                }
            case 4:
                if(circle==1||circle==0) {
                    payMess = "有效期12个月";
                    return "年";
                }else{
                    return circle+"年";
                }
            case 5:
                if(circle==1||circle==0) {
                    payMess = "有效期3个月";
                    return "季";
                }else{
                    payMess = "有效期"+(3*circle)+"个月";
                    return circle+"季";
                }
            case 6:
                if(circle==1||circle==0) {
                    return "分";
                }else{
                    return circle+"分";
                }
            case 7:
                payMess = "按次收费";
                return "次";
        }
        return "次";
    }

    public void setProductBean(AuthorizeBean.ProductBean productBean){
        this.productBean = productBean;
        this.invalidate();
    }

    public void setSelect(boolean isSelect){
        this.isSelect = isSelect;
        if(isSelect) {
            this.setScaleX(1.2f);
            this.setScaleY(1.2f);
        }else{
            this.setScaleX(1.0f);
            this.setScaleY(1.0f);
        }
        b_selPayType = false;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint = new Paint();
        canvas.drawBitmap(bgBit,0,0,paint);
        paint.setTextAlign(Paint.Align.RIGHT);
        paint.setColor(Color.WHITE);
        paint.setTextSize(55);
        paint.setHinting(Paint.HINTING_ON);
        paint.setTypeface(Typeface.create("SANS_SERIF",Typeface.NORMAL));
        paint.setFakeBoldText(true);
        canvas.drawText(f_price+"",bgBit.getWidth()/2,(int)(beginY+bgBit.getHeight()*0.3),paint);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(35);
        canvas.drawText(unit,bgBit.getWidth()/2,(int)(beginY+bgBit.getHeight()*0.3),paint);
        paint.setTextSize(20);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(payMess,bgBit.getWidth()/2,beginY+(int)(bgBit.getHeight()*0.5),paint);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(35);
        canvas.drawText(desc,bgBit.getWidth()/2,beginY+(int)(bgBit.getHeight()*0.7),paint);
        if(isSelect){
            paint.setTextSize(15.0f);
            canvas.drawText(msg,bgBit.getWidth()/2,beginY+(int)(bgBit.getHeight()*0.85),paint);
        }
//        super.onDraw(canvas);
    }


    public AuthorizeBean.ProductBean getProductBean(){
        return productBean;
    }



}
