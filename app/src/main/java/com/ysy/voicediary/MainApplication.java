package com.ysy.voicediary;

import android.app.Application;
import android.content.Context;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.ysy.voicediary.utils.DataBaseUtil;

public class MainApplication extends Application {
    private Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        // 语音初始化
        // 应用程序入口处调用，避免手机内存过小，杀死后台进程后通过历史intent进入Activity造成SpeechUtility对象为null
        SpeechUtility.createUtility(mContext, SpeechConstant.APPID +"=5e730cc0");
        //本地数据库
        DataBaseUtil.getInstance().initGreenDao(this);
        //分享
        UMConfigure.init(this,"5e7cca4c895ccab92a00033c"
                ,"umeng", UMConfigure.DEVICE_TYPE_PHONE,"");

        //集成微信，qq，新浪
        PlatformConfig.setWeixin("wx78fe05dd629be263","208c25b7cfd370a999c64932deb36069");
        PlatformConfig.setQQZone("1109649481", "SwwqgUsmFJ3jjA5E");
    }



}
