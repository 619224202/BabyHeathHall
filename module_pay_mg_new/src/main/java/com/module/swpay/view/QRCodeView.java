package com.module.swpay.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.constraint.ConstraintLayout;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.widget.ImageView;

import com.module.swpay.PaySdkForMG;
import com.module.swpay.PayService;
import com.module.swpay.ThirdPayActivity;
import com.swkj.module_pay_mg_new.R;

/**
 * Created by wanglu on 2019/10/10.
 */
public class QRCodeView extends ConstraintLayout {
    private ImageView imgView;
    private ThirdPayActivity thirdPayActivity;

    public QRCodeView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.activity_third_pay,this);
        this.thirdPayActivity = (ThirdPayActivity)context;
        PaySdkForMG paySdkForMG = thirdPayActivity.getPaySdkForMG();
        imgView = findViewById(R.id.thirdPay);
        String imgStr = paySdkForMG.getImgData();
        setImgView(imgStr);
    }

    protected void setImgView(String imgStr){
        byte[] data = Base64.decode(imgStr,Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
        imgView.setImageBitmap(bitmap);
    }

    public boolean doPressed(int keyCode, KeyEvent event) {
        switch(keyCode){
            case KeyEvent.KEYCODE_BACK:
                System.out.println("停止检测，返回");
                thirdPayActivity.getPaySdkForMG().back();
                return true;
            case KeyEvent.KEYCODE_ENTER:
                thirdPayActivity.getPaySdkForMG().stopCheck();
                System.out.println("重新下单，并更改图片，重新开始检测");
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}
