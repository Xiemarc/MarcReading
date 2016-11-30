package com.marc.marclibs.richtext.callback;


import com.marc.marclibs.richtext.ImageHolder;

/**
 * Created by zhou on 16-5-28.
 * ImageFixCallback
 */
public interface ImageFixCallback {
    /**
     * 修复图片尺寸的方法
     *
     * @param holder ImageHolder对象
     */
    void onFix( ImageHolder holder);
}
