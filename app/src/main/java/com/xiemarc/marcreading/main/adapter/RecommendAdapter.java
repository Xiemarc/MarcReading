package com.xiemarc.marcreading.main.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.xiemarc.marcreading.R;
import com.xiemarc.marcreading.bean.Recommend;
import com.xiemarc.marcreading.manager.Constant;
import com.xiemarc.marcreading.manager.SettingManager;
import com.xiemarc.marcreading.recycleview.adapter.BaseViewHolder;
import com.xiemarc.marcreading.recycleview.adapter.RecyclerArrayAdapter;
import com.xiemarc.marcreading.utils.FileUtils;
import com.xiemarc.marcreading.utils.FormatUtils;

import java.text.NumberFormat;

/**
 * 描述：书架的adapter
 * 作者：Marc on 2016/11/23 18:27
 * 邮箱：aliali_ha@yeah.net
 */
public class RecommendAdapter extends RecyclerArrayAdapter<Recommend.RecommendBooks> {
    public RecommendAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseViewHolder<Recommend.RecommendBooks>(parent, R.layout.item_recommend_list) {
            @Override
            public void setData(final Recommend.RecommendBooks item) {
                super.setData(item);
                String latelyUpdate = "";
                if (!TextUtils.isEmpty(FormatUtils.getDescriptionTimeFromDateString(item.updated))) {
                    latelyUpdate = FormatUtils.getDescriptionTimeFromDateString(item.updated) + ":";
                }
                holder.setText(R.id.tvRecommendTitle, item.title)
                        .setText(R.id.tvLatelyUpdate, latelyUpdate)
                        .setText(R.id.tvRecommendShort, item.lastChapter)
                        .setVisible(R.id.ivTopLabel, item.isTop)
                        .setVisible(R.id.ckBoxSelect, item.showCheckBox)
                        .setVisible(R.id.ivUnReadDot, FormatUtils.formatZhuiShuDateString(item.updated)
                                .compareTo(item.recentReadingTime) > 0);
                if (item.isFromSD) {
                    holder.setImageResource(R.id.ivRecommendCover, R.drawable.home_shelf_txt_icon);
                    long fileLen = FileUtils.getChapterFile(item._id, 1).length();
                    if (fileLen > 10) {
                        double progress = ((double) SettingManager.getInstance().getReadProgress(item._id)[2]) / fileLen;
                        NumberFormat fmt = NumberFormat.getPercentInstance();
                        fmt.setMaximumFractionDigits(2);
                        holder.setText(R.id.tvRecommendShort, "当前阅读进度：" + fmt.format(progress));
                    }
                } else if (!SettingManager.getInstance().isNoneCover()) {
                    holder.setRoundImageUrl(R.id.ivRecommendCover, Constant.IMG_BASE_URL + item.cover,
                            R.drawable.cover_default);
                } else {
                    holder.setImageResource(R.id.ivRecommendCover, R.drawable.cover_default);
                }

                CheckBox ckBoxSelect = holder.getView(R.id.ckBoxSelect);
                ckBoxSelect.setChecked(item.isSeleted);
                ckBoxSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        if (isChecked) {
                            item.isSeleted = true;
                        } else {
                            item.isSeleted = false;
                        }
                    }
                });
            }
        };
    }
}
