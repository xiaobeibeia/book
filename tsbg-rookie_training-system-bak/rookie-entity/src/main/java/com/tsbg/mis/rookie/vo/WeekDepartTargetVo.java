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
@ApiModel(description = "编辑周目标的view对象")
public class WeekDepartTargetVo {

    //报告任务id，从report_creation_list中获取
    private Integer creationId;

    private Date targetStartDate;

    private Date targetEndDate;

    //0待审核；1审核通过（可修改）；2审核通过（不可修改）；3被驳回
    private Integer examineState;

    private List<DepartTargetList> departTargetLists;
}
