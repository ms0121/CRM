package com.xxxx.crm.model;

/**
 * @author lms
 * @date 2021-06-30 - 20:38
 * 目的是为了去对应我们的ztree树类型的角色分配图
 */
public class TreeModel {
    private Integer id;
    private Integer pId;
    private String name;
    // 复选框是否被勾选，true表示勾选，false表示不勾选
    private boolean checked = false;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getpId() {
        return pId;
    }

    public void setpId(Integer pId) {
        this.pId = pId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
