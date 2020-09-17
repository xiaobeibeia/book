package com.tsbg.mis.signed.vo;

import com.tsbg.mis.signed.model.SignedWorkflow;
import io.swagger.annotations.ApiModel;
import lombok.*;

import java.util.List;

/**
 * @author 汪永晖
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@ApiModel(description = "签核流程前端展示对象")
public class SignedWorkflowVo {

    private Integer businessId;
    /**
     * 签核类型编号id
     */
    private Integer signTypeId;
    /**
     * 签核事务名称/描述
     */
    private String businessName;
    /**
     * 事务对应项目系统编号id
     */
    private Integer projId;
    /**
     * 事务对应项目系统名称
     */
    private String proName;
    /**
     * 该事物是否在用：1是；0否
     */
    private Integer status;

    private List<SignedWorkflow> signedWorkflowList;
}
