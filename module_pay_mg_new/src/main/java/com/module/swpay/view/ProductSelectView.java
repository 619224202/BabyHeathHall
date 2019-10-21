package com.module.swpay.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.module.swpay.ThirdPayActivity;
import com.module.swpay.http.bean.AuthorizeBean;
import com.swkj.module_pay_mg_new.R;
import java.util.ArrayList;

/**
 * Created by wanglu on 2019/10/14.
 */
public class ProductSelectView extends ConstraintLayout {
    private ArrayList<AuthorizeBean.ProductBean> productList;
    private ArrayList<ProductInfoView> productInfoList = new ArrayList<>();

    private PayTypeView payTypeView;
    private ProductInfoView productInfoView;

    private int groupIndex;
    private int infoIndex;
    //0为话费，1为扫码，2为都存在
    private int payType;

    private boolean isInMove;



    private RelativeLayout linearLayout;
    private LayoutParams layoutParams;

    private int moveLenth = 0;
    private int nowMoveLenth = 20;
    private int moveSpeed = 10;
    private int addMoveSpeed = -8;


    private Handler mainHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int msgCode = msg.what;
            switch(msgCode){
                case 1001:
                    isInMove = false;
                    invalidate();
                    break;
                case 1011:
                    System.out.println("移动距离为"+msg.arg1);
                    linearLayout.scrollBy(msg.arg1,0);
                    invalidate();
                    break;
            }
        }
    };

    public ProductSelectView(Context context, ArrayList<AuthorizeBean.ProductBean> productList) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.product_select_view,this);
        linearLayout = findViewById(R.id.payInfoLayout);
        payTypeView = findViewById(R.id.payTypeSelView);
        this.productList = productList;
        init();
    }

    private void init(){
        createProductInfoList(productList);
        setInfoIndex(0);
        setGroupIndex(0);
    }

    /**
     * 设置操作的范围，0位价格，1位下方选择
     * @param groupIndex
     */
    public void setGroupIndex(int groupIndex){
        this.groupIndex = groupIndex;
        switch(groupIndex){
            case 0:
                if(payTypeView!=null){
                    payTypeView.setB_select(false);
                }
                break;
            case 1:
                if(payTypeView!=null){
                    payTypeView.setB_select(true);
                }
                break;
        }
    }


    public void setInfoIndex(int infoIndex){
        if(productInfoView!=null){
            productInfoView.setSelect(false);
            productInfoView = null;
        }
        this.infoIndex = infoIndex;
        productInfoView = productInfoList.get(infoIndex);
        productInfoView.setSelect(true);
        payTypeView.setProductBean(productInfoView.getProductBean());
    }

    public void createProductInfoList(ArrayList<AuthorizeBean.ProductBean> productList){
        productInfoList.clear();
        linearLayout.removeAllViews();
        int index = 0;

        for(AuthorizeBean.ProductBean productBean:productList){

            ProductInfoView productInfoView = new ProductInfoView(getContext(),productBean);
            layoutParams = (LayoutParams) productInfoView.getLayoutParams();

            productInfoView.setX(544+(384*index));
            productInfoView.setY(33);
//            this.addView(productInfoView);
            linearLayout.addView(productInfoView);
            productInfoList.add(productInfoView);
            index ++;
        }
    }


    public ProductSelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.product_select_view,this);
    }

    public boolean doPressed(int keyCode, KeyEvent event){
        if(isInMove){
            return false;
        }
        switch(keyCode){
            case KeyEvent.KEYCODE_DPAD_LEFT:
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                if(groupIndex==0){
                    moveInfo(keyCode);
                    invalidate();
                }else{
                    payTypeView.doPressed(keyCode,event);
                }
                break;
            case KeyEvent.KEYCODE_DPAD_UP:
                if(groupIndex==1){
                    setGroupIndex(0);
                    invalidate();
                }
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                if(groupIndex==0){
                    setGroupIndex(1);
                    invalidate();
                }
                break;
            case KeyEvent.KEYCODE_ENTER:
            case KeyEvent.KEYCODE_DPAD_CENTER:
                if(groupIndex==0){
                    setGroupIndex(1);
                    invalidate();
                }else{
                    payTypeView.doFire();
                    ((ThirdPayActivity)getContext()).showPayConfirm();
                }
                break;
            case KeyEvent.KEYCODE_BACK:
                ((ThirdPayActivity)getContext()).getPaySdkForMG().doPayEnd(false);
                return true;
        }
        return true;
    }

    public void moveInfo(int keyCode){
        if(keyCode==KeyEvent.KEYCODE_DPAD_RIGHT){
            if(infoIndex<productInfoList.size()-1){
//                linearLayout.scrollBy(384,0);
                moveStart(384,false);
                setInfoIndex(infoIndex+1);
            }
        }else if(keyCode==KeyEvent.KEYCODE_DPAD_LEFT){
            if(infoIndex>0){
//                linearLayout.scrollBy(-384,0);
                moveStart(-384,true);
                setInfoIndex(infoIndex-1);
            }
        }
    }


    public void moveStart(final int moveLenth, final boolean isLeft){
        this.moveLenth = moveLenth;
        this.nowMoveLenth = 0;
        if(isLeft){
            moveSpeed = -10;
            addMoveSpeed = -8;
        }else{
            moveSpeed = 10;
            addMoveSpeed = 8;
        }
        isInMove = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                int offX = 0;
                boolean isMove = true;
                while(isMove){
                    nowMoveLenth += moveSpeed;
                    if(Math.abs(moveSpeed)<=40) {
                        moveSpeed += addMoveSpeed;
                    }
                    if(Math.abs(nowMoveLenth)>=Math.abs(moveLenth)){
                        System.out.println("移动结束>>>>>>>>>>>>>>>");
                        isMove = false;
                        if(isLeft){
                            offX = moveSpeed-(nowMoveLenth-moveLenth);
                        }else{
                            offX = moveSpeed-(nowMoveLenth-moveLenth);
                        }
                    }else{
                        offX = moveSpeed;
                    }
                    Message message = new Message();
                    message.what = 1011;
                    message.arg1 = offX;
                    mainHandler.sendMessage(message);
                    try {
                        Thread.sleep(60);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                isInMove = false;
            }
        }).start();
    }

}
