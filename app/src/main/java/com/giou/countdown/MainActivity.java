package com.giou.countdown;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTvOtp = (TextView) findViewById(R.id.tv_otp);
        mCountDownIndicator = (CountDownIndicator) findViewById(R.id.countdown);

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
            String otpCode = mPasscodeGenerator.generateTimeoutCode(isCreated, timeOffset);
            mTvOtp.setText(otpCode);
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

}
