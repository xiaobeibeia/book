package com.tsbg.mis.rookie.vo;

import com.tsbg.mis.rookie.model.ReportProductionWeekQuestion;
import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author :张梦雅
 * @description : 产线实习月度考核 周报前后端交互vo
 * @create :2020-07-31 14:20:00
 */
@Data
@Builder
public class WeeklyReportProductionListVo {
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
    public List<ReportProductionWeekQuestion> reportProductionWeekQuestionList;


    public WeeklyReportProductionListVo(Integer reportId, Integer creationId, String staffCode, String staffName, Integer missionType, String missionTypeName, String reportType, String reportTypeName, Integer isHaveProblem, Date reportDate, Integer lineId, String internshipState, String stageRemark, String knowledgeSummary, String internshipContent, String questionDescription, String internshipHarvest, String questionSuggestion, String improveCategory, String improveRemark, Integer fileId, Integer stateId, String mentalState, String mentalRemark, String attendance, Date createDate, Date updateDate, Integer status, String lineName, List<ReportProductionWeekQuestion> reportProductionWeekQuestionList) {
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
        this.reportProductionWeekQuestionList = reportProductionWeekQuestionList;
    }

    public WeeklyReportProductionListVo() {
    }
}
