package com.tsbg.mis.service.rookie;

import com.tsbg.mis.jurisdiction.bag.Key;
import com.tsbg.mis.jurisdiction.model.UserInfo;
import com.tsbg.mis.jurisdiction.vo.RoleAndInfoVo;
import com.tsbg.mis.jurisdiction.vo.UserInfoVo;
import com.tsbg.mis.util.ResultUtils;

/**
 * 菁干班权限管理
 *
 * @author 汪永晖
 */
public interface UserPowerService {

    /**
     * 查询菁干班系统所有用户
     *
     * @return
     */
    ResultUtils inquireManager();

    /**
     * 菁干班系统新增用户——搜索用户
     *
     * @param key 模糊搜索关键字
     * @return
     */
    ResultUtils searchAllUser(Key key);

    /**
     * 菁干班系统新增用户
     *
     * @param userInfoVo
     * @return
     */
    ResultUtils addManager(UserInfoVo userInfoVo);

    /**
     * 搜索菁干班系统的用户
     *
     * @param key 模糊搜索关键字
     * @return
     */
    ResultUtils searchHrmManager(Key key);

    /**
     * 查询菁干班系统所有角色信息
     *
     * @return
     */
    ResultUtils inquireRoleList();

    /**
     * 修改菁干班用户角色信息
     *
     * @param roleAndInfoVo
     * @return
     */
    ResultUtils modifyRoleByUserId(RoleAndInfoVo roleAndInfoVo);

    /**
     * 启用菁干班系统用户
     *
     * @param userId
     * @return
     */
    ResultUtils enableUser(Integer userId);

    /**
     * 停用菁干班系统用户
     *
     * @param userId
     * @return
     */
    ResultUtils disableUser(Integer userId);
}
