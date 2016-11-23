package com.xiemarc.marcreading.rx.retrofit;

import com.xiemarc.marcreading.bean.Recommend;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 描述：retrofit请求接口
 * 作者：Marc on 2016/11/14 16:57
 * 邮箱：aliali_ha@yeah.net
 */
public interface ApiStore {
    /**
     * 得到书架内容
     * @param gender
     * @return
     */
    @GET("/book/recommend")
    Observable<Recommend> getRecommend(@Query("gender") String gender);


}
