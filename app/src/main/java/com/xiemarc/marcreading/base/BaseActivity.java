package com.xiemarc.marcreading.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.marc.marclibs.base.BaseAppCompatActivity;
import com.marc.marclibs.netstatus.NetUtils;
import com.xiemarc.marcreading.BaseApplication;
import com.xiemarc.marcreading.R;
import com.xiemarc.marcreading.utils.UIUtils;
import com.xiemarc.marcreading.widget.PeopleProgressDialog;

import butterknife.ButterKnife;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * 描述：
 * 作者：Marc on 2016/11/14 15:48
 * 邮箱：aliali_ha@yeah.net
 */
public abstract class BaseActivity<V, T extends BasePresenter<V>> extends BaseAppCompatActivity implements BaseView {
    private static final String TAG = "BaseActivity";
    /**
     * 统一使用toolar
     */
    protected Toolbar mToolbar;//toolbar
    //    protected TextView mToolbarTv;//toolbar中间的textView
//    protected ImageView mImageRight;//toolbar右侧的图片
//    protected TextView mTitleRight;
    protected T mPresenter;

    protected PeopleProgressDialog mPeopleProgressDialog;//统一使用小人加载动画



    public BaseApplication getBaseApplicatub() {
        return (BaseApplication) getApplication();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mPresenter = createPresenter();
        statusBarColor = ContextCompat.getColor(this, R.color.colorPrimary);
        super.onCreate(savedInstanceState);
        mPresenter.attachView((V) this);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        mToolbar = ButterKnife.findById(this, R.id.toolbar);
//        mToolbarTv = ButterKnife.findById(this, R.id.toolbar_tv);
//        mImageRight = ButterKnife.findById(this, R.id.image_right);
//        mTitleRight = ButterKnife.findById(this, R.id.title_right);
        if (null != mToolbar) {
            mToolbar.setTitle("");
            setSupportActionBar(mToolbar);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private CompositeSubscription mCompositeSubscription;

    public void onUnsubscribe() {
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();//取消注册，以避免内存泄露
        }
    }

    /**
     * 这里不对其进去指定具体线程操作
     *
     * @param subscription
     */
    public void addSubscription(Subscription subscription) {
//        if (mCompositeSubscription == null) {
        mCompositeSubscription = new CompositeSubscription();
//        }
        mCompositeSubscription.add(subscription);
    }


    @Override
    protected void onDestroy() {
        onUnsubscribe();
        super.onDestroy();
        mPresenter.detachView();
        dissPeepleDialog();
    }

    /**
     * 显示小人加载动画
     */
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

    protected void gone(final View... views) {
        if (views != null && views.length > 0) {
            for (View view : views) {
                if (view != null) {
                    view.setVisibility(View.GONE);
                }
            }
        }
    }

    protected void visible(final View... views) {
        if (views != null && views.length > 0) {
            for (View view : views) {
                if (view != null) {
                    view.setVisibility(View.VISIBLE);
                }
            }
        }

    }

    protected boolean isVisible(View view) {
        return view.getVisibility() == View.VISIBLE;
    }


    //================BaseView方法开始=================//
    @Override
    public void showLoading(String msg) {
        showPeopleDialog();
    }

    @Override
    public void hideLoading() {
        dissPeepleDialog();
    }

    /**
     * 具体哪个具体实现类单独实现做操作
     *
     * @param msg
     */
    @Override
    public void showError(String msg) {
        showToast(msg);
//        toggleShowError(true, msg, null);
    }

    @Override
    public void showException(String msg) {
        toggleShowError(true, msg, null);
    }

    @Override
    public void showNetError() {
        toggleNetworkError(true, null);
    }


    //================BaseView方法开始=================//
    //==================================实现父类的方法开始====================================//

    /**
     * 网络操作最好在各个子类中实现，因为具体操作不同
     *
     * @param type
     */
    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {
        UIUtils.showToast("联网了");
    }

    @Override
    protected void onNetworkDisConnected() {
        UIUtils.showToast("断网了");
    }

    @Override
    protected boolean toggleOverridePendingTransition() {
        return false;
    }

    @Override
    protected TransitionMode getOverridePendingTransitionMode() {
        return null;
    }


    //==================================实现父类的方法结束====================================//

    //=============================baseActivity新的抽象方法需要子类实现的========================//


    /**
     * 获得指定类型的presenter
     *
     * @return
     */
    protected abstract T createPresenter();


}
