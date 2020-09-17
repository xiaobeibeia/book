package com.tsbg.mis.rookie.vo;

import lombok.Data;



/**
 * @author :张梦雅
 * @description :产线实习与个人潜力信息集合 返回给前端对象
 * @create :2020-07-28 16:39:00
 */
@Data
public class ProductionLineAndPotentialData {

    private StudentInformationVo studentInformationVo;
    private InternshipCommentInformationVo internshipCommentInformationVo;
    private PersonalPotentialByLineGroupLeaderVo personalPotentialByLineGroupLeaderVo;
    private PersonalPotentialByLineManagerVo personalPotentialByLineManagerVo;
}
