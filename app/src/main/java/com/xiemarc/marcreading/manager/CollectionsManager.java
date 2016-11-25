package com.xiemarc.marcreading.manager;

import android.text.TextUtils;

import com.marc.marclibs.utils.ACache;
import com.xiemarc.marcreading.bean.Recommend;
import com.xiemarc.marcreading.utils.FormatUtils;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述：收藏列表管理
 * 作者：Marc on 2016/11/24 15:50
 * 邮箱：aliali_ha@yeah.net
 */
public class CollectionsManager {

    private volatile static CollectionsManager singleton;

    private CollectionsManager() {

    }

    public static CollectionsManager getInstance() {
        if (singleton == null) {
            synchronized (CollectionsManager.class) {
                if (singleton == null) {
                    singleton = new CollectionsManager();
                }
            }
        }
        return singleton;
    }

    /**
     * 获取收藏列表
     *
     * @return
     */
    public List<Recommend.RecommendBooks> getCollectionList() {
        List<Recommend.RecommendBooks> list = (ArrayList<Recommend.RecommendBooks>) ACache.get(new File(Constant.PATH_COLLECT))
                .getAsObject("collection");
        return list == null ? null : list;
    }

    public void putCollectionList(List<Recommend.RecommendBooks> list) {
        ACache.get(new File(Constant.PATH_COLLECT)).put("collection", (Serializable) list);
    }

    /**
     * 设置最近阅读时间
     *
     * @param bookId
     */
    public void setRecentReadingTime(String bookId) {
        List<Recommend.RecommendBooks> list = getCollectionList();
        if (list == null) {
            return;
        }
        for (Recommend.RecommendBooks bean : list) {
            if (TextUtils.equals(bean._id, bookId)) {
                bean.recentReadingTime = FormatUtils.getCurrentTimeString(FormatUtils.FORMAT_DATE_TIME);
                list.remove(bean);
                list.add(bean);
                putCollectionList(list);
                break;
            }
        }
    }

}
