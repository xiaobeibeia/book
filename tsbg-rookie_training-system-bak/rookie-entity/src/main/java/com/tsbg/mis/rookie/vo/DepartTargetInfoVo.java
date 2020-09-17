package com.tsbg.mis.rookie.vo;

import lombok.Data;

import java.util.List;

/**
 * @PackgeName: com.tsbg.mis.rookie.vo
 * @ClassName: DepartTargetInfoVo
 * @Author: 陳觀泰
 * Date: 2020/8/11 10:59
 * Description:
 */
@Data
public class DepartTargetInfoVo {

    //目标单号
    private String targetNum;

    private List<DepartTargetSetInfoVo> departTargetSetInfoVoList;


}
