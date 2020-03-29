package com.ysy.voicediary.utils;

import android.app.DatePickerDialog;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.lxj.xpopup.XPopup;
import com.ysy.voicediary.widget.dialog.UpdatePWDDialog;

import java.util.Calendar;

public class DialogUtil {
    /**
     * 日期选择器
     */
    public static void showDatePickerDialog(AppCompatActivity activity, DatePickerDialog.OnDateSetListener onDateSetListener) {
        Calendar ca = Calendar.getInstance();
        int mYear = ca.get(Calendar.YEAR);
        int mMonth = ca.get(Calendar.MONTH);
        int mDay = ca.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(activity, onDateSetListener, mYear, mMonth, mDay).show();
    }

    /**
     * 修改密码
     */
    public static void showUpdate(Context context) {
        new XPopup.Builder(context)
                .asCustom(new UpdatePWDDialog(context))
                .show();
    }
}
