package com.tsbg.mis.rookie.vo;

import lombok.Builder;
import lombok.Data;

import java.util.Date;


/**
 * @author :张梦雅
 * @description :
 * @create :2020-07-22 16:54:00
 */
@Data
@Builder
public class ProductionLineExamineVo {


    //菁干班同学名
    private String studentName;
    //菁干班同学工号
    private String studentCode;
    //任务类型 从type_list表中获取
    private Integer missionType;
    private String missionTypeName;
    //报告任务创建id
    private Integer creationId;
    //任务开始时间
    private Date missionStartTime;
    //任务结束时间
    private Date missionEndTime;
    //开放填写时间
    private Date startTime;
    //关闭填写时间
    private Date endTime;
    //是否启用 1是；0否
    private Integer isEnable;
    //状态
    private Integer stateId;
    private String stateName;

}
