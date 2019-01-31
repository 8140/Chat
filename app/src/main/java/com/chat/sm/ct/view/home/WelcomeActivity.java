package com.chat.sm.ct.view.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.chat.sm.ct.R;
import com.chat.sm.ct.utils.SharedPrefsUtil;
import sm.baseactivity.AppManager;
import sm.baseactivity.BaseActivity;

/**
 * 2019 重庆指讯科技股份有限公司
 *
 * @author: Wsm
 * @date: 2019/1/30 15:34
 * @description: 欢迎页
 */
public class WelcomeActivity extends BaseActivity {
    @Override
    public void initView() {
        delayIntent();
    }
    private Handler mHandler;
    void delayIntent(){
        mHandler=new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if ("".equals(SharedPrefsUtil.getString(WelcomeActivity.this, "Key"))) {
                    startActivity(new Intent().setClass(WelcomeActivity.this, LoginActivity.class));
                } else {
                    startActivity(new Intent().setClass(WelcomeActivity.this, MainActivity.class));
                }
                AppManager.getAppManager().finishActivity();
            }
        },1000);
    }


    @Override
    protected void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();

    }
    @Override
    public int getLayoutId() {
        return R.layout.activity_welcome_layout;
    }
}
