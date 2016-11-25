package com.xiemarc.marcreading.base;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.xiemarc.marcreading.R;
import com.xiemarc.marcreading.recycleview.EasyRecyclerView;
import com.xiemarc.marcreading.recycleview.adapter.OnLoadMoreListener;
import com.xiemarc.marcreading.recycleview.adapter.RecyclerArrayAdapter;
import com.xiemarc.marcreading.recycleview.swipe.OnRefreshListener;
import com.xiemarc.marcreading.utils.UIUtils;

import java.lang.reflect.Constructor;

import butterknife.Bind;


/**
 * 描述：使用recycleview的fragment
 * 作者：Marc on 2016/11/23 18:15
 * 邮箱：aliali_ha@yeah.net
 *
 * @param <V>  view
 * @param <T>  presenter
 * @param <T2> 数据类型
 */
public abstract class BaseRVFragment<V, T extends BasePresenter<V>, T2> extends BaseFragment<V, T> implements
        RecyclerArrayAdapter.OnItemClickListener, OnRefreshListener, OnLoadMoreListener {

    @Bind(R.id.recyclerview)
    protected EasyRecyclerView mRecyclerView;
    protected RecyclerArrayAdapter<T2> mAdapter;
    protected int start = 0;
    protected int limit = 20;

    /**
     * 初始化adapter
     *
     * @param refreshable  是否可以刷新
     * @param loadmoreable 加载更多是否可用
     */
    protected void initAdapter(boolean refreshable, boolean loadmoreable) {
        if (null != mRecyclerView) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(UIUtils.getContext()));
            mRecyclerView.setItemDecoration(ContextCompat.getColor(mContext, R.color.common_divider_narrow), 1, 0, 0);
            mRecyclerView.setAdapterWithProgress(mAdapter);
        }
        if (null != mAdapter) {
            mAdapter.setOnItemClickListener(this);
            mAdapter.setError(R.layout.common_net_error_view).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //重新加载
                    mAdapter.resumeMore();
                }
            });
            if (loadmoreable) {
                mAdapter.setMore(R.layout.common_more_view, this);
                mAdapter.setNoMore(R.layout.common_nomore_view);
            }
            if (refreshable && mRecyclerView != null) {
                mRecyclerView.setRefreshListener(this);
            }
        }
    }

    protected void initAdapter(Class<? extends RecyclerArrayAdapter<T2>> clazz, boolean refreshable, boolean loadmoreable) {
        mAdapter = (RecyclerArrayAdapter<T2>) createInstance(clazz);
        initAdapter(refreshable, loadmoreable);
    }

    public Object createInstance(Class<?> cls) {
        Object obj;
        try {
            Constructor c1 = cls.getDeclaredConstructor(new Class[]{Context.class});
            c1.setAccessible(true);
            obj = c1.newInstance(new Object[]{mContext});
        } catch (Exception e) {
            obj = null;
        }
        return obj;
    }

    @Override
    public void onLoadMore() {
    }


    @Override
    public void onRefresh() {
        mRecyclerView.setRefreshing(true);
    }

    protected void loaddingError() {
        if (mAdapter.getCount() < 1) { // 说明缓存也没有加载，那就显示errorview，如果有缓存，即使刷新失败也不显示error
            mAdapter.clear();
        }
        mAdapter.pauseMore();
        mRecyclerView.setRefreshing(false);
        mRecyclerView.showTipViewAndDelayClose("似乎没有网络哦");
    }
}
