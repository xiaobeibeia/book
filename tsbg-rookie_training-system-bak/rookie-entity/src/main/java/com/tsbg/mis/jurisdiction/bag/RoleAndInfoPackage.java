package com.tsbg.mis.jurisdiction.bag;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.Objects;

/**
 * 人资系统用户的角色信息
 *
 * @author 汪永晖
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@ApiModel(description = "人资系统用户的角色信息")
public class RoleAndInfoPackage {

    @ApiModelProperty(value = "user_role_id")
    private Integer uroleId;
    @ApiModelProperty(value = "用户 id", example = "52", required = true)
    private Integer userId;
    @ApiModelProperty(value = "角色 id", example = "16", required = true)
    private Integer roleId;
    @ApiModelProperty(value = "用户是否被停用")
    private Integer isLock;
    @ApiModelProperty(value = "该角色所处状态", example = "1", required = true)
    private Integer status;
    @ApiModelProperty(value = "创建人工号")
    private String createCode;
    @ApiModelProperty(value = "修改时间")
    private Date createDate;
    @ApiModelProperty(value = "上次修改人的工号")
    private String lastUpdateCode;
    @ApiModelProperty(value = "上次修改时间")
    private Date lastUpdateDate;
    @ApiModelProperty(value = "项目 id")
    private Integer projId;
    @ApiModelProperty(value = "用户工号", example = "F1337300", required = true)
    private String staffCode;
    @ApiModelProperty(value = "用户姓名", example = "張穩", required = true)
    private String userName;
    @ApiModelProperty(value = "事业处 id")
    private Integer unitId;
    @ApiModelProperty(value = "事业处")
    private String unitName;
    @ApiModelProperty(value = "用户 email")
    private String emailAddress;
    @ApiModelProperty(value = "用户角色")
    private String roleName;
    @ApiModelProperty(value = "用户角色描述信息")
    private String description;
    @ApiModelProperty(value = "厂区信息")
    private String factoryName;
    @ApiModelProperty(value = "部门名称")
    private String departName;
    @ApiModelProperty(value = "组织名称")
    private String organizationName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RoleAndInfoPackage)) return false;
        RoleAndInfoPackage that = (RoleAndInfoPackage) o;
        return Objects.equals(getRoleId(), that.getRoleId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getRoleId());
    }
}
