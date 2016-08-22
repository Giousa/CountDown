package com.giou.countdown;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.giou.countdown.utils.PasscodeGenerator;
import com.giou.countdown.views.CountDownIndicator;

import java.util.Timer;
import java.util.TimerTask;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {

    private TextView mTvOtp;
    private int REFRESH_INTERVAL_SEC = 30;//间隔时间,也是倒计时的总时长
    private int REFRESH_PROGRESS_INTERVAL_SEC = 100;//刷新时间
    private final int REFRESH_PROGRESS = 1;
    private final int UPDATE_PROGRESS = 2;
    public static final int UPDATE_COUNTDOWN = 3;//第一次确定倒计时从哪里开始的消息
    private String secretStr = "bdsfger435325";
    private boolean isCreated = true;
    private long timeOffset = 0;
    private CountDownIndicator mCountDownIndicator;
    private double mPhase;
    private double mPhaseStep;//执行次数
    private ImageView ivBit1;
    private ImageView ivBit2;
    private ImageView ivBit3;
    private ImageView ivBit4;
    private ImageView ivBit5;
    private ImageView ivBit6;

    // OTP图片资源
    private int[] OTPImages = { R.drawable.num_0, R.drawable.num_1,
            R.drawable.num_2, R.drawable.num_3, R.drawable.num_4,
            R.drawable.num_5, R.drawable.num_6, R.drawable.num_7,
            R.drawable.num_8, R.drawable.num_9 };

    Timer mTimer;

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case REFRESH_PROGRESS:
                    mCountDownIndicator.setPhase(1-mPhase);
                    break;

                case UPDATE_PROGRESS:
                    updateOtp();
                    break;

                case UPDATE_COUNTDOWN:
                    int i = msg.arg1;
                    mPhase = 1 - (double)i/REFRESH_INTERVAL_SEC;
                    mCountDownIndicator.setPhase(1-mPhase);
                    break;
            }
        }
    };
    private Mac mMac;
    private PasscodeGenerator mPasscodeGenerator;
    private String mOtpCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTvOtp = (TextView) findViewById(R.id.tv_otp);
        mCountDownIndicator = (CountDownIndicator) findViewById(R.id.countdown);
        ivBit1 = (ImageView) findViewById(R.id.iv_bit_1);
        ivBit2 = (ImageView) findViewById(R.id.iv_bit_2);
        ivBit3 = (ImageView) findViewById(R.id.iv_bit_3);
        ivBit4 = (ImageView) findViewById(R.id.iv_bit_4);
        ivBit5 = (ImageView) findViewById(R.id.iv_bit_5);
        ivBit6 = (ImageView) findViewById(R.id.iv_bit_6);

        mPhaseStep = REFRESH_PROGRESS_INTERVAL_SEC/(double)(REFRESH_INTERVAL_SEC * 1000);

        mTimer = new Timer();
        mTimer.schedule(new OtpTask(),0,REFRESH_PROGRESS_INTERVAL_SEC);

        updateOtp();

    }

    private void updateOtp(){
        try {
            if(mMac==null){
                mMac = Mac.getInstance("HMACSHA1");
                mMac.init(new SecretKeySpec(secretStr.getBytes(), ""));
            }

            if(mPasscodeGenerator == null){
                //6位动态密码
                mPasscodeGenerator = new PasscodeGenerator(mMac, 6, REFRESH_INTERVAL_SEC,mHandler);
            }
            mOtpCode = mPasscodeGenerator.generateTimeoutCode(isCreated, timeOffset);
            mTvOtp.setText(mOtpCode);
            startAnim();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class OtpTask extends TimerTask{

        @Override
        public void run() {
            //执行需要完成的方法
            mPhase += mPhaseStep;
            if(mPhase >= 1){
                mPhase = 0;
                //更新otp倒计时
                mHandler.sendEmptyMessage(UPDATE_PROGRESS);

            }
            //刷新倒计时
//            mCountDownIndicator.setPhase(mPhase);
            mHandler.sendEmptyMessage(REFRESH_PROGRESS);
        }
    }

    // 实现6位数字的动画效果
    private void startAnim() {
        // Logger.i("Test", "passcode---->"+passcode);
        int frameNumber = 9;
        int frameTime = 30;

        final AnimationDrawable ad1 = new AnimationDrawable();
        final AnimationDrawable ad2 = new AnimationDrawable();
        final AnimationDrawable ad3 = new AnimationDrawable();
        final AnimationDrawable ad4 = new AnimationDrawable();
        final AnimationDrawable ad5 = new AnimationDrawable();
        final AnimationDrawable ad6 = new AnimationDrawable();

        ad1.setOneShot(true);
        ad2.setOneShot(true);
        ad3.setOneShot(true);
        ad4.setOneShot(true);
        ad5.setOneShot(true);
        ad6.setOneShot(true);

        for (int i = 0; i < frameNumber; i++) {
            int random = (int) Math.round(Math.random() * 9);
            Drawable frame = getResources().getDrawable(OTPImages[random]);
            ad1.addFrame(frame, frameTime);
        }
        for (int i = 0; i < frameNumber; i++) {
            int random = (int) Math.round(Math.random() * 9);
            Drawable frame = getResources().getDrawable(OTPImages[random]);
            ad2.addFrame(frame, frameTime);
        }
        for (int i = 0; i < frameNumber; i++) {
            int random = (int) Math.round(Math.random() * 9);
            Drawable frame = getResources().getDrawable(OTPImages[random]);
            ad3.addFrame(frame, frameTime);
        }
        for (int i = 0; i < frameNumber; i++) {
            int random = (int) Math.round(Math.random() * 9);
            Drawable frame = getResources().getDrawable(OTPImages[random]);
            ad4.addFrame(frame, frameTime);
        }
        for (int i = 0; i < frameNumber; i++) {
            int random = (int) Math.round(Math.random() * 9);
            Drawable frame = getResources().getDrawable(OTPImages[random]);
            ad5.addFrame(frame, frameTime);
        }
        for (int i = 0; i < frameNumber; i++) {
            int random = (int) Math.round(Math.random() * 9);
            Drawable frame = getResources().getDrawable(OTPImages[random]);
            ad6.addFrame(frame, frameTime);
        }

        Drawable frame1 = getResources().getDrawable(
                OTPImages[Integer.parseInt(mOtpCode.charAt(0) + "")]);
        Drawable frame2 = getResources().getDrawable(
                OTPImages[Integer.parseInt(mOtpCode.charAt(1) + "")]);
        Drawable frame3 = getResources().getDrawable(
                OTPImages[Integer.parseInt(mOtpCode.charAt(2) + "")]);
        Drawable frame4 = getResources().getDrawable(
                OTPImages[Integer.parseInt(mOtpCode.charAt(3) + "")]);
        Drawable frame5 = getResources().getDrawable(
                OTPImages[Integer.parseInt(mOtpCode.charAt(4) + "")]);
        Drawable frame6 = getResources().getDrawable(
                OTPImages[Integer.parseInt(mOtpCode.charAt(5) + "")]);

        ad1.addFrame(frame1, frameTime);
        ad2.addFrame(frame2, frameTime);
        ad3.addFrame(frame3, frameTime);
        ad4.addFrame(frame4, frameTime);
        ad5.addFrame(frame5, frameTime);
        ad6.addFrame(frame6, frameTime);

        ivBit1.setBackgroundDrawable(ad1);
        ivBit2.setBackgroundDrawable(ad2);
        ivBit3.setBackgroundDrawable(ad3);
        ivBit4.setBackgroundDrawable(ad4);
        ivBit5.setBackgroundDrawable(ad5);
        ivBit6.setBackgroundDrawable(ad6);

        if (ad1.isRunning()) {
            ad1.stop();
        }
        if (ad2.isRunning()) {
            ad2.stop();
        }
        if (ad3.isRunning()) {
            ad3.stop();
        }
        if (ad4.isRunning()) {
            ad4.stop();
        }
        if (ad5.isRunning()) {
            ad5.stop();
        }
        if (ad6.isRunning()) {
            ad6.stop();
        }

        ivBit1.post(new Runnable() {

            @Override
            public void run() {
                ad1.start();
                ad2.start();
                ad3.start();
                ad4.start();
                ad5.start();
                ad6.start();
            }
        });
    }

}
