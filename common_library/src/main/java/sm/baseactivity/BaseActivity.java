package sm.baseactivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gyf.barlibrary.ImmersionBar;
import com.gyf.barlibrary.OSUtils;
import com.noober.background.BackgroundLibrary;
import com.zhy.http.okhttp.OkHttpUtils;

import butterknife.ButterKnife;
import oom.wsm.com.common_library.R;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

/**
 * 重庆指讯科技股份有限公司
 *
 * @author: Wsm
 * @date: 2018/12/8 13:40
 * @description: BaseActivity
 */
public abstract class BaseActivity extends AppCompatActivity implements IActivity {

    /***封装toast对象**/
    private static Toast toast;
    /***获取TAG的activity名称**/
    protected final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        BackgroundLibrary.inject(this);
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //关键代码
        AppManager.getAppManager().addActivity(this);
        //设置布局
        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        setContentView(getLayoutId());
        ImmersionBar.with(this).statusBarDarkFont(true, 0.2f).navigationBarEnable(false).init();
        //AndroidVirtualKeyWorkaround.assistActivity(findViewById(android.R.id.content));
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            //强制横屏
            //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        ButterKnife.bind(this);
        GetPermission();//获取权限
        initView();//初始化view
    }



    public void BLog(String str) {
        Log.i("MyApplication:", str + "");
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            //获取触摸动作，如果ACTION_UP，计时开始。
            case MotionEvent.ACTION_DOWN:
                //触摸。取消计时，开始计时
//                BaseApplication.getSingleTime().startTimer();
                break;
            //否则其他动作计时取消
            default:

                break;
        }
        return super.dispatchTouchEvent(ev);

    }


    public void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示长toast
     *
     * @param msg
     */
//
    public void toastLong(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    public void readyGo(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    /**
     * 显示Fragment
     *
     * @param frameLayoutId frameLayout 的ID
     * @param showFragment  显示的Fragment
     */
    public void showFragment(int frameLayoutId, Fragment showFragment) {
        getSupportFragmentManager().beginTransaction().replace(frameLayoutId, showFragment).commitAllowingStateLoss();
    }

    /**
     * 显示Fragment
     *
     * @param frameLayoutId frameLayout 的ID
     * @param showFragment  显示的Fragment
     */
    public void showAddToBackStackFragment(int frameLayoutId, Fragment showFragment) {
        getSupportFragmentManager().beginTransaction().replace(frameLayoutId, showFragment).addToBackStack(null).commitAllowingStateLoss();
    }


    /**
     * 显示短toast
     *
     * @param msg
     */
    public void toastShort(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void GetPermission() {
        String[] array = getUsesPermission();
        if (Build.VERSION.SDK_INT >= 23 && array.length>0) {
            requestPermissions(array, 100);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] == PERMISSION_GRANTED && requestCode == 100) {
                //同意了权限
            } else {
                //拒绝了权限
            }
        }
    }


    /**
     * 获取manifests配置的权限
     *
     * @return
     */
    private String[] getUsesPermission() {
        try {
            PackageManager packageManager = this.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), PackageManager.GET_PERMISSIONS);
            String[] usesPermissionsArray = packageInfo.requestedPermissions;
            return usesPermissionsArray;
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }
    @Override
    public void onResume() {
        super.onResume();
        // 非必加
        // 如果你的app可以横竖屏切换，适配了华为emui3系列系统手机，并且navigationBarWithEMUI3Enable为true，
        // 请在onResume方法里添加这句代码（同时满足这三个条件才需要加上代码哦：1、横竖屏可以切换；2、华为emui3系列系统手机；3、navigationBarWithEMUI3Enable为true）
        // 否则请忽略
        if (OSUtils.isEMUI3_x()) {
            ImmersionBar.with(this).statusBarDarkFont(true, 0.2f).navigationBarEnable(false).init();
        }
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // 非必加
        // 如果你的app可以横竖屏切换，适配了4.4或者华为emui3.1系统手机，并且navigationBarWithKitkatEnable为true，
        // 请务必在onConfigurationChanged方法里添加如下代码（同时满足这三个条件才需要加上代码哦：1、横竖屏可以切换；2、android4.4或者华为emui3.1系统手机；3、navigationBarWithKitkatEnable为true）
        // 否则请忽略
        ImmersionBar.with(this).statusBarDarkFont(true, 0.2f).navigationBarEnable(false).init();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkHttpUtils.getInstance().cancelTag(this);
        Glide.get(this).clearMemory();
        Log.i("AAonDestroy", "销毁");
        ImmersionBar.with(this).destroy();
        //必须调用该方法，防止内存泄漏
//       AppManager.getAppManager().finishActivity();

    }

    /**
     * 屏蔽返回事件super.onBackPressed();
     * FragmentManager中如果存在Fragment,则返回上一个页面，不存在则结束当前Activity,并执行返回返回事件
     */
    @Override
    public void onBackPressed() {
        //应该增加一个,判断当前Activity名字,在某些页面禁止返回
        if (mOnBackCallBack != null) {
            mOnBackCallBack.mOnBackPressed();
        }
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            AppManager.getAppManager().finishActivity();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    public void setmOnBackCallBack(BaseActivity.mOnBackCallBack mOnBackCallBack) {
        this.mOnBackCallBack = mOnBackCallBack;
    }

    private mOnBackCallBack mOnBackCallBack;

    public interface mOnBackCallBack {
        void mOnBackPressed();
    }


    private void unbindDrawables(View view) {
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup && !(view instanceof AdapterView)) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }

    /**
     * 内存回收
     */
    public void Gc() {
        //取得当前jvm的运行时实例
        Runtime run = Runtime.getRuntime();
        run.gc();
    }

    /**
     * 重启App
     */
    public void appExit() {
        Intent intent = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(getBaseContext().getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        int nPid = android.os.Process.myPid();
        android.os.Process.killProcess(nPid);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
        }else if(keyCode == KeyEvent.KEYCODE_BACK){
//            if(getClass().getName().contains("MainActivity")){
//                return true;
//            }

        }else if(keyCode == KeyEvent.KEYCODE_MENU){
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}


