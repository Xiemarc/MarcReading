package com.xiemarc.marcreading.bookread.presenter;

import com.xiemarc.marcreading.base.BasePresenter;
import com.xiemarc.marcreading.bean.BookToc;
import com.xiemarc.marcreading.bean.ChapterRead;
import com.xiemarc.marcreading.bookread.view.BookReadView;
import com.xiemarc.marcreading.rx.rxjava.ApiCallback;
import com.xiemarc.marcreading.rx.rxjava.SubscriberCallBack;
import com.xiemarc.marcreading.utils.StringUtils;

import java.util.List;

import rx.Observable;

/**
 * 描述：阅读书籍的presenter
 * 作者：Marc on 2016/11/24 14:33
 * 邮箱：aliali_ha@yeah.net
 */
public class BookReadPresenter extends BasePresenter<BookReadView> {

    private BookReadView mView;

    public BookReadPresenter(BookReadView mView) {
        this.mView = mView;
    }

    /**
     * 加载书籍
     *
     * @param bookId
     * @param viewChapters
     */
    public void getBookToc(final String bookId, String viewChapters) {
        String key = StringUtils.creatAcacheKey("book-toc", bookId, viewChapters);

        mView.showLoading(null);
        //map变换
        Observable<BookToc.mixToc> fromNet = apiStore.getBookToc(bookId, viewChapters).map(bookToc -> bookToc.mixToc);
        //这里不考虑缓存
        addSubscription(fromNet, new SubscriberCallBack<>(new ApiCallback<BookToc.mixToc>() {

            @Override
            public void onSuccess(BookToc.mixToc data) {
                List<BookToc.mixToc.Chapters> list = data.chapters;
                if (null != list && !list.isEmpty() && mView != null) {
                    mView.showBookToc(list);
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                mView.showError(msg);
            }

            @Override
            public void onCompleted() {
                mView.hideLoading();
            }
        }));
    }

    public void getChapterRead(String url, final int chapter) {
        mView.showLoading(null);
        addSubscription(apiStore.getChapterRead(url), new SubscriberCallBack<>(new ApiCallback<ChapterRead>() {
            @Override
            public void onSuccess(ChapterRead data) {
                if (data.chapter != null && mView != null) {
                    mView.showChapterRead(data.chapter, chapter);
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                mView.showError(msg);
            }

            @Override
            public void onCompleted() {
                mView.hideLoading();
            }
        }));
    }

    public void getBookSource(String viewSummary, String book) {

    }

}
