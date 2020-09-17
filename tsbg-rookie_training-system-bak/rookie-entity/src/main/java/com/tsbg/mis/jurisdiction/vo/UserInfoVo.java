package com.tsbg.mis.jurisdiction.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * @author 汪永晖
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@ApiModel(description = "人资系统新增用户")
public class UserInfoVo {

    @ApiModelProperty(value = "用户 id", example = "52", required = true)
    private Integer userId;
    @ApiModelProperty(value = "用户工号", example = "F1336537", required = true)
    private String userCode;
    @ApiModelProperty(value = "用户姓名", example = "汪永晖", required = true)
    private String userName;
    @ApiModelProperty(value = "用户手机号")
    private String phoneNumber;
    @ApiModelProperty(value = "用户 email 地址")
    private String emailAddress;
    @ApiModelProperty(value = "用户创建时间")
    private Date createTime;
    @ApiModelProperty(value = "用户是否被停用")
    private Integer isLock;
}
