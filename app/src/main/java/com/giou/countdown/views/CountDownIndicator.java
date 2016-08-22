package com.giou.countdown.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.nfc.Tag;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Description:
 * Author:zhangmengmeng
 * Date:16/8/21
 * Time:下午11:24
 */
public class CountDownIndicator extends View {

    private final String TAG = CountDownIndicator.class.getSimpleName();
    private Paint mPaint;
    private Context mContext;
    private double phase = 1;//百分比

    public CountDownIndicator(Context context) {
        this(context,null);
    }

    public CountDownIndicator(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CountDownIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        this.mContext = context;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        RadialGradient radialGradient = new RadialGradient(112,112,75, Color.argb(255,143,201,233),
                Color.argb(255,166,212,235), Shader.TileMode.MIRROR);
        mPaint.setShader(radialGradient);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //距离 上下左右的距离
        RectF localRect = new RectF(0,0,getWidth(),getHeight());
        //画扇形 f2 起始角度 f1 扫描角度 水平的角度是0,Y轴上是270
        float f1 = (float) (phase * 360);
//        float f2 = f1;
        float f2 = 270-f1;
        canvas.drawArc(localRect,f2,f1,true,mPaint);
    }

    public void setPhase(double phase){
        if(phase < 0 || phase >1){
            Log.d(TAG,"");
        }
        this.phase = phase;
        invalidate();//重新执行onDraw方法,重新绘制图形
    }
}
