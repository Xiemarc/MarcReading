package com.xiemarc.marcreading.base;

/**
 * 描述：view的基类
 * 作者：Marc on 2016/11/14 16:00
 * 邮箱：aliali_ha@yeah.net
 */
public interface BaseView {

    /**
     * 显示加载中
     * @param msg
     */
    void showLoading(String msg);

    /**
     * 隐藏加载中
     */
    void hideLoading();

    /**
     * 显示错误信息
     */
    void showError(String msg);

    /**
     * 显示异常信息
     */
    void showException(String msg);

    /**
     * 显示网络错误
     */
    void showNetError();



}
