package com.tsbg.mis.rookie.vo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author :张梦雅
 * @description : 产线实习月度考核 查看月报,周报 前后端传输VO
 * @create :2020-07-29 17:40:00
 */
@Builder
public class ReportProductionListVo {
    private Integer reportId;

    private Integer creationId;

    private String staffCode;
    private String staffName;

    private Integer  missionType;
    private String  missionTypeName;

    private String reportType;
    private String reportTypeName;

    private Integer isHaveProblem;

    private Date reportDate;

    private Integer lineId;

    private String internshipState;

    private String stageRemark;
    private String knowledgeSummary;

    private String internshipContent;

    private String questionDescription;

    private String internshipHarvest;

    private String questionSuggestion;

    private String improveCategory;

    private String improveRemark;

    private Integer fileId;

    private Integer stateId;

    private String mentalState;

    private String mentalRemark;

    private String attendance;

    private Date createDate;

    private Date updateDate;

    private Integer status;

    private String lineName;

    public ReportProductionListVo() {
    }

    public ReportProductionListVo(Integer reportId, Integer creationId, String staffCode, String staffName, Integer missionType, String missionTypeName, String reportType, String reportTypeName, Integer isHaveProblem, Date reportDate, Integer lineId, String internshipState, String stageRemark, String knowledgeSummary, String internshipContent, String questionDescription, String internshipHarvest, String questionSuggestion, String improveCategory, String improveRemark, Integer fileId, Integer stateId, String mentalState, String mentalRemark, String attendance, Date createDate, Date updateDate, Integer status, String lineName) {
        this.reportId = reportId;
        this.creationId = creationId;
        this.staffCode = staffCode;
        this.staffName = staffName;
        this.missionType = missionType;
        this.missionTypeName = missionTypeName;
        this.reportType = reportType;
        this.reportTypeName = reportTypeName;
        this.isHaveProblem = isHaveProblem;
        this.reportDate = reportDate;
        this.lineId = lineId;
        this.internshipState = internshipState;
        this.stageRemark = stageRemark;
        this.knowledgeSummary = knowledgeSummary;
        this.internshipContent = internshipContent;
        this.questionDescription = questionDescription;
        this.internshipHarvest = internshipHarvest;
        this.questionSuggestion = questionSuggestion;
        this.improveCategory = improveCategory;
        this.improveRemark = improveRemark;
        this.fileId = fileId;
        this.stateId = stateId;
        this.mentalState = mentalState;
        this.mentalRemark = mentalRemark;
        this.attendance = attendance;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.status = status;
        this.lineName = lineName;
    }

    public String getReportTypeName() {
        return reportTypeName;
    }

    public void setReportTypeName(String reportTypeName) {
        this.reportTypeName = reportTypeName;
    }

    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(Integer reportId) {
        this.reportId = reportId;
    }

    public Integer getCreationId() {
        return creationId;
    }

    public void setCreationId(Integer creationId) {
        this.creationId = creationId;
    }

    public String getStaffCode() {
        return staffCode;
    }

    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public Integer getMissionType() {
        return missionType;
    }

    public void setMissionType(Integer missionType) {
        this.missionType = missionType;
    }

    public String getMissionTypeName() {
        return missionTypeName;
    }

    public void setMissionTypeName(String missionTypeName) {
        this.missionTypeName = missionTypeName;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public Integer getIsHaveProblem() {
        return isHaveProblem;
    }

    public void setIsHaveProblem(Integer isHaveProblem) {
        this.isHaveProblem = isHaveProblem;
    }

    public Date getReportDate() {
        return reportDate;
    }

    public void setReportDate(Date reportDate) {
        this.reportDate = reportDate;
    }

    public Integer getLineId() {
        return lineId;
    }

    public void setLineId(Integer lineId) {
        this.lineId = lineId;
    }

    public String getInternshipState() {
        return internshipState;
    }

    public void setInternshipState(String internshipState) {
        this.internshipState = internshipState;
    }

    public String getStageRemark() {
        return stageRemark;
    }

    public void setStageRemark(String stageRemark) {
        this.stageRemark = stageRemark;
    }

    public String getKnowledgeSummary() {
        return knowledgeSummary;
    }

    public void setKnowledgeSummary(String knowledgeSummary) {
        this.knowledgeSummary = knowledgeSummary;
    }

    public String getInternshipContent() {
        return internshipContent;
    }

    public void setInternshipContent(String internshipContent) {
        this.internshipContent = internshipContent;
    }

    public String getQuestionDescription() {
        return questionDescription;
    }

    public void setQuestionDescription(String questionDescription) {
        this.questionDescription = questionDescription;
    }

    public String getInternshipHarvest() {
        return internshipHarvest;
    }

    public void setInternshipHarvest(String internshipHarvest) {
        this.internshipHarvest = internshipHarvest;
    }

    public String getQuestionSuggestion() {
        return questionSuggestion;
    }

    public void setQuestionSuggestion(String questionSuggestion) {
        this.questionSuggestion = questionSuggestion;
    }

    public String getImproveCategory() {
        return improveCategory;
    }

    public void setImproveCategory(String improveCategory) {
        this.improveCategory = improveCategory;
    }

    public String getImproveRemark() {
        return improveRemark;
    }

    public void setImproveRemark(String improveRemark) {
        this.improveRemark = improveRemark;
    }

    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    public Integer getStateId() {
        return stateId;
    }

    public void setStateId(Integer stateId) {
        this.stateId = stateId;
    }

    public String getMentalState() {
        return mentalState;
    }

    public void setMentalState(String mentalState) {
        this.mentalState = mentalState;
    }

    public String getMentalRemark() {
        return mentalRemark;
    }

    public void setMentalRemark(String mentalRemark) {
        this.mentalRemark = mentalRemark;
    }

    public String getAttendance() {
        return attendance;
    }

    public void setAttendance(String attendance) {
        this.attendance = attendance;
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

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }
}
