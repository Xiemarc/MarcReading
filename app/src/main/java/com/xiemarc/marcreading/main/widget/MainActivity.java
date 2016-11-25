package com.xiemarc.marcreading.main.widget;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.xiemarc.marcreading.R;
import com.xiemarc.marcreading.base.BaseActivity;
import com.xiemarc.marcreading.main.presenter.MainPresenter;
import com.xiemarc.marcreading.main.view.MainView;
import com.xiemarc.marcreading.manager.FragmentFactory;
import com.xiemarc.marcreading.utils.UIUtils;
import com.xiemarc.marcreading.widget.CustomeIndicator;
import com.xiemarc.marcreading.widget.GenderPopupWindow;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;

public class MainActivity extends BaseActivity<MainView, MainPresenter> implements MainView {

    @Bind(R.id.viewpage)
    ViewPager mViewPager;
    @Bind(R.id.container)
    LinearLayout mContainer;
    @Bind(R.id.indicator)
    CustomeIndicator mIndicator;
    //首页指示器的集合
    private List<String> mDatas;
    //首页的3个fragment的集合
    private FragmentPagerAdapter mAdapter;
    /**
     * 选择男和女的对话框
     */
    private GenderPopupWindow mGenderPopWindow;

//    @Override
//    protected boolean isApplyKitKatTranslucency() {
//        return true;
//    }

    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter(this);
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected int getContentViewLayoutID() {
//        statusBarColor = ContextCompat.getColor(this, R.color.colorAccent);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return R.layout.activity_main;
    }

    @Override
    protected View getLoadingTargetView() {
        return mViewPager;
    }

    @Override
    protected void initViewsAndEvents() {
        mToolbar.setLogo(R.drawable.menu);
        mToolbar.setTitle("");
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        //第一次进入弹出男女选择对话框
        initDatas();
        configViews();
    }


    /**
     * 初始化数据
     */
    private void initDatas() {
        mDatas = Arrays.asList(UIUtils.getResoucer().getStringArray(R.array.home_tabs));
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return FragmentFactory.getMainFragment(position);
            }

            @Override
            public int getCount() {
                return mDatas.size();
            }
        };
    }

    /**
     * 初始化view
     */
    private void configViews() {
        //设置indicator的标题
        mIndicator.setTabItemTitles(mDatas);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(3);
        mIndicator.setViewPager(mViewPager, 0);
        //弹出选择男女对话框
        mIndicator.postDelayed(() ->
                //延迟500毫秒 弹出选择男女对话框
                        showChooseSexPopWindow()
                , 500);
    }

    private void showChooseSexPopWindow() {
        if (null == mGenderPopWindow) {
            mGenderPopWindow = new GenderPopupWindow(MainActivity.this);
        }
        if (!mGenderPopWindow.isShowing()) {
            mGenderPopWindow.showAtLocation(mToolbar, Gravity.CENTER, 0, 0);
        }

    }

    /**
     * 子fragment使用
     *
     * @param position
     */
    public void setCurrentItem(int position) {
        mViewPager.setCurrentItem(position);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        setIconsVisible(menu,true);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * 解决menu不显示图标问题
     * @param menu
     * @param flag
     */
    private void setIconsVisible(Menu menu, boolean flag) {
        //判断menu是否为空
        if(menu != null) {
            try {
                //如果不为空,就反射拿到menu的setOptionalIconsVisible方法
                Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                //暴力访问该方法
                method.setAccessible(true);
                //调用该方法显示icon
                method.invoke(menu, flag);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_login:

                break;
            case R.id.action_my_message:
                break;
            case R.id.action_sync_bookshelf:
                break;
            case R.id.action_scan_local_book:
                break;
            default:

                break;
        }
        showToast(item.getTitle().toString());
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void loginSuccess() {

    }

    @Override
    public void syncBookShelfCompleted() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (FragmentFactory.getListFragment() != null && FragmentFactory.getListFragment().size() > 0) {
            FragmentFactory.removeAllFragment();
        }
    }

}
