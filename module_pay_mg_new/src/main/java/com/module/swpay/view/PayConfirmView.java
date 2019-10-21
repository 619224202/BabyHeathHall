package com.module.swpay.view;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.module.swpay.ThirdPayActivity;
import com.swkj.module_pay_mg_new.R;


/**
 * Created by wanglu on 2019/10/9.
 */
public class PayConfirmView extends ConstraintLayout {
    private TextView priceView;
    private TextView phoneView;
    private ImageView xk3;
    private ImageView xk2;
    private ImageView xk1;
    private EquationView equationView;

    private ResultView resultView;

    private int result;
    private int numIndex;
    private int butIndex;
    private int groupIndex;

    private ConstraintSet xk2ConstraintSet = new ConstraintSet();
    private ConstraintSet xk3ConstraintSet = new ConstraintSet();
    private ConstraintLayout constraintLayout;

    private float[][] numPos = {{0.425f,0.515f},{0.513f,0.515f},{0.605f,0.515f},{0.683f,0.515f},{0.78f,0.515f},
            {0.425f,0.681f},{0.513f,0.681f},{0.605f,0.681f},{0.683f,0.681f},{0.78f,0.681f}};

    private float[][] anniuPos = {{0.474f,0.91f},{0.878f,0.91f}};

    private ThirdPayActivity thirdPayActivity;

    public PayConfirmView(Context context,String price,String phone){
        super(context);
        setThirdPayActivity((ThirdPayActivity)context );
        LayoutInflater.from(context).inflate(R.layout.pay_comfire,this);
        this.setId(View.generateViewId());
        priceView = findViewById(R.id.priceText1);
        phoneView = findViewById(R.id.phoneText2);
        int f_price = Integer.parseInt(price)/100;
        priceView.setText(f_price+"");
        phoneView.setText(phone);
        xk3 = findViewById(R.id.xk3);
        xk2 = findViewById(R.id.xk2);
        xk1 = findViewById(R.id.xk1);
        constraintLayout = findViewById(R.id.pay_confirm_layout);

        equationView = findViewById(R.id.equationView);
        resultView = findViewById(R.id.numResult);
        setEquation();
        setXk2Pos(0);
//        setFocusable(true);
        xk2ConstraintSet.connect(R.id.xk2,ConstraintSet.LEFT,R.id.thirdPay,ConstraintSet.LEFT);
        xk2ConstraintSet.connect(R.id.xk2,ConstraintSet.RIGHT,R.id.thirdPay,ConstraintSet.RIGHT);
        xk2ConstraintSet.connect(R.id.xk2,ConstraintSet.TOP,R.id.thirdPay,ConstraintSet.TOP);
        xk2ConstraintSet.connect(R.id.xk2,ConstraintSet.BOTTOM,R.id.thirdPay,ConstraintSet.BOTTOM);

        xk3ConstraintSet.connect(R.id.xk3,ConstraintSet.LEFT,R.id.thirdPay,ConstraintSet.LEFT);
        xk3ConstraintSet.connect(R.id.xk3,ConstraintSet.RIGHT,R.id.thirdPay,ConstraintSet.RIGHT);
        xk3ConstraintSet.connect(R.id.xk3,ConstraintSet.TOP,R.id.thirdPay,ConstraintSet.TOP);
        xk3ConstraintSet.connect(R.id.xk3,ConstraintSet.BOTTOM,R.id.thirdPay,ConstraintSet.BOTTOM);
//        setOnKeyListener(new OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (event.getAction() == KeyEvent.ACTION_DOWN) {
//
//                }
//                return true;
//            }
//        });
    }

    public PayConfirmView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public void setEquation(){
        int symbolId = Math.abs((int)(Math.random()*10))%2;
        int num1 = 0;
        int num2 = 0;
        String symbolStr = "";
        if(symbolId==0){
            symbolStr = "+";
        }else{
            symbolStr = "-";
        }
        if(symbolId==1){
            num1 = Math.abs((int)(4+Math.random()*6));
            num2 = Math.abs((int)(Math.random()*num1));
            result = num1-num2;
        }else{
            num1 = Math.abs((int)(Math.random()*10));
            num2 = Math.abs((int)(Math.random()*10));
            result = num1+num2;
        }
        equationView.setEquation(num1+"",num2+"",symbolStr);
    }

