package com.giou.countdown.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Description:
 * Author:zhangmengmeng
 * Date:16/8/21
 * Time:下午11:01
 */
public class RingView extends View {

    private final String TAG = RingView.class.getSimpleName();
    private Paint mPaint;
    private Context mContext;

    public RingView(Context context) {
        super(context);
        mContext = context;
        initView();
    }


    public RingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();

    }

    public RingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();

    }

    private void initView() {
        mPaint = new Paint();
    }


    /**
     * 绘制
     * @param canvas 画布
     */
    @Override
    protected void onDraw(Canvas canvas) {

        int center = getWidth()/2;
        float innerCircle = dip2px(mContext,83);//内圆半径
        float outerCircle = dip2px(mContext,5);//外圆半径


        super.onDraw(canvas);
        mPaint.setAntiAlias(true);//没有锯齿 默认是false 是有锯齿的
        mPaint.setStyle(Paint.Style.STROKE);//画空心
        //绘制内圆
        mPaint.setARGB(155,167,190,206);
        mPaint.setStrokeWidth(2);//设置线条宽度
        // x y r 画笔
        canvas.drawCircle(center,center,innerCircle,mPaint);

        //绘制圆环
        mPaint.setARGB(255,212,225,233);
        mPaint.setStrokeWidth(3);//中间圆环宽度
        canvas.drawCircle(center,center,innerCircle+1+outerCircle/2,mPaint);


        //绘制外圆
        mPaint.setARGB(155,167,190,206);
        mPaint.setStrokeWidth(2);
        canvas.drawCircle(center,center,innerCircle+outerCircle,mPaint);

    }

    public int dip2px(Context context,float dpValue){

        final  float scale = context.getResources().getDisplayMetrics().density;
        Log.d(TAG,"手机密度:"+scale);
        return (int)(dpValue*scale+0.5f);

    }
}
