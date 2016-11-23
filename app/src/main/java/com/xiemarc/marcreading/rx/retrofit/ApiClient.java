package com.xiemarc.marcreading.rx.retrofit;

import com.marc.marclibs.utils.logger.Logger;
import com.xiemarc.marcreading.manager.Constant;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 描述： retrofit配置
 * 作者：Marc on 2016/11/14 16:57
 * 邮箱：aliali_ha@yeah.net
 */
public class ApiClient {

    public static Retrofit mRetrofit;

    public static Retrofit retrofit() {
        if (null == mRetrofit) {
            LoggingInterceptor logging = new LoggingInterceptor(new MyLog());
            logging.setLevel(LoggingInterceptor.Level.BODY);
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.addInterceptor(logging)
                    .addInterceptor(new HeaderInterceptor());

            OkHttpClient okHttpClient = builder.build();
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(Constant.API_BASE_URL)

                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return mRetrofit;
    }

    public static class MyLog implements LoggingInterceptor.Logger {
        @Override
        public void log(String message) {
            Logger.i(String.format("oklog:%s", message));
        }
    }
}
