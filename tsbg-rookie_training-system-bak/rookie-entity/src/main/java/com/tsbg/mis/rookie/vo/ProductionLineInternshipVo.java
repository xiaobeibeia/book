package com.tsbg.mis.rookie.vo;

import lombok.Data;

/**
 * @author :张梦雅
 * @description :产线实习 个人潜质前端传输对象
 * @create :2020-07-25 10:05:00
 */
@Data
public class ProductionLineInternshipVo {
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

    /**
     * 产线实习总得分
     */
    private String productionLineInternshipGrade;
    /**
     * 产线组长品行信用评分
     */
    private String conductCreditByLineGroupLeader;
    /**
     * 个人潜力初评总得分
     */
    private String potentialGradeByLineGroupLeader;

    /**
     * 个产线组长人潜质评分
     */
    private String personalPotentialByLineGroupLeader;
    /**
     * 产线负责人品行信用评分
     */
    private String conductCreditByLineManager;
    /**
     * 个产线负责人人潜质评分
     */
    private String personalPotentialByLineManager;
    /**
     * 个人潜力复评总得分
     */
    private String potentialGradeByLineManager;
    private String typeName;//考核类型
    private String examineGrade;//考核分数
    private Integer isReExamine;//是否复评

    public ProductionLineInternshipVo() {
    }

    public ProductionLineInternshipVo(String studentStaffCode, String studentStaffName, String graduateSchool, String major, String organizationName, String gradeId, String lineGroupLeaderName, String lineManagerName, String internshipComment, String comment, String monthlyReportGrade, String proposalReportGrade, String productionLineInternshipGrade, String conductCreditByLineGroupLeader, String potentialGradeByLineGroupLeader, String personalPotentialByLineGroupLeader, String conductCreditByLineManager, String personalPotentialByLineManager, String potentialGradeByLineManager, String typeName, String examineGrade, Integer isReExamine) {
        this.studentStaffCode = studentStaffCode;
        this.studentStaffName = studentStaffName;
        this.graduateSchool = graduateSchool;
        this.major = major;
        this.organizationName = organizationName;
        this.gradeId = gradeId;
        this.lineGroupLeaderName = lineGroupLeaderName;
        this.lineManagerName = lineManagerName;
        this.internshipComment = internshipComment;
        this.comment = comment;
        this.monthlyReportGrade = monthlyReportGrade;
        this.proposalReportGrade = proposalReportGrade;
        this.productionLineInternshipGrade = productionLineInternshipGrade;
        this.conductCreditByLineGroupLeader = conductCreditByLineGroupLeader;
        this.potentialGradeByLineGroupLeader = potentialGradeByLineGroupLeader;
        this.personalPotentialByLineGroupLeader = personalPotentialByLineGroupLeader;
        this.conductCreditByLineManager = conductCreditByLineManager;
        this.personalPotentialByLineManager = personalPotentialByLineManager;
        this.potentialGradeByLineManager = potentialGradeByLineManager;
        this.typeName = typeName;
        this.examineGrade = examineGrade;
        this.isReExamine = isReExamine;
    }

    public String getStudentStaffCode() {
        return studentStaffCode;
    }

    public void setStudentStaffCode(String studentStaffCode) {
        this.studentStaffCode = studentStaffCode;
    }

    public String getStudentStaffName() {
        return studentStaffName;
    }

    public void setStudentStaffName(String studentStaffName) {
        this.studentStaffName = studentStaffName;
    }

    public String getGraduateSchool() {
        return graduateSchool;
    }

    public void setGraduateSchool(String graduateSchool) {
        this.graduateSchool = graduateSchool;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public String getLineGroupLeaderName() {
        return lineGroupLeaderName;
    }

    public void setLineGroupLeaderName(String lineGroupLeaderName) {
        this.lineGroupLeaderName = lineGroupLeaderName;
    }

    public String getLineManagerName() {
        return lineManagerName;
    }

    public void setLineManagerName(String lineManagerName) {
        this.lineManagerName = lineManagerName;
    }

    public String getInternshipComment() {
        return internshipComment;
    }

    public void setInternshipComment(String internshipComment) {
        this.internshipComment = internshipComment;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getMonthlyReportGrade() {
        return monthlyReportGrade;
    }

    public void setMonthlyReportGrade(String monthlyReportGrade) {
        this.monthlyReportGrade = monthlyReportGrade;
    }

    public String getProposalReportGrade() {
        return proposalReportGrade;
    }

    public void setProposalReportGrade(String proposalReportGrade) {
        this.proposalReportGrade = proposalReportGrade;
    }

    public String getProductionLineInternshipGrade() {
        return productionLineInternshipGrade;
    }

    public void setProductionLineInternshipGrade(String productionLineInternshipGrade) {
        this.productionLineInternshipGrade = productionLineInternshipGrade;
    }

    public String getConductCreditByLineGroupLeader() {
        return conductCreditByLineGroupLeader;
    }

    public void setConductCreditByLineGroupLeader(String conductCreditByLineGroupLeader) {
        this.conductCreditByLineGroupLeader = conductCreditByLineGroupLeader;
    }

    public String getPotentialGradeByLineGroupLeader() {
        return potentialGradeByLineGroupLeader;
    }

    public void setPotentialGradeByLineGroupLeader(String potentialGradeByLineGroupLeader) {
        this.potentialGradeByLineGroupLeader = potentialGradeByLineGroupLeader;
    }

    public String getPersonalPotentialByLineGroupLeader() {
        return personalPotentialByLineGroupLeader;
    }

    public void setPersonalPotentialByLineGroupLeader(String personalPotentialByLineGroupLeader) {
        this.personalPotentialByLineGroupLeader = personalPotentialByLineGroupLeader;
    }

    public String getConductCreditByLineManager() {
        return conductCreditByLineManager;
    }

    public void setConductCreditByLineManager(String conductCreditByLineManager) {
        this.conductCreditByLineManager = conductCreditByLineManager;
    }

    public String getPersonalPotentialByLineManager() {
        return personalPotentialByLineManager;
    }

    public void setPersonalPotentialByLineManager(String personalPotentialByLineManager) {
        this.personalPotentialByLineManager = personalPotentialByLineManager;
    }

    public String getPotentialGradeByLineManager() {
        return potentialGradeByLineManager;
    }

    public void setPotentialGradeByLineManager(String potentialGradeByLineManager) {
        this.potentialGradeByLineManager = potentialGradeByLineManager;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getExamineGrade() {
        return examineGrade;
    }

    public void setExamineGrade(String examineGrade) {
        this.examineGrade = examineGrade;
    }

    public Integer getIsReExamine() {
        return isReExamine;
    }

    public void setIsReExamine(Integer isReExamine) {
        this.isReExamine = isReExamine;
    }
}
