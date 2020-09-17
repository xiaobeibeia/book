package com.tsbg.mis.rookie.vo;

import com.tsbg.mis.rookie.model.ClassStudentInfo;
import lombok.Data;

import java.util.List;

/**
 * @Author F1337200
 * @Date: 2020/6/24
 **/
@Data
public class CommitteeVo {

    private Integer gradeId;    //班级id

    private List<ClassStudentInfo> classStudentInfos;   //学生信息
}
