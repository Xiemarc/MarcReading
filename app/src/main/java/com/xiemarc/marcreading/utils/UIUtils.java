package com.xiemarc.marcreading.utils;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.xiemarc.marcreading.BaseApplication;
import com.xiemarc.marcreading.R;

import java.security.MessageDigest;

/**
 * 描述：
 * 作者：Marc on 2016/11/14 15:49
 * 邮箱：aliali_ha@yeah.net
 */
public class UIUtils {
    /**
     * 得到上下文
     *
     * @return
     */
    public static Context getContext() {
        return BaseApplication.getmContext();
    }

    /**
     * 得到res对象
     *
     * @return
     */
    public static Resources getResoucer() {
        return getContext().getResources();
    }

    /**
     * 得到string.xml中的字符串
     *
     * @param resId
     * @return
     */
    public static String getString(int resId) {
        return getResoucer().getString(resId);
    }

    /**
     * 得到String.xml中的字符串,带占位符
     */
    public static String getString(int id, Object... formatArgs) {
        return getResoucer().getString(id, formatArgs);
    }

    /**
     * 得到String.xml文件中的字符串数组
     *
     * @param arrId
     * @return
     */
    public static String[] getStringArr(int arrId) {
        return getResoucer().getStringArray(arrId);
    }

    /**
     * 得到color.xml文件中的颜色
     *
     * @param colorId
     * @return
     */
    public static int getColor(int colorId) {
        return getResoucer().getColor(colorId);
    }

    public static String getPackageName() {
        return getContext().getPackageName();
    }


    /**
     * 当前UI线程ID
     *
     * @return
     */
    public static long getMainThreadId() {
        return BaseApplication.getmMainThreadId();
    }

    /**
     * 得到主线程 的handler
     *
     * @return
     */
    public static Handler getMainThreadHandler() {
        return BaseApplication.getmMainHandler();
    }

    /**
     * 安全的执行任务 <br>
     *
     * @param task 这里注意，runnable  不是子线程，是任务。线程是Thread，不要混淆
     */
    public static void postTaskSafely(Runnable task) {
        int curThreadId = android.os.Process.myTid();//当前线程id
        if (curThreadId == getMainThreadId()) {
            //如果当前线程是主线程,任务可以直接执行
            task.run();
        } else {
            //如果当前线程不是主线程，handler发送到主线程
            getMainThreadHandler().post(task);
        }
    }


    /**
     * 延迟执行
     *
     * @param task
     * @param delay 延迟多久
     */
    public static void postTaskDelay(Runnable task, int delay) {
        getMainThreadHandler().postDelayed(task, delay);
    }

    /**
     * 移除任务
     *
     * @param task
     */
    public static void removeTask(Runnable task) {
        getMainThreadHandler().removeCallbacks(task);
    }

    /**
     * dip 转化为 px
     *
     * @param dip
     * @return
     */
    public static int dip2px(int dip) {
        //这里要用到密度比值
        int density = (int) getResoucer().getDisplayMetrics().density;
        int px = dip * density;
        return px;
    }

    /**
     * px转化为dip
     *
     * @param px
     * @return
     */
    public static int px2dip(int px) {
        int density = (int) getResoucer().getDisplayMetrics().density;
        int dip = px / density;
        return dip;
    }

    /**
     * editext抖动效果
     *
     * @param view
     */
    public static void viewShake(View view) {
        Animation shake = AnimationUtils.loadAnimation(UIUtils.getContext(), R.anim.shake);
        view.startAnimation(shake);
    }

    public static String MD5(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;

            for (byte byte0 :
                    md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }

            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 解决连续出现toast等待现象
     */
    public static Toast mToast;

    public static void showToast(String msg) {
        if (null == mToast) {
            mToast = Toast.makeText(getContext(), "", Toast.LENGTH_SHORT);
        }
        mToast.setText(msg);
        mToast.show();
    }
}
