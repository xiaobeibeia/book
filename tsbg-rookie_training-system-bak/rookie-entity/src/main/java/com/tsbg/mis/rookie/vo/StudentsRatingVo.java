package com.tsbg.mis.rookie.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @author :张梦雅
 * @description :菁干班同学所有评分情况
 * @create :2020-07-28 10:00:00
 */
@Data
@Builder
public class StudentsRatingVo {
    private String studentCode;
    private String studentName;
    //考核类别id
    private String examineTypeId;
    //考核分数
    private String examineGrade;
}
