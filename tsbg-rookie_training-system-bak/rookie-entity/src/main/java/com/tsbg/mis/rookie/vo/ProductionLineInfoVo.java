package com.tsbg.mis.rookie.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;

@Data
public class ProductionLineInfoVo {
    private Integer studentId;//学生id

    private String staffCode;//学生工号

    private Integer lineId; //产线id

    private String lineName;//产线名称

    private Integer typeId; //产线id

    private String typeName;//类型名称

    private Integer lineMemberId;//成员id

    private String lineLeaderCode;//产线线长工号

    private String lineLeaderName;//产线线长姓名

    private String lineGroupLeaderCode;//产线组长工号

    private String lineGroupLeaderName;//产线组长姓名

    private String lineManagerCode;//产下负责人工号

    private String lineManagerName;//产线负责人姓名

    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date internshipStartDate;//实习开始时间

    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date internshipEndDate;//实习结束时间

    private String departName;//部门名称

    private String coachCode;//教练员工号

    private String coachName;//教练员姓名

    private String directManagerCode;//直属主管工号

    private String directManagerName;//直属主管姓名

    private String departManagerCode;//部门主管工号

    private String departManagerName;//部门主管姓名

    private String unitManagerCode;//处级主管工号

    private String unitManagerName;//处级主管姓名

    private String linePhoneNumber;//产线线长电话

    private String lineGroupPhoneNumber;//产线组长电话

    private String lineManagerPhoneNumber;//产线负责人电话

    private String coachuserExt;//教练员分机号

    private String directManageruserExt;//直属主管分机号

    private String departManageruserExt;//部门主管分机号

    private String unitManageruserExt;//处级主管分机号

    private String coachemailAddress;//教练员邮箱

    private String directManageremailAddress;//直属主管邮箱

    private String departManageremailAddress;//部门主管邮箱

    private String unitManageremailAddress;//处级主管邮箱
}
