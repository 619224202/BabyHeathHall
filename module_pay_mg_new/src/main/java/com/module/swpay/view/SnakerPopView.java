package com.module.swpay.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.swkj.module_pay_mg_new.R;

/**
 * Created by wanglu on 2019/10/10.
 */
public class SnakerPopView {
    //Hanler消息
    private final static int DISSMISS_POPWINDOW = 0;
    //类属性 多个实例共用的属性 不易多次初始化
    //因为界面上显示线程比较单一 不存在多线程共用 所以不进行同步控制
    private static PopupWindowTouchListener touchListener;
    private static TimerHandler timerHander = new TimerHandler();
    private static PopupWindow popupWindow;
    private static View content;
    private static TextView text;
    private static ImageView icon;


    private boolean isChanged;
    //如果没有设置高度 则默认是80dp
    private int DEFAULT_HEIGHT = 80;//dp
    private ActionCallBack callBack;


    //用作延迟消失消息处理
    static class TimerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DISSMISS_POPWINDOW:
                    dismiss();
                    break;
            }
        }
    }
    //私有构造器 使用ContentBuilder 创建
    private SnakerPopView(ContentBuilder contentBuilder) {


        //popupWindow实例共用属性 随类加载一次
        if (popupWindow == null) {
//            Log.d("SnakerPopView", "popupWindow init");
            popupWindow = new PopupWindow(LinearLayout.LayoutParams.WRAP_CONTENT, contentBuilder.snakerPopViewHeight == 0 ? DEFAULT_HEIGHT : contentBuilder.snakerPopViewHeight);
            popupWindow.setOutsideTouchable(true);
        }
        //content view实例共用属性 随类加载一次
        if (content == null) {
//            Log.d("SnakerPopView", "content init");
            content = LayoutInflater.from(contentBuilder.context).inflate(R.layout.snaker_pop_view, null);
            icon = (ImageView) content.findViewById(R.id.pop_icon);
            //icon.setId(View.generateViewId());
            text = (TextView) content.findViewById(R.id.pop_text);
//            text.setId(View.generateViewId());
        }




        if (contentBuilder.isChanged) {
//            Log.d("SnakerPopView", "isChanged");
            //设置提示文本
            text.setText(contentBuilder.text);
            //设置图片icon
            if (contentBuilder.imageResource != 0) {
                icon.setBackgroundResource(contentBuilder.imageResource);
                icon.setVisibility(View.VISIBLE);
            } else {
                icon.setVisibility(View.GONE);
            }
            //设置背景颜色 默认为R.color.colorAccent
            content.setBackgroundColor(contentBuilder.context.getResources().getColor(contentBuilder.colorResource == 0 ? R.color.colorAccent : contentBuilder.colorResource));
            popupWindow.setContentView(content);
            //设置显示和消失动画  默认R.style.SnakerPopWindowAnimation
//            popupWindow.setAnimationStyle(contentBuilder.animationStyle == 0 ? R.style.SnakerPopWindowAnimation : contentBuilder.animationStyle);
//            popupWindow.setAnimationStyle(contentBuilder.animationStyle == 0 ? R.style. : contentBuilder.animationStyle);
        }
        //设置了提示消息不变也要更新显示 或者 消息等内容改变提示显示
        this.isChanged = contentBuilder.isChanged || contentBuilder.textNoChangeEfectEnable;


    }

    class PopupWindowTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
