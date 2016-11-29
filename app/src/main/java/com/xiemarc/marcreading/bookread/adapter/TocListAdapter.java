package com.xiemarc.marcreading.bookread.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiemarc.marcreading.R;
import com.xiemarc.marcreading.bean.BookToc;
import com.xiemarc.marcreading.recycleview.adapter.BaseViewHolder;
import com.xiemarc.marcreading.recycleview.adapter.RecyclerArrayAdapter;
import com.xiemarc.marcreading.utils.FileUtils;
import com.xiemarc.marcreading.utils.UIUtils;

/**
 * 描述：bottom弹出章节的adapter
 * 作者：Marc on 2016/11/25 14:26
 * 邮箱：aliali_ha@yeah.net
 */
public class TocListAdapter extends RecyclerArrayAdapter<BookToc.mixToc.Chapters> {
    //当前章节
    private int currentChapter;
    //书籍的id
    private String bookId;

    public TocListAdapter(Context context, int currentChapter, String bookId) {
        super(context);
        this.currentChapter = currentChapter;
        this.bookId = bookId;
    }


    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder<BookToc.mixToc.Chapters> holder = new BaseViewHolder<BookToc.mixToc.Chapters>(parent, R.layout.item_book_read_toc_list) {
            @Override
            public void setData(BookToc.mixToc.Chapters item) {
                super.setData(item);
                TextView tvTocItem = holder.getView(R.id.tvTocItem);//章节名称
                tvTocItem.setText(item.title);
                Drawable drawable;
                //动态设置textview左侧的drawable
                int position = holder.getLayoutPosition();
                if (currentChapter == position + 1) {
                    //如果是当前章节。就设置成前面是红色的drawable
                    tvTocItem.setTextColor(UIUtils.getColor(R.color.light_red));
                    drawable = ContextCompat.getDrawable(mContext, R.drawable.ic_toc_item_activated);
                } else if (FileUtils.getChapterFile(bookId, position + 1).length() > 10) {
                    //如果是缓存的章节
                    tvTocItem.setTextColor(ContextCompat.getColor(mContext, R.color.light_black));
                    drawable = ContextCompat.getDrawable(mContext, R.drawable.ic_toc_item_download);
                } else {
                    tvTocItem.setTextColor(ContextCompat.getColor(mContext, R.color.light_black));
                    drawable = ContextCompat.getDrawable(mContext, R.drawable.ic_toc_item_normal);
                }
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tvTocItem.setCompoundDrawables(drawable, null, null, null);
            }
        };
        return holder;
    }

    /**
     * 向外暴露方法，设置当前章节
     *
     * @param chapter
     */
    public void setCurrentChapter(int chapter) {
        currentChapter = chapter;
        notifyDataSetChanged();
    }
}
