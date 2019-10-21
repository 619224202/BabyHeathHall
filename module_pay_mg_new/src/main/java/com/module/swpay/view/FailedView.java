package com.module.swpay.view;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.module.swpay.ThirdPayActivity;
import com.swkj.module_pay_mg_new.R;

/**
 * Created by wanglu on 2019/10/11.
 */
public class FailedView extends ConstraintLayout {
    //订购成功
    public FailedView(Context context,String textStr){
        super(context);
        LayoutInflater.from(context).inflate(R.layout.pay_fail_view,this);
        TextView textView = findViewById(R.id.failedText);
        textView.setText(textStr);
//        setFocusable(true);

//        setOnKeyListener(new OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if(event.getAction()==KeyEvent.ACTION_DOWN){
//                    System.out.println("按键按下>>>>>>>>>>>>>>>");
//                    switch(keyCode){
//                        case KeyEvent.KEYCODE_BACK:
//                        case KeyEvent.KEYCODE_ENTER:
//                        case KeyEvent.KEYCODE_DPAD_CENTER:
//                            ((ThirdPayActivity)getContext()).doEnd();
//                            return true;
//                    }
//                }
//                return true;
//            }
//        });
    }

    public FailedView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.pay_fail_view,this);
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
//        System.out.println("按键按下>>>>>>>>>>>>>>>");
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
