package com.tsbg.mis.rookie.vo;

import lombok.Data;

/**
 * @author :张梦雅
 * @description :产线考核列表按条件查询
 * @create :2020-07-29 08:34:00
 */
@Data
public class InquireStudentsByParamsVo {
    private String examinStatus;//考核状态(待评,已评)
    private String studentStaffNameOrCode;//考核同学姓名或者工号

    public InquireStudentsByParamsVo(String examinStatus, String studentStaffNameOrCode) {
        this.examinStatus = examinStatus;
        this.studentStaffNameOrCode = studentStaffNameOrCode;
    }

    public InquireStudentsByParamsVo() {
    }

    public String getExaminStatus() {
        return examinStatus;
    }

    public void setExaminStatus(String examinStatus) {
        this.examinStatus = examinStatus;
    }

    public String getStudentStaffNameOrCode() {
        return studentStaffNameOrCode;
    }

    public void setStudentStaffNameOrCode(String studentStaffNameOrCode) {
        this.studentStaffNameOrCode = studentStaffNameOrCode;
    }
}
