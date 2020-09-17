package com.tsbg.mis.rookie.bag;

import com.tsbg.mis.rookie.model.RookieApprovalInfo;
import io.swagger.annotations.ApiModel;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author 汪永晖
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@ApiModel(description = "月目标签核的前端传输类")
public class MonthTargetApprovalInfoPackage {

    /**
     * 签核的事务id
     */
//    @NotNull(message = "签核的事务id不能为空")
//    private Integer businessId;

    /**
     * 签核意见
     */
    private String approveOpinion;

    /**
     * 审核是否通过：1通过；0不通过/驳回；默认为1通过
     */
    @NotNull(message = "审核是否通过不能为空")
    private Integer isPass;

    /**
     * 一组 targetNum
     */
    private List<String> targetNumList;

    /**
     * 签核记录列表
     */
    private List<Integer> rookieApprovalInfoList;
}
