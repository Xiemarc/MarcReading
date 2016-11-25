package com.xiemarc.marcreading.bookread.widget;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.ListPopupWindow;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.xiemarc.marcreading.R;
import com.xiemarc.marcreading.base.BaseActivity;
import com.xiemarc.marcreading.bean.BookSource;
import com.xiemarc.marcreading.bean.BookToc;
import com.xiemarc.marcreading.bean.ChapterRead;
import com.xiemarc.marcreading.bean.Recommend;
import com.xiemarc.marcreading.bean.support.BookMark;
import com.xiemarc.marcreading.bean.support.ReadTheme;
import com.xiemarc.marcreading.bookread.adapter.TocListAdapter;
import com.xiemarc.marcreading.bookread.listener.FontAndBrightnessSeekBarChangeListener;
import com.xiemarc.marcreading.bookread.listener.VolumeCheckBoxListener;
import com.xiemarc.marcreading.bookread.presenter.BookReadPresenter;
import com.xiemarc.marcreading.bookread.view.BookReadView;
import com.xiemarc.marcreading.manager.CollectionsManager;
import com.xiemarc.marcreading.manager.SettingManager;
import com.xiemarc.marcreading.manager.ThemeManager;
import com.xiemarc.marcreading.utils.ScreenUtils;
import com.xiemarc.marcreading.utils.UIUtils;
import com.xiemarc.marcreading.view.readview.BaseReadView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 描述：阅读的activity
 * 作者：Marc on 2016/11/24 14:29
 * 邮箱：aliali_ha@yeah.net
 */
public class ReadActivity extends BaseActivity<BookReadView, BookReadPresenter> implements BookReadView {


    @Bind(R.id.ivBack)
    ImageView mIvBack;//返回键
    @Bind(R.id.tvBookReadReading)
    TextView mTvBookReadReading;//头部布局的 朗读textview
    @Bind(R.id.tvBookReadCommunity)
    TextView mTvBookReadCommunity;//头部布局的社区textdview
    @Bind(R.id.tvBookReadIntroduce)
    TextView mTvBookReadChangeSource;//头部布局的简介textview

    @Bind(R.id.flReadWidget)
    FrameLayout flReadWidget;//加载阅读自定义控件

    @Bind(R.id.llBookReadTop)
    LinearLayout mLlBookReadTop;// 顶部的父布局
    @Bind(R.id.tvBookReadTocTitle)
    TextView mTvBookReadTocTitle;// 顶部的书名textview
    @Bind(R.id.tvBookReadMode)
    TextView mTvBookReadMode;//底部的显示夜间白天的textdview
    @Bind(R.id.tvBookReadSettings)
    TextView mTvBookReadSettings;//底部的显示设置的textview
    @Bind(R.id.tvBookReadDownload)
    TextView mTvBookReadDownload;//底部的显示缓存下载的textview
    @Bind(R.id.tvBookReadToc)
    TextView mTvBookReadToc;//底部的显示目录的textview
    @Bind(R.id.llBookReadBottom)
    LinearLayout mLlBookReadBottom;//底部的父布局
    @Bind(R.id.rlBookReadRoot)
    RelativeLayout mRlBookReadRoot;// 根布局
    @Bind(R.id.tvDownloadProgress)
    TextView mTvDownloadProgress;//显示加载进度的textview

    @Bind(R.id.rlReadAaSet)
    LinearLayout rlReadAaSet;//字体设置的 linearlayout布局
    @Bind(R.id.ivBrightnessMinus)
    ImageView ivBrightnessMinus;// 亮度模式减小的imageview（点击progress减小）
    @Bind(R.id.seekbarLightness)
    SeekBar seekbarLightness;//调整亮度的progress
    @Bind(R.id.ivBrightnessPlus)
    ImageView ivBrightnessPlus;//亮度正大的imageview（点击progress增大）
    @Bind(R.id.tvFontsizeMinus)
    TextView tvFontsizeMinus;//字体减小的  textview
    @Bind(R.id.seekbarFontSize)
    SeekBar seekbarFontSize;//字体调整的progress
    @Bind(R.id.tvFontsizePlus)
    TextView tvFontsizePlus;//字体增大的textview

