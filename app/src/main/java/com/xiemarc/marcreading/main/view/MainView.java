package com.xiemarc.marcreading.main.view;

import com.xiemarc.marcreading.base.BaseView;

/**
 * des:首页的view
 * author: marc
 * date:  2016/11/14 22:48
 * email：aliali_ha@yeah.net
 */

public interface MainView extends BaseView{
    /**
     * 登录成功
     */
    void loginSuccess();

    /**
     * 同步书籍
     */
    void syncBookShelfCompleted();
}
