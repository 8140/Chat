package sm.baseactivity;

import android.app.Application;

/**
 * 2019 重庆指讯科技股份有限公司
 *
 * @author: Wsm
 * @date: 2019/1/23 18:16
 * @description:
 */
public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        getSingleTime();
    }
    static SingleTon singleTon;

    public static SingleTon getSingleTime(){
        if(singleTon==null){
            //五分钟，倒计时 1分钟一次
            singleTon=new SingleTon(60000*5, 60000*5);
//            singleTon=new SingleTon(6000, 6000);
            singleTon.startTimer();
        }
        return singleTon;
    }
}
