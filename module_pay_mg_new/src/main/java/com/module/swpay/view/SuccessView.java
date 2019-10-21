package com.module.swpay.view;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;

import com.module.swpay.ThirdPayActivity;
import com.swkj.module_pay_mg_new.R;

/**
 * Created by wanglu on 2019/10/11.
 */
public class SuccessView extends ConstraintLayout {
    //订购成功
    public SuccessView(Context context){
        super(context);
        LayoutInflater.from(context).inflate(R.layout.pay_success_view,this);
//        setFocusable(true);
//        setOnKeyListener(new OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if(event.getAction()==KeyEvent.ACTION_DOWN){
//
//                }
//                return true;
//            }
//        });
    }

    public SuccessView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.pay_success_view,this);
    }

    public boolean doPressed(int keyCode,KeyEvent event){
        switch(keyCode){
            case KeyEvent.KEYCODE_BACK:
            case KeyEvent.KEYCODE_ENTER:
            case KeyEvent.KEYCODE_DPAD_CENTER:
                ((ThirdPayActivity)getContext()).doEnd();
                return true;
        }
        return true;
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        switch(keyCode){
//            case KeyEvent.KEYCODE_BACK:
//            case KeyEvent.KEYCODE_ENTER:
//            case KeyEvent.KEYCODE_DPAD_CENTER:
//                ((ThirdPayActivity)getContext()).doEnd();
//                return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
}
