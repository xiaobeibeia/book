package com.tsbg.mis.rookie.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tsbg.mis.rookie.model.ClassStudentInfo;
import com.tsbg.mis.rookie.model.ProductionLineList;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProductionLineListVo extends ProductionLineList {
    private String staffName;//姓名

    private String staffCode;//工号

    private String studentCode;

    private List<ClassStudentInfo> studentInfos;//接收前端学生信息用List

    private Date internshipStartDate;//实习开始时间

    private Date internshipEndDate;//实习结束时间

    private Integer gender;//性别

    private String departName;//部门名称
}
