package sm.webview;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

/**
 * Created by System on 2018/6/26.
 */

public class CWebView extends WebView {
    private static Field sConfigCallback;
    static {
        try {
            sConfigCallback = Class.forName("android.webkit.BrowserFrame").getDeclaredField("sConfigCallback");
            sConfigCallback.setAccessible(true);
        } catch (Exception e) {
            // ignored
        }

    }

    public CWebView(Context context, AttributeSet attrs) {
        super(context.getApplicationContext(), attrs);
        initContext(context);
    }

    public CWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context.getApplicationContext(), attrs, defStyle);
        initContext(context);
    }

    @Override
    public void destroy() {
        super.destroy();
        try {
            if( sConfigCallback!=null ){
                sConfigCallback.set(null, null);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected static class MyWebViewClient extends WebViewClient {
        protected WeakReference<Activity> activityRef;

        public MyWebViewClient( Activity activity ) {
            this.activityRef = new WeakReference<Activity>(activity);
        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            try {
                final Activity activity = activityRef.get();
                if(activity!=null ){
                    if (url.indexOf("hello.html") != -1 && url.indexOf("119.6.84.89:7001/scwssb/hello.html") != -1) {

                    } else {
                        view.loadUrl(url);
                    }
                }
            }catch( RuntimeException ignored ) {
                // ignore any url parsing exceptions
            }
            return true;
        }
    }

    public CWebView(Context context) {
        super(context);
        initContext(context);
    }

    @Override
    public void loadUrl(String url) {
        if (url.indexOf("http://") == -1 && url.indexOf("https://") == -1 && url.indexOf("file:///android_asset")==-1) {
            url = "http://" + url;
        }
        Log.i("url","WebView地址:" + url);
        super.loadUrl(url);
    }


    void initContext(Context context) {
        WebSettings settings = this.getSettings();
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setBuiltInZoomControls(true);
        settings.setSupportZoom(false);
        settings.setJavaScriptEnabled(true);
        settings.setDisplayZoomControls(false);
        setWebViewClient(new MyWebViewClient((Activity)context) );

        //屏蔽长按事件
        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });


        //获取网页标题
        setWebChromeClient(new WebChromeClient(){
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if(mCallBack!=null){
                    mCallBack.Back_Title(title);
                }

            }
        });

    }

    private CallBack mCallBack;
    public void mSetCallback(CallBack cb)
    {
        mCallBack=cb;
    }

    public interface CallBack {
        public void Back_Title(String title);
    }
}
