package com.xiemarc.marcreading.main.view;

import com.xiemarc.marcreading.base.BaseView;
import com.xiemarc.marcreading.bean.Recommend;

import java.util.List;

/**
 * des:推荐的view
 * author: marc
 * date:  2016/11/15 20:00
 * email：aliali_ha@yeah.net
 */

public interface RecommendView extends BaseView{
    /**
     * 展示书架的书架
     * @param list
     */
    void showRecommendList(List<Recommend.RecommendBooks> list);

    /**
     * 结束的时候关闭刷新
     */
    void complete();
}
