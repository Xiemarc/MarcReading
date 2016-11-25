package com.xiemarc.marcreading.view.readview;

/**
 * 描述：书的状态
 * 作者：Marc on 2016/11/24 13:50
 * 邮箱：aliali_ha@yeah.net
 */
public enum BookStatus {
    //没有上一页
    NO_PRE_PAGE,
    //没有下一页
    NO_NEXT_PAGE,
    //上一章节加载失败
    PRE_CHAPTER_LOAD_FAILURE,
    //下一章节加载失败
    NEXT_CHAPTER_LOAD_FAILURE,
    //加载成功
    LOAD_SUCCESS
}
