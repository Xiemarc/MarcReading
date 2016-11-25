package com.xiemarc.marcreading.bookread.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.xiemarc.marcreading.bean.support.ReadTheme;
import com.xiemarc.marcreading.recycleview.adapter.BaseViewHolder;
import com.xiemarc.marcreading.recycleview.adapter.RecyclerArrayAdapter;

/**
 * 描述：阅读的主题的adapter
 * 作者：Marc on 2016/11/25 15:43
 * 邮箱：aliali_ha@yeah.net
 */
public class ReadThemeAdapter extends RecyclerArrayAdapter<ReadTheme> {
    //当前选中的主题
    private int selected = 0;

    public ReadThemeAdapter(Context context, int selected) {
        super(context);
        this.selected = selected;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {

        return null;
    }
}
