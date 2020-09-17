package com.tsbg.mis.rookie.vo;

import io.swagger.annotations.ApiModel;
import lombok.*;

import java.util.Date;
import java.util.Objects;

/**
 * @author 汪永晖
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@ApiModel(description = "签核信息记录表(RookieApprovalInfoVo)Vo类")
public class RookieApprovalInfoVo {

    /**
     * 菁干班系统签核记录表id
     */
    private Integer rookieApprovalId;
    /**
     * 签核日期
     */
    private Date signedDate;
    /**
     * 签核的事务id
     */
    private Integer businessId;
    /**
     * 签核节点id
     */
    private Integer nodeId;
    /**
     * 签核节点名称
     */
    private String nodeName;
    /**
     * 签核主管工号
     */
    private String approveStaffCode;
    /**
     * 签核主管姓名
     */
    private String approveStaffName;
    /**
     * 签核主管邮箱地址
     */
    private String approveStaffMail;
    /**
     * 签核顺序，用来标记当前核准人是第几个，可由此看出层级
     */
    private Integer approveSequence;
    /**
     * 签核意见
     */
    private String approveOpinion;
    /**
     * 评分
     */
    private String grade;
    /**
     * 审核是否通过：1通过；0不通过/驳回；默认为1通过（产线无驳回）
     */
    private Integer isPass;
    /**
     * 对应系统项目id
     */
    private Integer projId;
    /**
     * 签核事务对应表名
     */
    private String investTableName;
    /**
     * 签核事务对应表中字段名
     */
    private String investFieldName;
    /**
     * 签核事务对应表中字段值
     */
    private String investFieldValue;
    /**
     * 是否有效：1是/有效；0否/无效
     */
    private Integer status;

    private String businessName;

    /**
     * 实习人工号
     */
    private String staffCode;

    /**
     * 实习人姓名
     */
    private String staffName;

    /**
     * 报告类型
     */
    private Integer reportType;

    /**
     * 实习类型
     */
    private Integer missionType;

    /**
     * 报告类型名称
     */
    private String typeName;

    /**
     * 评级
     */
    private Integer levelType;

    /**
     * 评级
     */
    private String levelTypeName;

    /**
     * 目标完成度
     */
    private String fulfillmentOfSchedule;

    /**
     * 目标完成度评语
     */
    private String targetComment;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RookieApprovalInfoVo)) return false;
        RookieApprovalInfoVo that = (RookieApprovalInfoVo) o;
        return getIsPass().equals(that.getIsPass()) &&
                getInvestTableName().equals(that.getInvestTableName()) &&
                getInvestFieldName().equals(that.getInvestFieldName()) &&
                getInvestFieldValue().equals(that.getInvestFieldValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIsPass(), getInvestTableName(), getInvestFieldName(), getInvestFieldValue());
    }
}
