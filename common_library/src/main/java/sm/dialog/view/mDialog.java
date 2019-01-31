package sm.dialog.view;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;

import com.cazaea.sweetalert.SweetAlertDialog;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

/**
 * 2018 重庆指讯科技股份有限公司
 *
 * @author: Wsm
 * @date: 2018/12/15 13:39
 * @description:
 */
public class mDialog {

    private SweetAlertDialog pDialog;
    private int autodistime = 1000;
    private WeakReference<Context> wr;

    /**
     * 进入的时候自动显示，不需要手动调用show
     */
    public mDialog(Context context) {
        pDialog = null;
        wr = new WeakReference<>(context);
        pDialog = new SweetAlertDialog(wr.get(), SweetAlertDialog.PROGRESS_TYPE);
        //设置进度条颜色
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("正在提交中...");
        //设置是否可以返回
        pDialog.setCancelable(true);
        //取消文字 设置文字的时候有对控件设置显示，但是下面设置了对控件的隐藏没有效果
        //pDialog.setCancelText("取消");
        //确定文字
        //pDialog.setConfirmText("重试");

        //隐藏取消按钮
        pDialog.showCancelButton(false);
        //隐藏确定按钮
        pDialog.showConfirmButton(false);

        //是否可以触摸外部取消
        pDialog.setCanceledOnTouchOutside(true);

        pDialog.show();
        setContentScroll();
    }

    public void cancel() {
        if (pDialog != null) {
            pDialog.cancel();
        }
    }


    /**
     * 对Dialog 进行关闭
     */
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 1) {
                runnable.run();
            }
            return false;
        }
    });

    /**
     * 对Dialog 进行关闭
     */
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Context activity = wr.get();
            if (activity != null) {
                pDialog.dismissWithAnimation();
            }
        }
    };


    private DialogOnClick DialogOnClick;

    /**
     * 这里对原来的确定，取消点击事件接口进行了封转
     */
    public void DialogOnClick(DialogOnClick dialogOnClick) {
        this.DialogOnClick = dialogOnClick;
    }

    public interface DialogOnClick {
        void cancelButton();

        //        SweetAlertDialog sweetAlertDialog
        void confirmButton();
    }


    private DialogConfirmOnClick DialogConfirmOnClick;

    /**
     * 这里对原来的确定，取消点击事件接口进行了封转
     */
    public void DialogConfirmOnClick(DialogConfirmOnClick dialogOnClick) {
        this.DialogConfirmOnClick = dialogOnClick;
    }

    public interface DialogConfirmOnClick {
        void confirmButton();
    }


    /**
     * 修改dialog中提示内容的最大行数，以及设置TextView的滚动显示
     * TextView在onCreate中执行的，所以得在在对话框加载后去修改,避免TextView NPE异常
     */
    private void setContentScroll() {
        try {
            Field fieldcontent = pDialog.getClass().getDeclaredField("mContentTextView");
            //开放权限
            fieldcontent.setAccessible(true);
            //pDialog类实例
            TextView t = (TextView) fieldcontent.get(pDialog);
            t.setVerticalScrollBarEnabled(true);
            t.setMaxLines(6);
            t.setMovementMethod(ScrollingMovementMethod.getInstance());
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            Log.i(this.getClass().getSimpleName(), "修改Dialog最大行数失败" + e.getMessage());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            Log.i(this.getClass().getSimpleName(), "修改Dialog最大行数失败" + e.getMessage());

        }
    }

}
