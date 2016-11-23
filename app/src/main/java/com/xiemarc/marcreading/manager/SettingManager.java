package com.xiemarc.marcreading.manager;

import com.xiemarc.marcreading.utils.SharedPreferencesUtil;

/**
 * des:一些配置管理类
 * author: marc
 * date:  2016/11/15 21:20
 * email：aliali_ha@yeah.net
 */

public class SettingManager {
    private volatile static SettingManager manager;

    public static SettingManager getInstance() {
        return manager != null ? manager : (manager = new SettingManager());
    }


    //===============文章阅读相关开始=======================//

    /**
     * 得到 章节 关键字
    * @param bookId
     * @return
     */
    private String getChapterKey(String bookId) {
        return bookId + "-chapter";
    }

    private String getEndPosKey(String bookId) {
        return bookId + "-endPos";
    }


    /**
     * 得到开始读取位置关键字
      * @param bookId
     * @return
     */
    private String getStartPosKey(String bookId) {
        return bookId + "-startPos";
    }




    /**
     * 获取上次阅读章节及位置
     *
     * @param bookId
     * @return int数组【上一个章节id，开始读取位置，结束读取位置】
     */
    public int[] getReadProgress(String bookId) {
        int lastChapter = SharedPreferencesUtil.getInstance().getInt(getChapterKey(bookId), 1);
        int startPos = SharedPreferencesUtil.getInstance().getInt(getStartPosKey(bookId), 0);
        int endPos = SharedPreferencesUtil.getInstance().getInt(getEndPosKey(bookId), 0);

        return new int[]{lastChapter, startPos, endPos};
    }
    //===============文章阅读相关结束=======================//




    //===============保存用户选择的性别=================//

    /**
     * 保存用户选择的性别
     * @param sex
     */
    public void saveUserChooseSex(String sex) {
        SharedPreferencesUtil.getInstance().putString(Constant.SP_USER_SEX, sex);
    }

    /**
     * 拿到用户选择的性别
     * @return 返回选择的性别，默认返回男性
     */
    public String getUserChooseSex() {
        return SharedPreferencesUtil.getInstance().getString(Constant.SP_USER_SEX, Constant.Gender.MALE);
    }

    /**
     * 用户是否已经选择了性别<br>返回true。说明保存过，已经选择过
     */
    public boolean isUserChooseSex(){
        return SharedPreferencesUtil.getInstance().exists(Constant.SP_USER_SEX);
    }

    /**
     * 是否设置书籍封面
     * @return
     */
    public boolean isNoneCover() {
        return SharedPreferencesUtil.getInstance().getBoolean("isNoneCover", false);
    }

    /**
     * 保存书籍封面
     * @param isNoneCover
     */
    public void saveNoneCover(boolean isNoneCover) {
        SharedPreferencesUtil.getInstance().putBoolean("isNoneCover", isNoneCover);
    }
}
