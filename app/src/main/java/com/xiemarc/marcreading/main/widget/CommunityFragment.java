package com.xiemarc.marcreading.main.widget;

import android.view.View;

import com.xiemarc.marcreading.R;
import com.xiemarc.marcreading.base.BaseFragment;
import com.xiemarc.marcreading.main.presenter.CommunityPresenter;
import com.xiemarc.marcreading.main.view.CommunityView;

/**
 * 描述：社区
 * 作者：Marc on 2016/11/15 16:57
 * 邮箱：aliali_ha@yeah.net
 */
public class CommunityFragment extends BaseFragment<CommunityView,CommunityPresenter> implements CommunityView{
    @Override
    protected CommunityPresenter createPresenter() {
        return new CommunityPresenter();
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.people_progress_dialog;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {

    }

    @Override
    protected void onFirstUserVisible() {
    }

    @Override
    protected void onUserVisible() {

    }

    @Override
    protected void onUserInvisible() {

    }
}
