package com.tsbg.mis.rookie.model;

import java.util.Date;
import java.util.List;

public class ReportProductionProposalList{

    private Integer proposalId;

    private Integer reportType;

    private Date proposalDate;

    private String staffCode;

    private String proposalType;

    private String proposalTheme;

    private String analyzeMethod;

    private String aMethodRemark;

    private String analyzeContent;

    private Integer fileId;

    private String improveCategory;

    private String iCategoryRemark;

    private String iPurposeDescription;

    private String improveMethod;

    private String executePlan;

    private String performanceDescription;

    private Integer yearCostSaving;

    private Integer onceCostSaving;

    private String costSavingReason;

    private Integer stateId;

    private Date createDate;

    private Date updateDate;

    private Integer status;

    private List<Integer> proposalTypeList;

    private List<Integer> analyzeMethodList;

    private List<Integer> improveCategoryList;

    private Integer creationId;

    private String reportTypeName;

    private String missionTypeName;

    private Date missionStartTime;

    private Date missionEndTime;

    private String staffName;

    public String getMissionTypeName() {
        return missionTypeName;
    }

    public void setMissionTypeName(String missionTypeName) {
        this.missionTypeName = missionTypeName;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public Date getMissionStartTime() {
        return missionStartTime;
    }

    public void setMissionStartTime(Date missionStartTime) {
        this.missionStartTime = missionStartTime;
    }

    public Date getMissionEndTime() {
        return missionEndTime;
    }

    public void setMissionEndTime(Date missionEndTime) {
        this.missionEndTime = missionEndTime;
    }

    public String getReportTypeName() {
        return reportTypeName;
    }

    public void setReportTypeName(String reportTypeName) {
        this.reportTypeName = reportTypeName;
    }

    public FileInfo getFileInfo() {
        return fileInfo;
    }

    public void setFileInfo(FileInfo fileInfo) {
        this.fileInfo = fileInfo;
    }

    private FileInfo fileInfo;

    public Integer getCreationId() {
        return creationId;
    }

    public void setCreationId(Integer creationId) {
        this.creationId = creationId;
    }

    //----下 根据业务增加的字段
    private Integer reportId;

    private List<RookieApprovalInfo> rookieApprovalInfos;

    public Integer getProposalId() {
        return proposalId;
    }

    public void setProposalId(Integer proposalId) {
        this.proposalId = proposalId;
    }

    public Integer getReportType() {
        return reportType;
    }

    public void setReportType(Integer reportType) {
        this.reportType = reportType;
    }

    public Date getProposalDate() {
        return proposalDate;
    }

    public void setProposalDate(Date proposalDate) {
        this.proposalDate = proposalDate;
    }

    public String getStaffCode() {
        return staffCode;
    }

    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }

    public String getProposalType() {
        return proposalType;
    }

    public void setProposalType(String proposalType) {
        this.proposalType = proposalType;
    }

    public String getProposalTheme() {
        return proposalTheme;
    }

    public void setProposalTheme(String proposalTheme) {
        this.proposalTheme = proposalTheme;
    }

    public String getAnalyzeMethod() {
        return analyzeMethod;
    }

    public void setAnalyzeMethod(String analyzeMethod) {
        this.analyzeMethod = analyzeMethod;
    }

    public String getaMethodRemark() {
        return aMethodRemark;
    }

    public void setaMethodRemark(String aMethodRemark) {
        this.aMethodRemark = aMethodRemark;
    }

    public String getAnalyzeContent() {
        return analyzeContent;
    }

    public void setAnalyzeContent(String analyzeContent) {
        this.analyzeContent = analyzeContent;
    }

    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    public String getImproveCategory() {
        return improveCategory;
    }

    public void setImproveCategory(String improveCategory) {
        this.improveCategory = improveCategory;
    }

    public String getiCategoryRemark() {
        return iCategoryRemark;
    }

    public void setiCategoryRemark(String iCategoryRemark) {
        this.iCategoryRemark = iCategoryRemark;
    }

    public String getiPurposeDescription() {
        return iPurposeDescription;
    }

    public void setiPurposeDescription(String iPurposeDescription) {
        this.iPurposeDescription = iPurposeDescription;
    }

    public String getImproveMethod() {
        return improveMethod;
    }

    public void setImproveMethod(String improveMethod) {
        this.improveMethod = improveMethod;
    }

    public String getExecutePlan() {
        return executePlan;
    }

    public void setExecutePlan(String executePlan) {
        this.executePlan = executePlan;
    }

    public String getPerformanceDescription() {
        return performanceDescription;
    }

    public void setPerformanceDescription(String performanceDescription) {
        this.performanceDescription = performanceDescription;
    }

    public Integer getYearCostSaving() {
        return yearCostSaving;
    }

    public void setYearCostSaving(Integer yearCostSaving) {
        this.yearCostSaving = yearCostSaving;
    }

    public Integer getOnceCostSaving() {
        return onceCostSaving;
    }

    public void setOnceCostSaving(Integer onceCostSaving) {
        this.onceCostSaving = onceCostSaving;
    }

    public String getCostSavingReason() {
        return costSavingReason;
    }

    public void setCostSavingReason(String costSavingReason) {
        this.costSavingReason = costSavingReason;
    }

    public Integer getStateId() {
        return stateId;
    }

    public void setStateId(Integer stateId) {
        this.stateId = stateId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<Integer> getProposalTypeList() {
        return proposalTypeList;
    }

    public void setProposalTypeList(List<Integer> proposalTypeList) {
        this.proposalTypeList = proposalTypeList;
    }

    public List<Integer> getAnalyzeMethodList() {
        return analyzeMethodList;
    }

    public void setAnalyzeMethodList(List<Integer> analyzeMethodList) {
        this.analyzeMethodList = analyzeMethodList;
    }

    public List<Integer> getImproveCategoryList() {
        return improveCategoryList;
    }

    public void setImproveCategoryList(List<Integer> improveCategoryList) {
        this.improveCategoryList = improveCategoryList;
    }

    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(Integer reportId) {
        this.reportId = reportId;
    }

    public List<RookieApprovalInfo> getRookieApprovalInfos() {
        return rookieApprovalInfos;
    }

    public void setRookieApprovalInfos(List<RookieApprovalInfo> rookieApprovalInfos) {
        this.rookieApprovalInfos = rookieApprovalInfos;
    }
}
