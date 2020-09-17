package com.tsbg.mis.rookie.vo;

import com.tsbg.mis.rookie.model.ClassStudentInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ClassStudentInfoVo extends ClassStudentInfo {

    private String gradeName;//班级名称
}
