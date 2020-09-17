package com.tsbg.mis.rookie.model;

import io.swagger.annotations.ApiModel;
import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@ApiModel(description = "产线报告实体类")
@EqualsAndHashCode(callSuper = true)
public class ReportProductionList extends ReportDepartList{
    private Integer reportId;

    private String staffCode;

    private Integer missionType;

    private Integer reportType;

    private Integer isHaveProblem;

    private Date reportDate;

    private Integer lineId;

    private String internshipState;

    private String stageRemark;

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

    private Integer creationId;

    private String knowledgeSummary;
    //---↓添加的业务字段

    private List<Integer> improveCategoryList;//前台传过来的improve_category(改善范畴)格式

    private List<Integer> internshipStateList;

    private List<Integer> mentalStateList;

    private List<Integer> attendanceList;

    private String lineName;

    private List<ReportProductionWeekQuestion> reportProductionWeekQuestionList;

    private List<RookieApprovalInfo> rookieApprovalInfos;

    private String missionTypeName;

    private String reportTypeName;

    private String staffName;

    private Date missionStartTime;

    private Date missionEndTime;

    private FileInfo fileInfo;

    private List<ReportProductionList> dailyReportList;

    private String dailyDate;
}
