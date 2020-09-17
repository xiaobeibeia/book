package com.tsbg.mis.rookie.vo;

import com.tsbg.mis.rookie.model.ClassSquadList;
import com.tsbg.mis.rookie.model.ClassStudentInfo;
import lombok.Data;

import java.util.List;

/**
 * @Author F1337200
 **/
//@Data
public class ClassSquadListVo {
    private ClassSquadList classSquadList;                  //小组信息

    private List<ClassStudentInfo> classStudentInfos;       //学生信息List

    public ClassSquadList getClassSquadList() {
        return classSquadList;
    }

    public void setClassSquadList(ClassSquadList classSquadList) {
        this.classSquadList = classSquadList;
    }

    public List<ClassStudentInfo> getClassStudentInfos() {
        return classStudentInfos;
    }

    public void setClassStudentInfos(List<ClassStudentInfo> classStudentInfos) {
        this.classStudentInfos = classStudentInfos;
    }
}
