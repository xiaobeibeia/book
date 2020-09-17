package com.tsbg.mis.rookie.bag;

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
@ApiModel(description = "编辑周目标的前端传输对象")
public class WeekDepartTargetPackage {

    //报告任务id，从report_creation_list中获取
    private Integer creationId;

    private List<String> targetNumList;
}
