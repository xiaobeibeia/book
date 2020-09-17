package com.tsbg.mis.signed.bag;

import io.swagger.annotations.ApiModel;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author 汪永晖
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@ApiModel(description = "签核信息记录表(RookieApprovalInfoPackage)前端传输类")
public class RookieApprovalInfoPackage {

    /**
     * 菁干班系统签核记录表id
     */
    @NotNull(message = "菁干班系统签核记录表id不能为空")
    private Integer rookieApprovalId;
    /**
     * 签核的事务id
     */
    @NotNull(message = "签核的事务id不能为空")
    private Integer businessId;
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
    @NotNull(message = "审核是否通过不能为空")
    private Integer isPass;

    /**
     * 实习人工号
     */
    @NotBlank(message = "实习人工号不能为空")
    private String staffCode;

    private Integer lineId;

    /**
     * 评级
     */
    private Integer levelType;

    /**
     * 目标完成度
     */
    private String fulfillmentOfSchedule;

    /**
     * 目标完成度评语
     */
    private String targetComment;
}
