package com.tsbg.mis.rookie.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @PackgeName: com.tsbg.mis.rookie.vo
 * @ClassName: DepartTargetInfoVoList
 * @Author: 陳觀泰
 * Date: 2020/8/11 18:13
 * Description:
 */
@Data
public class DepartTargetInfoVoList {

    //目标单号
    private String targetNum;

    //目标状态
    private Integer examineState;

    //设立人id
    private Integer setUpId;

    //目标名称
    private String targetName;

    //目标内容
    private String targetContent;

    //类型
    private Integer typeId;

    //报告任务创建id
    private Integer creationId;

    //报告类型（type_list中获取，任务为实习考核时该字段为空）
    private Integer reportType;

    //设立人工号
    private String createCode;

    //设立人姓名
    private String createName;

    //任务开始时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date targetStartDate;

    //任务结束时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date targetEndDate;

    //学生工号
    private String staffCode;

    //学生姓名
    private String staffName;

    //学生繁体姓名
    private String staffSimpleName;

    //设立人工号
    private String staffCodeInfo;

    //设立人姓名
    private String staffNameInfo;

    //报告任务创建id
    private Integer buId;

    //处级单位名称
    private String buName;

    //组织名称
    private String organizationName;

    //签核主管工号
    private String approveStaffCode;

    //签核主管姓名
    private String approveStaffName;

    //签核事务对应表名
    private String investTableName;

    //签核事务对应表中字段名
    private String investFieldName;

    //签核事务对应表中字段值
    private String investFieldValue;

    private Date createDate;

}
