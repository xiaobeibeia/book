package com.tsbg.mis.rookie.bag;

import io.swagger.annotations.ApiModel;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author 汪永晖
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@ApiModel(description = "月目标提交审核的前端传输类")
public class MonthlyTargetPackage {

    /**
     * 月目标单号
     */
    private List<String> targetNum;

    /**
     * 审核人的 StaffCode
     */
    @NotBlank(message = "审核人的 StaffCode 不能为空")
    private String staffCode;
}
