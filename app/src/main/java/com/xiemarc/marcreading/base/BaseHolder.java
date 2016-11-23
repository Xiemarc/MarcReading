package com.xiemarc.marcreading.base;

import android.view.View;

/**
 * 描述：这里准备改为面向holder编程
 * T 为传入的数据
 * 作者：Marc on 2016/11/14 16:13
 * 邮箱：aliali_ha@yeah.net
 */
public abstract class BaseHolder<T> {

    /**
     * 主视图
     */
    public View mHolderView;

    /**
     * 数据集合
     */
    private T mData;

    public BaseHolder() {
        //1.初始化根布局
        mHolderView = initHolderView();
        //绑定tag
        mHolderView.setTag(this);
    }

    /**
     * 设置数据和刷新视图<br>
     * 需要设置数据和刷新数据的时候调用
     */
    public void setDataAndRefreshHolderView(T data) {
        //保存数据
        mData = data;
        //显示刷新
        refreshHolderView(data);
    }


    /**
     * 初始化根视图 holderview<br>
     * BaseHolder 构造方法的时候就调用
     *
     * @return
     */
    public abstract View initHolderView();


    /**
     * 刷新holder视图<br>
     * setDataAndRefreshHolderView这个方法被调用的时候调用该方法
     *
     * @param data
     */
    public abstract void refreshHolderView(T data);

}
