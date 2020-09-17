package com.tsbg.mis.rookie.model;

import lombok.Data;

import java.util.Date;

@Data
public class ReportDepartTarget {
    private Integer autoId;

    private Integer targetId;

    private Integer reportId;

    private String  fulfillmentOfSchedule;

    private String unfinishedReason;

    private Date createDate;

    private Date updateDate;

    private Integer status;
}
