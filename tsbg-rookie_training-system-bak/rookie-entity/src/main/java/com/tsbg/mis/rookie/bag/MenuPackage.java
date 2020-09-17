package com.tsbg.mis.rookie.bag;

import com.tsbg.mis.rookie.group.Create;
import com.tsbg.mis.rookie.group.Update;
import io.swagger.annotations.ApiModel;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @author 汪永晖
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@ApiModel(description = "修改菜单的前端传输对象")
public class MenuPackage {

    /**
     * 菜单ID
     */
    @NotNull(message = "菜单ID不能为空", groups = Update.class)
    private Integer menuId;
    /**
     * 上级菜单ID，一级菜单为0，默认为0
     */
    @NotNull(message = "上级菜单ID不能为空", groups = {Update.class, Create.class})
    private Integer parentId;

    /**
     * 菜单名称（英文）
     */
    @NotBlank(message = "菜单名称（英文）不能为空", groups = {Update.class, Create.class})
    private String name;
    /**
     * 菜单标题（中文）
     */
    @NotBlank(message = "菜单标题（中文）不能为空", groups = {Update.class, Create.class})
    private String title;
    /**
     * 菜单层级
     */
    @NotNull(message = "菜单层级不能为空", groups = {Update.class, Create.class})
    private Integer level;
    /**
     * 排序
     */
    @NotNull(message = "排序不能为空", groups = {Update.class, Create.class})
    private Integer sort;
    /**
     * 重定向路由
     */
    private String redirect;
    /**
     * 菜单类型  1目录 2 菜单 3 按钮 4特殊
     */
    @NotNull(message = "菜单类型不能为空", groups = {Update.class, Create.class})
    private Integer menuType;
    /**
     * 组件名称
     */
    @NotBlank(message = "组件名称不能为空", groups = {Update.class, Create.class})
    private String component;
    /**
     * 路由
     */
    @NotBlank(message = "路由不能为空", groups = {Update.class, Create.class})
    private String path;
    /**
     * 图标
     */
    private String icon;
    /**
     * 是否隐藏：1是；0否
     */
    @NotNull(message = "是否隐藏不能为空", groups = {Update.class, Create.class})
    private Integer isHidden;
    /**
     * 是否禁用：1是；0否
     */
    @NotNull(message = "是否禁用不能为空", groups = {Update.class, Create.class})
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

    /**
     * 对应的 role_id
     */
    private List<Integer> roleIds;
}