    @Bind(R.id.rlReadMark)
    LinearLayout rlReadMark;//书签的linearlayout
    @Bind(R.id.tvAddMark)
    TextView tvAddMark;//添加书签的textview
    @Bind(R.id.lvMark)
    ListView lvMark;//书签列表

    @Bind(R.id.cbVolume)
    CheckBox cbVolume;//音量翻页键CheckBox
    @Bind(R.id.cbAutoBrightness)
    CheckBox cbAutoBrightness;//自动调整亮度checkbix
    @Bind(R.id.gvTheme)
    GridView gvTheme;//加载主题背景的gridview

    private View decodeView;
    private List<BookToc.mixToc.Chapters> mChapterList = new ArrayList<>();//网络获取到的章节集合
    private ListPopupWindow mTocListPopupWindow;//显示目录的popwindow弹出框
    private TocListAdapter mTocListAdapter;

    private List<BookMark> mMarkList;
//    private BookMarkAdapter mMarkAdapter;

    private int currentChapter = 1;

    /**
     * 是否开始阅读章节
     **/
    private boolean startRead = false;

//    /**
//     * 朗读 播放器
//     */
//    private TTSPlayer mTtsPlayer;
//    private TtsConfig ttsConfig;

    private BaseReadView mPageWidget;
    private int curTheme = -1;
    private List<ReadTheme> themes;
    //    private ReadThemeAdapter gvAdapter;
//    private Receiver receiver = new Receiver();
    private IntentFilter intentFilter = new IntentFilter();
    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

    public static final String INTENT_BEAN = "recommendBooksBean";
    public static final String INTENT_SD = "isFromSD";

    private Recommend.RecommendBooks recommendBooks;
    private String bookId;

    private boolean isAutoLightness = false; // 记录其他页面是否自动调整亮度
    private boolean isFromSD = false;

    @Override
    protected BookReadPresenter createPresenter() {
        return new BookReadPresenter(this);
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        //取出传递过来的数据
        recommendBooks = (Recommend.RecommendBooks) getIntent().getSerializableExtra(INTENT_BEAN);
        bookId = recommendBooks._id;
        isFromSD = getIntent().getBooleanExtra(INTENT_SD, false);
    }

    @Override
    protected int getContentViewLayoutID() {
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //状态栏设置成黑色
        statusBarColor = ContextCompat.getColor(this, R.color.reader_menu_bg_color);
        return R.layout.activity_read;
    }

    @Override
    protected View getLoadingTargetView() {
        return flReadWidget;
    }

    @Override
    protected void initViewsAndEvents() {
        //设置书籍名
        mTvBookReadTocTitle.setText(recommendBooks.title);
        if (Intent.ACTION_VIEW.equals(getIntent().getAction())) {
            String filePath = Uri.decode(getIntent().getDataString().replace("file://", ""));
            String fileName;
            if (filePath.lastIndexOf(".") > filePath.lastIndexOf("/")) {
                fileName = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.lastIndexOf("."));
            } else {
                fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
            }
//            CollectionsManager.getInstance().remove(fileName);
        }
        //添加电池
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        //添加时间
        intentFilter.addAction(Intent.ACTION_TIME_TICK);
        //设置最近阅读时间
        CollectionsManager.getInstance().setRecentReadingTime(bookId);
//        Observable.timer(1000, TimeUnit.MILLISECONDS)
//                .subscribe(aLong ->
//                //延迟1s加载书架
//
//                );
        //隐藏状态栏
        hideStatusBar();
        decodeView = getWindow().getDecorView();
        //设置顶部布局的高度比状态栏高度小2个单位
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mLlBookReadTop.getLayoutParams();
        params.topMargin = ScreenUtils.getStatusBarHeight(this) - 2;
        mLlBookReadTop.setLayoutParams(params);

        initTocList();

        initAASet();

