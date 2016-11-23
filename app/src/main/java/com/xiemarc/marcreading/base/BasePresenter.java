package com.xiemarc.marcreading.base;

import com.xiemarc.marcreading.rx.retrofit.ApiClient;
import com.xiemarc.marcreading.rx.retrofit.ApiStore;

import java.lang.ref.WeakReference;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * 描述：Prensetn的基类
 * 解决 activity请求结束前被销毁，由于网络原因，没有返回，导致Prenseter一直持有activity对象，
 * 就发生了内存泄露
 * 这里可以考虑实现个总的方法，让子类实现，但是考虑到持有对象，算了
 * 作者：Marc on 2016/11/14 16:02
 * 邮箱：aliali_ha@yeah.net
 */
public abstract class BasePresenter<T> {

    protected WeakReference<T> mvpView;
    private CompositeSubscription mCompositeSubscription;
    public ApiStore apiStore = ApiClient.retrofit().create(ApiStore.class);

    public void attachView(T mvpView) {
        this.mvpView = new WeakReference<T>(mvpView);
    }

    public void detachView() {
        if (mvpView != null) {
            this.mvpView.clear();
            this.mvpView = null;
        }
        onUnsubscribe();
    }

    public boolean isViewAttached() {
        return mvpView != null && mvpView.get() != null;
    }

    public T getMvpView() {
        return mvpView.get();
    }

    //RXjava取消注册，以避免内存泄露
    public void onUnsubscribe() {
        if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.unsubscribe();
        }
    }

    //添加订阅关系
    public void addSubscription(Observable observable, Subscriber subscriber) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(observable
                .subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程,就是observable发生在io线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程//subscriber发生在主线程
                .subscribe(subscriber));
    }
}
