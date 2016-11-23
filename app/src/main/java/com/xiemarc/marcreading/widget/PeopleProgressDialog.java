package com.xiemarc.marcreading.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiemarc.marcreading.R;


/**
 * 描述：logo加载动画
 * 邮箱：aliali_ha@yeah.net
 */
public class PeopleProgressDialog extends Dialog {

    private Context context = null;
    private int anim = 0;
    private static PeopleProgressDialog commProgressDialog = null;

    public PeopleProgressDialog(Context context) {
        super(context);
        this.context = context;
    }

    public PeopleProgressDialog(Context context, int theme, int anim) {
        super(context, theme);
        this.anim = anim;
    }

    public static PeopleProgressDialog createDialog(Context context, int anim) {
        commProgressDialog = new PeopleProgressDialog(context, R.style.CommProgressDialog, anim);
        commProgressDialog.setContentView(R.layout.people_progress_dialog);
        commProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        return commProgressDialog;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (commProgressDialog == null) {
            return;
        }
        ImageView imageView = (ImageView) commProgressDialog.findViewById(R.id.loadingIv);
        if (anim != 0) {
            imageView.setBackgroundResource(anim);
        }
        AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();
        animationDrawable.start();
    }

    /**
     * 设置标题
     *
     * @param strTitle
     * @return
     */
    public PeopleProgressDialog setTitile(String strTitle) {
        return commProgressDialog;
    }

    /**
     * 设置提示内容
     *
     * @param strMessage
     * @return 66.
     */
    public PeopleProgressDialog setMessage(String strMessage) {
        TextView tvMsg = (TextView) commProgressDialog.findViewById(R.id.loadingTv);
        if (tvMsg != null) {
            tvMsg.setText(strMessage);
        }
        return commProgressDialog;
    }

    /**
     * 屏蔽返回键
     **/
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
            return true;
        return super.onKeyDown(keyCode, event);
    }

    //    private AnimationDrawable mAnimation;
//
//    private Context mContext;
//    private ImageView mImageView;
//    private String mLoadingTip;
//    private TextView mLoadingTv;
//    private int count = 0;
//    private String oldLoadingTip;
//    private int mResid;
//
//    private static PeopleProgressDialog commProgressDialog = null;
//
//
//    public PeopleProgressDialog(Context context, String content, int theme,int id) {
//        super(context);
//        this.mContext = context;
//        this.mLoadingTip = content;
//        this.mResid = id;
////        setCanceledOnTouchOutside(true);//外部是否可以取消
//    }
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        initView();
//        initData();
//    }
//
//    private void initView() {
//        setContentView(R.layout.people_progress_dialog);
//        mLoadingTv = (TextView) findViewById(R.id.loadingTv);
//        mImageView = (ImageView) findViewById(R.id.loadingIv);
//    }
//
//    private void initData() {
//
//        mImageView.setBackgroundResource(mResid);
//        // 通过ImageView对象拿到背景显示的AnimationDrawable
//        mAnimation = (AnimationDrawable) mImageView.getBackground();
//        // 为了防止在onCreate方法中只显示第一帧的解决方案之一
//        mImageView.post(new Runnable() {
//            @Override
//            public void run() {
//                mAnimation.start();
//            }
//        });
//        mLoadingTv.setText(mLoadingTip);
//    }
//
//    /**
//     * 设置加载显示的值
//     *
//     * @param str
//     */
//    public void setContent(String str) {
//        mLoadingTv.setText(str);
//    }


}