    public void addNumView(int num){
        resultView.addNum(num);
//        int numId = getNumId(num);
//        ImageView imageView = new ImageView(this.getContext());
//        imageView.setId(View.generateViewId());
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),numId);
//        if(PayityUnity.scaleX!=1||PayityUnity.scaleY!=1){
//            Matrix matrix = new Matrix();
//            matrix.postScale(PayityUnity.scaleX,PayityUnity.scaleY);
//            bitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
//        }
//        imageView.setImageBitmap(bitmap);
    }

    public void removeNum(){
        resultView.removeNum();
//        ImageView imageView = numViewList.get(numViewList.size()-1);
//        linearLayout.removeView(imageView);
//        numViewList.remove(imageView);
//        resultBuf.delete(resultBuf.length()-1,resultBuf.length());
//        imageView = null;
    }

    public static int getNumId(int num){
        switch(num){
        case 0:
            return R.drawable.num0;
        case 1:
            return R.drawable.num1;
        case 2:
            return R.drawable.num2;
        case 3:
            return R.drawable.num3;
        case 4:
            return R.drawable.num4;
        case 5:
            return R.drawable.num5;
        case 6:
            return R.drawable.num6;
        case 7:
            return R.drawable.num7;
        case 8:
            return R.drawable.num8;
        case 9:
            return R.drawable.num9;
        }
        return R.drawable.num0;
    }

    public void doLeft(){
        if(groupIndex==0){
            if(numIndex==0 || numIndex==5){
                setXk2Pos(numIndex+4);
            }else{
                setXk2Pos(numIndex-1);
            }
        }else if(groupIndex==1){
            xk1.setVisibility(View.INVISIBLE);
            xk2.setVisibility(View.VISIBLE);
            groupIndex = 0;
        }else if(groupIndex==2){
            if(butIndex==0){
                setXK3Pos(1);
            }else{
                setXK3Pos(0);
            }
        }
        equationView.setDraw(false);
    }

    public void doRight(){
        if(groupIndex==0){
            if(numIndex==9 || numIndex==4){
                xk1.setVisibility(View.VISIBLE);
                xk2.setVisibility(View.INVISIBLE);
                groupIndex = 1;
            }else{
                setXk2Pos(numIndex+1);
            }
            System.out.println("numIndex="+numIndex);
        }else if(groupIndex==1){
//            xk1.setVisibility(View.INVISIBLE);
//            xk2.setVisibility(View.VISIBLE);
//            groupIndex = 0;
        }else if(groupIndex==2){
            if(butIndex==0){
                setXK3Pos(1);
            }else{
                setXK3Pos(0);
            }
        }
        equationView.setDraw(false);
    }

    public void doUp(){
        if(groupIndex==0){
            if(numIndex/5==1){
                setXk2Pos(numIndex-5);
            }
        }else if(groupIndex==2){
            groupIndex = 0;
            xk3.setVisibility(INVISIBLE);
            xk2.setVisibility(VISIBLE);
        }
        equationView.setDraw(false);
    }

    public void doDown(){
        if(groupIndex==0){
            if(numIndex/5==0){
                setXk2Pos(numIndex+5);
            }else{
                groupIndex = 2;
                xk3.setVisibility(VISIBLE);
                xk2.setVisibility(INVISIBLE);
            }
        }else if(groupIndex==1){
            xk1.setVisibility(INVISIBLE);
            xk3.setVisibility(VISIBLE);
            groupIndex = 2;
        }
        equationView.setDraw(false);
    }

    public void doFire(){
        if(groupIndex==0){
            if(resultView.getNumBuf().length()<2) {
                if(numIndex==9){
                    addNumView(0);
                }else {
                    addNumView(numIndex + 1);
                }
            }
        }else if(groupIndex==1){
            removeNum();
        }else{
            if(butIndex==0){
                int bufResult = -1;
                if(resultView.getNumBuf().length()==0){
                    showTips("请输入验证码");
                }else {
                    System.out.println("输入的数字为:"+resultView.getNumBuf()+",本身的数字为"+getResult());
                    bufResult = Integer.parseInt(resultView.getNumBuf());
                    if(bufResult!=getResult()){
                        showTips("请输入正确的验证码");
                    }else{
                        //下一步
                        thirdPayActivity.getPaySdkForMG().doPay();
                    }
                }
            }else{
                doBack();
            }
        }
        equationView.setDraw(false);
    }

    public void doBack(){
        //返回
        thirdPayActivity.getPaySdkForMG().back();
    }

    private void showTips(String str){


        //通过Toast类的构造方法创建消息提示框
        Toast toast=new Toast(getContext());
        toast.setDuration(Toast.LENGTH_SHORT);//设置持续时间
        toast.setGravity(Gravity.CENTER,67, -42);//设置对齐方式
        LinearLayout ll=new LinearLayout(getContext());//创建一个线性布局管理器
        TextView tv=new TextView(getContext());
        tv.setText(str);
        tv.setBackgroundColor(0xFFFFFF);
        ll.addView(tv);
        toast.setView(ll);//设置消息提示框中要显示的视图
        toast.show();//显示消息提示框
    }


    public int getResult(){
        return result;
    }

    public void setXk2Pos(int numIndex){
        int count = this.getChildCount();
        xk2ConstraintSet.clone(constraintLayout);
        this.numIndex = numIndex;
        xk2ConstraintSet.setHorizontalBias(R.id.xk2,numPos[numIndex][0]);
        xk2ConstraintSet.setVerticalBias(R.id.xk2,numPos[numIndex][1]);
        xk2ConstraintSet.applyTo(constraintLayout);
        equationView.setDraw(false);
    }

    public void setXK3Pos(int butIndex){
        xk3ConstraintSet.clone(constraintLayout);
        this.butIndex = butIndex;
        xk3ConstraintSet.setHorizontalBias(R.id.xk3,anniuPos[butIndex][0]);
        xk3ConstraintSet.setVerticalBias(R.id.xk3,anniuPos[butIndex][1]);
        xk3ConstraintSet.applyTo(constraintLayout);
        equationView.setDraw(false);
    }

    public void setThirdPayActivity(ThirdPayActivity thirdPayActivity){
        this.thirdPayActivity = thirdPayActivity;
    }


