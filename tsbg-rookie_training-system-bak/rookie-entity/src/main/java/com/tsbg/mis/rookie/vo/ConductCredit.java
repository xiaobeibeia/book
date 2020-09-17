package com.tsbg.mis.rookie.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @author :张梦雅
 * @description :品行信用范前端对象
 * @create :2020-07-29 14:38:00
 */
@Data
@Builder
public class ConductCredit {
    private Integer examineTypeId;
    private String grade;
}
