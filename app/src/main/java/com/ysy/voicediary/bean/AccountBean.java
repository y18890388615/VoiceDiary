package com.ysy.voicediary.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Index;

/**
 * 账号密码
 */
@Entity
public class AccountBean {
    @Id(autoincrement = true)
    private Long userId;
    @Index(unique = true)//设置唯一性
    private String Account;
    private String pwd;
    @Generated(hash = 1407846500)
    public AccountBean(Long userId, String Account, String pwd) {
        this.userId = userId;
        this.Account = Account;
        this.pwd = pwd;
    }
    @Generated(hash = 1267506976)
    public AccountBean() {
    }
    public Long getUserId() {
        return this.userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public String getAccount() {
        return this.Account;
    }
    public void setAccount(String Account) {
        this.Account = Account;
    }
    public String getPwd() {
        return this.pwd;
    }
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
