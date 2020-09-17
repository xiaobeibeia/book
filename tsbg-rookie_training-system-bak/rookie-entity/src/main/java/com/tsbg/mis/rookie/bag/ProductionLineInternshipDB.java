package com.tsbg.mis.rookie.bag;

import lombok.Data;

/**
 * @author :张梦雅
 * @description :产线实习 个人潜质数据库输出对象
 * @create :2020-07-27 14:08:00
 */
@Data
public class ProductionLineInternshipDB {
    private String studentStaffCode;//学生工号
    private String studentStaffName;//学生姓名
    private String graduateSchool;//学校
    private String major;//专业
    private String organizationName;//组织名称(实习部门)
    private String gradeId;//班级
    private String lineGroupLeaderName;//产线负责组长
    private String lineManagerName;//产线负责人
    /**
     * 实习内容（产线实习考核-产线实习部分必填）
     */
    private String internshipComment;
    /**
     * 评语
     */
    private String comment;
    /**
     * 月报得分
     */
    private String monthlyReportGrade;
    /**
     * 提案改善报告得分
     */
    private String proposalReportGrade;


    private String typeName;//考核类型
    private String examineGrade;//考核分数
    private Integer isReExamine;//是否复评
}
