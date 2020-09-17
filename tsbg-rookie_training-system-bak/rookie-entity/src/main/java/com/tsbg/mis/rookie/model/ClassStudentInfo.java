package com.tsbg.mis.rookie.model;


import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class ClassStudentInfo {

    private Integer studentId;              //学生id
    @Excel(name = "工号",orderNum = "0")
    private String staffCode;               //学生工号
    @Excel(name = "姓名",orderNum = "1")
    private String staffName;               //学生繁体姓名
    private String staffSimpleName;         //学生简体姓名
    private Integer gradeId;                //所属班级id
    private Integer squadId;                //所属小组id
    private Integer studentType;            //学生类别id
    private Date graduateDate;              //毕业日期
    private String graduateSchool;          //毕业学校
    private Integer gender;                 //性别
    @Excel(name = "分发日期",orderNum = "2")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date joinDate;                  //入职日期
    private String phoneNum;                //手机号码
    private String schoolType;              //学校类别
    private String education;               //学历
    private Integer factoryId;              //厂区id
    private String factoryName;             //厂区名称
    private Integer bgId;                   //BG事业群id
    private String bgName;                  //事业群名称
    private Integer unitId;                 //事业处id
    @Excel(name = "事业处",orderNum = "3")
    private String unitName;                //事业处名称
    private Integer buId;                   //处级单位id
    @Excel(name = "处",orderNum = "4")
    private String buName;                  //处级单位名称
    private Integer departId;               //所属部门id
    private String departName;              //部门名称
    private Integer classId;                //课级单位id
    private String className;               //课级单位名称
    @Excel(name = "直属主管工号",orderNum = "14")
    private String directManagerCode;       //直属主管工号
    @Excel(name = "直属主管姓名",orderNum = "15")
    private String directManagerName;       //直属主管姓名
    @Excel(name = "处级主管工号",orderNum = "10")
    private String unitManagerCode;         //处级主管工号
    @Excel(name = "处级主管姓名",orderNum = "11")
    private String unitManagerName;         //处级主管姓名
    private String classManagerCode;        //课级主管工号
    private String classManagerName;        //课级主管名称
    @Excel(name = "部级主管工号",orderNum = "12")
    private String departManagerCode;       //部门主管工号
    @Excel(name = "部级主管姓名",orderNum = "13")
    private String departManagerName;       //部门主管姓名
    private String classPeriod;             //菁干班届别
    @Excel(name = "部门代码",orderNum = "5")
    private String organizationCode;        //组织代码
    private String organizationName;        //组织名称
    private Integer studentInternshipState; //实习情况
    @Excel(name = "加班类别",orderNum = "6")
    private String overtimeControlType;     //加班管控类型
    @Excel(name = "岗位名称",orderNum = "8")
    private String postName;                //岗位名称
    @Excel(name = "职系",orderNum = "7")
    private String jobSeries;               //职系
    @Excel(name = "直简接",orderNum = "9")
    private String personType;              //人员类型（直简接）
    @Excel(name = "工作楼层",orderNum = "18")
    private String workPlace;               //工作楼层
    private String createrCode;             //创建人工号
    private Date createDate;                //创建日期
    private Integer status;                 //状态

    private String typeName;                //学生类别名称
    private String gradeName;               //所属班级名称
    private String squadName;               //所属小组名称

    private Integer creationId;             //报告类型（type_list中获取，任务为实习考核时该字段为空）
    private Integer reportType;             //报告类型（type_list中获取，任务为实习考核时该字段为空）
    private Date missionStartTime;          //任务开始时间
    private Date missionEndTime;            //任务结束时间

    @Excel(name = "教练员工号",orderNum = "16")
    private String coachCode;
    @Excel(name = "教练员姓名",orderNum = "17")
    private String coachName;

}
