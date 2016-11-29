package com.xiemarc.marcreading.main.widget;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.marc.marclibs.rxbus.RxBus;
import com.marc.marclibs.rxbus.RxBusSubscriber;
import com.xiemarc.marcreading.R;
import com.xiemarc.marcreading.base.BaseRVFragment;
import com.xiemarc.marcreading.bean.Recommend;
import com.xiemarc.marcreading.bookread.widget.ReadActivity;
import com.xiemarc.marcreading.main.adapter.RecommendAdapter;
import com.xiemarc.marcreading.main.presenter.RecommendPresenter;
import com.xiemarc.marcreading.main.view.RecommendView;
import com.xiemarc.marcreading.manager.CollectionsManager;
import com.xiemarc.marcreading.manager.Constant;
import com.xiemarc.marcreading.recycleview.adapter.RecyclerArrayAdapter;
import com.xiemarc.marcreading.rx.event.RefreshCollectionListEvent;
import com.xiemarc.marcreading.rx.eventbus.UserSexChooseFinishedEvent;
import com.xiemarc.marcreading.utils.UIUtils;

import java.util.List;

import butterknife.Bind;
import rx.Subscription;

/**
 * 描述：书架 * 作者：Marc on 2016/11/15 16:57
 * 邮箱：aliali_ha@yeah.net
 */
public class RecommendFragment extends BaseRVFragment<RecommendView, RecommendPresenter, Recommend.RecommendBooks>
        implements RecommendView {


    @Bind(R.id.tvSelectAll)
    TextView mTvSelectAll;

    @Bind(R.id.tvDelete)
    TextView mTvDelete;

    @Bind(R.id.llBatchManagement)
    LinearLayout llBatchManagement;

    private boolean isSelectAll = false;

    @Override
    protected RecommendPresenter createPresenter() {
        return new RecommendPresenter(this);
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragmeng_recommend;
    }

    @Override
    protected View getLoadingTargetView() {
        return mRecyclerView;
    }

    @Override
    protected void initViewsAndEvents() {
        initAdapter(RecommendAdapter.class, false, false);
        mAdapter.setOnItemClickListener(this);
        mAdapter.addFooter(new RecyclerArrayAdapter.ItemView() {
            @Override
            public View onCreateView(ViewGroup parent) {
                View footerView = LayoutInflater.from(mContext).inflate(R.layout.foot_view_shelf, parent, false);
                return footerView;
            }

            @Override
            public void onBindView(View footerView) {
                footerView.setOnClickListener(view ->
                        //进入发现fragment
                        ((MainActivity) mContext).setCurrentItem(2));
            }
        });
        mRecyclerView.getEmptyView().findViewById(R.id.btnToAdd).setOnClickListener(view ->
                //进入发现fgragment
                ((MainActivity) mContext).setCurrentItem(2));
//        onRefresh();
        Subscription sexEvent = RxBus.getDefault().toObservable(UserSexChooseFinishedEvent.class)
                .subscribe(new RxBusSubscriber<UserSexChooseFinishedEvent>() {
                    @Override
                    protected void onEvent(UserSexChooseFinishedEvent userSexChooseFinishedEvent) throws Exception {
                        //接受到事件，把flag设置为true
                        mPresenter.getRecommendList();
                    }
                });
        addSubscription(sexEvent);
        Subscription refreshEvent = RxBus.getDefault().toObservable(RefreshCollectionListEvent.class)
                .subscribe(new RxBusSubscriber<RefreshCollectionListEvent>() {
                    @Override
                    protected void onEvent(RefreshCollectionListEvent refreshCollectionListEvent) throws Exception {
                        //更新书籍状态，不显示小红点
                        llBatchManagement.setVisibility(View.GONE);
                        List<Recommend.RecommendBooks> data = CollectionsManager.getInstance().getCollectionListBySort();
                    }
                });
        addSubscription(refreshEvent);

    }


    @Override
    protected void onFirstUserVisible() {
        //第一次可见进入该代码部分

    }

    @Override
    protected void onUserVisible() {

    }

    @Override
    protected void onUserInvisible() {

    }


    /**
     * 拿到数据展示
     *
     * @param lists
     */
    @Override
    public void showRecommendList(List<Recommend.RecommendBooks> lists) {
        //拿到数据后关闭刷新
        mRecyclerView.setRefreshing(false);
        mAdapter.clear();
        mAdapter.addAll(lists);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RxBus.getDefault().removeAllStickyEvents();
        RxBus.getDefault().clear();
    }


    /**
     * 点击进入阅读界面
     *
     * @param position
     */
    @Override
    public void onItemClick(int position) {
        //如果是批量管理的时候，屏蔽掉点击事件
        if (llBatchManagement.getVisibility() == View.VISIBLE)
            return;
        //点击进入阅读页面
//        ReadActivity.startActivity(UIUtils.getContext(), mAdapter.getItem(position), mAdapter.getItem(position).isFromSD);
        Intent intent = new Intent(UIUtils.getContext(), ReadActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Constant.INTENT_BEAN, mAdapter.getItem(position));
        intent.putExtra(Constant.INTENT_SD, mAdapter.getItem(position).isFromSD);
        startActivity(intent);
    }

}
