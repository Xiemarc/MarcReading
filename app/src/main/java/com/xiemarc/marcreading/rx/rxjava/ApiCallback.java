package com.xiemarc.marcreading.rx.rxjava;

/**
 * des:
 * author: marc
 * date:  2016/11/14 22:26
 * email：aliali_ha@yeah.net
 */
public interface ApiCallback<T> {

    void onSuccess(T model);

    void onFailure(int code, String msg);

    void onCompleted();
}
