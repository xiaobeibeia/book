package com.tsbg.mis.signed.model;

import com.tsbg.mis.rookie.group.Create;
import com.tsbg.mis.rookie.group.Update;
import io.swagger.annotations.ApiModel;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 签核流程信息表，用于存储已经定好的签核流程与主管信息(SignedWorkflow)实体类
 *
 * @author makejava
 * @since 2020-06-08 14:18:54
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@ApiModel(description = "签核流程信息表")
public class SignedWorkflow implements Serializable {
    private static final long serialVersionUID = 644982404334167294L;
    /**
     * 签核流程编号id
     */
    private Integer signWorkflowId;
    /**
     * 签核类型编号id，从sign_type表中获取
     */
    private Integer signTypeId;
    /**
     * 对应事务id，从business_list表中获取
     */
    private Integer businessId;
    /**
     * 签核顺序，用数字1、2、3等表示各个节点签核的顺序
     */
    @NotNull(message = "签核顺序不能为空", groups = {Update.class, Create.class})
    private Integer signSequence;
    /**
     * 签核节点编号id，从signed_node表中获取
     */
    private Integer signNodeId;
    /**
     * 签核节点名称，从signed_node表中获取
     */
    @NotBlank(message = "签核节点名称不能为空", groups = {Update.class, Create.class})
    private String nodeName;
    /**
     * 签核节点角色id
     */
    @NotNull(message = "签核节点角色不能为空", groups = {Update.class, Create.class})
    private Integer roleId;
    /**
     * 签核主管工号
     */
    private String managerCode;
    /**
     * 签核主管姓名
     */
    private String managerName;
    /**
     * 签核主管邮箱地址
     */
    private String managerMailAddress;
    /**
     * 对应项目系统id
     */
    private Integer projId;
    /**
     * 该流程是否在用：1是；0否
     */
    private Integer status;

}