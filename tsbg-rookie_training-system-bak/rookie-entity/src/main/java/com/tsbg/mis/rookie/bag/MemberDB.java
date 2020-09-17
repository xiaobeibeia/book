package com.tsbg.mis.rookie.bag;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.tsbg.mis.rookie.model.ClassStudentInfo;
import lombok.Data;

import java.util.Date;

@Data
public class MemberDB{
    // TODO ----↓  以下字段名称可能会变(这里用下划线还是驼峰？)

    @Excel(name = "工号",orderNum = "0")
    private String staffCode;//工号

    @Excel(name = "姓名",orderNum = "1")
    private String staffName;//姓名

    @Excel(name = "分发日期",orderNum = "2")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date joinDate;//分发日期(入职日期

    @Excel(name = "事业处",orderNum = "3")
    private String unitName;//事业处

    @Excel(name = "处",orderNum = "4")
    private String buName;//处

    @Excel(name = "部门代码",orderNum = "5")
    private String organizationCode;//部门代码

    @Excel(name = "加班类别",orderNum = "6")
    private String overtimeControlType;//加班类别

    @Excel(name = "职系",orderNum = "7")
    private String jobSeries;//职系

    @Excel(name = "岗位名称",orderNum = "8")
    private String postName;//岗位名称

    @Excel(name = "直简接",orderNum = "9")
    private String personType;//人员类型（直简接）

    @Excel(name = "课级主管工号",orderNum = "10")
    private String classManagerCode;//课级主管工号

    @Excel(name = "课级主管",orderNum = "11")
    private String classManagerName;//课级主管

    @Excel(name = "部级主管工号",orderNum = "12")
    private String departManagerCode;//部级主管工号

    @Excel(name = "部级主管",orderNum = "13")
    private String departManagerName;//部级主管

    @Excel(name = "工作楼层",orderNum = "14")
    private String workPlace;//工作楼层
}
