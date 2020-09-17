package com.tsbg.mis.rookie.vo;

import lombok.Data;

/**
 * @author :张梦雅
 * @description :产线实习考核总评分 前后端传输vo
 * @create :2020-08-01 16:16:00
 */
@Data
public class ProductionLineExamineTotalGradeVo {
    private String staffCode;//菁干班学生工号
    private String staffName;//菁干班学生姓名
    private String totalGrade;//考核总得分
    private Integer rank;//获得等级 38:优 39:甲 40:乙 41:丙 42:丁

    public ProductionLineExamineTotalGradeVo() {
    }

    public ProductionLineExamineTotalGradeVo(String staffCode, String staffName, String totalGrade, Integer rank) {
        this.staffCode = staffCode;
        this.staffName = staffName;
        this.totalGrade = totalGrade;
        this.rank = rank;
    }

    public String getStaffCode() {
        return staffCode;
    }

    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getTotalGrade() {
        return totalGrade;
    }

    public void setTotalGrade(String totalGrade) {
        this.totalGrade = totalGrade;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }
}
