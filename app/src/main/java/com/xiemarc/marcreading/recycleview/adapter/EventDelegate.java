package com.xiemarc.marcreading.recycleview.adapter;

import android.view.View;

/**
 * 描述：事件代理
 * 作者：Marc on 2016/11/23 16:47
 * 邮箱：aliali_ha@yeah.net
 */
public interface EventDelegate {
    void addData(int length);
    void clear();

    void stopLoadMore();
    void pauseLoadMore();
    void resumeLoadMore();

    void setMore(View view, OnLoadMoreListener listener);
    void setNoMore(View view);
    void setErrorMore(View view);
}
