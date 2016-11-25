package com.xiemarc.marcreading.bookread.presenter;

import com.xiemarc.marcreading.base.BasePresenter;
import com.xiemarc.marcreading.bean.BookToc;
import com.xiemarc.marcreading.bookread.view.BookReadView;
import com.xiemarc.marcreading.rx.rxjava.ApiCallback;
import com.xiemarc.marcreading.rx.rxjava.SubscriberCallBack;
import com.xiemarc.marcreading.utils.StringUtils;

import java.util.List;

import static android.R.attr.data;

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
        //这里不考虑缓存
        addSubscription(apiStore.getBookToc(bookId, viewChapters), new SubscriberCallBack<>(new ApiCallback<BookToc.mixToc>() {
            @Override
            public void onSuccess(BookToc.mixToc model) {
                List<BookToc.mixToc.Chapters> list = model.chapters;
                if (list != null && !list.isEmpty() && mView != null) {
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

    }

    public void getBookSource(String viewSummary, String book) {

    }

}
