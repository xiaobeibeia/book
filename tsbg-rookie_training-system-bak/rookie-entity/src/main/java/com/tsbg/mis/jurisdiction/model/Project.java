package com.tsbg.mis.jurisdiction.model;

import java.util.Date;

public class Project {
    private Integer projId;

    private String proName;

    private Date proStart;

    private String databaseName;

    private Date proEnd;

    private String proDirector;

    private String remark;

    private Integer status;

    private String projUrl;

    private String createCode;

    private Date createDate;

    private String lastUpdateCode;

    private Date lastUpdateDate;

    public Integer getProjId() {
        return projId;
    }

    public void setProjId(Integer projId) {
        this.projId = projId;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName == null ? null : proName.trim();
    }

    public Date getProStart() {
        return proStart;
    }

    public void setProStart(Date proStart) {
        this.proStart = proStart;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public Date getProEnd() {
        return proEnd;
    }

    public void setProEnd(Date proEnd) {
        this.proEnd = proEnd;
    }

    public String getProDirector() {
        return proDirector;
    }

    public void setProDirector(String proDirector) {
        this.proDirector = proDirector == null ? null : proDirector.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getProjUrl() {
        return projUrl;
    }

    public void setProjUrl(String projUrl) {
        this.projUrl = projUrl == null ? null : projUrl.trim();
    }

    public String getCreateCode() {
        return createCode;
    }

    public void setCreateCode(String createCode) {
        this.createCode = createCode == null ? null : createCode.trim();
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getLastUpdateCode() {
        return lastUpdateCode;
    }

    public void setLastUpdateCode(String lastUpdateCode) {
        this.lastUpdateCode = lastUpdateCode == null ? null : lastUpdateCode.trim();
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }
}