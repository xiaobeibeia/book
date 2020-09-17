package com.tsbg.mis.rookie.vo;

import com.tsbg.mis.rookie.group.Create;
import com.tsbg.mis.rookie.group.Update;
import io.swagger.annotations.ApiModel;
import lombok.*;

import javax.validation.constraints.NotBlank;

/**
 * @author 汪永晖
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@ApiModel(description = "签核进度Vo类")
public class SignedProgressVo {

    /**
     * 签核节点名称，从signed_node表中获取
     */
    private String nodeName;

    /**
     * 审核是否通过：1通过；0不通过/驳回；2提交；3待審核；
     */
    private Integer isPass;

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
     * 签核主管邮箱手机号
     */
    private String approveStaffPhoneNumber;

    /**
     * 签核意见
     */
    private String approveOpinion;
    /**
     * 评分
     */
    private String grade;

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

}
