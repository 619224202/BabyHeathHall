package com.module.swpay.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.swkj.module_pay_mg_new.R;

/**
 * Created by wanglu on 2019/10/14.
 */
public class SpinnerHub extends Dialog {
    public SpinnerHub(Context content){
        super(content);
        setContentView(R.layout.spinner_layout);
    }

    public SpinnerHub(Context context, int theme){
        super(context,theme);
        setContentView(R.layout.spinner_layout);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        ImageView imageView = findViewById(R.id.spinnerView);
        AnimationDrawable spinner = (AnimationDrawable) imageView.getBackground();
        System.out.println("hasFocus1111111111111"+hasFocus);
        if(hasFocus) {
            spinner.start();
            System.out.println("开始播放>>>>>>>>>>>>>>>>>");
        }else{
            if(spinner.isRunning()) {
                spinner.stop();
            }
        }
        super.onWindowFocusChanged(hasFocus);
    }

    public void setText(CharSequence message){
        if(message!=null && message.length()>0){
            TextView textVeiw = findViewById(R.id.spinnerMessage);
            textVeiw.setVisibility(View.VISIBLE);
            textVeiw.setText(message);
            textVeiw.invalidate();
        }
    }

    public static SpinnerHub show(Context context, CharSequence message, boolean indeterminate, boolean cancelable,
                     OnCancelListener cancelListener){

        SpinnerHub dialog = new SpinnerHub(context,R.style.ProgressHUD);
        dialog.setTitle("加载中");
        if(message == null || message.length() == 0) {
            dialog.findViewById(R.id.spinnerMessage).setVisibility(View.GONE);
        } else {
            TextView txt = (TextView)dialog.findViewById(R.id.spinnerMessage);
            txt.setText(message);
        }
        dialog.setCancelable(cancelable);
        dialog.setOnCancelListener(cancelListener);
        dialog.getWindow().getAttributes().gravity= Gravity.CENTER;//设置dialog显示在屏幕中央
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount=0.8f; //设置控件的黑暗度，值为0-1。0.0f完全不暗，1.0f完全黑暗。也可以设置控件的透明度，如：<span style="color: rgb(73, 73, 73); font-family: simsun; font-size: 16px; line-height: 24px; background-color: rgb(230, 232, 231);">lp.alpha=1.0f;</span>
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        dialog.show();
        return dialog;
    }
}
