package com.tsbg.mis.rookie.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 考核管理---实习期考核细项评分结果表(ExamineInternshipGrade)实体类
 *
 * @author makejava
 * @since 2020-07-24 18:49:53
 */
@Data
@Builder
public class ExamineInternshipGrade  {
    /**
     * 考核记录id
     */
    private Integer examineId;
    /**
     * 学生工号
     */
    private String staffCode;
    /**
     * 父ID即本表ID：即各个细项评分完成后对应大类的一个总得分记录
     */
    private Integer pid;
    /**
     * 报告任务id，从report_creation_list中获取
     */
    private Integer creationId;

    /**
     * 实习考核类型：examine_type_list中取产线/部门/转正调薪考核pid=0的记录编号即可
     */
    private Integer internshipType;
    /**
     * 产线id（产线考核必填）
     */
    private Integer lineId;
    /**
     * 实习内容（产线实习考核-产线实习部分必填）
     */
    private String internshipComment;
    /**
     * 评语
     */
    private String comment;
    /**
     * 考核类别id，此ID取examine_type_list中取得：若为考评级别的评分记录，则取细项ID；若为各个大类的总评分记录，则取大类的ID
     */
    private Integer examineTypeId;
    /**
     * 评分（类别为大类时表示：考核总得分）
     */
    private String examineGrade;
    /**
     * 考勤以及奖惩部分的次数（产线考核-出勤与奖惩部分必填）
     */
    private Integer countNum;
    /**
     * 考勤部分缺勤原因值（产线考核-出勤与奖惩部分必填）
     */
    private String reason;
    /**
     * 是否复评：1是；0否；默认为0
     */
    private Integer isReExamine;
    /**
     * 创建人工号
     */
    private String createCode;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 更新人工号
     */
    private String updateCode;
    /**
     * 更新时间
     */
    private Date updateDate;
    /**
     * 有效状态值：1有效；0无效
     */
    private Integer status;

    //以下不是表中字段
    /*private String lineGroupLeaderName ;//产线组长
    private String lineManagerName;//产线负责人
    private String conductCredit;//品行信用
    private String PersonalPotential;//个人潜质得分*/


    public Integer getExamineId() {
        return examineId;
    }

    public void setExamineId(Integer examineId) {
        this.examineId = examineId;
    }

    public String getStaffCode() {
        return staffCode;
    }

    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Integer getInternshipType() {
        return internshipType;
    }

    public void setInternshipType(Integer internshipType) {
        this.internshipType = internshipType;
    }

    public Integer getLineId() {
        return lineId;
    }

    public void setLineId(Integer lineId) {
        this.lineId = lineId;
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

    public Integer getExamineTypeId() {
        return examineTypeId;
    }

    public void setExamineTypeId(Integer examineTypeId) {
        this.examineTypeId = examineTypeId;
    }

    public String getExamineGrade() {
        return examineGrade;
    }

    public void setExamineGrade(String examineGrade) {
        this.examineGrade = examineGrade;
    }

    public Integer getCountNum() {
        return countNum;
    }

    public void setCountNum(Integer countNum) {
        this.countNum = countNum;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getIsReExamine() {
        return isReExamine;
    }

    public void setIsReExamine(Integer isReExamine) {
        this.isReExamine = isReExamine;
    }

    public String getCreateCode() {
        return createCode;
    }

    public void setCreateCode(String createCode) {
        this.createCode = createCode;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUpdateCode() {
        return updateCode;
    }

    public void setUpdateCode(String updateCode) {
        this.updateCode = updateCode;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCreationId() {
        return creationId;
    }

    public void setCreationId(Integer creationId) {
        this.creationId = creationId;
    }

    public ExamineInternshipGrade() {
    }

    public ExamineInternshipGrade(Integer examineId, String staffCode, Integer pid, Integer creationId, Integer internshipType, Integer lineId, String internshipComment, String comment, Integer examineTypeId, String examineGrade, Integer countNum, String reason, Integer isReExamine, String createCode, Date createDate, String updateCode, Date updateDate, Integer status) {
        this.examineId = examineId;
        this.staffCode = staffCode;
        this.pid = pid;
        this.creationId = creationId;
        this.internshipType = internshipType;
        this.lineId = lineId;
        this.internshipComment = internshipComment;
        this.comment = comment;
        this.examineTypeId = examineTypeId;
        this.examineGrade = examineGrade;
        this.countNum = countNum;
        this.reason = reason;
        this.isReExamine = isReExamine;
        this.createCode = createCode;
        this.createDate = createDate;
        this.updateCode = updateCode;
        this.updateDate = updateDate;
        this.status = status;
    }
}