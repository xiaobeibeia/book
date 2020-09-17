package com.tsbg.mis.dao.rookie;

import com.tsbg.mis.rookie.model.SysMenuRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统菜单与系统角色中间表(SysMenuRole)表数据库访问层
 *
 * @author makejava
 * @since 2020-06-03 14:38:34
 */
public interface SysMenuRoleDao {

    /**
     * 通过ID查询单条数据
     *
     * @param menuRoleId 主键
     * @return 实例对象
     */
    SysMenuRole queryById(Integer menuRoleId);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<SysMenuRole> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param sysMenuRole 实例对象
     * @return 对象列表
     */
    List<SysMenuRole> queryAll(SysMenuRole sysMenuRole);

    /**
     * 新增数据
     *
     * @param sysMenuRole 实例对象
     * @return 影响行数
     */
    int insert(SysMenuRole sysMenuRole);

    /**
     * 修改数据
     *
     * @param sysMenuRole 实例对象
     * @return 影响行数
     */
    int update(SysMenuRole sysMenuRole);

    /**
     * 通过主键删除数据
     *
     * @param menuRoleId 主键
     * @return 影响行数
     */
    int deleteById(Integer menuRoleId);

    /**
     * 通过menu_id删除数据
     *
     * @param menuId
     */
    int deleteByMenuId(Integer menuId);

    /**
     * 查询用户角色对应的菜单
     *
     * @param roleIds
     * @return
     */
    List<SysMenuRole> queryAllWithRoles(List<Integer> roleIds);

    /**
     * 查询 userImg
     * @param staffCode
     * @return
     */
    String selectUserImg(String staffCode);
}