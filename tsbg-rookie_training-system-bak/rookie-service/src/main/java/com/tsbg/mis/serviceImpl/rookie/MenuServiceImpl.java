package com.tsbg.mis.serviceImpl.rookie;

import com.tsbg.mis.annotation.SysLog;
import com.tsbg.mis.dao.jurisdiction.UserRoleDao;
import com.tsbg.mis.dao.rookie.SysMenuListDao;
import com.tsbg.mis.dao.rookie.SysMenuRoleDao;
import com.tsbg.mis.jurisdiction.model.Role;
import com.tsbg.mis.jurisdiction.model.UserInfo;
import com.tsbg.mis.rookie.bag.MenuPackage;
import com.tsbg.mis.rookie.model.SysMenuRole;
import com.tsbg.mis.rookie.vo.SysMenuListVo;
import com.tsbg.mis.service.rookie.MenuService;
import com.tsbg.mis.serviceImpl.jurisdiction.base.TokenAnalysis;
import com.tsbg.mis.util.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 汪永晖
 */
@Service
public class MenuServiceImpl implements MenuService {

    private final SysMenuRoleDao sysMenuRoleDao;

    private final SysMenuListDao sysMenuListDao;

    private final TokenAnalysis tokenAnalysis;

    private final UserRoleDao userRoleDao;

    public MenuServiceImpl(SysMenuRoleDao sysMenuRoleDao, SysMenuListDao sysMenuListDao, TokenAnalysis tokenAnalysis, UserRoleDao userRoleDao) {
        this.sysMenuRoleDao = sysMenuRoleDao;
        this.sysMenuListDao = sysMenuListDao;
        this.tokenAnalysis = tokenAnalysis;
        this.userRoleDao = userRoleDao;
    }

    /**
     * 查询所有菜单列表
     *
     * @return
     */
    @Override
    public ResultUtils query() {
        List<SysMenuListVo> sysMenuListVos = sysMenuListDao.queryAllWithRoles();
        List<SysMenuRole> sysMenuRoleList = sysMenuRoleDao.queryAll(SysMenuRole.builder().status(1).build());

        if (sysMenuListVos == null || sysMenuRoleList == null) {
            return new ResultUtils(501, "暫無數據");
        }

        for (SysMenuListVo sysMenuListVo : sysMenuListVos) {
            // 菜单对应的角色信息
            List<SysMenuRole> sysMenuRoleIds = sysMenuRoleList.stream()
                    .filter(sysMenuRole -> {
                        if (sysMenuListVo.getMenuId() == null || sysMenuRole.getMenuId() == null) {
                            return false;
                        }
                        if (sysMenuListVo.getMenuId().equals(sysMenuRole.getMenuId())) {
                            return true;
                        }
                        return false;
                    })
                    .collect(Collectors.toList());
            if (sysMenuRoleIds == null) {
                return new ResultUtils(501, "暫無數據");
            }

            if (CollectionUtils.isEmpty(sysMenuRoleIds)) {
                continue;
            }
            List<Integer> roleIds = sysMenuRoleIds.stream()
                    .map(SysMenuRole::getRoleId)
                    .collect(Collectors.toList());
            sysMenuListVo.setRoleIds(roleIds);

            // 根据 roleIds 查询角色详细信息
            List<Role> roleList = userRoleDao.queryRoleInfoWithRoleIds(roleIds);
            sysMenuListVo.setRoleList(roleList);
        }

        return new ResultUtils(100, "成功查詢所有菜單信息", sysMenuListVos);
    }

    /**
     * 修改菜单
     *
     * @return
     */
    @Override
    @SysLog(value = "修改菜单")
    public ResultUtils update(MenuPackage menuPackage) {
        UserInfo currentUser = tokenAnalysis.getTokenUser();
        String staffCode = currentUser.getStaffCode();

        // 修改 sys_menu_list表和删除sys_menu_role中的数据
        if (menuPackage == null) {
            return new ResultUtils(500, "修改失敗");
        }
        menuPackage.setUpdateCode(staffCode);
        menuPackage.setCreateDate(new Date());
        sysMenuListDao.update(menuPackage);
        sysMenuRoleDao.deleteByMenuId(menuPackage.getMenuId());

        // 往sys_menu_role中新增数据

        List<Integer> roleIds = menuPackage.getRoleIds();
        if (roleIds == null || CollectionUtils.isEmpty(roleIds)) {
            return new ResultUtils(500, "修改失敗");
        }
        for (Integer roleId : roleIds) {
            sysMenuRoleDao.insert(SysMenuRole.builder()
                    .createDate(new Date())
                    .creatorCode(staffCode)
                    .menuId(menuPackage.getMenuId())
                    .roleId(roleId)
                    .status(1)
                    .build());
        }
        return new ResultUtils(101, "成功修改菜單");
    }

    /**
     * 新增菜单
     *
     * @param menuPackage
     * @return
     */
    @Override
    @SysLog(value = "新增菜单")
    public ResultUtils insert(MenuPackage menuPackage) {
        UserInfo currentUser = tokenAnalysis.getTokenUser();
        String staffCode = currentUser.getStaffCode();

        if (menuPackage == null) {
            return new ResultUtils(500, "新增失敗");
        }
        menuPackage.setCreatorCode(staffCode);
        menuPackage.setCreateDate(new Date());
        menuPackage.setStatus(1);
        sysMenuListDao.insert(menuPackage);

        // 往sys_menu_role中新增数据
        List<Integer> roleIds = menuPackage.getRoleIds();
        if (roleIds == null || CollectionUtils.isEmpty(roleIds)) {
            return new ResultUtils(500, "新增失敗");
        }
        for (Integer roleId : roleIds) {
            sysMenuRoleDao.insert(SysMenuRole.builder()
                    .createDate(new Date())
                    .creatorCode(staffCode)
                    .menuId(menuPackage.getMenuId())
                    .roleId(roleId)
                    .status(1)
                    .build());
        }

        return new ResultUtils(101, "成功新增菜單");
    }

    @Override
    @SysLog(value = "删除菜单")
    public ResultUtils delete(Integer menuId) {
        sysMenuRoleDao.deleteByMenuId(menuId);
        sysMenuListDao.deleteById(menuId);
        return new ResultUtils(101, "刪除成功");
    }

}
