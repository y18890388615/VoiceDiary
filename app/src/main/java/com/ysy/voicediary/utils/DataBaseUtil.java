package com.ysy.voicediary.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ysy.voicediary.db.DaoMaster;
import com.ysy.voicediary.db.DaoSession;
import com.ysy.voicediary.db.MyOpenHelper;


public class DataBaseUtil {
    private volatile static DataBaseUtil dataBaseUtil;
    private SQLiteDatabase db;

    public static DataBaseUtil getInstance() {
        if (dataBaseUtil == null) {
            synchronized (DataBaseUtil.class) {
                if (dataBaseUtil == null) {
                    dataBaseUtil = new DataBaseUtil();
                }
            }
        }
        return dataBaseUtil;
    }

    /**
     * 初始化GreenDao,直接在Application中进行初始化操作
//     */
    public void initGreenDao(Context context) {
        MyOpenHelper mHelper = new MyOpenHelper(context, "diary.db", null);
        db = mHelper.getWritableDatabase();
    }


    public DaoSession getDaoSession() {
        return new DaoMaster(db).newSession();
    }
}
