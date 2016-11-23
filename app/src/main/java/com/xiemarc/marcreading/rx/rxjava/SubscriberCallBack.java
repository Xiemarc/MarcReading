package com.xiemarc.marcreading.rx.rxjava;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * des:T是 retrofit返回的类型<br>
 * V 是成功后需要转化的类型<br>
 * author: marc
 * date:  2016/11/14 22:27
 * email：aliali_ha@yeah.net
 */

public class SubscriberCallBack<T> extends Subscriber<T> {
    private ApiCallback<T> apiCallback;

    public SubscriberCallBack(ApiCallback<T> apiCallback) {
        this.apiCallback = apiCallback;
    }

    @Override
    public void onCompleted() {
        apiCallback.onCompleted();
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            //httpException.response().errorBody().string()
            int code = httpException.code();
            String msg = httpException.getMessage();
            if (code == 504) {
                msg = "网络不给力";
            }
            apiCallback.onFailure(code, msg);
        } else {
            apiCallback.onFailure(0, e.getMessage());
        }
        apiCallback.onCompleted();
    }

    @Override
    public void onNext(T t) {
        apiCallback.onSuccess(t);
    }
}
