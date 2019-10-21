package com.module.swpay.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.Image;
import android.util.AttributeSet;
import android.view.View;

import com.module.swpay.PayityUnity;

import java.util.ArrayList;

/**
 * Created by wanglu on 2019/10/11.
 */
public class ResultView extends View {
    public ArrayList<Bitmap> bitmapList = new ArrayList<>();
    private StringBuffer strBuf= new StringBuffer();

    private Paint paint = null;

    private int paintW;

    private int beginX,pw;

    public ResultView(Context context) {
        super(context);
    }

    public ResultView(Context context, AttributeSet atts){
        super(context,atts);
    }

    public void addNum(int num){
        int numId = PayConfirmView.getNumId(num);
        Bitmap numBit = BitmapFactory.decodeResource(getResources(),numId);
        if(PayityUnity.scaleX!=1||PayityUnity.scaleY!=1){
            Matrix matrix = new Matrix();
            matrix.postScale(PayityUnity.scaleX,PayityUnity.scaleY);
            numBit = Bitmap.createBitmap(numBit,0,0,numBit.getWidth(),numBit.getHeight(),matrix,true);

        }
        System.out.println("数字图片的宽度为  bitW="+numBit.getWidth()+",pw="+getWidth());
        bitmapList.add(numBit);
        strBuf.append(num);

        paintW = (numBit.getWidth()-10)*bitmapList.size();
        beginX = getWidth()/2-paintW/2;
        invalidate();
    }

    public void removeNum(){
        if(strBuf.length()>0) {
            strBuf.delete(strBuf.length() - 1, strBuf.length());
            Bitmap bitmap = bitmapList.get(bitmapList.size()-1);
            bitmapList.remove(bitmap);
            paintW = (bitmap.getWidth()-10)*bitmapList.size();
            beginX = getWidth()/2-paintW/2;
            bitmap = null;
            invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int index = 0;
        paint = new Paint();
        for(Bitmap numBit:bitmapList){
            canvas.drawBitmap(numBit,beginX+(numBit.getWidth()-10)*index,0,paint);
            index ++;
        }
        super.onDraw(canvas);
    }

    public String getNumBuf(){
        return strBuf.toString();
    }
}
