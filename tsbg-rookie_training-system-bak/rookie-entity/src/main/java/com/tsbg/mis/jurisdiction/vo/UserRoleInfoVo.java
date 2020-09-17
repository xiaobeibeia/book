package com.tsbg.mis.jurisdiction.vo;

import com.tsbg.mis.jurisdiction.model.Role;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * 人资系统用户视图对象
 *
 * @author 汪永晖
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@ApiModel(description = "人资系统用户视图对象")
public class UserRoleInfoVo {

    @ApiModelProperty(value = "用户 id", example = "F1336537", required = true)
    private Integer userId;
    @ApiModelProperty(value = "用户工号", example = "F1337300", required = true)
    private String staffCode;
    @ApiModelProperty(value = "用户姓名", example = "張穩", required = true)
    private String userName;
    @ApiModelProperty(value = "修改时间")
    private Date createDate;
    @ApiModelProperty(value = "事业处")
    private String unitName;
    @ApiModelProperty(value = "厂区信息")
    private String factoryName;
    @ApiModelProperty(value = "部门名称")
    private String departName;
    @ApiModelProperty(value = "组织名称")
    private String organizationName;
    @ApiModelProperty(value = "用户 email")
    private String emailAddress;
    @ApiModelProperty(value = "用户是否被停用")
    private Integer isLock;
    @ApiModelProperty(value = "用户角色列表")
    private List<Role> roles;
}
