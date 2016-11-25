package com.xiemarc.marcreading.bookread.listener;

import android.app.Activity;
import android.widget.SeekBar;

import com.xiemarc.marcreading.manager.SettingManager;
import com.xiemarc.marcreading.utils.ScreenUtils;

/**
 * 描述：字体大小和亮度调节的seekbar改变监听器
 * 作者：Marc on 2016/11/25 15:04
 * 邮箱：aliali_ha@yeah.net
 */
public class FontAndBrightnessSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {

    private SeekBar fontSeekBar;//调节字体的seekbar
    private SeekBar barLightSeekBar;//调节亮度的seekbar
    private Activity mActivity;//要改变亮度的activity

    /**
     * 使用依赖注入的方式
     *
     * @param fontSeekBar
     * @param barLightSeekBar
     */
    public FontAndBrightnessSeekBarChangeListener(SeekBar fontSeekBar, SeekBar barLightSeekBar, Activity activity) {
        this.fontSeekBar = fontSeekBar;
        this.barLightSeekBar = barLightSeekBar;
        this.mActivity = activity;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (seekBar.getId() == fontSeekBar.getId() && fromUser) {
            calcFontSize(progress);
        } else if (seekBar.getId() == barLightSeekBar.getId() && fromUser &&
                !SettingManager.getInstance().isAutoBrightness()) {
            // 非自动调节模式下 才可调整屏幕亮度
            ScreenUtils.setScreenBrightness(progress, mActivity);
            SettingManager.getInstance().saveReadBrightness(progress);
        }
    }

    /**
     * 计算字体大小
     *
     * @param progress
     */
    private void calcFontSize(int progress) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
