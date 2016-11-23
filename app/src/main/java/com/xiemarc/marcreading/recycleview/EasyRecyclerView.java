package com.xiemarc.marcreading.recycleview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorRes;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.xiemarc.marcreading.R;
import com.xiemarc.marcreading.recycleview.adapter.RecyclerArrayAdapter;
import com.xiemarc.marcreading.recycleview.decoration.DividerDecoration;
import com.xiemarc.marcreading.recycleview.swipe.OnRefreshListener;
import com.xiemarc.marcreading.recycleview.swipe.SwipeRefreshLayout;
import com.xiemarc.marcreading.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：加载视图布局的recycleview
 * 作者：Marc on 2016/11/23 17:09
 * 邮箱：aliali_ha@yeah.net
 */
public class EasyRecyclerView extends FrameLayout {
    private Context mContext;

    public static final String TAG = "EasyRecyclerView";
    public static boolean DEBUG = false;

    protected RecyclerView mRecycler;//主的recycleview
    protected TextView tipView;//提示的textview
    protected ViewGroup mProgressView;//显示进度的progress容器视图
    protected ViewGroup mEmptyView;//显示空视图的容器视图
    protected ViewGroup mErrorView;//显示错误视图的容器视图

    private int mProgressId;
    private int mEmptyId;
    private int mErrorId;

    protected boolean mClipToPadding;
    protected int mPadding;//左上右下的padding
    protected int mPaddingTop;//上padding
    protected int mPaddingBottom;//下padding
    protected int mPaddingLeft;//左padding
    protected int mPaddingRight;//右padding
    protected int mScrollbarStyle;//scorebar的样式
    protected int mScrollbar;//
    /**
     * 内部滑动的监听
     */
    protected RecyclerView.OnScrollListener mInternalOnScrollListener;
    /**
     * 外部滑动的监听
     */
    protected RecyclerView.OnScrollListener mExternalOnScrollListener;

    protected SwipeRefreshLayout mPtrLayout;
    protected OnRefreshListener mRefreshListener;

    //分割线的集合
    public List<RecyclerView.ItemDecoration> decorations = new ArrayList<>();

    /**
     * 得到加载框view
     *
     * @return
     */
    public SwipeRefreshLayout getSwipeToRefresh() {
        return mPtrLayout;
    }

    /**
     * 拿到recycelview视图
     *
     * @return
     */
    public RecyclerView getRecyclerView() {
        return mRecycler;
    }


    public EasyRecyclerView(Context context) {
        this(context, null);
    }

