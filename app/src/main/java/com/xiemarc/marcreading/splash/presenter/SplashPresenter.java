package com.xiemarc.marcreading.splash.presenter;

import android.widget.FrameLayout;

import com.xiemarc.marcreading.R;
import com.xiemarc.marcreading.base.BasePresenter;
import com.xiemarc.marcreading.splash.view.SplashView;

/**
 * 描述：
 * 作者：Marc on 2016/11/15 09:54
 * 邮箱：aliali_ha@yeah.net
 */
public class SplashPresenter extends BasePresenter<SplashView> {

    private SplashView mView;

    public SplashPresenter(SplashView mView) {
        this.mView = mView;
    }

    public void setBackGround(FrameLayout fl) {
        fl.setBackgroundResource(R.drawable.splash);
        mView.goHome();
    }
}
