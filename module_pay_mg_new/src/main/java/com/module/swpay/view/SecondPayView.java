package com.module.swpay.view;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.module.swpay.ThirdPayActivity;
import com.swkj.module_pay_mg_new.R;

/**
 * Created by wanglu on 2019/10/8.
 */
public class SecondPayView extends ConstraintLayout {
    private TextView priceView;
    private TextView phoneView;
    public SecondPayView(Context context,String price,String phone) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.second_view,this);
        priceView = findViewById(R.id.priceText);
        phoneView = findViewById(R.id.phoneText);
//        this.setFocusable(true);
        setPrice(price);
        setPhone(phone);

    }

    public void setPrice(String price){
        int f_price = Integer.parseInt(price)/100;
        priceView.setText(f_price+"元");
    }

    public void setPhone(String phone){
        phoneView.setText(phone);
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        switch(keyCode){
//            case KeyEvent.KEYCODE_ENTER:
//            case KeyEvent.KEYCODE_DPAD_CENTER:
//                System.out.println("按下确定键>>>>>>>>>>>>>");
//                this.setFocusable(false);
//                ((ThirdPayActivity)getContext()).showPayConfirm();
//                return true;
//            case KeyEvent.KEYCODE_BACK:
//                ((ThirdPayActivity)getContext()).getPaySdkForMG().doPayEnd(false);
//                return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    public boolean doPressed(int keyCode,KeyEvent event){
        if(event.getAction()==KeyEvent.ACTION_DOWN){
            switch(keyCode){
                case KeyEvent.KEYCODE_ENTER:
                case KeyEvent.KEYCODE_DPAD_CENTER:
                    System.out.println("按下确定键>>>>>>>>>>>>>");
                    SecondPayView.this.setFocusable(false);

                    ((ThirdPayActivity)getContext()).showPayConfirm();
                    return true;
                case KeyEvent.KEYCODE_BACK:
                    ((ThirdPayActivity)getContext()).getPaySdkForMG().doPayEnd(false);
                    return true;
            }
        }
        return false;
    }
}
