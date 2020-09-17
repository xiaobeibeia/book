package com.tsbg.mis.rookie.vo;

import com.tsbg.mis.rookie.model.DepartTargetList;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class DepartTargetListVo extends DepartTargetList {
    private Integer resultId;

    private String resultName;

    private String resultContent;

    private List<DepartTargetList> departTargetLists;
}
