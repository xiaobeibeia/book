package com.tsbg.mis.signed.bag;

import com.tsbg.mis.rookie.group.Create;
import com.tsbg.mis.rookie.group.Update;
import com.tsbg.mis.signed.model.SignedWorkflow;
import io.swagger.annotations.ApiModel;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 创建和编辑签核流程的前端传输对象
 *
 * @author 汪永晖
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@ApiModel(description = "创建和编辑签核流程的前端传输对象")
public class SignedPackage {

    /**
     * 对应事务id，从business_list表中获取
     */
    @NotNull(message = "菜单名称（英文）不能为空", groups = {Update.class})
    private Integer businessId;

    /**
     * 签核事务名称/描述
     */
    @NotBlank(message = "菜单名称（英文）不能为空", groups = {Update.class, Create.class})
    private String businessName;

    @Valid
    private List<SignedWorkflow> signedWorkflowList;
}
