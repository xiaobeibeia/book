package com.tsbg.mis.dao.rookie;

import com.tsbg.mis.rookie.bag.MenuPackage;
import com.tsbg.mis.rookie.model.SysMenuList;
import com.tsbg.mis.rookie.vo.SysMenuListVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统菜单配置表(SysMenuList)表数据库访问层
 *
 * @author makejava
 * @since 2020-06-03 14:38:34
 */
public interface SysMenuListDao {

    /**
     * 通过ID查询单条数据
     *
     * @param menuId 主键
     * @return 实例对象
     */
    SysMenuList queryById(Integer menuId);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<SysMenuList> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param sysMenuList 实例对象
     * @return 对象列表
     */
    List<SysMenuList> queryAll(SysMenuList sysMenuList);

    /**
     * 新增数据
     *
     * @param menuPackage 实例对象
     * @return 影响行数
     */
    int insert(MenuPackage menuPackage);

    /**
     * 修改数据
     *
     * @param menuPackage 实例对象
     * @return 影响行数
     */
    int update(MenuPackage menuPackage);

    /**
     * 通过主键删除数据
     *
     * @param menuId 主键
     * @return 影响行数
     */
    int deleteById(Integer menuId);

    /**
     * 查询所有菜单
     *
     * @return
     */
    List<SysMenuListVo> queryAllWithRoles();

    /**
     * 根据 menu_id 查询菜单
     *
     * @param menuIdList
     * @return
     */
    List<SysMenuListVo> queryByMenuIds(List<Integer> menuIdList);
}