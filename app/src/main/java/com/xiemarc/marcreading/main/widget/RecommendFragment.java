package com.xiemarc.marcreading.main.widget;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.marc.marclibs.rxbus.RxBus;
import com.xiemarc.marcreading.R;
import com.xiemarc.marcreading.base.BaseRVFragment;
import com.xiemarc.marcreading.bean.Recommend;
import com.xiemarc.marcreading.main.adapter.RecommendAdapter;
import com.xiemarc.marcreading.main.presenter.RecommendPresenter;
import com.xiemarc.marcreading.main.view.RecommendView;
import com.xiemarc.marcreading.recycleview.adapter.RecyclerArrayAdapter;

import java.util.List;

import butterknife.Bind;

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
        initAdapter(RecommendAdapter.class, true, false);
        mAdapter.setOnItemClickListener(this);

        mAdapter.addFooter(new RecyclerArrayAdapter.ItemView() {
            @Override
            public View onCreateView(ViewGroup parent) {
                View headerView = LayoutInflater.from(mContext).inflate(R.layout.foot_view_shelf, parent, false);
                return headerView;
            }

            @Override
            public void onBindView(View headerView) {
                headerView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //点击进入发现
                        ((MainActivity) mContext).setCurrentItem(2);
                    }
                });
            }
        });
        mRecyclerView.getEmptyView().findViewById(R.id.btnToAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果是空试图 点击进入发现
                ((MainActivity) mContext).setCurrentItem(2);
            }
        });
        onRefresh();
    }


    @Override
    protected void onFirstUserVisible() {
        mPresenter.getRecommendList();
    }

    @Override
    protected void onUserVisible() {

    }

    @Override
    protected void onUserInvisible() {

    }

    @Override
    public void complete() {
        //拿到数据后关闭刷新
        mRecyclerView.setRefreshing(false);
    }


    /**
     * 拿到数据展示
     *
     * @param lists
     */
    @Override
    public void showRecommendList(List<Recommend.RecommendBooks> lists) {
        mAdapter.clear();
        mAdapter.addAll(lists);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RxBus.getDefault().removeAllStickyEvents();
        RxBus.getDefault().clear();
    }


}
