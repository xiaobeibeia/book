package com.tsbg.mis.rookie.model;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @PackgeName: com.tsbg.mis.rookie.model
 * @ClassName: RewardPunishRecord
 * @Author: 陳觀泰
 * Date: 2020/8/6 16:07
 * Description:獎懲實體類
 */
@Data
public class RewardPunishRecord {

    /**
     * 獎懲記錄id
     */
    private Integer recordId;

    /**
     *員工工號
     */
    @Excel(name = "员工工号",orderNum = "0")
    private String staffCode;

    /**
     *員工姓名
     */
    @Excel(name = "员工姓名",orderNum = "1")
    private String staffName;

    /**
     *奖惩类别名称：如事業群活動等，给模板时要说明输入繁体字，以便与examine_type_list中的数据作对比算分数
     */
    @Excel(name = "奖惩类别名称",orderNum = "2")
    private String examineType;

    /**
     * 奖惩类别id：从examine_type_list中获取
     */
    private Integer examineTypeId;

    /**
     * 奖惩日期（如人资提供则存储，不提则不存）
     */
    @Excel(name = "奖惩日期",orderNum = "3")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date date;

    /**
     * 奖惩原因（如人资提供则存储，不提则不存）
     */
    @Excel(name = "奖惩原因",orderNum = "4")
    private String reason;

    /**
     * 奖惩次数
     */
    private Integer countNum;

    /**
     * 奖惩分数，根据数据自行计算
     */
    private String grade;

    /**
     * 创建人工号
     */
    private String createCode;

    /**
     * 創建時間
     */
    private Date createTime;

    /**
     * 有效状态值：1有效；0无效；默认为1
     */
    private Integer status;

}
