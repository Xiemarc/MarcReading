package com.xiemarc.marcreading.view.readview;

/**
 * 描述：阅读状态改变监听
 * 作者：Marc on 2016/11/24 13:51
 * 邮箱：aliali_ha@yeah.net
 */
public interface OnReadStateChangeListener {
    //章节改变
    void onChapterChanged(int chapter);
    // 页码改变
    void onPageChanged(int chapter, int page);
    // 加载章节失败
    void onLoadChapterFailure(int chapter);
    //点击中间区域
    void onCenterClick();
    // 在翻转中
    void onFlip();
}
