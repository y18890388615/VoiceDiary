package com.ysy.voicediary.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class DiaryBean {
    @Id(autoincrement = true)
    private Long diaryId;
    private String content;//日记内容
    private long update_time;
    private int type;
    private String title;
    private int priority;//优先级
    private int important;//重要级
    private String time;//如果是待办，就有处理时间
    private String address;//地址
    private String account;//账号


    @Generated(hash = 1749744078)
    public DiaryBean() {
    }


    @Generated(hash = 170395636)
    public DiaryBean(Long diaryId, String content, long update_time, int type,
            String title, int priority, int important, String time, String address,
            String account) {
        this.diaryId = diaryId;
        this.content = content;
        this.update_time = update_time;
        this.type = type;
        this.title = title;
        this.priority = priority;
        this.important = important;
        this.time = time;
        this.address = address;
        this.account = account;
    }


    public Long getDiaryId() {
        return this.diaryId;
    }

    public void setDiaryId(Long diaryId) {
        this.diaryId = diaryId;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getUpdate_time() {
        return this.update_time;
    }

    public void setUpdate_time(long update_time) {
        this.update_time = update_time;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPriority() {
        return this.priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }


    public int getImportant() {
        return this.important;
    }


    public void setImportant(int important) {
        this.important = important;
    }


    public String getTime() {
        return this.time;
    }


    public void setTime(String time) {
        this.time = time;
    }


    public String getAddress() {
        return this.address;
    }


    public void setAddress(String address) {
        this.address = address;
    }


    public String getAccount() {
        return this.account;
    }


    public void setAccount(String account) {
        this.account = account;
    }
    


}