//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        switch(keyCode){
//        case KeyEvent.KEYCODE_DPAD_UP:
//                doUp();
//                return false;
//            case KeyEvent.KEYCODE_DPAD_DOWN:
//                doDown();
//                return true;
//            case KeyEvent.KEYCODE_DPAD_LEFT:
//                doLeft();
//                return true;
//            case KeyEvent.KEYCODE_DPAD_RIGHT:
//                doRight();
//                return true;
//            case KeyEvent.KEYCODE_ENTER:
//            case KeyEvent.KEYCODE_DPAD_CENTER:
//                doFire();
//                return true;
//            case KeyEvent.KEYCODE_BACK:
//                doBack();
//                return true;
//        }
//        return super.onKeyDown(keyCode,event);
//    }

    public boolean doPressed(int keyCode,KeyEvent event){
        switch(keyCode){
            case KeyEvent.KEYCODE_DPAD_UP:
                doUp();
                return false;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                doDown();
                return true;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                doLeft();
                return true;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                doRight();
                return true;
            case KeyEvent.KEYCODE_ENTER:
            case KeyEvent.KEYCODE_DPAD_CENTER:
                doFire();
                return true;
            case KeyEvent.KEYCODE_BACK:
                doBack();
                return true;
        }
        return true;
    }
}
