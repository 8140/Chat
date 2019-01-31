package sm.baseactivity;

import android.os.CountDownTimer;
import android.util.Log;

/**
 * 2019 重庆指讯科技股份有限公司
 *
 * @author: Wsm
 * @date: 2019/1/23 17:58
 * @description:
 */
public class SingleTon extends CountDownTimer {

    String dataStr="MainActivity";

    public SingleTon(long millisInFuture, long countDownInterval, String dataStr) {
        super(millisInFuture, countDownInterval);
        this.dataStr=dataStr;
    }
    public SingleTon(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    @Override
    public void onTick(long millisUntilFinished) {
    }

    @Override
    public void onFinish() {
        //判断
        String name=AppManager.getAppManager().currentActivity().getClass().getName();
        if(!name.contains(dataStr)){
            AppManager.getAppManager().finishNotActivity(dataStr);
        }


    }

    public void startTimer(){
        cancel();
        start();
    }
    public void cancelTimer(){
        cancel();
    }

}
