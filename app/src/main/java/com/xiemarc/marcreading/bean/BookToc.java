package com.xiemarc.marcreading.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 描述：目录 实体entry
 * 作者：Marc on 2016/11/24 13:55
 * 邮箱：aliali_ha@yeah.net
 */
public class BookToc extends Base {
    /**
     * _id:577e528e2160421a02d7380d
     * name:优质书源
     * link:http://vip.zhuishushenqi.com/toc/577e528e2160421a02d7380d
     */
    public mixToc mixToc;
    public static class mixToc implements Serializable {
        public String _id;
        public String book;
        public String chaptersUpdated;
        /**
         * title : 第一章 死在万花丛中
         * link : http://vip.zhuishushenqi.com/chapter/577e5290260289ff64a29213?cv=1467896464908
         * id : 577e5290260289ff64a29213
         * currency : 15
         * unreadble : false
         * isVip : false
         */

        public List<Chapters> chapters;

        public static class Chapters implements Serializable {
            public String title;
            public String link;
            public String id;
            public int currency;
            public boolean unreadble;
            public boolean isVip;
        }
    }
}
