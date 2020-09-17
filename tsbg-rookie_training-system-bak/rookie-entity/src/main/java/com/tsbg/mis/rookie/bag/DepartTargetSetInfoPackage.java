package com.tsbg.mis.rookie.bag;


import com.tsbg.mis.rookie.group.Create;
import com.tsbg.mis.rookie.group.Update;
import com.tsbg.mis.rookie.model.DepartTargetList;
import com.tsbg.mis.rookie.model.DepartTargetStudent;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class DepartTargetSetInfoPackage {

    private List<String> targetNumList;

    //部门学生关系表
    private List<DepartTargetStudent> departTargetStudent;

    //部门目标设置信息表
    private List<DepartTargetList> departTargetList;

    /**
     * 月目标单号
     */
    @NotBlank(message = "月目标单号不能为空", groups = Create.class)
    private String monthTargetNum;

    /**
     * 周目标单号
     */
    @NotBlank(message = "周目标单号不能为空", groups = Update.class)
    private String weekTargetNum;

    /**
     * 学生工号
     */
    @NotBlank(message = "学生工号不能为空", groups = Update.class)
    private String studentStaffCode;

}
