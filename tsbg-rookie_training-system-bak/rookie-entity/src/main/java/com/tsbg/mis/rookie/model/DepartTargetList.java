package com.tsbg.mis.rookie.model;

import lombok.Data;

import java.util.Date;

@Data
public class DepartTargetList {
    //目标id
    private Integer targetId;

    //目标单号
    private String targetNum;

    //目标设立人工号
    private String staffCode;

    //目标设立人姓名
    private String createName;

    //处级单位名称
    private String buName;

    //设立人id
    private Integer setUpId;

    //菁干班届别
    private String classPeriod;

    //目标类型id
    private Integer typeId;

    //报告任务id，从report_creation_list中获取
    private Integer creationId;

 /*   //本表ID，周目标必有父ID即月目标ID；月目标父ID为0；默认为0
    private Integer pId;*/
    private Integer pid;

    //目标名称：目标一/二/三...
    private String targetName;

    //目标内容
    private String targetContent;

    //目标权重
    private String targetWeight;

    //目标生效时间（对应周目标设置时即为“周开始时间”）
//    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date targetStartDate;

    //完成截止时间（周目标必填）
//    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date endDate;

     //目标失效时间（对应周目标设置时即为“周结束时间”）
//    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date targetEndDate;

    //创建时间
//    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    //更新/修改时间
//    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateDate;

    //0待审核；1审核通过（可修改）；2审核通过（不可修改）；3被驳回
    private Integer examineState;

    //有效状态值：1有效；0无效
    private Integer status;
}
