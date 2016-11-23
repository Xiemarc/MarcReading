package com.xiemarc.marcreading.splash.widget;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.xiemarc.marcreading.R;
import com.xiemarc.marcreading.base.BaseActivity;
import com.xiemarc.marcreading.main.widget.MainActivity;
import com.xiemarc.marcreading.splash.presenter.SplashPresenter;
import com.xiemarc.marcreading.splash.view.SplashView;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import rx.Observable;
import rx.Subscription;

/**
 * 描述：闪屏activity
 * 作者：Marc on 2016/11/15 10:14
 * 邮箱：aliali_ha@yeah.net
 */
public class SplashActivity extends BaseActivity<SplashView, SplashPresenter> implements SplashView {

    @Bind(R.id.tvSkip)
    TextView mTvSkip;
    @Bind(R.id.container)
    FrameLayout mContainer;

    @Override
    protected boolean isApplyKitKatTranslucency() {
        return false;
    }

    @Override
    protected SplashPresenter createPresenter() {
        return new SplashPresenter(this);
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_splash;
    }

    @Override
    protected View getLoadingTargetView() {
        return mContainer;
    }

    @Override
    protected void initViewsAndEvents() {
        mPresenter.setBackGround(mContainer);
        mTvSkip.setOnClickListener(view ->
                readyGoThenKill(null, MainActivity.class)
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onUnsubscribe();
    }

    @Override
    public void goHome() {
        Subscription subscribe = Observable.timer(2, TimeUnit.SECONDS)
                .subscribe(aLong -> {
                    readyGoThenKill(null, MainActivity.class);
                });
        addSubscription(subscribe);
    }
}
