package com.xiemarc.marcreading.recycleview.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * 描述：默认代理事件
 * 作者：Marc on 2016/11/23 16:47
 * 邮箱：aliali_ha@yeah.net
 */
public class DefaultEventDelegate implements EventDelegate {
    private RecyclerArrayAdapter adapter;
    private OnLoadMoreListener onLoadMoreListener;
    private EventFooter footer;
    private boolean hasData = false;
    private boolean isLoadingMore = false;

    private boolean hasMore = false;
    private boolean hasNoMore = false;
    private boolean hasError = false;

    private int status = STATUS_INITIAL;
    private static final int STATUS_INITIAL = 291;
    private static final int STATUS_MORE = 260;
    private static final int STATUS_NOMORE = 408;
    private static final int STATUS_ERROR = 732;

    public DefaultEventDelegate(RecyclerArrayAdapter adapter) {
        this.adapter = adapter;
        footer = new EventFooter();
        adapter.addFooter(footer);
    }

    public void onMoreViewShowed() {
        if (!isLoadingMore && onLoadMoreListener != null) {
            isLoadingMore = true;
            onLoadMoreListener.onLoadMore();
        }
    }

    public void onErrorViewShowed() {
        resumeLoadMore();
    }

    //-------------------5个状态触发事件开始-------------------//
    @Override
    public void addData(int length) {
        if (hasMore) {
            if (length == 0) {
                //当添加0个时，认为已结束加载到底
                if (status == STATUS_INITIAL || status == STATUS_MORE) {
                    footer.showNoMore();
                }
            } else {
                //当Error或初始时。添加数据，如果有More则还原。
                if (hasMore && (status == STATUS_INITIAL || status == STATUS_ERROR)) {
                    footer.showMore();
                }
                hasData = true;
            }
        }else {
            if (hasNoMore){
                //隐藏more
                footer.showNoMore();
                status = STATUS_NOMORE;
            }
        }
        isLoadingMore = false;
    }

    @Override
    public void clear() {
        hasData = false;
        status = STATUS_INITIAL;
        footer.hide();
        isLoadingMore = false;
    }

    @Override
    public void stopLoadMore() {
        footer.showNoMore();
        status = STATUS_NOMORE;
        isLoadingMore = false;
    }

    @Override
    public void pauseLoadMore() {
        footer.showError();
        status = STATUS_ERROR;
        isLoadingMore = false;
    }

    @Override
    public void resumeLoadMore() {
        isLoadingMore = false;
        footer.showMore();
        onMoreViewShowed();
    }
    //-------------------5个状态触发事件结束-------------------//

    //-------------------3种View设置开始-------------------//

    /**
     * 设置more视图，并且添加监听
     * @param view
     * @param listener
     */
    @Override
    public void setMore(View view, OnLoadMoreListener listener) {
        this.footer.setMoreView(view);
        this.onLoadMoreListener = listener;
        hasMore = true;
    }

    /**
     * 设置 more视图隐藏
     * @param view
     */
    @Override
    public void setNoMore(View view) {
        this.footer.setNoMoreView(view);
        hasNoMore = true;
    }

    /**
     * 设置错误视图
     * @param view
     */
    @Override
    public void setErrorMore(View view) {
        this.footer.setErrorView(view);
        hasError = true;
    }

    private class EventFooter implements RecyclerArrayAdapter.ItemView {
        //主内容布局
        private FrameLayout container;
        //加载更多的view
        private View moreView;
        //不加载更多view
        private View noMoreView;
        //错误view
        private View errorView;
        private int flag = Hide;
        public static final int Hide = 0;
        public static final int ShowMore = 1;
        public static final int ShowError = 2;
        public static final int ShowNoMore = 3;

        public EventFooter() {
            container = new FrameLayout(adapter.getContext());
            container.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                    , ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        //创建视图
        @Override
        public View onCreateView(ViewGroup parent) {
            return container;
        }

        //绑定视图
        @Override
        public void onBindView(View headerView) {
            switch (flag) {
                case ShowMore:
                    onMoreViewShowed();
                    break;
                case ShowError:
                    onErrorViewShowed();
                    break;
            }
        }

        /**
         * 刷新状态
         */
        public void refreshStatus() {
            if (container != null) {
                if (flag == Hide) {
                    container.setVisibility(View.GONE);
                    return;
                }
                if (container.getVisibility() != View.VISIBLE)
                    container.setVisibility(View.VISIBLE);
                View view = null;
                switch (flag) {
                    case ShowMore:
                        view = moreView;
                        break;
                    case ShowError:
                        view = errorView;
                        break;
                    case ShowNoMore:
                        view = noMoreView;
                        break;
                }
                if (view == null) {
                    hide();
                    return;
                }
                if (view.getParent() == null) container.addView(view);
                for (int i = 0; i < container.getChildCount(); i++) {
                    if (container.getChildAt(i) == view) view.setVisibility(View.VISIBLE);
                    else container.getChildAt(i).setVisibility(View.GONE);
                }
            }
        }

        public void showError() {
            flag = ShowError;
            refreshStatus();
        }

        public void showMore() {
            flag = ShowMore;
            refreshStatus();
        }

        public void showNoMore() {
            flag = ShowNoMore;
            refreshStatus();
        }

        //初始化
        public void hide() {
            flag = Hide;
            refreshStatus();
        }

        public void setMoreView(View moreView) {
            this.moreView = moreView;
        }

        public void setNoMoreView(View noMoreView) {
            this.noMoreView = noMoreView;
        }

        public void setErrorView(View errorView) {
            this.errorView = errorView;
        }
    }
}
