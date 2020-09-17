package com.tsbg.mis.rookie.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepartTargetStudent {
    //自增ID
    private Integer autoId;

    //学生工号
    private String staffCode;

    //学生简体姓名
    private String staffName;

    //学生繁体姓名
    private String staffSimpleName;

    //学生处级单位名称
    private String buName;

    //学生部门名称
    private String departName;

    //课级单位名称
    private String className;

    //学生的组织名称
    private String organizationName;

    //目标单号
    private String targetNum;

    //创建时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    //更新时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateDate;

    //有效状态值：1有效；0无效
    private Integer status;

}
