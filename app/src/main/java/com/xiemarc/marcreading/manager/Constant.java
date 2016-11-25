package com.xiemarc.marcreading.manager;

import android.support.annotation.StringDef;

import com.xiemarc.marcreading.utils.FileUtils;
import com.xiemarc.marcreading.utils.UIUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 描述：常量类
 * 作者：Marc on 2016/11/14 17:01
 * 邮箱：aliali_ha@yeah.net
 */
public class Constant {

    public static final String IMG_BASE_URL = "http://statics.zhuishushenqi.com";

    public static final String API_BASE_URL = "http://api.zhuishushenqi.com";


    public static String PATH_DATA = FileUtils.createRootPath(UIUtils.getContext()) + "/cache";

    public static String PATH_COLLECT = FileUtils.createRootPath(UIUtils.getContext()) + "/collect";

    public static String BASE_PATH = FileUtils.createRootPath(UIUtils.getContext()) + "/book/";

    public static final String SP_USER_SEX = "userChooseSex";

    public static final String ISNIGHT = "isNight";

    @StringDef({
            Gender.MALE,
            Gender.FEMALE
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface Gender {
        String MALE = "male";
        String FEMALE = "female";
    }

}
