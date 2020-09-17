package com.tsbg.mis.rookie.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @author :张梦雅
 * @description :个人潜质 返前端对象
 * @create :2020-07-29 14:45:00
 */
@Data
@Builder
public class PersonalPotential {
    private Integer examineTypeId;
    private String grade;
}
