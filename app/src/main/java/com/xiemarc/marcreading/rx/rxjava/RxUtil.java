package com.xiemarc.marcreading.rx.rxjava;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.marc.marclibs.utils.ACache;
import com.marc.marclibs.utils.LogUtils;
import com.xiemarc.marcreading.BaseApplication;
import com.xiemarc.marcreading.utils.UIUtils;

import java.lang.reflect.Field;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * 描述：使用rxjava缓存工具类
 * 作者：Marc on 2016/11/23 15:02
 * 邮箱：aliali_ha@yeah.net
 */
public class RxUtil {

    /**
     * 使用rxjava的方式获取缓存在磁盘上面的json数据
     *<br>这里不对线程做处理，因为basePresenter中的addSus中已经统一做了线程处理
     * @param key   关键字
     * @param clazz 返回的指定类型
     * @param <T>   类型
     * @return
     */
    public static <T> Observable rxCreateDiskObservable(String key, Class<T> clazz) {
        Observable<T> tObservable = Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                LogUtils.d("从磁盘获取缓存数据开始" + key);
                String json = ACache.get(UIUtils.getContext()).getAsString(key);
                LogUtils.d("从磁盘获取缓存接结束 json= " + json);
                if (!TextUtils.isEmpty(json)) {
                    //执行下面的map矩阵变幻
                    subscriber.onNext(json);
                }
                subscriber.onCompleted();
            }
            //矩阵变幻，把String类型变化为指定的T类型
        }).map(new Func1<String, T>() {
            @Override
            public T call(String s) {
                T t = new Gson().fromJson(s, clazz);
                return t;
            }
        });
        return tObservable;
    }

    /**
     * 使用rxjava的方式缓存list列表的数据
     *<br>这里不对线程做处理，因为basePresenter中的addSus中已经统一做了线程处理
     * @param <T>
     * @param key 存储的key
     * @return 返回携带缓存数据的被观察者
     */
    public static <T> Observable.Transformer<T, T> rxCacheListHelper(String key) {
        Observable.Transformer transformer = new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> observable) {
                //被观察者调用观察者
                //被观察者产生了缓存list数据的事件
                Observable<T> tObservable = observable
//                        subscribeOn(Schedulers.io())//在io线程执行缓存
                        .doOnNext(new Action1<T>() {
                            @Override
                            public void call(T data) {
                                //创建子线程执行任务
                                Schedulers.io().createWorker().schedule(new Action0() {
                                    @Override
                                    public void call() {
                                        LogUtils.d("从网络获取数据完成，开始缓存");
                                        //通过反射获取List,再判空决定是否缓存
                                        Class clazz = data.getClass();
                                        Field[] fields = clazz.getFields();
                                        for (Field field : fields) {
                                            //得到该类的名称
                                            String className = field.getType().getSimpleName();
                                            // 如果类的名称是List的话  拿到属性值
                                            if (className.equalsIgnoreCase("List")) {
                                                try {
                                                    List list = (List) field.get(data);
                                                    LogUtils.d("list==" + list);
                                                    if (!list.isEmpty()) {
                                                        //如果不为空
                                                        ACache.get(BaseApplication.getmContext())
                                                                .put(key, new Gson().toJson(data, clazz));
                                                        LogUtils.d("缓存结束");
                                                    }
                                                } catch (IllegalAccessException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    }
                                });
                            }
                        });
                return tObservable;
            }
        };
        return transformer;
    }

    /**
     * 使用rxjava方式缓存实体bean对象的方法
     *<br>这里不对线程做处理，因为basePresenter中的addSus中已经统一做了线程处理
     * @param <T>
     * @return 返回携带缓存数据的被观察者
     */
    public static <T> Observable.Transformer<T, T> rxCachaeBeanHelper(String key) {
        Observable.Transformer transformer = new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> observable) {
                //被观察者产生了 缓存实体bean对象的事件
                Observable<T> tObservable = observable.doOnNext(new Action1<T>() {
                    @Override
                    public void call(T t) {
                        Schedulers.io().createWorker().schedule(new Action0() {
                            @Override
                            public void call() {
                                LogUtils.d(String.format("%s", "从网络获取数据结束，开始缓存"));
                                ACache.get(UIUtils.getContext()).put(key, new Gson().toJson(t, t.getClass()));
                                LogUtils.d(String.format("%s", "缓存到achae结束"));
                            }
                        });
                    }
                });
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread());
                return tObservable;
            }
        };
        return transformer;
    }
}
