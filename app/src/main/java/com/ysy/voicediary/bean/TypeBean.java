package com.ysy.voicediary.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class TypeBean {
    @Id(autoincrement = true)
    private Long typeId;
    private String typeName;//类型名称
    @Generated(hash = 578675260)
    public TypeBean(Long typeId, String typeName) {
        this.typeId = typeId;
        this.typeName = typeName;
    }
    @Generated(hash = 119682038)
    public TypeBean() {
    }
    public Long getTypeId() {
        return this.typeId;
    }
    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }
    public String getTypeName() {
        return this.typeName;
    }
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
