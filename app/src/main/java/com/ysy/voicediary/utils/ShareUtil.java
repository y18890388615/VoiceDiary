package com.ysy.voicediary.utils;

import android.app.Activity;
import android.graphics.Bitmap;

import com.blankj.utilcode.util.ToastUtils;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

public class ShareUtil {

    /**
     * 分享
     * @param activity Activity
     * @param platform 传入哪个平台 类似于SHARE_MEDIA.QQ
     * @param title  标题
     * @param bitmap 图片
     * @param webUrl 网址
     */
    public static void share(Activity activity, SHARE_MEDIA platform, String title, String description, Bitmap bitmap, String webUrl){
        UMImage image = new UMImage(activity, bitmap);//bitmap文件
        UMWeb web = new UMWeb(webUrl);//连接地址
        web.setTitle(title);//标题
        web.setThumb(image);//设置缩略图
        web.setDescription(description);//描述
        new ShareAction(activity)
                .setPlatform(platform)//传入平台
                .withMedia(web)//分享网址
                .setCallback(shareListener)//回调监听器
                .share();
    }

    /**
     * 分享的回调
     */
    private static UMShareListener shareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {
        }
        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            ToastUtils.showShort( "分享成功");
        }
        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            ToastUtils.showShort( "分享失败");
        }
        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            ToastUtils.showShort( "取消分享");
        }
    };
}
