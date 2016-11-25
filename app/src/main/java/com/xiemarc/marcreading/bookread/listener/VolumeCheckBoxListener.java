package com.xiemarc.marcreading.bookread.listener;

import android.app.Activity;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.xiemarc.marcreading.bookread.widget.ReadActivity;
import com.xiemarc.marcreading.manager.SettingManager;

/**
 * 描述：音量键是否控制翻页监听
 * 作者：Marc on 2016/11/25 15:32
 * 邮箱：aliali_ha@yeah.net
 */
public class VolumeCheckBoxListener implements CompoundButton.OnCheckedChangeListener {
    private CheckBox cbVolume;//音量键
    private CheckBox cbAutoBrightness;//自动亮度
    private ReadActivity mActivity;
    public VolumeCheckBoxListener(CheckBox cbVolume, CheckBox cbAutoBrightness,Activity activity) {
        this.cbVolume = cbVolume;
        this.cbAutoBrightness = cbAutoBrightness;
        //强制转化为ReadActivity
        this.mActivity = (ReadActivity) activity;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(buttonView.getId() == cbVolume.getId()){
            //如果是音量键checkbox
            SettingManager.getInstance().saveVolumeFlipEnable(isChecked);
        }else if(buttonView.getId() == cbAutoBrightness.getId()){
            if (isChecked) {
                mActivity.startAutoLightness();
            } else {
                mActivity.stopAutoLightness();
            }
        }
    }

}
