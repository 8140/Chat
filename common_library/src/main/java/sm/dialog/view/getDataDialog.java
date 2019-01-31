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
public class getDataDialog {

    private SweetAlertDialog pDialog;
    private int autodistime = 1000;
    private WeakReference<Context> wr;

    /**
     * 进入的时候自动显示，不需要手动调用show
     */
    public getDataDialog(Context context) {
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
        pDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                //这里触发位置应该是用户主动点击取消
                pDialog.getProgressHelper().stopSpinning();
                handler.removeMessages(1);
                dialog.cancel();
            }
        });

        pDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                //取消进度条
                pDialog.cancel();
                //用户点击了取消
                if (DialogOnClick != null) {
                    DialogOnClick.cancelButton();
                }

            }
        });
        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                pDialog.cancel();
                //用户点击了重试
                if (DialogOnClick != null) {
                    DialogOnClick.confirmButton();
                }
                if (DialogConfirmOnClick != null) {
                    DialogConfirmOnClick.confirmButton();
                }

            }
        });


    }

    /**
     * 数据加载成功
     * 默认为 autodistime毫秒 消失对话框
     */
    public void loadSuccess() {
        //设置弹出框样式为成功Type
        pDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
        pDialog.setTitleText("提交成功");
        //成功不显示取消按钮
        pDialog.showCancelButton(false);
        //成功不显示确定按钮
        pDialog.showConfirmButton(false);
        handler.sendEmptyMessageDelayed(1, autodistime);
    }



    /**
     * 数据加载成功
     * 默认为 autodistime毫秒 设置加载成功的提示内容 消失对话框
     * @param content 提示内容
     * @param distime 消失时间
     */
    public void loadSuccess(String content,int distime) {
        //设置弹出框样式为成功Type
        pDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
        pDialog.setTitleText("提交成功");
        pDialog.setContentText(content);
        //成功不显示取消按钮
        pDialog.showCancelButton(false);
        //成功不显示确定按钮
        pDialog.showConfirmButton(false);
        handler.sendEmptyMessageDelayed(1, autodistime);
    }

    /**
     * 数据加载成功
     * 默认为 autodistime毫秒 设置加载成功的提示内容 消失对话框
     * @param content 提示内容
     */
    public void loadSuccess(String content) {
        //设置弹出框样式为成功Type
        pDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
        pDialog.setTitleText("提交成功");
        pDialog.setContentText(content);
        //成功不显示取消按钮
        pDialog.showCancelButton(false);
        //成功不显示确定按钮
        pDialog.showConfirmButton(false);
        handler.sendEmptyMessageDelayed(1, autodistime);
    }

    /**
     * 数据加载成功 消失时间需要手动传入->毫秒
     *
     * @param distime 消失时间
     */
    public void loadSuccess(int distime) {
        //设置弹出框样式为成功Type
        pDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
        pDialog.setTitleText("提交成功...");
        //成功不显示取消按钮
        pDialog.showCancelButton(false);
        //成功不显示确定按钮
        pDialog.showConfirmButton(false);
        handler.sendEmptyMessageDelayed(1, distime);
    }

    /**
     * 数据加载失败
     */
    public void loadFailed() {
        //设置弹出框样式为成功Type
        pDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
        pDialog.setTitleText("提交失败");
        //成功不显示取消按钮
        pDialog.showCancelButton(true);
        //成功不显示确定按钮
        pDialog.showConfirmButton(true);

        pDialog.setCancelText("取消");
        //确定文字
        pDialog.setConfirmText("重试");
        //handler.sendEmptyMessageDelayed(1,5000);
    }

    /**
     * 数据加载失败
     */
    public void loadFailed(String content) {
        //设置弹出框样式为成功Type
        pDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
        pDialog.setTitleText("提交失败");
        pDialog.setContentText(content);
        //成功不显示取消按钮
        pDialog.showCancelButton(true);
        //成功不显示确定按钮
        pDialog.showConfirmButton(true);
        //设置文字会显示按钮
        pDialog.setCancelText("取消");
        //确定文字
        pDialog.setConfirmText("重试");
        //handler.sendEmptyMessageDelayed(1,5000);
    }
    /**
     * 数据加载失败 隐藏重试按钮 -> 暂时适用于 单一登录结果，不适用重试的
     */
    public void loadFailed(String content,boolean onlymode) {
        //设置弹出框样式为成功Type
        pDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
        pDialog.setTitleText("提交失败");
        pDialog.setContentText(content);


        pDialog.setCancelText("取消");
        //确定文字
        pDialog.setConfirmText("重试");
        //成功不显示取消按钮
        pDialog.showCancelButton(true);
        //成功不显示确定按钮
        pDialog.showConfirmButton(onlymode);
        //handler.sendEmptyMessageDelayed(1,5000);
    }

    /**
     * 设置弹出了 提交失败的消失时间
     *
     * @param distime 消失时间
     */
    public void loadFailed(int distime) {
        //设置弹出框样式为成功Type
        pDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
        pDialog.setTitleText("提交失败...");
        //成功不显示取消按钮
        pDialog.showCancelButton(true);
        //成功不显示确定按钮
        pDialog.showConfirmButton(true);

        pDialog.setCancelText("取消");
        //确定文字
        pDialog.setConfirmText("重试");
        //handler.sendEmptyMessageDelayed(1,5000);
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
