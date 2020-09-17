package com.tsbg.mis.rookie.vo;

import lombok.Data;

import java.util.List;

/**
 * @author :张梦雅
 * @description :
 * @create :2020-07-27 18:48:00
 */
@Data
public class AttendaceRewardsAndPunishmentsVo {
    List<AttendanceVo>  attendanceVoList;
    List<OtherRewardsAndPunishmentsVo> otherRewardsAndPunishmentsVoList;

    private String attendanceAndRewardsAndPunishGrade;//出勤与奖惩总分
    private String studentStaffName;//菁干班学生姓名
    private String studentCode;//菁干班学生工号

    public AttendaceRewardsAndPunishmentsVo() {
    }

    public AttendaceRewardsAndPunishmentsVo(List<AttendanceVo> attendanceVoList, List<OtherRewardsAndPunishmentsVo> otherRewardsAndPunishmentsVoList, String attendanceAndRewardsAndPunishGrade, String studentStaffName, String studentCode) {
        this.attendanceVoList = attendanceVoList;
        this.otherRewardsAndPunishmentsVoList = otherRewardsAndPunishmentsVoList;
        this.attendanceAndRewardsAndPunishGrade = attendanceAndRewardsAndPunishGrade;
        this.studentStaffName = studentStaffName;
        this.studentCode = studentCode;
    }

    public List<AttendanceVo> getAttendanceVoList() {
        return attendanceVoList;
    }

    public void setAttendanceVoList(List<AttendanceVo> attendanceVoList) {
        this.attendanceVoList = attendanceVoList;
    }

    public List<OtherRewardsAndPunishmentsVo> getOtherRewardsAndPunishmentsVoList() {
        return otherRewardsAndPunishmentsVoList;
    }

    public void setOtherRewardsAndPunishmentsVoList(List<OtherRewardsAndPunishmentsVo> otherRewardsAndPunishmentsVoList) {
        this.otherRewardsAndPunishmentsVoList = otherRewardsAndPunishmentsVoList;
    }

    public String getStudentStaffName() {
        return studentStaffName;
    }

    public void setStudentStaffName(String studentStaffName) {
        this.studentStaffName = studentStaffName;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public String getAttendanceAndRewardsAndPunishGrade() {
        return attendanceAndRewardsAndPunishGrade;
    }

    public void setAttendanceAndRewardsAndPunishGrade(String attendanceAndRewardsAndPunishGrade) {
        this.attendanceAndRewardsAndPunishGrade = attendanceAndRewardsAndPunishGrade;
    }
}
