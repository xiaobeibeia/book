package com.tsbg.mis.rookie.vo;


import java.util.List;

/**
 * @Author F1337200
 **/
public class ClassGradeInfoVo {

    private List<String> staffCodes;    //学生工号list

    private Integer gradeId;            //班级id

    private Integer squadId;            //小组id


    public List<String> getStaffCodes() {
        return staffCodes;
    }

    public void setStaffCodes(List<String> staffCodes) {
        this.staffCodes = staffCodes;
    }

    public Integer getGradeId() {
        return gradeId;
    }

    public void setGradeId(Integer gradeId) {
        this.gradeId = gradeId;
    }

    public Integer getSquadId() {
        return squadId;
    }

    public void setSquadId(Integer squadId) {
        this.squadId = squadId;
    }
}