        initPagerWidget();
        //加载书籍
        mPresenter.getBookToc(bookId, "chapters");
    }

    /**
     * 初始化章节的popwindowlist
     */
    private void initTocList() {
        mTocListPopupWindow = new ListPopupWindow(this);
        mTocListPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mTocListPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置ListPopupWindow的锚点，即关联PopupWindow的显示位置和这个锚点
        mTocListPopupWindow.setAnchorView(mLlBookReadTop);
        //放入章节的adapter\
        mTocListAdapter = new TocListAdapter(UIUtils.getContext(), currentChapter, bookId);
        mTocListPopupWindow.setAdapter(null);
        //添加关闭listpopwindow的监听
        mTocListPopupWindow.setOnDismissListener(() -> {
            //把显示书名的texdtview隐藏
            gone(mTvBookReadTocTitle);
            visible(mTvBookReadReading, mTvBookReadCommunity, mTvBookReadChangeSource);
        });
        //点击
        mTocListPopupWindow.setOnItemClickListener((adapterView, view, position, id) -> {
            mTocListPopupWindow.dismiss();
            currentChapter = position + 1;
            mTocListAdapter.setCurrentChapter(currentChapter);
            startRead = false;
            readCurrentChapter();
            hideReadBar();
        });
        mTocListAdapter.setOnItemClickListener(position -> {
            mTocListPopupWindow.dismiss();
            currentChapter = position + 1;
            mTocListAdapter.setCurrentChapter(currentChapter);
            startRead = false;
            readCurrentChapter();
            hideReadBar();
        });
    }

    /**
     * 读取currentChapter章节。章节文件存在则直接阅读，不存在则请求
     */
    public void readCurrentChapter() {
//        先不考虑缓存
//        if (CacheManager.getInstance().getChapterFile(bookId, currentChapter) != null) {
//            showChapterRead(null, currentChapter);
//        } else {
        mPresenter.getChapterRead(mChapterList.get(currentChapter - 1).link, currentChapter);
//        }
    }

    /**
     * 初始化设置字体和亮度
     */
    private void initAASet() {
        //当前阅读的主题
        curTheme = SettingManager.getInstance().getReadTheme();
        ThemeManager.setReaderTheme(curTheme, mRlBookReadRoot);
        seekbarFontSize.setMax(10);//设置最大字体为10
        //得到字体像素大小
        int fontSizePx = SettingManager.getInstance().getReadFontSize();
        int progress = (int) ((ScreenUtils.pxToDpInt(fontSizePx) - 12) / 1.7f);
        seekbarFontSize.setProgress(progress);
        seekbarFontSize.setOnSeekBarChangeListener(new FontAndBrightnessSeekBarChangeListener
                (seekbarFontSize, seekbarLightness, ReadActivity.this));
        // 亮度
        seekbarLightness.setMax(100);
        seekbarLightness.setOnSeekBarChangeListener(new FontAndBrightnessSeekBarChangeListener
                (seekbarFontSize, seekbarLightness, ReadActivity.this));
        seekbarLightness.setProgress(SettingManager.getInstance().getReadBrightness());
        isAutoLightness = ScreenUtils.isAutoBrightness(this);
        //如果开启了自动亮度
        if (SettingManager.getInstance().isAutoBrightness())
            startAutoLightness();
        else
            stopAutoLightness();

        //音量键控制翻页
        cbVolume.setChecked(SettingManager.getInstance().isVolumeFlipEnable());
        cbVolume.setOnCheckedChangeListener(new VolumeCheckBoxListener(cbVolume, cbAutoBrightness, ReadActivity.this));

        cbAutoBrightness.setChecked(SettingManager.getInstance().isAutoBrightness());
        cbAutoBrightness.setOnCheckedChangeListener(new VolumeCheckBoxListener(cbVolume, cbAutoBrightness, ReadActivity.this));
//        gvAdapter = new ReadThemeAdapter(this, (themes = ThemeManager.getReaderThemeData(curTheme)), curTheme);
    }


    /**
     * 初始化读书页面
     */
    private void initPagerWidget() {
        你好();
    }
    public void 你好(){
    }

    //添加收藏需要，所以跳转的时候传递整个实体类
    public static void startActivity(Context context, Recommend.RecommendBooks recommendBooks) {
        startActivity(context, recommendBooks, false);
    }

    // 开始阅读
    public static void startActivity(Context context, Recommend.RecommendBooks recommendBooks, boolean isFromSD) {
        context.startActivity(new Intent(context, ReadActivity.class)
                .putExtra(INTENT_BEAN, recommendBooks)
                .putExtra(INTENT_SD, isFromSD));
    }

    @Override
    public void showBookToc(List<BookToc.mixToc.Chapters> list) {
        mChapterList.clear();
        mChapterList.addAll(list);

//        readCurrentChapter();
    }

    @Override
    public void showChapterRead(ChapterRead.Chapter data, int chapter) {

    }

    @Override
    public void showBookSource(List<BookSource> list) {

    }

    /**
     * 隐藏阅读书籍的那些 各种bar
     */
    private synchronized void hideReadBar() {
        //缓存提示的， 底部的栏，顶部的栏。字体设置 .书签设置
        gone(mTvDownloadProgress, mLlBookReadBottom, mLlBookReadTop, rlReadAaSet, rlReadMark);
        //同时隐藏状态栏
        hideStatusBar();
        //动态设置状态栏
//        setSystemUiVisibility(int visibility)方法可传入的实参为：
//
//        1. View.SYSTEM_UI_FLAG_VISIBLE：显示状态栏，Activity不全屏显示(恢复到有状态的正常情况)。
//
//        2. View.INVISIBLE：隐藏状态栏，同时Activity会伸展全屏显示。
//
//        3. View.SYSTEM_UI_FLAG_FULLSCREEN：Activity全屏显示，且状态栏被隐藏覆盖掉。
//
//        4. View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN：Activity全屏显示，但状态栏不会被隐藏覆盖，状态栏依然可见，Activity顶端布局部分会被状态遮住。
//
//        5. View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION：效果同View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//
//        6. View.SYSTEM_UI_LAYOUT_FLAGS：效果同View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//
//        7. View.SYSTEM_UI_FLAG_HIDE_NAVIGATION：隐藏虚拟按键(导航栏)。有些手机会用虚拟按键来代替物理按键。
//
//        8. View.SYSTEM_UI_FLAG_LOW_PROFILE：状态栏显示处于低能显示状态(low profile模式)，状态栏上一些图标显示会被隐藏。
        decodeView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
    }

    /**
     * 显示状态栏和上下的工具栏
     */
    private synchronized void showReadBar() { // 显示工具栏
        visible(mLlBookReadBottom, mLlBookReadTop);
        showStatusBar();
        decodeView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    /**
     * 控制显示关闭 阅读上下的栏目
     */
    private void toggleReadBar() { // 切换工具栏 隐藏/显示 状态
        if (isVisible(mLlBookReadTop)) {
            hideReadBar();
        } else {
            showReadBar();
        }
    }

    /**
     * 开启自动亮度
     */
    public void startAutoLightness() {
        //开启自动亮度，就把sp中的自动亮度值设为tue
        SettingManager.getInstance().saveAutoBrightness(true);
//        开启系统的自动亮度
        ScreenUtils.startAutoBrightness(ReadActivity.this);
//        自动亮度的seekbar不可用
        seekbarLightness.setEnabled(false);
    }

    /**
     * 关闭自动亮度
     */
    public void stopAutoLightness() {
        //关闭自动亮度，就把sp张的自动亮度值设为false
        SettingManager.getInstance().saveAutoBrightness(false);
//        关闭系统自动亮度
        ScreenUtils.stopAutoBrightness(ReadActivity.this);
        //拿到之前sp中保存的亮度设定值
        int value = SettingManager.getInstance().getReadBrightness();
        //自动亮度seekbar可用
        seekbarLightness.setEnabled(true);
        seekbarLightness.setProgress(value);
        //把屏幕亮度设置成当前sp保存的值
        ScreenUtils.setScreenBrightness(value, ReadActivity.this);
    }
}
