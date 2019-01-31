package com.chat.sm.ct.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import com.squareup.leakcanary.LeakCanary;

/**
 * 2019 重庆指讯科技股份有限公司
 *
 * @author: Wsm
 * @date: 2019/1/30 13:33
 * @description:
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initLeakCanary();
//        /xxxxxxxxxx
    }
    /**
     * 实例化内存泄露工具
     */
    private void initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            //You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


}
