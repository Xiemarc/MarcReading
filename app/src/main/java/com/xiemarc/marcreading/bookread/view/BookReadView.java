package com.xiemarc.marcreading.bookread.view;

import com.xiemarc.marcreading.base.BaseView;
import com.xiemarc.marcreading.bean.BookSource;
import com.xiemarc.marcreading.bean.BookToc;
import com.xiemarc.marcreading.bean.ChapterRead;

import java.util.List;

/**
 * 描述：书籍阅读的view
 * 作者：Marc on 2016/11/24 14:31
 * 邮箱：aliali_ha@yeah.net
 */
public interface BookReadView extends BaseView{
    void showBookToc(List<BookToc.mixToc.Chapters> list);

    void showChapterRead(ChapterRead.Chapter data, int chapter);

    void showBookSource(List<BookSource> list);

}
