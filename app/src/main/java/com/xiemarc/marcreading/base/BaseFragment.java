package com.xiemarc.marcreading.base;

import android.os.Bundle;

import com.marc.marclibs.base.BaseLazyFragment;
import com.xiemarc.marcreading.R;
import com.xiemarc.marcreading.widget.PeopleProgressDialog;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * 描述：fragment的基类
 * <br>那些已经在BaseFragment中实现的抽象方法，子类看需求重写</>
 * 作者：Marc on 2016/11/14 16:34
 * 邮箱：aliali_ha@yeah.net
 */
public abstract class BaseFragment<V, T extends BasePresenter<V>> extends BaseLazyFragment implements BaseView {

    protected T mPresenter;//基类presenter引用

    protected PeopleProgressDialog mPeopleProgressDialog;//统一使用小人加载动画

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();
        mPresenter.attachView((V) this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
        if (mPeopleProgressDialog != null) {
            mPeopleProgressDialog = null;
        }
        onUnsubscribe();
    }

    public void showPeopleDialog() {
        if (mPeopleProgressDialog == null) {
            mPeopleProgressDialog = PeopleProgressDialog.createDialog(mContext, R.drawable.fram);
        }
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

    protected abstract T createPresenter();

    private CompositeSubscription mCompositeSubscription;

    public void onUnsubscribe() {
        //取消注册，以避免内存泄露
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
        }
    }

    public void addSubscription(Subscription subscription) {
        //        if (mCompositeSubscription == null) {
        mCompositeSubscription = new CompositeSubscription();
        //        }
        mCompositeSubscription.add(subscription);
    }

    //==============BaseView方法实现开始=================//
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
        toggleShowError(true, msg, null);
    }

    @Override
    public void showException(String msg) {
        toggleShowError(true, msg, null);
    }

    @Override
    public void showNetError() {
        toggleNetworkError(true, null);
    }
    //==============BaseView方法实现结束=================//


}
