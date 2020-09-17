package com.tsbg.mis.rookie.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ReportDepartList {
    private Integer reportId;

    private Integer creationId;

    private String staffCode;

    private Integer reportType;

    private Date reportDate;

    private String workHarvest;

    private String lifeHarvest;

    private String workSuggestion;

    private Date createDate;

    private Date updateDate;

    private Integer stateId;

    private Integer status;

    private String missionTypeName;

    private String reportTypeName;

    private String staffName;

    //----↓ 根据业务增加的字段
    private List<RookieApprovalInfo> rookieApprovalInfos;//评分评语集合
    //TODO 這個對象還有沒有必要(暫時還有)
    private List<ReportDepartWork> reportDepartWorkList;//工作内容集合


    private List<DepartTargetList> departTargetLists;//本周目标集合

/*    private List<ReportDepartWork> OtherMissionList;//上週設立的下週其他任務,就是本週其他任務

    private List<ReportDepartWork> leaveOverMissionList;//遗留的任务集合

    private List<ReportDepartWork> helpMissionList;//上週設立的下週需要幫助協調的任務，帶到本週就是這週本週需幫助協調*/

    private List<DepartTargetList> nextDepartTargetLists;//下周目标集合

    //private List<ReportDepartWork> nextHelpMissionList;//本週設立的下週需幫助協調


    private Integer missionType;

    private Date missionStartTime;

    private Date missionEndTime;

}
