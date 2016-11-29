package com.xiemarc.marcreading.manager;

import com.xiemarc.marcreading.bean.ChapterRead;
import com.xiemarc.marcreading.utils.FileUtils;
import com.xiemarc.marcreading.utils.StringUtils;

import java.io.File;

/**
 * 描述：缓存类
 * 作者：Marc on 2016/11/29 17:45
 * 邮箱：aliali_ha@yeah.net
 */
public class CacheManager {
    private static CacheManager manager;

    //获得单利模式
    public static CacheManager getInstance() {
        return manager == null ? (manager = new CacheManager()) : manager;
    }

    /**
     * 获得保存的章节文件
     *
     * @param bookId
     * @param chapter
     * @return
     */
    public File getChapterFile(String bookId, int chapter) {
        File file = FileUtils.getChapterFile(bookId, chapter);
        if (file != null && file.length() > 50)
            return file;
        return null;
    }

    /**
     * 保存章节文件
     *
     * @param bookId
     * @param chapter
     * @param data
     */
    public void saveChapterFile(String bookId, int chapter, ChapterRead.Chapter data) {
        File file = FileUtils.getChapterFile(bookId, chapter);
        FileUtils.writeFile(file.getAbsolutePath(), StringUtils.formatContent(data.body), false);
    }


}
