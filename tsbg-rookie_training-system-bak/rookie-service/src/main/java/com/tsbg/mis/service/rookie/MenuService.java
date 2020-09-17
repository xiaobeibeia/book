package com.tsbg.mis.service.rookie;

import com.tsbg.mis.rookie.bag.MenuPackage;
import com.tsbg.mis.util.ResultUtils;

/**
 * @author 汪永晖
 */
public interface MenuService {

    /**
     * 查询所有菜单列表
     *
     * @return
     */
    ResultUtils query();

    /**
     * 修改菜单
     *
     * @return
     */
    ResultUtils update(MenuPackage menuPackage);

    /**
     * 新增菜单
     *
     * @param menuPackage
     * @return
     */
    ResultUtils insert(MenuPackage menuPackage);

    /**
     * 删除菜单
     *
     * @param menuId
     * @return
     */
    ResultUtils delete(Integer menuId);
}
