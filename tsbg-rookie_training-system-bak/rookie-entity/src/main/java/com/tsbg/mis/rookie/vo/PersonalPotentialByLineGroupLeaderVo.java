package com.tsbg.mis.rookie.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @author :张梦雅
 * @description :
 * @create :2020-07-28 16:47:00
 */
@Data
public class PersonalPotentialByLineGroupLeaderVo {
    /**
     * 产线组长品行信用评分
     */
    private ConductCredit conductCredit;
    /**
     * 产线组长个人潜质评分
     */
    private PersonalPotential personalPotential;
    /**
     * 个人潜力初评总得分
     */
    private String potentialGradeByLineGroupLeader;

}

