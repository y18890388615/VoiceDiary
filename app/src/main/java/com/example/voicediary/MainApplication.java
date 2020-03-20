package com.example.voicediary;

import android.app.Application;
import android.content.Context;

import com.blankj.utilcode.util.LogUtils;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

public class MainApplication extends Application {
    private Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        // 将“12345678”替换成您申请的APPID，申请地址：http://www.xfyun.cn
        // 请勿在“=”与appid之间添加任何空字符或者转义符
        // appid 必须和下载的SDK保持一致，否则会出现10407错误
        // 应用程序入口处调用，避免手机内存过小，杀死后台进程后通过历史intent进入Activity造成SpeechUtility对象为null
        SpeechUtility.createUtility(mContext, SpeechConstant.APPID +"=5e730cc0");
    }
}
