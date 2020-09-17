package com.tsbg.mis.rookie.model;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @PackgeName: com.tsbg.mis.rookie.model
 * @ClassName: AttendanceEntity
 * @Author: 陳觀泰
 * Date: 2020/8/4 15:59
 * Description:导入考勤信息时用到的模板
 */
@Data
public class AttendanceRecord {

    //员工工号
    @Excel(name = "工號",orderNum = "0")
    private String staffCode;

    //员工姓名
    @Excel(name = "姓名",orderNum = "1")
    private String staffName;

    //部门
    @Excel(name = "部門",orderNum = "2")
    private String organizationName;

    //班别
    @Excel(name = "班別",orderNum = "3")
    private String shiftsType;

    //考勤状况
    @Excel(name = "考勤狀況",orderNum = "4")
    private String attendanceType;

    //异常时数（分）
    @Excel(name = "異常時數(分)",orderNum = "5")
    private String abnormalTime;

    //日期
    @Excel(name = "日期",orderNum = "6")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date attendanceDate;

    //上午上班

    private String morningStartTime;

    //上午下班

    private String morningEndTime;

    //下午上班

    private String afternoonStartTime;

    //下午下班

    private String afternoonEndTime;

    //开始加班

    private String startExtraWork;

    //结束加班

    private String endExtraWork;

    //系统计算加班时数/出勤工时
    @Excel(name = "系統計算加班時數/出勤工時",orderNum = "13")
    private String attendanceTime;

    //最后分析时间
    @Excel(name = "最後分析時間",orderNum = "14")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date lastAnalyzeTime;

    private String createCode;

    private Date createTime;

    private Integer status;

    private Integer examineTypeId;

    private Integer attendanceRecordId;

    //上午上班
    @Excel(name = "上午上班",orderNum = "7")
    private Date morningStartTimeDate;

    //上午下班
    @Excel(name = "上午下班",orderNum = "8")
    private Date morningEndTimeDate;

    //下午上班
    @Excel(name = "下午上班",orderNum = "9")
    private Date afternoonStartTimeDate;

    //下午下班
    @Excel(name = "下午下班",orderNum = "10")
    private Date afternoonEndTimeDate;

    //开始加班
    @Excel(name = "開始加班",orderNum = "11")
    private Date startExtraWorkDate;

    //结束加班
    @Excel(name = "結束加班",orderNum = "12")
    private Date endExtraWorkDate;


}
