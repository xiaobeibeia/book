package com.tsbg.mis.rookie.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @author :张梦雅
 * @description :
 * @create :2020-07-28 16:45:00
 */
@Builder
public class InternshipCommentInformationVo {
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

    public InternshipCommentInformationVo() {
    }

    public InternshipCommentInformationVo(String internshipComment, String comment, String monthlyReportGrade, String proposalReportGrade, String productionLineInternshipGrade) {
        this.internshipComment = internshipComment;
        this.comment = comment;
        this.monthlyReportGrade = monthlyReportGrade;
        this.proposalReportGrade = proposalReportGrade;
        this.productionLineInternshipGrade = productionLineInternshipGrade;
    }
}
