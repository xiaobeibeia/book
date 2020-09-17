package com.tsbg.mis.log.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: 海波
 * @Date: 2019/12/4 15:38
 */
@TableName("system_log")
@Data
public class SystemLog implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId
    private Integer logId;              //日誌log編號id
    private String dataValue;           //請求或修改信息的值
    private String requestUserCode;     //請求/修改人工號
    private String requestUserName;     //請求/更新/修改人姓名
    private String requestIp;           //請求方IP地址
    private String requestMethod;       //請求方式
    private Integer requestCode;       //請求返回狀態碼
    private String versionNumber;       //系統版本號
    private String operation;
    private String params;
    private Long time;
    private Date createTime;
    private String requestMessage;
}
