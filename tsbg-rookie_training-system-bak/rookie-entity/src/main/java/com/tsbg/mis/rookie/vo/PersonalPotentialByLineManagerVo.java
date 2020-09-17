package com.tsbg.mis.rookie.vo;

import lombok.Data;

/**
 * @author :张梦雅
 * @description :
 * @create :2020-07-28 16:50:00
 */
@Data
public class PersonalPotentialByLineManagerVo {
    /**
     * 产线负责人品行信用评分
     */
    private ConductCredit conductCredit;
    /**
     * 个产线负责人人潜质评分
     */
    private PersonalPotential personalPotential;
    /**
     * 个人潜力复评总得分
     */
    private String potentialGradeByLineManager;
}
