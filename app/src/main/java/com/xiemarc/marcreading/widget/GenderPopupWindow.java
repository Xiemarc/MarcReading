package com.xiemarc.marcreading.widget;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.xiemarc.marcreading.R;
import com.xiemarc.marcreading.manager.Constant;
import com.xiemarc.marcreading.manager.SettingManager;


/**
 * 描述：选择性别男女的popwindow
 * 作者：Marc on 2016/11/15 15:29
 * 邮箱：aliali_ha@yeah.net
 */
public class GenderPopupWindow extends PopupWindow {

    private View mContentView;
    private Button mBtnMale;
    private Button mBtnFamale;
    private ImageView mIvClose;
    private Activity mActivity;

    public GenderPopupWindow(Activity activity) {
        this.mActivity = activity;
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mContentView = LayoutInflater.from(mActivity).inflate(R.layout.layout_gender_popup_window, null);
        setContentView(mContentView);
        setFocusable(true);
        setOutsideTouchable(true);
        setTouchable(true);
        setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        //添加动画
        setAnimationStyle(R.style.LoginPopup);
        mBtnMale = (Button) mContentView.findViewById(R.id.mBtnMale);
        mBtnFamale = (Button) mContentView.findViewById(R.id.mBtnFemale);
        mIvClose = (ImageView) mContentView.findViewById(R.id.mIvClose);
        mIvClose.setOnClickListener(view -> dismiss());
        mBtnFamale.setOnClickListener(view -> {
            //点击设置成选择女性
            SettingManager.getInstance().saveUserChooseSex(Constant.Gender.FEMALE);
            dismiss();
        });
        mBtnMale.setOnClickListener(view -> {
            //点击设置成选择男性
            SettingManager.getInstance().saveUserChooseSex(Constant.Gender.MALE);
            dismiss();
        });
        setOnDismissListener(()->{
            //屏幕背景恢复亮度
            lighton();
        });
    }
    /**
     * 背景变暗
     */
    private void lightoff() {
        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        lp.alpha = 0.8f;
        mActivity.getWindow().setAttributes(lp);
    }

    /**
     * 背景透明度不变
     */
    private void lighton() {
        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        lp.alpha = 1.0f;
        mActivity.getWindow().setAttributes(lp);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        lightoff();
        super.showAsDropDown(anchor);
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        lightoff();
        super.showAtLocation(parent, gravity, x, y);
    }
}
