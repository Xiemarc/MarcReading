package com.xiemarc.marcreading;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;

import com.marc.marclibs.base.BaseAppManager;
import com.marc.marclibs.netstatus.NetStateReceiver;
import com.xiemarc.marcreading.utils.SharedPreferencesUtil;

/**
 * 描述：自定义application
 * 作者：Marc on 2016/11/14 15:50
 * 邮箱：aliali_ha@yeah.net
 */
public class BaseApplication extends Application {

    private static Context mContext;//上下文
    private static Thread mThread;
    private static long mMainThreadId;
    private static Looper mMainLooper;
    private static Handler mMainHandler;

    public static Context getmContext() {
        return mContext;
    }

    public static Thread getmThread() {
        return mThread;
    }

    public static long getmMainThreadId() {
        return mMainThreadId;
    }

    public static Looper getmMainThreadLooper() {
        return mMainLooper;
    }

    public static Handler getmMainHandler() {
        return mMainHandler;
    }

    public static Looper getmMainLooper() {
        return mMainLooper;
    }

    private static BaseApplication sInstance;

    public static BaseApplication getsInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化一些常用的属性
        mContext = getApplicationContext();
        //主线程
        mThread = Thread.currentThread();
        //主线程id
        mMainThreadId = Process.myTid();
        mMainLooper = getMainLooper();

        //定义一个Hanlder
        mMainHandler = new Handler();
        NetStateReceiver.registerNetworkStateReceiver(this);
        super.onCreate();
        this.sInstance = this;
        initPrefs();
    }

    /**
     * 初始化SharedPreference
     */
    private void initPrefs() {
        SharedPreferencesUtil.init(getApplicationContext(), getPackageName() + "_preference"
                , Context.MODE_MULTI_PROCESS);
    }

    public void exitApp() {
        BaseAppManager.getInstance().clear();
        System.gc();
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
