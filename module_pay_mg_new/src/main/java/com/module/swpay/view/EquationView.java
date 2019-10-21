package com.module.swpay.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.module.swpay.PayityUnity;
import com.swkj.module_pay_mg_new.R;

/**
 * Created by wanglu on 2019/10/9.
 */
public class EquationView extends View {
    private Bitmap number1Bit;
    private Bitmap number2Bit;
    private Bitmap symbolBit;
    private Bitmap equlesBit;
    public String equation;
    private boolean isDraw;
    private boolean isInit;
    private Paint paint;

    private float numW,numH,desW;
    public EquationView(Context context) {
        super(context);
    }

    public EquationView(Context context, AttributeSet attrs){
        super(context,attrs);
    }

    public void setEquation(String equation){
        this.equation = equation.trim();
        int symbolIndex = -1;
        if(equation.contains("+")){
            symbolIndex = this.equation.indexOf("+");
        }else if(equation.contains("-")){
            symbolIndex = this.equation.indexOf("-");
        }
        String num1 = this.equation.substring(0,symbolIndex);
        String num2 = this.equation.substring(symbolIndex+1,equation.length());
        String symbol = this.equation.substring(symbolIndex,symbolIndex+1);
        setEquation(num1,num2,symbol);
    }

    //数字为0-9的数字
    public void setEquation(String s_num1,String s_num2,String symbol){
        if(!symbol.equals("-")&&!(symbol.equals("+"))){
            return;
        }
        if(symbol.equals("-")){
            symbolBit = BitmapFactory.decodeResource(getResources(), R.drawable.jian);
        }else if(symbol.equals("+")){
            symbolBit = BitmapFactory.decodeResource(getResources(), R.drawable.jia);
        }
        equlesBit = BitmapFactory.decodeResource(getResources(),R.drawable.deng);
        int num1 = Integer.parseInt(s_num1);
        int num2 = Integer.parseInt(s_num2);
        int bit1Id = getNumBitId(num1);
        int bit2Id = getNumBitId(num2);
        number1Bit = BitmapFactory.decodeResource(getResources(), bit1Id);
        number2Bit = BitmapFactory.decodeResource(getResources(),bit2Id);

        symbolBit = createBitMap(symbolBit);
        equlesBit = createBitMap(equlesBit);
        number1Bit = createBitMap(number1Bit);
        number2Bit = createBitMap(number2Bit);
        numW = number1Bit.getWidth()*PayityUnity.scaleX;
        numH = number1Bit.getHeight()*PayityUnity.scaleY;
        desW = 22*PayityUnity.scaleX;
        isInit = true;
    }


    private int getNumBitId(int num){
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

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(isInit && !isDraw){
            paint = new Paint();
            canvas.drawBitmap(number1Bit,0,0,paint);
            canvas.drawBitmap(symbolBit,numW+desW,0,paint);
            canvas.drawBitmap(number2Bit,(numW+desW)*2,0,paint);
            canvas.drawBitmap(equlesBit,(numW+desW)*3,0,paint);
            isDraw = true;
        }
    }

    private Bitmap createBitMap(Bitmap localBit){
        Bitmap bitmap = null;
        if(PayityUnity.scaleY!=1 || PayityUnity.scaleX!=1){
            Matrix matrix = new Matrix();
            matrix.postScale(PayityUnity.scaleX,PayityUnity.scaleY);
            bitmap = Bitmap.createBitmap(localBit,0,0,localBit.getWidth(),localBit.getHeight(),matrix,true);
        }else{
            bitmap = localBit;
        }
        return bitmap;
    }

    public void setDraw(boolean isDraw){
        this.isDraw = isDraw;
    }
}
