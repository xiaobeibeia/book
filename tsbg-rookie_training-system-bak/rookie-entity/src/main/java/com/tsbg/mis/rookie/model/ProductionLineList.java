package com.tsbg.mis.rookie.model;

import lombok.Data;

import java.util.Date;

@Data
public class ProductionLineList {
    //产线id
    private Integer lineId;

    //产线名称
    private String lineName;

    //产线线长工号
    private String lineLeaderCode;

    //产线线长姓名
    private String lineLeaderName;

    //产线线长邮箱
    private String lineLeaderEmail;

    //产线组长工号
    private String lineGroupLeaderCode;

    //产线组长姓名
    private String lineGroupLeaderName;

    //产线组长邮箱
    private String lineGroupLeaderEmail;

    //产线负责人工号
    private String lineManagerCode;

    //产线负责人姓名
    private String lineManagerName;

    //产线负责人邮箱
    private String lineManagerEmail;

    //创建人工号
    private String creatorCode;

    //创建时间
    private Date createDate;

    //更新/修改人工号
    private String updateCode;

    //更新/修改时间
    private Date updateDate;

    //有效状态：1有效；0无效
    private Integer status;
}
