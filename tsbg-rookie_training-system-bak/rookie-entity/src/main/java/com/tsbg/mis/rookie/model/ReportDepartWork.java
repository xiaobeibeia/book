package com.tsbg.mis.rookie.model;

import lombok.Data;

import java.util.Date;

@Data
public class ReportDepartWork {
    /**
     *部门实习工作内容id
     */
    private Integer workId;

    /**
     * 本表ID，默认为0，未完成事项必须填写它上一周的的work_id
     */
    private Integer pid;

    /**
     * 工作类型：0今日/本周/本月工作内容；1今日/本周/本月待做事项；2下周/下月需要协调、帮助的工作
     */
    private Integer tab;

    /**
     * 部门实习报告id
     */
    private Integer reportId;

    /**
     * 工作名称：事项X/工作X
     */
    private String workName;

    /**
     * 目标id（周报、月报中的目标部分必填）
     */
    private Integer targetId;

    /**
     * 工作内容
     */
    private String workContent;

    /**
     * 完成进度（tab值为0时必填）
     */
    private String fulfillmentOfSchedule;

    /**
     * 完成用时（tab值为0时必填）
     */
    private String totalTime;

    /**
     * 证据支持（日报时填写）
     */
    private String finishProof;

    /**
     * 证据支持附件id（日报时填写）
     */
    private Integer proofFileId;

    /**
     * 完成进度非100%时填写
     */
    private String unfinishedReason;

    /**
     *未完成目标时填写“下周待做”
     */
    private String nextWeekWork;

    /**
     * 是否继续：1是；0否（周报/月报项目未完成时必填）
     */
    private Integer isContinue;

    /**
     * 本周/本月待做任务(只是为了查询用的字段)
     */
    private String toDoWork;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 更新/修改时间
     */
    private Date updateDate;

    /**
     * 有效状态值：1有效；0无效
     */
    private Integer status;

    /**
     * 文件
     */
    private FileInfo fileInfo;
}
