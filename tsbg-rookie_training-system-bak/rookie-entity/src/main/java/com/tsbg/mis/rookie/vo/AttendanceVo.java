package com.tsbg.mis.rookie.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @author :张梦雅
 * @description :菁干班出勤考核前后端交互vo
 * @create :2020-07-23 14:44:00
 */
@Data
@Builder
public class AttendanceVo {
    public String examineType;//考核类别
    public Integer countNum;//次数
    public String reason;//原因
    public String examineGrade;//得分
    public String content;//评分标准

    public AttendanceVo() {
    }

    public AttendanceVo(String examineType, Integer countNum, String reason, String examineGrade, String content) {
        this.examineType = examineType;
        this.countNum = countNum;
        this.reason = reason;
        this.examineGrade = examineGrade;
        this.content = content;
    }

    public String getExamineType() {
        return examineType;
    }

    public void setExamineType(String examineType) {
        this.examineType = examineType;
    }

    public Integer getCountNum() {
        return countNum;
    }

    public void setCountNum(Integer countNum) {
        this.countNum = countNum;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getExamineGrade() {
        return examineGrade;
    }

    public void setExamineGrade(String examineGrade) {
        this.examineGrade = examineGrade;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
