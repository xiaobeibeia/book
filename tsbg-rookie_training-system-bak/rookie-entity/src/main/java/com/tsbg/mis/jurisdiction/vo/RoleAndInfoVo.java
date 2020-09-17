package com.tsbg.mis.jurisdiction.vo;

import com.tsbg.mis.jurisdiction.bag.RoleAndInfoPackage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
public class RoleAndInfoVo {

    @ApiModelProperty(value = "用户 id", example = "F1336537", required = true)
    private Integer userId;
    @ApiModelProperty(value = "人资系统用户的角色信息", required = true)
    private List<RoleAndInfoPackage> roleAndInfoPackageList;
}
