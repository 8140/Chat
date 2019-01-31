package sm.rv.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import oom.wsm.com.common_library.R;

/**
 * 2018 重庆指讯科技股份有限公司
 *
 * @author: Wsm
 * @date: 2018/12/4 16:05
 * @description: 封转的刷新+RecyclerView
 */
public class SRecyclerview extends LinearLayout implements BaseQuickAdapter.OnItemClickListener {
    Context context;

    public SRecyclerview(Context context) {
        super(context);
        this.context = context;
    }

    /**
     * 刷新控件
     */
    SmartRefreshLayout mSmartRefreshLayout;
    /**
     * RecyclerView
     */
    RecyclerView mRecyclerView;

    public SRecyclerview(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        View view = View.inflate(context, R.layout.s_recyclerview_layout, this);
        //刷新控件
        mSmartRefreshLayout = (SmartRefreshLayout) view.findViewById(R.id.rv_s_smartrefreshlayout);
        //RecyclerView
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_s_recyclerview);

        //默认没有阴影
        mRecyclerView.setOverScrollMode(OVER_SCROLL_NEVER);
        //设置RecyclerView默认布局管理器
        LinearLayoutManager lm = new LinearLayoutManager(context);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(lm);

        //配置刷新器
        setSmart();

    }


    /**
     * 初始化刷新控件配置
     */
    void setSmart() {
        mSmartRefreshLayout.setRefreshHeader(new MaterialHeader(context));
        mSmartRefreshLayout.setEnableRefresh(false);
        mSmartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (mCallBack != null) {
                    mCallBack.LoadMore(refreshLayout);
                    mCallBack.Refresh(refreshLayout);
                }
                if (mLoadCallBack != null) {
                    mLoadCallBack.LoadMore(refreshLayout);
                }
                if (mFreshCallBack != null) {
                    mFreshCallBack.Refresh(refreshLayout);
                }
                if (mLoad_ItemCallBack != null) {
                    mLoad_ItemCallBack.LoadMore(refreshLayout);
                }

            }
        });
    }

    /**
     * 设置RecyclerView 阴影默认
     * OVER_SCROLL_ALWAYS
     * OVER_SCROLL_IF_CONTENT_SCROLLS &&
     * OVER_SCROLL_NEVER)
     */
    public void mSetOverScrollMode(int mode) {
        mRecyclerView.setOverScrollMode(mode);
    }

    /**
     * 禁止刷新，加载更多
     */
    public void EnableSmartRefreshLayout() {
        mSmartRefreshLayout.setEnableLoadMore(false);
        mSmartRefreshLayout.setEnableRefresh(false);
    }

    /**
     * @param mode  是否启用加载更多
     * @param mode1 是否启用刷新
     */
    public void EnableSmartRefreshLayout(boolean mode, boolean mode1) {
        mSmartRefreshLayout.setEnableLoadMore(mode);
        mSmartRefreshLayout.setEnableRefresh(mode1);
    }

    /**
     * 设置是否刷新，加载更多
     */
    public void EnableSmartRefreshLayout(boolean mode) {
        mSmartRefreshLayout.setEnableLoadMore(mode);
        mSmartRefreshLayout.setEnableRefresh(mode);
    }

    /**
     * @param lm 布局管理器
     */
    public void setLayoutManager(LinearLayoutManager lm) {
        mRecyclerView.setLayoutManager(lm);
    }

    /**
     * 设置布局管理器
     *
     * @param glm 布局管理器
     */

    public void setLayoutManager(GridLayoutManager glm) {
        mRecyclerView.setLayoutManager(glm);
    }

    /**
     * 设置布局管理器
     *
     * @param count 行数
     */
    public void setLayoutManager(int count) {
        GridLayoutManager glm = new GridLayoutManager(context, count);
        mRecyclerView.setLayoutManager(glm);
    }

    BaseQuickAdapter adapter;

    /**
     * 设置Adapter
     *
     * @param adapter
     */
    public void setAdapter(BaseQuickAdapter adapter) {
        this.adapter = adapter;
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
    }

    /**
     * 刷新数据
     */
    public void notifyDataSetChanged() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 设置空布局
     *
     * @param v 布局
     */
    public void setEmptyView(View v) {
        adapter.setEmptyView(v);
    }

    /**
     * 设置空布局，引用布局ID
     *
     * @param ResId 布局id
     */
    public void setEmptyView(int ResId) {
        View v = (View) View.inflate(context, ResId, null);
        adapter.setEmptyView(v);
    }

    /**
     * 加载完成
     */
    public void finishLoadMore() {
        mSmartRefreshLayout.finishLoadMore();
    }

    /**
     * 刷新完成
     */
    public void finishRefresh() {
        mSmartRefreshLayout.finishRefresh();
    }

    /**
     * 是否启用加载更多
     */
    public void setEnableLoadMore(boolean enable) {
        mSmartRefreshLayout.setEnableLoadMore(enable);
    }

    /**
     * 是否启用刷新
     */
    public void setEnableRefresh(boolean enable) {
        mSmartRefreshLayout.setEnableRefresh(enable);
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;

    }

    /**
     * 是否禁止使用刷新
     *
     * @param enable false表示禁用
     */
    public void setEnableLoadFresh(boolean enable) {
        mSmartRefreshLayout.setEnableLoadMore(enable);
        mSmartRefreshLayout.setEnableRefresh(enable);
    }

    /**
     * @return 刷新控件
     */
    public SmartRefreshLayout getSmartRefreshLayout() {
        return mSmartRefreshLayout;
    }

    /**
     * RecyclerView Adapter的点击事件回调
     *
     * @param adapter
     * @param view
     * @param position
     */
    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (mLoad_ItemCallBack != null) {
            mLoad_ItemCallBack.OnItemClick(adapter, view, position);
        }
        if(mItemCallBack!=null){
            mItemCallBack.OnItemClick(adapter, view, position);
        }
    }




    private mCallBack mCallBack;
    /**
     * @param cb mCallBack接口
     */
    public void mSetCallback(mCallBack cb) {
        this.mCallBack = cb;
    }


    private mLoadCallBack mLoadCallBack;
    /**
     * @param cb mLoadCallBack接口
     */
    public void mSetCallback(mLoadCallBack cb) {
        this.mLoadCallBack = cb;
    }



    private mFreshCallBack mFreshCallBack;
    /**
     * @param cb mFreshCallBack接口
     */
    public void mSetCallback(mFreshCallBack cb) {
        mFreshCallBack = cb;
    }

    private mItemCallBack mItemCallBack;
    /**
     * @param mItemCallBack mItemCallBack接口
     */
    public void mSetCallback(SRecyclerview.mItemCallBack mItemCallBack) {
        this.mItemCallBack = mItemCallBack;
    }


    private mLoad_ItemCallBack mLoad_ItemCallBack;
    /**
     * @param cb mLoad_ItemCallBack接口
     */
    public void mSetCallback(mLoad_ItemCallBack cb) {
        mLoad_ItemCallBack = cb;
    }

    /**
     * 加载
     */
    public interface mLoadCallBack {
        void LoadMore(RefreshLayout refreshLayout);
    }


    /**
     * 加载，Adapter点击
     */
    public interface mLoad_ItemCallBack {
        void LoadMore(RefreshLayout refreshLayout);

        void OnItemClick(BaseQuickAdapter adapter, View view, int position);
    }

    /**
     * 刷新
     */
    public interface mFreshCallBack {
        void Refresh(RefreshLayout refreshLayout);
    }



    /**
     *  加载更多，刷新
     */
    public interface mCallBack {
        void LoadMore(RefreshLayout refreshLayout);

        void Refresh(RefreshLayout refreshLayout);
    }

    /**
     * Adapter Item点击
     */
    public interface mItemCallBack {
        void OnItemClick(BaseQuickAdapter adapter, View view, int position);
    }
}




