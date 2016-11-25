package com.xiemarc.marcreading.main.presenter;

import com.xiemarc.marcreading.base.BasePresenter;
import com.xiemarc.marcreading.bean.Recommend;
import com.xiemarc.marcreading.main.view.RecommendView;
import com.xiemarc.marcreading.manager.SettingManager;
import com.xiemarc.marcreading.rx.rxjava.ApiCallback;
import com.xiemarc.marcreading.rx.rxjava.RxUtil;
import com.xiemarc.marcreading.rx.rxjava.SubscriberCallBack;
import com.xiemarc.marcreading.utils.StringUtils;

import java.util.List;

import rx.Observable;

/**
 * 书架的presenter
 * author: marc
 * date:  2016/11/15 20:00
 * email：aliali_ha@yeah.net
 */

public class RecommendPresenter extends BasePresenter<RecommendView> {

    private RecommendView mView;

    public RecommendPresenter(RecommendView mView) {
        this.mView = mView;
    }

    /**
     * 获得推荐列表数据
     */
    public void getRecommendList() {
        //显示加载中弹窗
        mView.showLoading(null);
        //关键字
        String key = StringUtils.creatAcacheKey("recommend-list", SettingManager.getInstance().getUserChooseSex());
        //从网络数据
        Observable<Recommend> netWorkObservable = apiStore.getRecommend(SettingManager.getInstance().getUserChooseSex())
                .compose(RxUtil.<Recommend>rxCacheListHelper(key));
        //Rxjava 操作符 concat 依次执行  按照顺序。
        //依次检查disk、network
        Observable concatObservable = Observable.concat(RxUtil.rxCreateDiskObservable(key, Recommend.class), netWorkObservable);
        //
        addSubscription(concatObservable, new SubscriberCallBack<>(new ApiCallback<Recommend>() {
            @Override
            public void onSuccess(Recommend model) {
                if (null != model) {
                    List<Recommend.RecommendBooks> books = model.books;
                    if (books != null && !books.isEmpty() && mView != null) {
                        mView.showRecommendList(books);
                    }
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                mView.showError(msg);
            }

            @Override
            public void onCompleted() {
                mView.hideLoading();
            }
        }));
    }
}
