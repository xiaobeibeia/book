package com.tsbg.mis.rookie.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @author :张梦雅
 * @description :产线菁干班学生信息
 * @create :2020-07-28 16:41:00
 */
@Data
@Builder
public class StudentInformationVo {
    private String studentStaffCode;//学生工号
    private String studentStaffName;//学生姓名
    private String graduateSchool;//学校
    private String major;//专业
    private String organizationName;//组织名称(实习部门)
    private String gradeName;//班级
    private String lineGroupLeaderName;//产线负责组长
    private String lineGroupLeaderStaffCode;//产线负责组长工号
    private String lineManagerName;//产线负责人
    private String lineManagerStaffCode;//产线负责人工号
}
