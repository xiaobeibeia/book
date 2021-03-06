package com.tsbg.mis.rookie.model;

import io.swagger.annotations.ApiModel;
import lombok.*;

import java.util.Date;
import java.io.Serializable;

/**
 * 系统菜单配置表(SysMenuList)实体类
 *
 * @author makejava
 * @since 2020-06-03 14:29:19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@ApiModel(description = "系统菜单配置表(SysMenuList)实体类")
public class SysMenuList implements Serializable {
    private static final long serialVersionUID = -27866158389652898L;
    /**
     * 菜单ID
     */
    private Integer menuId;
    /**
     * 上级菜单ID，一级菜单为0，默认为0
     */
    private Integer parentId;
    /**
     * 菜单名称（英文）
     */
    private String name;
    /**
     * 菜单标题（中文）
     */
    private String title;
    /**
     * 菜单层级
     */
    private Integer level;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 重定向路由
     */
    private String redirect;
    /**
     * 菜单类型  1目录 2 菜单 3 按钮 4特殊
     */
    private Integer menuType;
    /**
     * 组件名称
     */
    private String component;
    /**
     * 路由
     */
    private String path;
    /**
     * 图标
     */
    private String icon;
    /**
     * 是否隐藏：1是；0否
     */
    private Integer isHidden;
    /**
     * 是否禁用：1是；0否
     */
    private Integer isLock;
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