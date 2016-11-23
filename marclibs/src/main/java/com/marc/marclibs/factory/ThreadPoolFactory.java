package com.marc.marclibs.factory;


import com.marc.marclibs.manager.ThreadPoolProxy;

/**
 * 描述：线程池工厂
 * 作者：Marc on 2016/7/9 10:47
 * 邮箱：aliali_ha@yeah.net
 */
public class ThreadPoolFactory {
    static ThreadPoolProxy mNormalPool;//默认线程池
    static ThreadPoolProxy mDownLoadPool;//下载线程池

    /**
     * 得到一个普通的线程池
     *
     * @return
     */
    public static ThreadPoolProxy getNormalPool() {
        if (mNormalPool == null) {
            //加个类锁
            synchronized (ThreadPoolProxy.class) {
                if (mNormalPool == null) {
                    mNormalPool = new ThreadPoolProxy(5, 5, 3000);
                }
            }
        }
        return mNormalPool;
    }

    /**
     * 得到下载的线程池
     * @return
     */
    public static ThreadPoolProxy getDownLoadPool() {
        if (mDownLoadPool == null) {
            synchronized (ThreadPoolProxy.class) {
                if (mDownLoadPool == null) {
                    //下载的时候 最大线程数智只能是3个
                    mDownLoadPool = new ThreadPoolProxy(3, 3, 3000);
                }
            }
        }
        return mDownLoadPool;
    }
}
