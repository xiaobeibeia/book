package com.tsbg.mis.rookie.vo;

import com.tsbg.mis.rookie.model.DepartTargetList;
import io.swagger.annotations.ApiModel;
import lombok.*;

import java.util.Date;
import java.util.List;

/**
 * @author 汪永晖
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@ApiModel(description = "菁干班查询目标的view对象")
public class TargetVo {

    private Date targetStartDate;

    private Date targetEndDate;

    //目标单号
    private String targetNum;

    /**
     * 目标类型
     */
    private String typeName;

    /**
     * 目标详情
     */
    private List<DepartTargetList> departTargetLists;

}