    public EasyRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EasyRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        if (attrs != null)
            initAttrs(attrs);
        initView();
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.superrecyclerview);
        try {
            mClipToPadding = a.getBoolean(R.styleable.superrecyclerview_recyclerClipToPadding, false);
            mPadding = (int) a.getDimension(R.styleable.superrecyclerview_recyclerPadding, -1.0f);//初始化-1.0f
            mPaddingTop = (int) a.getDimension(R.styleable.superrecyclerview_recyclerPaddingTop, 0.0f);
            mPaddingBottom = (int) a.getDimension(R.styleable.superrecyclerview_recyclerPaddingBottom, 0.0f);
            mPaddingLeft = (int) a.getDimension(R.styleable.superrecyclerview_recyclerPaddingLeft, 0.0f);
            mPaddingRight = (int) a.getDimension(R.styleable.superrecyclerview_recyclerPaddingRight, 0.0f);
            mScrollbarStyle = a.getInteger(R.styleable.superrecyclerview_scrollbarStyle, -1);
            mScrollbar = a.getInteger(R.styleable.superrecyclerview_scrollbars, -1);

            mEmptyId = a.getResourceId(R.styleable.superrecyclerview_layout_empty, 0);
            mProgressId = a.getResourceId(R.styleable.superrecyclerview_layout_progress, 0);
            mErrorId = a.getResourceId(R.styleable.superrecyclerview_layout_error, R.layout.common_net_error_view);
        } finally {
            a.recycle();
        }
    }

    private void initView() {
        if (isInEditMode()) {
            //解决可视化窗口无法编辑该自定义控件
            return;
        }
        //生成主View
        View v = LayoutInflater.from(getContext()).inflate(R.layout.common_recyclerview, this);
        mPtrLayout = (SwipeRefreshLayout) v.findViewById(R.id.ptr_layout);
        mPtrLayout.setEnabled(false);
        //加载框viewgroup布局
        mProgressView = (ViewGroup) v.findViewById(R.id.progress);
        //如果加载框id存在
        if (mProgressId != 0)
            LayoutInflater.from(getContext()).inflate(mProgressId, mProgressView);
        //得到空试图
        mEmptyView = (ViewGroup) v.findViewById(R.id.empty);
        //如果空试图存在
        if (mEmptyId != 0)
            LayoutInflater.from(getContext()).inflate(mEmptyId, mEmptyView);
        //错误视图
        mErrorView = (ViewGroup) v.findViewById(R.id.error);
        //如果错误的id存在，则加载该错误view
        if (mErrorId != 0)
            LayoutInflater.from(getContext()).inflate(mErrorId, mErrorView);
        initRecyclerView(v);
    }

    /**
     * 初始化recycleview
     *
     * @param view
     */
    private void initRecyclerView(View view) {
        mRecycler = (RecyclerView) view.findViewById(android.R.id.list);
        tipView = (TextView) view.findViewById(R.id.tvTip);
        setItemAnimator(null);
        if (null != mRecycler) {
            mRecycler.setHasFixedSize(true);
            mRecycler.setClipToPadding(mClipToPadding);
            mInternalOnScrollListener = new RecyclerView.OnScrollListener() {
                //内部滑动的监听器

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (mExternalOnScrollListener != null)
                        mExternalOnScrollListener.onScrolled(recyclerView, dx, dy);
                }

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (mExternalOnScrollListener != null)
                        mExternalOnScrollListener.onScrollStateChanged(recyclerView, newState);
                }
            };
            mRecycler.addOnScrollListener(mInternalOnScrollListener);
            if (mPadding != -1.0f) {
                mRecycler.setPadding(mPadding, mPadding, mPadding, mPadding);
            } else {
                mRecycler.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom);
            }
            if (mScrollbarStyle != -1) {
                //如果设置滑动条方向，就设置个recycleview。
                mRecycler.setScrollBarStyle(mScrollbarStyle);
            }
            switch (mScrollbar) {
                case 0:
                    setVerticalScrollBarEnabled(false);
                    break;
                case 1:
                    setHorizontalScrollBarEnabled(false);
                    break;
                case 2:
                    setVerticalScrollBarEnabled(false);
                    setHorizontalScrollBarEnabled(false);
                    break;
            }
        }
    }

    //重写view的方法
    @Override
    public void setVerticalScrollBarEnabled(boolean verticalScrollBarEnabled) {
        super.setVerticalScrollBarEnabled(verticalScrollBarEnabled);
    }

    //重写view的方法
    @Override
    public void setHorizontalScrollBarEnabled(boolean horizontalScrollBarEnabled) {
        mRecycler.setHorizontalScrollBarEnabled(horizontalScrollBarEnabled);
    }

    /**
     * 设置recyclevView的方向
     *
     * @param manager
     */
    public void setLayoutManager(RecyclerView.LayoutManager manager) {
        mRecycler.setLayoutManager(manager);
    }

    /**
     * 设置recycleview的分割线
     *
     * @param color
     * @param height
     * @param paddingLeft
     * @param paddingRight
     */
    public void setItemDecoration(int color, int height, int paddingLeft, int paddingRight) {
        DividerDecoration itemDecoration = new DividerDecoration(color, height, paddingLeft, paddingRight);
        itemDecoration.setDrawLastItem(false);
        decorations.add(itemDecoration);
        mRecycler.addItemDecoration(itemDecoration);
    }

    /**
     * 数据改变<br>
     * 仿观察者模式
     */
    public static class EasyDataObserver extends RecyclerView.AdapterDataObserver {
        private EasyRecyclerView recyclerView;

        public EasyDataObserver(EasyRecyclerView recyclerView) {
            this.recyclerView = recyclerView;
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            super.onItemRangeChanged(positionStart, itemCount);
            update();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            update();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            update();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount);
            update();
        }

        @Override
        public void onChanged() {
            super.onChanged();
            update();
        }

        private void update() {
            int count;
            if (recyclerView.getAdapter() instanceof RecyclerArrayAdapter) {
                count = ((RecyclerArrayAdapter) recyclerView.getAdapter()).getCount();
            } else {
                count = recyclerView.getAdapter().getItemCount();
            }
            if (count == 0 && !NetworkUtils.isAvailable(recyclerView.getContext())) {
                //如果adaper的数据的个数为0并且网络状态不可用显示错误视图
                recyclerView.showError();
                return;
            }

            if (count == 0 && ((RecyclerArrayAdapter) recyclerView.getAdapter()).getHeaderCount() == 0) {
                //如果adapter的数据源的个数为0并且头部视图的个数也为0，显示空视图
                recyclerView.showEmpty();
            } else {
                recyclerView.showRecycler();
            }
        }
    }

    /**
     * 设置适配器，关闭所有副view。展示recyclerView
     * 适配器有更新，自动关闭所有副view。根据条数判断是否展示EmptyView
     *
     * @param adapter
     */
    public void setAdapter(RecyclerView.Adapter adapter) {
        mRecycler.setAdapter(adapter);
        adapter.registerAdapterDataObserver(new EasyDataObserver(this));
        showRecycler();
    }

    /**
     * 设置适配器，关闭所有副view。展示进度条View
     * 适配器有更新，自动关闭所有副view。根据条数判断是否展示EmptyView
     *
     * @param adapter
     */
    public void setAdapterWithProgress(RecyclerView.Adapter adapter) {
        mRecycler.setAdapter(adapter);
        adapter.registerAdapterDataObserver(new EasyDataObserver(this));
        //只有Adapter为空时才显示ProgressView
        if (adapter instanceof RecyclerArrayAdapter) {
            //如果adapter的类型是RecyclerArrayAdapter 并且adapter的数据源的个数为0，先展示加载框
            if (((RecyclerArrayAdapter) adapter).getCount() == 0) {
                showProgress();
            } else {
                showRecycler();
            }
        } else {
            if (adapter.getItemCount() == 0) {
                showProgress();
            } else {
                showRecycler();
            }
        }
    }

    /**
     * 从recycleview中删除适配器
     */
    public void clear() {
        mRecycler.setAdapter(null);
    }

    /**
     * 隐藏所有视图
     */
    private void hideAll() {
        mEmptyView.setVisibility(View.GONE);
        mProgressView.setVisibility(View.GONE);
        mErrorView.setVisibility(GONE);
//        mPtrLayout.setRefreshing(false);
        mRecycler.setVisibility(View.INVISIBLE);
    }

    public void showError() {
        if (mErrorView.getChildCount() > 0) {
            hideAll();
            mErrorView.setVisibility(View.VISIBLE);
        } else {
            showRecycler();
        }

    }

    public void showEmpty() {
        if (mEmptyView.getChildCount() > 0) {
            hideAll();
            mEmptyView.setVisibility(View.VISIBLE);
        } else {
            showRecycler();
        }
    }

    /**
     * 显示加载框
     */
    public void showProgress() {
        if (mProgressView.getChildCount() > 0) {
            hideAll();
            mProgressView.setVisibility(View.VISIBLE);
        } else {
            showRecycler();
        }
    }

    /**
     * 显示recycleview
     */
    public void showRecycler() {
        hideAll();
        mRecycler.setVisibility(View.VISIBLE);
    }

    /**
     * 显示提示文字然后自动关闭
     *
     * @param tip
     */
    public void showTipViewAndDelayClose(String tip) {
        tipView.setText(tip);
        Animation mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mShowAction.setDuration(500);
        tipView.startAnimation(mShowAction);
        tipView.setVisibility(View.VISIBLE);

        tipView.postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                        0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                        Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                        -1.0f);
                mHiddenAction.setDuration(500);
                tipView.startAnimation(mHiddenAction);
                tipView.setVisibility(View.GONE);
            }
        }, 2200);
    }

    /**
     * 显示提示文字view
     *
     * @param tip
     */
    public void showTipView(String tip) {
        tipView.setText(tip);
        Animation mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mShowAction.setDuration(500);
        tipView.startAnimation(mShowAction);
        tipView.setVisibility(View.VISIBLE);
    }

    /**
     * 关闭提示文字view
     *
     * @param delayMillis
     */
    public void hideTipView(long delayMillis) {
        tipView.postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                        0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                        Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                        -1.0f);
                mHiddenAction.setDuration(500);
                tipView.startAnimation(mHiddenAction);
                tipView.setVisibility(View.GONE);
            }
        }, delayMillis);
    }

    /**
     * 设置提示文字
     *
     * @param tip
     */
    public void setTipViewText(String tip) {
        if (!isTipViewVisible())
            showTipView(tip);
        else
            tipView.setText(tip);
    }

    /**
     * 提示文字view是否可见
     *
     * @return
     */
    public boolean isTipViewVisible() {
        return tipView.getVisibility() == View.VISIBLE;
    }

    /**
     * 设置监听触发刷新，使滑动时刷新布局
     *
     * @param listener
     */
    public void setRefreshListener(OnRefreshListener listener) {
        mPtrLayout.setEnabled(true);
        mPtrLayout.setOnRefreshListener(listener);
        this.mRefreshListener = listener;
    }

    /**
     * 设置刷新中
     *
     * @param isRefreshing
     */
    public void setRefreshing(final boolean isRefreshing) {
        mPtrLayout.post(new Runnable() {
            @Override
            public void run() {
                if (isRefreshing) { // 避免刷新的loadding和progressview 同时显示
                    mProgressView.setVisibility(View.GONE);
                }
                mPtrLayout.setRefreshing(isRefreshing);
            }
        });
    }

    /**
     * 设置刷新 带回调的
     *
     * @param isRefreshing
     * @param isCallbackListener
     */
    public void setRefreshing(final boolean isRefreshing, final boolean isCallbackListener) {
        mPtrLayout.post(new Runnable() {
            @Override
            public void run() {
                mPtrLayout.setRefreshing(isRefreshing);
                if (isRefreshing && isCallbackListener && mRefreshListener != null) {
                    mRefreshListener.onRefresh();
                }
            }
        });
    }

    /**
     * 给加载框设置颜色
     *
     * @param colRes
     */
    public void setRefreshingColorResources(@ColorRes int... colRes) {
        mPtrLayout.setColorSchemeResources(colRes);
    }

    /**
     * 给加载框设置颜色
     *
     * @param col
     */
    public void setRefreshingColor(int... col) {
        mPtrLayout.setColorSchemeColors(col);
    }

    /**
     * 给recycleview设置滑动监听
     *
     * @param listener
     */
    public void setOnScrollListener(RecyclerView.OnScrollListener listener) {
        mExternalOnScrollListener = listener;
    }

    /**
     * 给recycleiv的item设置OnTouchListener
     *
     * @param listener
     */
    public void addOnItemTouchListener(RecyclerView.OnItemTouchListener listener) {
        mRecycler.addOnItemTouchListener(listener);
    }

    /**
     * recylview的item移除OnTouchListener
     *
     * @param listener
     */
    public void removeOnItemTouchListener(RecyclerView.OnItemTouchListener listener) {
        mRecycler.removeOnItemTouchListener(listener);
    }

    /**
     * 得到recyclevie的适配器adapter
     *
     * @return
     */
    public RecyclerView.Adapter getAdapter() {
        return mRecycler.getAdapter();
    }

    /**
     * 设置OnTouchListener
     *
     * @param listener
     */
    public void setOnTouchListener(OnTouchListener listener) {
        mRecycler.setOnTouchListener(listener);
    }

    /**
     * item的动画
     *
     * @param animator
     */
    public void setItemAnimator(RecyclerView.ItemAnimator animator) {
        mRecycler.setItemAnimator(animator);
    }

    /**
     * 添加分割线
     *
     * @param itemDecoration
     */
    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        mRecycler.addItemDecoration(itemDecoration);
    }

    /**
     * 指定位置添加分割线
     *
     * @param itemDecoration
     * @param index
     */
    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration, int index) {
        mRecycler.addItemDecoration(itemDecoration, index);
    }

    /**
     * 移除分割线
     *
     * @param itemDecoration
     */
    public void removeItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        mRecycler.removeItemDecoration(itemDecoration);
    }

    /**
     * 移除所有的分割线
     */
    public void removeAllItemDecoration() {
        for (RecyclerView.ItemDecoration decoration : decorations) {
            mRecycler.removeItemDecoration(decoration);
        }
    }

    /**
     * 拿到错误view
     *
     * @return inflated error view or null
     */
    public View getErrorView() {
        if (mErrorView.getChildCount() > 0) return mErrorView.getChildAt(0);
        return null;
    }

    /**
     * 拿到加载框的view
     *
     * @return inflated progress view or null
     */
    public View getProgressView() {
        if (mProgressView.getChildCount() > 0) return mProgressView.getChildAt(0);
        return null;
    }


    /**
     * 拿到空视图
     *
     * @return inflated empty view or null
     */
    public View getEmptyView() {
        if (mEmptyView.getChildCount() > 0) return mEmptyView.getChildAt(0);
        return null;
    }

}
