package com.tsbg.mis.rookie.vo;

import lombok.Data;

/**
 * @author :张梦雅
 * @description :产线考核--菁干班同学对应的最新产线信息
 * @create :2020-08-28 17:15:00
 */
@Data
public class NewLineForStudentsVo {
    private String staffCode;
    private String staffName;
    private String lineId;
    private String lineLeaderCode;
    private String lineGroupLeaderCode;
    private String lineManageCode;
}
