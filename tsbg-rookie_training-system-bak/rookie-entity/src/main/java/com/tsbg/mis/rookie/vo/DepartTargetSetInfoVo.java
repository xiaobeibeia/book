package com.tsbg.mis.rookie.vo;

import lombok.Data;

import java.util.List;

@Data
public class DepartTargetSetInfoVo {

    //目标单号
    private String targetNum;

    //报告类型（type_list中获取，任务为实习考核时该字段为空）
    private Integer reportType;

    private List<DepartTargetSetInfoList> departTargetSetInfoLists;

}
