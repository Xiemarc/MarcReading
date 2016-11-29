package com.xiemarc.marcreading.bookread.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
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

import com.marc.marclibs.rxbus.RxBus;
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
import com.xiemarc.marcreading.manager.CacheManager;
import com.xiemarc.marcreading.manager.CollectionsManager;
import com.xiemarc.marcreading.manager.Constant;
import com.xiemarc.marcreading.manager.SettingManager;
import com.xiemarc.marcreading.manager.ThemeManager;
import com.xiemarc.marcreading.rx.event.RefreshCollectionListEvent;
import com.xiemarc.marcreading.utils.ScreenUtils;
import com.xiemarc.marcreading.utils.SharedPreferencesUtil;
import com.xiemarc.marcreading.utils.UIUtils;
import com.xiemarc.marcreading.view.readview.BaseReadView;
import com.xiemarc.marcreading.view.readview.OnReadStateChangeListener;
import com.xiemarc.marcreading.view.readview.OverlappedWidget;
import com.xiemarc.marcreading.view.readview.PageWidget;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Observable;

import static android.support.design.widget.BottomSheetBehavior.STATE_COLLAPSED;

/**
 * 描述：阅读的activity <br>必须先缓存才能正常阅读。因为PageFactory中的openBook方法是读取本地数据
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
    @Bind(R.id.tvClear)
    TextView tvClear;
    @Bind(R.id.tvBookMark)
    TextView tvBookMark;
    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    //底部弹出视图
    public BottomSheetBehavior behavior;
    @Bind(R.id.blackview)
    View blackView;

    private View decodeView;
    private List<BookToc.mixToc.Chapters> mChapterList = new ArrayList<>();//网络获取到的章节集合
    private TocListAdapter mTocListAdapter;

    private List<BookMark> mMarkList;
//    private BookMarkAdapter mMarkAdapter;

    private int currentChapter = 1;

    /**
     * 是否开始阅读章节
     **/
    private boolean startRead = false;
    private Receiver receiver = new Receiver();//电量的广播

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
        Observable.timer(1000, TimeUnit.MILLISECONDS)
                .subscribe(aLong -> {
                            //延迟1s加载书架
                            UIUtils.showToast("刷新书架，显示已阅读");
                            RxBus.getDefault().post(new RefreshCollectionListEvent());
                        }
                );
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
//        加载书籍
        mPresenter.getBookToc(bookId, "chapters");
    }

    /**
     * 初始化view翻页的widget
     */
    private void initPagerWidget() {
        if (SharedPreferencesUtil.getInstance().getInt(Constant.FLIP_STYLE, 0) == 0) {
            mPageWidget = new PageWidget(this, bookId, mChapterList, new ReadListener());
        } else {
            mPageWidget = new OverlappedWidget(this, bookId, mChapterList, new ReadListener());
        }
        registerReceiver(receiver, intentFilter);
        if (SharedPreferencesUtil.getInstance().getBoolean(Constant.ISNIGHT, false)) {
            //如果是夜间模式
            mPageWidget.setTextColor(ContextCompat.getColor(this, R.color.chapter_content_night),
                    ContextCompat.getColor(this, R.color.chapter_title_night));
        }
        flReadWidget.removeAllViews();
        flReadWidget.addView(mPageWidget);
    }

    /**
     * 初始化章节的popwindowlist
     */
    private void initTocList() {
        //设置ListPopupWindow的锚点，即关联PopupWindow的显示位置和这个锚点
        //放入章节的adapter\
        mTocListAdapter = new TocListAdapter(UIUtils.getContext(), currentChapter, bookId);
        recyclerview.setLayoutManager(new LinearLayoutManager(UIUtils.getContext()));//默认竖向
        //添加关闭listpopwindow的监听
        mTocListAdapter.setOnItemClickListener(position -> {
            currentChapter = position + 1;
            mTocListAdapter.setCurrentChapter(currentChapter);
            startRead = false;
            readCurrentChapter();
            hideReadBar();
            //关闭behavoir
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        });
        recyclerview.setAdapter(mTocListAdapter);
        behavior = BottomSheetBehavior.from(recyclerview);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == STATE_COLLAPSED || newState == BottomSheetBehavior.STATE_HIDDEN) {
                    //如果是完全打开或者完全关闭
                    gone(blackView);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                visible(blackView);
                //这个方法也可以调整窗口透明度
                ViewCompat.setAlpha(blackView, slideOffset);
            }
        });
        blackView.setBackgroundColor(Color.parseColor("#60000000"));
        gone(blackView);
        blackView.setOnClickListener(view ->
                //点击滑动关闭
                behavior.setState(STATE_COLLAPSED)
        );
    }

    /**
     * 读取currentChapter章节。章节文件存在则直接阅读，不存在则请求
     */
    public void readCurrentChapter() {
        // 先不考虑缓存
        mPresenter.getChapterRead(mChapterList.get(currentChapter - 1).link, currentChapter);
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


    @Override
    public void showBookToc(List<BookToc.mixToc.Chapters> list) {
        mChapterList.clear();
        mChapterList.addAll(list);
        mTocListAdapter.clear();
        mTocListAdapter.addAll(list);
        readCurrentChapter();
    }

    /**
     * 开始阅读本章节内容
     *
     * @param data
     * @param chapter
     */
    @Override
    public synchronized void showChapterRead(ChapterRead.Chapter data, int chapter) {
        if (data != null) {
            //保存书籍文件
            CacheManager.getInstance().saveChapterFile(bookId, chapter, data);
        }

        if (!startRead) {
            startRead = true;//当前阅读状态设置为true
            currentChapter = chapter;//当前章节设置为本章节
            if (!mPageWidget.isPrepared) {
                //如果阅读widget未初始化
                mPageWidget.init(curTheme);
            } else {
                mPageWidget.jumpToChapter(currentChapter);
            }
        }
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

    // 根据手指动作切换
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

        } else if (keyCode == KeyEvent.KEYCODE_MENU) {
            toggleReadBar();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @OnClick({R.id.ivBack, R.id.tvBookReadReading, R.id.tvBookReadCommunity
            , R.id.tvBookReadIntroduce, R.id.tvBookReadMode, R.id.tvBookReadSettings, R.id.tvBookReadDownload, R.id.tvBookMark, R.id.tvBookReadToc
            , R.id.ivBrightnessMinus, R.id.ivBrightnessPlus, R.id.tvFontsizeMinus, R.id.tvFontsizePlus, R.id.tvClear, R.id.tvAddMark
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.tvBookReadReading:
                showError("我母鸡啊");
                break;
            case R.id.tvBookReadToc://章节
                if (behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                break;
        }
    }

    class ReadListener implements OnReadStateChangeListener {
        @Override
        public void onChapterChanged(int chapter) {
            currentChapter = chapter;
            mTocListAdapter.setCurrentChapter(currentChapter);
            // 加载前一节 与 后三节
            for (int i = chapter - 1; i <= chapter + 3 && i <= mChapterList.size(); i++) {
                if (i > 0 && i != chapter) {
                    mPresenter.getChapterRead(mChapterList.get(i - 1).link, i);
                }
            }
        }

        @Override
        public void onPageChanged(int chapter, int page) {
        }

        @Override
        public void onLoadChapterFailure(int chapter) {
            startRead = false;
            //如果缓存为空，继续请求数据
            if (CacheManager.getInstance().getChapterFile(bookId, chapter) == null) {
                mPresenter.getChapterRead(mChapterList.get(chapter - 1).link, chapter);
            }
        }

        @Override
        public void onCenterClick() {
            toggleReadBar();
        }

        @Override
        public void onFlip() {
            //翻页的时候隐藏阅读的商业栏
            hideReadBar();
        }
    }

    class Receiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mPageWidget != null) {
                if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {
                    int level = intent.getIntExtra("level", 0);
                    mPageWidget.setBattery(100 - level);
                } else if (Intent.ACTION_TIME_TICK.equals(intent.getAction())) {
                    mPageWidget.setTime(sdf.format(new Date()));
                }
            }
        }
    }

}
