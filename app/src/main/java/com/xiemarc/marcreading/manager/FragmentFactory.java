package com.xiemarc.marcreading.manager;

import android.support.v4.util.SparseArrayCompat;

import com.xiemarc.marcreading.base.BaseFragment;
import com.xiemarc.marcreading.main.widget.CommunityFragment;
import com.xiemarc.marcreading.main.widget.FindFragment;
import com.xiemarc.marcreading.main.widget.RecommendFragment;

/**
 * 描述：fragment工厂类，为了不频繁创建销毁，节省内存
 * 作者：Marc on 2016/11/15 17:04
 * 邮箱：aliali_ha@yeah.net
 */
public class FragmentFactory {
    public static final int FRAGMENT_RECOMMEND = 0;
    public static final int FRAGMENT_COMMUNITY = 1;
    public static final int FRAGMENT_FIND = 2;
    /**
     * 首页的3个fragment
     */
    static SparseArrayCompat<BaseFragment> cachesMainFragment = new SparseArrayCompat<>();

    public static BaseFragment getMainFragment(int position) {
        BaseFragment fragment = null;
        if (null == cachesMainFragment) {
            cachesMainFragment = new SparseArrayCompat<>();
        }
        //如果缓存里面有对应的fragment，就直接取出返回，没有就实例化
        BaseFragment tempFragment = cachesMainFragment.get(position);
        if (tempFragment != null) {
            fragment = tempFragment;
            return fragment;
        }
        //没有就创建并放到集合中
        switch (position) {
            case FRAGMENT_RECOMMEND:
                fragment = new RecommendFragment();
                break;
            case FRAGMENT_COMMUNITY:
                fragment = new CommunityFragment();
                break;
            case FRAGMENT_FIND:
                fragment = new FindFragment();
                break;
        }
        //保存到集合中
        cachesMainFragment.put(position, fragment);
        return fragment;
    }


    public static void removeAllFragment() {
        if (cachesMainFragment != null && cachesMainFragment.size() > 0) {
            cachesMainFragment.clear();
            cachesMainFragment = null;
        }
    }

    public static SparseArrayCompat<BaseFragment> getListFragment() {
        if (cachesMainFragment != null && cachesMainFragment.size() > 0) {
            return cachesMainFragment;
        }
        return null;
    }

}
