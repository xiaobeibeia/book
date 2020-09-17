package com.tsbg.mis.rookie.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @author :张梦雅
 * @description :产线实习月度考核--查看整体评分情况 前后端交互
 * @create :2020-07-28 09:15:00
 */
@Data
@Builder
public class RatingVo {
    //考核内容
    private String examinedContent;
    //规定占比
    private String prescribedProportion;
    //现有占比
    private String currentShare;
    //现有人数
    private String currentPeople;
}
