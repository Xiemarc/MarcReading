package com.xiemarc.marcreading.manager;

import com.xiemarc.marcreading.utils.ScreenUtils;
import com.xiemarc.marcreading.utils.SharedPreferencesUtil;
import com.xiemarc.marcreading.utils.UIUtils;

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
     * 保存书籍阅读字体大小
     *
     * @param bookId     需根据bookId对应，避免由于字体大小引起的分页不准确
     * @param fontSizePx
     * @return
     */
    public void saveFontSize(String bookId, int fontSizePx) {
        // 书籍对应
        SharedPreferencesUtil.getInstance().putInt(getFontSizeKey(bookId), fontSizePx);
    }

    /**
     * 保存全局生效的阅读字体大小
     *
     * @param fontSizePx
     */
    public void saveFontSize(int fontSizePx) {
        saveFontSize("", fontSizePx);
    }

    public int getReadFontSize(String bookId) {
        return SharedPreferencesUtil.getInstance().getInt(getFontSizeKey(bookId), ScreenUtils.dpToPxInt(16));
    }

    //得到阅读 的文字大小
    public int getReadFontSize() {
        return getReadFontSize("");
    }

    //保存阅读进度
    public synchronized void saveReadProgress(String bookId, int currentChapter, int m_mbBufBeginPos, int m_mbBufEndPos) {
        SharedPreferencesUtil.getInstance()
                .putInt(getChapterKey(bookId), currentChapter)
                .putInt(getStartPosKey(bookId), m_mbBufBeginPos)
                .putInt(getEndPosKey(bookId), m_mbBufEndPos);
    }

    /**
     * 得到 章节 关键字
     *
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
     *
     * @param bookId
     * @return
     */
    private String getStartPosKey(String bookId) {
        return bookId + "-startPos";
    }

    //得到文字大小的key
    private String getFontSizeKey(String bookId) {
        return bookId + "-readFontSize";
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

    /**
     * 得到阅读的主题
     */
    public int getReadTheme() {
        if (SharedPreferencesUtil.getInstance().getBoolean(Constant.ISNIGHT, false)) {
            return ThemeManager.NIGHT;
        }
        return SharedPreferencesUtil.getInstance().getInt("readTheme", 3);
    }

    //    保存阅读主题
    public void saveReadTheme(int theme) {
        SharedPreferencesUtil.getInstance().putInt("readTheme", theme);
    }
    //===============文章阅读相关结束=======================//


    //===============保存用户选择的性别=================//

    /**
     * 保存用户选择的性别
     *
     * @param sex
     */
    public void saveUserChooseSex(String sex) {
        SharedPreferencesUtil.getInstance().putString(Constant.SP_USER_SEX, sex);
    }

    /**
     * 拿到用户选择的性别
     *
     * @return 返回选择的性别，默认返回男性
     */
    public String getUserChooseSex() {
        return SharedPreferencesUtil.getInstance().getString(Constant.SP_USER_SEX, Constant.Gender.MALE);
    }

    /**
     * 用户是否已经选择了性别<br>返回true。说明保存过，已经选择过
     */
    public boolean isUserChooseSex() {
        return SharedPreferencesUtil.getInstance().exists(Constant.SP_USER_SEX);
    }

    /**
     * 是否设置书籍封面
     *
     * @return
     */
    public boolean isNoneCover() {
        return SharedPreferencesUtil.getInstance().getBoolean("isNoneCover", false);
    }

    /**
     * 保存书籍封面
     *
     * @param isNoneCover
     */
    public void saveNoneCover(boolean isNoneCover) {
        SharedPreferencesUtil.getInstance().putBoolean("isNoneCover", isNoneCover);
    }

    /**
     * 是否是自动调节亮度
     *
     * @return true：自动调节亮度。false：不自动调节亮度
     */
    public boolean isAutoBrightness() {
        return SharedPreferencesUtil.getInstance().getBoolean("autoBrightness", false);
    }

    /**
     * 保存当前的亮度值
     *
     * @param progress
     */
    public void saveReadBrightness(int progress) {
        SharedPreferencesUtil.getInstance().putInt(getLightnessKey(), progress);
    }

    //生成亮度值key
    private String getLightnessKey() {
        return "readLightness";
    }

    /**
     * 得到当前的亮度值
     *
     * @return
     */
    public int getReadBrightness() {
        return SharedPreferencesUtil.getInstance().getInt(getLightnessKey(),
                (int) ScreenUtils.getScreenBrightness(UIUtils.getContext()));
    }

    /**
     * 保存自动亮度设置
     *
     * @param b true：开启自动亮度， false：不启用自动亮度
     */
    public void saveAutoBrightness(boolean b) {
        SharedPreferencesUtil.getInstance().putBoolean("autoBrightness", b);
    }

    /**
     * 音量键是否能控制翻页
     * @return true：可以控制翻页  false：不能控制翻页 .默认可以控制翻页
     */
    public boolean isVolumeFlipEnable() {
        return SharedPreferencesUtil.getInstance().getBoolean("volumeFlip",true);
    }

    /**
     * 保存音量键能否翻页
     * @param enable
     */
    public void saveVolumeFlipEnable(boolean enable) {
        SharedPreferencesUtil.getInstance().putBoolean("volumeFlip",enable);
    }
}
