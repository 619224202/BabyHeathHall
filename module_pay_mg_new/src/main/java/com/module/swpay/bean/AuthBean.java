package com.module.swpay.bean;

import java.util.Date;

/**
 * Created by wanglu on 2019/7/9.
 */
public class AuthBean {
    //项目编号
    private int projectId;
    //项目名称
    private String projectName;
    //项目定价
    private float price;
    //项目订购初始时间
    private Date beginDate;
    //项目订购到期时间
    private Date endDate;
    //订购产品码
    private String projectCode;

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }
}
