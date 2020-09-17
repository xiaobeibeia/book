package com.tsbg.mis.rookie.model;

import io.swagger.annotations.ApiModel;
import lombok.*;

import java.util.Date;
import java.io.Serializable;

/**
 * 系统菜单与系统角色中间表(SysMenuRole)实体类
 *
 * @author makejava
 * @since 2020-06-03 14:27:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@ApiModel(description = "系统菜单与系统角色中间表(SysMenuRole)实体类")
public class SysMenuRole implements Serializable {
    private static final long serialVersionUID = 693456884541767903L;
    /**
     * 中间表id
     */
    private Integer menuRoleId;
    /**
     * 菜单ID
     */
    private Integer menuId;
    /**
     * 角色自增id
     */
    private Integer roleId;
    /**
     * 创建人工号
     */
    private String creatorCode;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 更新/修改人工号
     */
    private String updateCode;
    /**
     * 更新/修改时间
     */
    private Date updateDate;
    /**
     * 有效状态：1有效；0无效
     */
    private Integer status;

}