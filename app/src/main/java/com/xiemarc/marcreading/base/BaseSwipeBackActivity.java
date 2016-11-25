package com.xiemarc.marcreading.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.marc.marclibs.base.BaseSwipeBackCompatActivity;
import com.marc.marclibs.netstatus.NetUtils;
import com.xiemarc.marcreading.R;
import com.xiemarc.marcreading.widget.PeopleProgressDialog;

import butterknife.ButterKnife;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * 描述：手势返回的activity
 * 作者：Marc on 2016/11/14 16:46
 * 邮箱：aliali_ha@yeah.net
 */
public abstract class BaseSwipeBackActivity<V, T extends BasePresenter<V>>
        extends BaseSwipeBackCompatActivity implements BaseView {

    /**
     * 统一使用toolar
     */
    protected Toolbar mToolbar;//toolbar
    protected TextView mToolbarTv;//toolbar中间的textView
    protected ImageView mImageRight;//toolbar右侧的图片
    protected TextView mTitleRight;
    protected T mPresenter;

    protected PeopleProgressDialog mPeopleProgressDialog;//统一使用小人加载动画

    private CompositeSubscription mCompositeSubscription;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mPresenter = createPresenter();
        statusBarColor = ContextCompat.getColor(this, R.color.colorPrimary);
        super.onCreate(savedInstanceState);
        mPresenter.attachView((V) this);
    }

    @Override
    protected void onDestroy() {
        onUnsubscribe();
        super.onDestroy();
        mPresenter.detachView();
        dissPeepleDialog();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        mToolbar = ButterKnife.findById(this, R.id.toolbar);
        mToolbarTv = ButterKnife.findById(this, R.id.toolbar_tv);
        mImageRight = ButterKnife.findById(this, R.id.image_right);
        mTitleRight = ButterKnife.findById(this, R.id.title_right);
        if (null != mToolbar) {
            mToolbar.setTitle("");
            setSupportActionBar(mToolbar);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public void onUnsubscribe() {
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();//取消注册，以避免内存泄露
        }
    }

    public void addSubscription(Subscription subscription) {
//        if (mCompositeSubscription == null) {
        mCompositeSubscription = new CompositeSubscription();
//        }
        mCompositeSubscription.add(subscription);
    }
    /**
     * 显示小人加载动画
     */
    public void showPeopleDialog() {
        if (mPeopleProgressDialog != null) {
            mPeopleProgressDialog = null;
        }
        mPeopleProgressDialog = PeopleProgressDialog.createDialog(mContext, R.drawable.fram);
        if (!mPeopleProgressDialog.isShowing()) {
            mPeopleProgressDialog.show();
        }
    }

    /**
     * 取消小人加载动画
     */
    public void dissPeepleDialog() {
        if (null != mPeopleProgressDialog && mPeopleProgressDialog.isShowing()) {
            mPeopleProgressDialog.dismiss();
            mPeopleProgressDialog = null;
        }
        if (null != mPeopleProgressDialog) {
            mPeopleProgressDialog = null;
        }
    }

    //=================BaseView实现接口开始===============//
    @Override
    public void showLoading(String msg) {
        showPeopleDialog();
    }

    @Override
    public void hideLoading() {
        dissPeepleDialog();
    }

    @Override
    public void showError(String msg) {
        toggleShowError(true,msg,null);
    }

    @Override
    public void showException(String msg) {
        toggleShowError(true,msg,null);
    }

    @Override
    public void showNetError() {
        toggleNetworkError(true,null);
    }
    //=================BaseView实现接口结束===============//

    //================BaseSwipeBackActivity自己的抽象方法开始===============//
    protected abstract boolean isApplyKitKatTranslucency();

    protected abstract T createPresenter();
    //================BaseSwipeBackActivity自己的抽象方法结束===============//


    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {
        showToast("联网了");
    }

    @Override
    protected void onNetworkDisConnected() {
        showToast("断网了");
    }
}
