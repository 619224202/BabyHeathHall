package com.module.swpay.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;

import com.module.swpay.PaySdkForMG;
import com.module.swpay.http.bean.AuthorizeBean;
import com.swkj.module_pay_mg_new.R;

/**
 * Created by wanglu on 2019/10/14.
 */
public class PayTypeView extends View {
    private Bitmap[] bit_payTypes;
    private AuthorizeBean.ProductBean productBean;
    private Bitmap bit_selPay;
    private int selIndex;

    private int payType = -1;
    private boolean b_select = false;

    private Paint paint;
    public PayTypeView(Context context) {
        super(context);
        init();
    }

    public PayTypeView(Context context,  AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init(){
        bit_payTypes = new Bitmap[2];
        bit_payTypes[0] = BitmapFactory.decodeResource(getResources(), R.drawable.b1);
        bit_payTypes[1] = BitmapFactory.decodeResource(getResources(),R.drawable.b2);
        bit_selPay = BitmapFactory.decodeResource(getResources(),R.drawable.bk);
    }

    public void setProductBean(AuthorizeBean.ProductBean productBean){
        this.productBean = productBean;
        payType = checkProduct();
        selIndex = 0;
        invalidate();
    }

    private int checkProduct(){
        boolean isHavePhone = false;
        boolean isHaveQR = false;

        int payType = Integer.parseInt(productBean.getPaymentType());
        if((payType|16)!=0){
            isHavePhone = true;
        }
        if((payType^16)!=0){
            isHaveQR = true;
        }
        if(isHavePhone && isHaveQR){
            return 2;
        }else if(isHavePhone){
            return 0;
        }else if(isHaveQR){
            return 1;
        }else{
            return -1;
        }
    }

    public void setB_select(boolean b_select){
        this.b_select = b_select;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint = new Paint();
        int posY = getHeight()/2-bit_payTypes[0].getHeight()/2;
        switch(payType){
            case 0:
                canvas.drawBitmap(bit_payTypes[0],getWidth()/2-bit_payTypes[0].getWidth()/2,
                        posY,paint);
                if(b_select){
                    canvas.drawBitmap(bit_selPay,getWidth()/2-bit_payTypes[0].getWidth()/2,
                            posY,paint);
                }
                break;
            case 1:
                canvas.drawBitmap(bit_payTypes[1],getWidth()/2-bit_payTypes[1].getWidth()/2,
                        posY,paint);
                if(b_select){
                    canvas.drawBitmap(bit_selPay,getWidth()/2-bit_payTypes[1].getWidth()/2,
                            posY,paint);
                }
                break;
            case 2:
                int offSetX = (getWidth()-50-bit_payTypes[0].getWidth()*2)/2;
                canvas.drawBitmap(bit_payTypes[0],offSetX,posY,paint);
                canvas.drawBitmap(bit_payTypes[1],getWidth()-bit_payTypes[1].getWidth()-offSetX,posY,paint);
                if(b_select) {
                    if (selIndex == 0) {
                        canvas.drawBitmap(bit_selPay, offSetX, posY, paint);
                    } else {
                        canvas.drawBitmap(bit_selPay, getWidth()-bit_payTypes[1].getWidth()-offSetX,posY, paint);
                    }
                }
                break;
        }
        super.onDraw(canvas);
    }

    public boolean doPressed(int keyCode, KeyEvent keyEvent){
        switch(keyCode){
            case KeyEvent.KEYCODE_DPAD_LEFT:
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                if(payType==0||payType==1){
                    return true;
                }
                if(selIndex==0){
                    selIndex=1;
                }else{
                    selIndex=0;
                }
                invalidate();
                break;
        }
        return false;
    }

    public void doFire(){
        PaySdkForMG.productCode = productBean.getProductCode();
        if(payType==1){
            PaySdkForMG.isUseThirdPay = true;
        }else if(payType==2 && selIndex==1){
            PaySdkForMG.isUseThirdPay = true;
        }else{
            PaySdkForMG.isUseThirdPay = false;
        }
    }
}
