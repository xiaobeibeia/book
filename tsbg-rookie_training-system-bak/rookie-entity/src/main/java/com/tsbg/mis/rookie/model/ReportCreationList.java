package com.tsbg.mis.rookie.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class ReportCreationList {

    //报告任务创建id
    private Integer creationId;

    //任务类型（type_list中获取）
    private Integer missionType;

    //任务名称
    private String missionName;

    //菁干班届别
    private String classPeriod;

    //实习开始时间
    private Date internshipStart;

    //实习结束时间
    private Date internshipEnd;

    //任务开始时间
    private Date missionStartTime;

    //任务结束时间
    private Date missionEndTime;

    //报告类型（type_list中获取）
    private Integer reportType;

    //默认开放填写时间（state_list中获取）
    private Integer defaultStartTime;

    //默认关闭填写时间（state_list中获取）
    private Integer defaultEndTime;

    //开放填写时间
    private Date startTime;

    //关闭填写时间
    private Date endTime;

    //是否启用：1是；0否
    private Integer isEnable;

    //创建人工号
    private String creatorCode;

    //创建时间
    private Date createDate;

    //最后更新人工号
    private String updateCode;

    //最后更新时间
    private Date updateDate;

    //有效状态值：1有效；0无效
    private Integer status;


    //---↓ 这下面的是不属于表字段的，附加的字段
    private String reportTypeName;

    private String missionTypeName;

    private Integer stateId;

    private String stateName;

    private Integer reportId;

}
