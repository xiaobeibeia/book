package com.tsbg.mis.rookie.vo;

import lombok.Data;

/**
 * @Author F1337200
 * 学生班委类别实体类
 **/
@Data
public class StudentCommitteeVo {

    private String staffCode;               //学生工号

    private String staffName;               //学生姓名

    private Integer studentType;            //学生类别id

    private String typeName;                //学生类别名称

    private Integer gradeId;                //所属班级id

    private String gradeName;               //所属班级名称

    private Integer squadId;                //所属小组id

    private String squadName;               //所属小组名称
}
