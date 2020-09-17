package com.tsbg.mis.rookie.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author F1337200
 **/

@Data
public class GradeStudentInfoVo {

    private List<ClassSquadListVo> classSquadListVos;       //返回给前端的某班级各小组成员信息list

}
