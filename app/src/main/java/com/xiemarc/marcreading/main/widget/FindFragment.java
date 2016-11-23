package com.xiemarc.marcreading.main.widget;

import android.view.View;

import com.xiemarc.marcreading.R;
import com.xiemarc.marcreading.base.BaseFragment;
import com.xiemarc.marcreading.main.presenter.FindPresenter;
import com.xiemarc.marcreading.main.view.FindView;

/**
 * 描述：发现
 * 作者：Marc on 2016/11/15 16:58
 * 邮箱：aliali_ha@yeah.net
 */
public class FindFragment extends BaseFragment<FindView,FindPresenter> implements FindView{
    @Override
    protected FindPresenter createPresenter() {
        return new FindPresenter();
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