//            Log.d("SnakerPopView", "onTouch"+event.getAction());
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
                case MotionEvent.ACTION_UP:
                    dismiss();
                    callBack.onAction();
                    break;
                case MotionEvent.ACTION_OUTSIDE:
                    remove();
                    break;
            }
            return false;
        }
    }


    public interface ActionCallBack {
        void onAction();
    }
    //清除显示
    private static void dismiss() {
        popupWindow.dismiss();
    }
    //清除显示|延迟消失消息  立即消失
    public static void clear() {
//        Log.d("SnakerPopView", "clear");
        timerHander.removeMessages(DISSMISS_POPWINDOW);
        popupWindow.setAnimationStyle(0);
        dismiss();
    }
    //清除显示|延迟消失消息
    public static void remove() {
        timerHander.removeMessages(DISSMISS_POPWINDOW);
        dismiss();
    }


    public static boolean isShowing() {
        if (popupWindow == null) return false;
        return popupWindow.isShowing();
    }


    /**
     * 手动取消的带回调提示
     *
     * @param aboveView 在这个View的下面
     * @param callback  需要点击的action回调
     */
    public void updateAndShowWithAction(View aboveView, ActionCallBack callback) {
        if (callback == null) {
            throw new IllegalArgumentException("You must set the ActionCallBack when call this method!");
        }
        this.callBack = callback;
        if (touchListener == null) {
            touchListener = new PopupWindowTouchListener();
        }
        updateAndShow(aboveView, 0, touchListener);
    }


    /**
     * 延迟消失的提示
     *
     * @param aboveView
     * @param delayTime
     */
    public void updateAndShowWithDelay(View aboveView, long delayTime) {
        if (delayTime <= 0) {
            throw new IllegalArgumentException("The delayTime must be positive number!(>0)");
        }
        updateAndShow(aboveView, delayTime, null);
    }


    private void updateAndShow(View aboveView, long delayTime, View.OnTouchListener touchListener) {
        if (!isChanged) {
//            Log.d("SnakerPopView", "no change return");
            return;
        }
        if (popupWindow.isShowing()) {
//            Log.d("SnakerPopView", "changed  dismiss before");
            remove();
        }
        //设置弹出View触摸事件监听器
        if (touchListener != null) {
            content.setOnTouchListener(touchListener);
        }else{
            content.setOnTouchListener(null);
        }
        //保持弹出视图长度和锚点View长度一致
        popupWindow.setWidth(aboveView.getMeasuredWidth());
        popupWindow.showAsDropDown(aboveView);
        if (delayTime > 0) {
            timerHander.sendMessageDelayed(timerHander.obtainMessage(DISSMISS_POPWINDOW), delayTime);
        }
    }


    /**
     * The ContentBuilder class.
     */
    public static class ContentBuilder {
        public Context context;
        public static int imageResource;
        public static String text;
        public static int colorResource;
        public static int animationStyle;
        public static int snakerPopViewHeight;
        public boolean textNoChangeEfectEnable;
        public boolean isChanged;


        /**
         * @param context
         * @param textNoChangeEfectEnable 实际提示文本没有改变 if textNoChangeEfectEnable is ture:依然弹出
         *                                false:不弹出
         *                                textNoChangeEfectEnable is false    一般用在文本必定更改的情况下
         */
        public ContentBuilder(Context context, boolean textNoChangeEfectEnable) {
            this.textNoChangeEfectEnable = textNoChangeEfectEnable;
            this.context = context.getApplicationContext();
        }


        public ContentBuilder setIcon(int imageResourceFrom) {
            if (imageResourceFrom == 0) {
                throw new IllegalArgumentException("imageResourceFrom should not be 0");
            }
            if (imageResource != imageResourceFrom) {
                imageResource = imageResourceFrom;
                isChanged = true;
            }


            return this;
        }


        public ContentBuilder setText(String textFrom) {
            if (textFrom == null) {
                throw new IllegalArgumentException("null text");
            }
            if (!textFrom.equals(text)) {
                text = textFrom;
                isChanged = true;
            }


            return this;
        }


        public ContentBuilder setBackgroundColor(int colorResourceFrom) {


            if (colorResourceFrom == 0) {
                throw new IllegalArgumentException("colorResourceFrom should not be 0");
            }
            if (colorResource != colorResourceFrom) {
                colorResource = colorResourceFrom;
                isChanged = true;
            }
            return this;
        }


        public ContentBuilder setContentViewAnimationStyle(int animationStyleFrom) {


            if (animationStyleFrom == 0) {
                throw new IllegalArgumentException("animationStyleFrom should not be 0");
            }
            if (animationStyle != animationStyleFrom) {
                animationStyle = animationStyleFrom;
                isChanged = true;
            }
            return this;
        }


        public ContentBuilder setSnakerPopViewHeight(int snakerPopViewHeightFrom) {


            if (snakerPopViewHeightFrom == 0) {
                throw new IllegalArgumentException("snakerPopViewHeightFrom should not be 0");
            }
            if (snakerPopViewHeight != snakerPopViewHeightFrom) {
                snakerPopViewHeight = snakerPopViewHeightFrom;
                isChanged = true;
            }
            return this;
        }




        public SnakerPopView build() {
            return new SnakerPopView(this);
        }
    }


}
