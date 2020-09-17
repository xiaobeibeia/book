package com.tsbg.mis.serviceImpl.rookie;

import com.tsbg.mis.annotation.SysLog;
import com.tsbg.mis.dao.rookie.UserPowerDao;
import com.tsbg.mis.jurisdiction.bag.Key;
import com.tsbg.mis.jurisdiction.bag.RoleAndInfoPackage;
import com.tsbg.mis.jurisdiction.model.Role;
import com.tsbg.mis.jurisdiction.model.UserInfo;
import com.tsbg.mis.jurisdiction.vo.RoleAndInfoVo;
import com.tsbg.mis.jurisdiction.vo.UserInfoVo;
import com.tsbg.mis.jurisdiction.vo.UserRoleInfoVo;
import com.tsbg.mis.service.jurisdiction.UserRoleService;
import com.tsbg.mis.service.rookie.UserPowerService;
import com.tsbg.mis.serviceImpl.jurisdiction.base.TokenAnalysis;
import com.tsbg.mis.signed.model.ManagerNode;
import com.tsbg.mis.util.ResultUtils;
import com.tsbg.mis.util.RoleAndInfoByCreateDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

import static java.util.stream.Collectors.toList;

/**
 * 菁干班权限管理
 *
 * @author 汪永晖
 */
@Service
public class UserPowerServiceImpl implements UserPowerService {

    private final TokenAnalysis tokenAnalysis;

    private final UserPowerDao userPowerDao;

    private final UserRoleService userRoleService;

    public UserPowerServiceImpl(TokenAnalysis tokenAnalysis, UserPowerDao userPowerDao, UserRoleService userRoleService) {
        this.tokenAnalysis = tokenAnalysis;
        this.userPowerDao = userPowerDao;
        this.userRoleService = userRoleService;
    }

    /**
     * 查询菁干班系统所有用户
     *
     * @return
     */
    @Override
    //@SysLog(value = "查询菁干班系统所有用户")
    public ResultUtils inquireManager() {

        // 查找用户
        List<RoleAndInfoPackage> roleAndInfoPackageList = userPowerDao.inquireManager();
        if (roleAndInfoPackageList == null || CollectionUtils.isEmpty(roleAndInfoPackageList)) {
            return new ResultUtils(501, "暂无数据");
        }

        // 过滤用户角色 status == 0
        List<RoleAndInfoPackage> newRoleAndInfoPackageList = roleAndInfoPackageList
                .stream()
                .filter(roleAndInfoPackage -> {
                    if (roleAndInfoPackage.getStatus() != null && roleAndInfoPackage.getStatus().equals(1)) {
                        return true;
                    }
                    return false;
                })
                .collect(toList());

        // 查找用户id
        if (newRoleAndInfoPackageList == null || CollectionUtils.isEmpty(newRoleAndInfoPackageList)) {
            return new ResultUtils(501, "暂无数据");
        }
        Set<Integer> userIdSet = new HashSet<>();
        for (RoleAndInfoPackage roleAndInfoPackage : newRoleAndInfoPackageList) {
            userIdSet.add(roleAndInfoPackage.getUserId());
        }
        List<Integer> userIdList = new ArrayList<>();
        userIdList.addAll(userIdSet);

        // 构建 RoleAndInfoVo 视图对象
        List<UserRoleInfoVo> roleAndInfoVoList = buildView(userIdList, newRoleAndInfoPackageList);

        return new ResultUtils(100, "成功查詢所有用戶", roleAndInfoVoList);
    }

    /**
     * 菁干班系统新增用户——搜索用户
     * 查找 user_info 表
     *
     * @param key 模糊搜索关键字
     * @return
     */
    @Override
    @SysLog(value = "菁干班系统新增用户——搜索用户")
    public ResultUtils searchAllUser(Key key) {

        List<UserInfoVo> userInfoVoList = userPowerDao.fuzzySearchByUserNameAndUserCode(key);

        // 判断查询结果是否为空
        if (CollectionUtils.isEmpty(userInfoVoList)) {
            return new ResultUtils(100, "新增用戶的模糊搜索結果為空", userInfoVoList);
        }

        return new ResultUtils(100, "成功返回新增用戶的模糊搜索結果", userInfoVoList);
    }

    /**
     * 菁干班系统新增用户
     *
     * @param userInfoVo
     * @return
     */
    @Override
    @SysLog(value = "菁干班系统新增用户")
    public ResultUtils addManager(UserInfoVo userInfoVo) {

        if (userInfoVo == null || userInfoVo.getUserId() == null) {
            return new ResultUtils(500, "增加人資用戶失敗");
        }

        // 判断该用户是否已经是菁干班系统的用户
        if (userInfoVo.getUserId() != null && userPowerDao.selectCountByUserId(userInfoVo.getUserId()) != null && (userPowerDao.selectCountByUserId(userInfoVo.getUserId()) > 0)) {
            return new ResultUtils(501, "該用戶已經是人資系統用戶，無需重複添加！");
        }

        Integer userId = userInfoVo.getUserId();

        UserInfo currentUser = tokenAnalysis.getTokenUser();
        String createCode = currentUser.getStaffCode();
        Date createDate = new Date();
        Date lastUpdateDate = new Date();

        // 添加数据至user_role
        if (userPowerDao.insertDateToUserRole(userId, createCode, createDate, lastUpdateDate) > 0) {
            return new ResultUtils(100, "成功增加人資用戶");
        }

        return new ResultUtils(500, "增加人資用戶失敗");
    }

    /**
     * 搜索菁干班系统的用户
     *
     * @param key 模糊搜索关键字
     * @return
     */
    @Override
    public ResultUtils searchHrmManager(Key key) {

        List<RoleAndInfoPackage> roleAndInfoPackageList = userPowerDao.fuzzySearchUserRoleByUserNameAndUserRole(key);

        // 判断查询结果是否为空
        if (CollectionUtils.isEmpty(roleAndInfoPackageList)) {
            return new ResultUtils(100, "查詢人資用戶的模糊搜索結果為空", roleAndInfoPackageList);
        }

        // 过滤用户角色 status == 0
        List<RoleAndInfoPackage> newRoleAndInfoPackageList = roleAndInfoPackageList
                .stream()
                .filter(roleAndInfoPackage -> {
                    if (roleAndInfoPackage.getStatus() != null && roleAndInfoPackage.getStatus().equals(1)) {
                        return true;
                    }
                    return false;
                })
                .collect(toList());

        // 查找用户id
        if (newRoleAndInfoPackageList == null || CollectionUtils.isEmpty(newRoleAndInfoPackageList)) {
            return new ResultUtils(501, "暂无数据");
        }
        Set<Integer> userIdSet = new HashSet<>();
        for (RoleAndInfoPackage roleAndInfoPackage : newRoleAndInfoPackageList) {
            userIdSet.add(roleAndInfoPackage.getUserId());
        }
        List<Integer> userIdList = new ArrayList<>();
        userIdList.addAll(userIdSet);

        // 构建 RoleAndInfoVo 视图对象
        List<UserRoleInfoVo> roleAndInfoVoList = buildView(userIdList, newRoleAndInfoPackageList);

        return new ResultUtils(100, "成功返回查詢人資用戶的模糊搜索結果", roleAndInfoVoList);
    }

    /**
     * 查询菁干班系统所有角色信息
     *
     * @return
     */
    @Override
    public ResultUtils inquireRoleList() {

        List<Role> roleList2 = userPowerDao.inquireRole();

        if (roleList2 == null || CollectionUtils.isEmpty(roleList2)) {
            return new ResultUtils(500, "查詢角色信息的結果為空");
        }

        // 过滤用户角色
        List<Role> roleList1 = roleList2.stream()
                .filter(rolePackage -> 9 != rolePackage.getRoleId())
                .collect(toList());

        if (roleList1 == null || CollectionUtils.isEmpty(roleList1)) {
            return new ResultUtils(500, "查詢角色信息的結果為空");
        }
        UserInfo currentUser = tokenAnalysis.getTokenUser();
        Integer currentUserId = currentUser.getUserId();
        List<Integer> role = userRoleService.getRole(currentUserId);

        List<Role> roleList = roleList1;
        if (role.contains(18)) {
            // 过滤用户的权限 ( roleId == 22 和 21 )
            roleList = roleList1
                    .stream()
                    .filter(rolePackage -> 18 != rolePackage.getRoleId() && 19 != rolePackage.getRoleId())
                    .collect(toList());
        }

        if (role.contains(19)) {
            // 过滤用户的权限 ( roleId == 22 )
            roleList = roleList1
                    .stream()
                    .filter(rolePackage -> 19 != rolePackage.getRoleId())
                    .collect(toList());
        }

        return new ResultUtils(100, "成功返回查詢人資用戶的角色信息的結果", roleList);
    }

    /**
     * 修改菁干班用户角色信息
     *
     * @param roleAndInfoVo
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @SysLog(value = "修改菁干班用户角色信息")
    public ResultUtils modifyRoleByUserId(RoleAndInfoVo roleAndInfoVo) {

        if (roleAndInfoVo == null) {
            return new ResultUtils(500, "修改失敗");
        }

        if (!judgeBasicRole(roleAndInfoVo.getUserId())) {
            return new ResultUtils(500, "修改失敗，該用戶已經處於停用狀態");
        }

        UserInfo currentUser = tokenAnalysis.getTokenUser();
        String createCode = currentUser.getStaffCode();
        Integer currentUserId = currentUser.getUserId();
        List<Integer> role = userRoleService.getRole(currentUserId);

        if (roleAndInfoVo.getRoleAndInfoPackageList() == null || roleAndInfoVo.getUserId() == null) {
            return new ResultUtils(500, "修改失敗1");
        }

        List<RoleAndInfoPackage> roleAndInfoPackageList = roleAndInfoVo.getRoleAndInfoPackageList();
        Integer userId = roleAndInfoVo.getUserId();
        if (roleAndInfoPackageList == null) {
            return new ResultUtils(500, "修改失敗2");
        }

        // 过滤用户的基本权限 ( roleId == 9 ) 和 验证角色的 status 参数
        List<RoleAndInfoPackage> newRoleAndInfoPackageList1 = roleAndInfoPackageList
                .stream()
                .filter(roleAndInfoPackage -> {
                    if (null == roleAndInfoPackage.getRoleId() || null == roleAndInfoPackage.getStatus()) {
                        return false;
                    }
                    if (9 != roleAndInfoPackage.getRoleId() && 1 == roleAndInfoPackage.getStatus()) {
                        return true;
                    }
                    return false;
                })
                .collect(toList());

        if (newRoleAndInfoPackageList1 == null) {
            return new ResultUtils(500, "修改失敗3");
        }
        List<RoleAndInfoPackage> newRoleAndInfoPackageList = newRoleAndInfoPackageList1;
        if (role.contains(18)) {
            // 过滤用户的权限 ( roleId == 18 和 19 )
            newRoleAndInfoPackageList = newRoleAndInfoPackageList1
                    .stream()
                    .filter(roleAndInfoPackage -> {
                        if (null == roleAndInfoPackage.getRoleId() || null == roleAndInfoPackage.getRoleId()) {
                            return false;
                        }
                        if (18 != roleAndInfoPackage.getRoleId() && 19 != roleAndInfoPackage.getRoleId()) {
                            return true;
                        }
                        return false;
                    })
                    .collect(toList());
        }

        if (role.contains(19)) {
            // 过滤用户的权限 ( roleId == 19 )
            newRoleAndInfoPackageList = newRoleAndInfoPackageList1
                    .stream()
                    .filter(roleAndInfoPackage -> {
                        if (null == roleAndInfoPackage.getRoleId()) {
                            return false;
                        }
                        if (19 != roleAndInfoPackage.getRoleId()) {
                            return true;
                        }
                        return false;
                    })
                    .collect(toList());
        }

        // 获取该用户原来的角色信息(包含 status=0 )
        List<RoleAndInfoPackage> oldRoleAndInfoPackageListStatus = userPowerDao.inquireRoleByUserId(userId);

        // 获取该用户原来的角色信息(不包含 status=0 )
        if (oldRoleAndInfoPackageListStatus == null) {
            return new ResultUtils(500, "修改失敗4");
        }
        List<RoleAndInfoPackage> oldRoleAndInfoPackageList1 = oldRoleAndInfoPackageListStatus
                .stream()
                .filter(roleAndInfoPackage -> {
                    if (null == roleAndInfoPackage.getRoleId() || null == roleAndInfoPackage.getStatus()) {
                        return false;
                    }
                    if (9 != roleAndInfoPackage.getRoleId() && 1 == roleAndInfoPackage.getStatus()) {
                        return true;
                    }
                    return false;
                })
                .collect(toList());
        if (oldRoleAndInfoPackageList1 == null) {
            return new ResultUtils(500, "修改失敗5");
        }
        List<RoleAndInfoPackage> oldRoleAndInfoPackageList = oldRoleAndInfoPackageList1;
        if (role.contains(18)) {
            // 过滤用户的权限 ( roleId == 18 和 19 )
            oldRoleAndInfoPackageList = oldRoleAndInfoPackageList1
                    .stream()
                    .filter(roleAndInfoPackage -> {
                        if (null == roleAndInfoPackage.getRoleId() || null == roleAndInfoPackage.getRoleId()) {
                            return false;
                        }
                        if (18 != roleAndInfoPackage.getRoleId() && 19 != roleAndInfoPackage.getRoleId()) {
                            return true;
                        }
                        return false;
                    })
                    .collect(toList());
        }

        if (role.contains(19)) {
            // 过滤用户的权限 ( roleId == 19 )
            oldRoleAndInfoPackageList = oldRoleAndInfoPackageList1
                    .stream()
                    .filter(roleAndInfoPackage -> {
                        if (null == roleAndInfoPackage.getRoleId()) {
                            return false;
                        }
                        if (19 != roleAndInfoPackage.getRoleId()) {
                            return true;
                        }
                        return false;
                    })
                    .collect(toList());
        }

        if (newRoleAndInfoPackageList == null || oldRoleAndInfoPackageList == null) {
            return new ResultUtils(500, "修改失敗6");
        }

        ArrayList<RoleAndInfoPackage> exists = new ArrayList<>(newRoleAndInfoPackageList);
        ArrayList<RoleAndInfoPackage> notexists = new ArrayList<>(newRoleAndInfoPackageList);
        ArrayList<RoleAndInfoPackage> oldNotexists = new ArrayList<>(oldRoleAndInfoPackageList);

        // new 不存在 old 中的 (需要新增的角色)
        exists.removeAll(oldRoleAndInfoPackageList);
        // new 存在于 old 中的 （不需要变化的角色）
        notexists.removeAll(exists);
        // old 不存在 new 中的 （需要删除的角色）
        oldNotexists.removeAll(notexists);

        if (CollectionUtils.isEmpty(exists) && CollectionUtils.isEmpty(oldNotexists)) {
            return new ResultUtils(502, "角色信息沒有更改，更新失敗");
        }

        // 批量删除角色
        if (!CollectionUtils.isEmpty(oldNotexists)) {
            // 取出所有的 uroleId
            List<Integer> deleteList = oldNotexists.stream().map(RoleAndInfoPackage::getUroleId).collect(toList());
            Integer delete = userPowerDao.batchDeletion(deleteList);
        }

        if (!CollectionUtils.isEmpty(exists)) {
            // 需要更新 status = 1
            ArrayList<RoleAndInfoPackage> exists2 = new ArrayList<>(exists);
            ArrayList<RoleAndInfoPackage> notexists2 = new ArrayList<>(exists);
            ArrayList<RoleAndInfoPackage> oldNotexists2 = new ArrayList<>(oldRoleAndInfoPackageListStatus);

            // new 不存在 old 中的 (需要新增的角色)
            exists2.removeAll(oldRoleAndInfoPackageListStatus);
            // new 存在于 old 中的 （需要更新的角色）
            notexists2.removeAll(exists2);
            // old 不存在 new 中的 （不需要变化的角色）
            oldNotexists2.removeAll(notexists2);

            // 为新增角色赋值
            if (!CollectionUtils.isEmpty(exists2)) {
                Date date = new Date();
                for (RoleAndInfoPackage roleAndInfoPackage : exists2) {
                    roleAndInfoPackage.setLastUpdateDate(date);
                    roleAndInfoPackage.setCreateCode(createCode);
                    roleAndInfoPackage.setCreateDate(date);
                }
                // 批量新增角色
                Integer add = userPowerDao.batchAdd(exists2);
            }

            // 更新角色
            if (!CollectionUtils.isEmpty(notexists2)) {
                Date date = new Date();
                for (RoleAndInfoPackage roleAndInfoPackage : notexists2) {
                    roleAndInfoPackage.setLastUpdateDate(date);
                    roleAndInfoPackage.setLastUpdateCode(createCode);
                }
                // 批量新增角色
                Integer update = userPowerDao.batchUpdate(notexists2);
            }


        }

//        updateManagerNodeByRole(userId);

        return new ResultUtils(101, "用戶角色信息更新成功");
    }

    /**
     * 启用菁干班系统用户
     *
     * @param userId
     * @return
     */
    @Override
    @SysLog(value = "启用菁干班系统用户")
    public ResultUtils enableUser(Integer userId) {
        if (judgeBasicRole(userId)) {
            return new ResultUtils(500, "啟用失敗，該用戶已經處於啟用狀態");
        }

        // 把该用户的所有角色的 isLock 变更为1
        Integer count = userPowerDao.updateUserAllRole(userId);

        if (count == null) {
            return new ResultUtils(500, "啟用用戶失敗");
        }

        return count > 0 ? new ResultUtils(100, "啟用用戶成功")
                : new ResultUtils(500, "啟用用戶失敗");
    }

    /**
     * 停用菁干班系统用户
     *
     * @param userId
     * @return
     */
    @Override
    @SysLog(value = "停用菁干班系统用户")
    public ResultUtils disableUser(Integer userId) {
        if (!judgeBasicRole(userId)) {
            return new ResultUtils(500, "停用失敗，該用戶已經處於停用狀態");
        }

        Integer count = userPowerDao.updateUserAllRoleDisable(userId);

        if (count == null) {
            return new ResultUtils(500, "啟用用戶失敗");
        }

        return count > 0 ? new ResultUtils(100, "停用用戶成功")
                : new ResultUtils(500, "停用用戶失敗");
    }


    /**
     * 判断用户的基本角色是否被停用
     *
     * @param userId
     * @return true -> 用户是启用状态
     */
    public boolean judgeBasicRole(Integer userId) {
        if (userPowerDao.selectBasicRoleByUserId(userId) == null) {
            return false;
        }
        return userPowerDao.selectBasicRoleByUserId(userId) > 0;
    }

    /**
     * 判断 key 是姓名还是工号
     *
     * @param key 模糊搜索关键字
     * @return 1 -> 工号   -1 -> 姓名  0 -> 空  100 -> 全部都有
     */
    public Integer judgeKey(Key key) {

        if ("".equals(key.getUserName())) {
            return 1;
        } else if ("".equals(key.getStaffCode())) {
            return -1;
        } else if ("".equals(key.getStaffCode()) && "".equals(key.getUserName())) {
            return 0;
        }

        return 100;
    }

    /**
     * 构建 RoleAndInfoVo 视图对象
     *
     * @param userIdList             用户 id 列表
     * @param roleAndInfoPackageList 根据 user_role 表中查出来的数据
     * @return List<UserRoleInfoVo>
     */
    public List<UserRoleInfoVo> buildView(List<Integer> userIdList, List<RoleAndInfoPackage> roleAndInfoPackageList) {

        // 构建 UserRoleInfoVo 视图对象
        List<UserRoleInfoVo> userRoleInfoVos = new ArrayList<>();

        for (int i = 0; i < userIdList.size(); i++) {

            UserRoleInfoVo userRoleInfoVo = new UserRoleInfoVo();
            userRoleInfoVo.setUserId(userIdList.get(i));

            List<Role> roleList = new ArrayList<>();

            for (RoleAndInfoPackage roleAndInfoPackage : roleAndInfoPackageList) {

                if (userIdList.get(i).equals(roleAndInfoPackage.getUserId())) {
                    userRoleInfoVo.setEmailAddress(roleAndInfoPackage.getEmailAddress());
                    userRoleInfoVo.setIsLock(roleAndInfoPackage.getIsLock());
                    userRoleInfoVo.setStaffCode(roleAndInfoPackage.getStaffCode());
                    userRoleInfoVo.setUserName(roleAndInfoPackage.getUserName());
                    userRoleInfoVo.setUnitName(roleAndInfoPackage.getUnitName());
                    userRoleInfoVo.setFactoryName(roleAndInfoPackage.getFactoryName());
                    userRoleInfoVo.setDepartName(roleAndInfoPackage.getDepartName());
                    userRoleInfoVo.setOrganizationName(roleAndInfoPackage.getOrganizationName());
                    userRoleInfoVo.setCreateDate(roleAndInfoPackage.getCreateDate());

                    Role role = new Role();
                    role.setRoleId(roleAndInfoPackage.getRoleId());
                    role.setRoleName(roleAndInfoPackage.getRoleName());
                    role.setRoleDescribe(roleAndInfoPackage.getDescription());
                    role.setStatus(roleAndInfoPackage.getStatus());
                    roleList.add(role);
                }
            }
            userRoleInfoVo.setRoles(roleList);
            userRoleInfoVos.add(userRoleInfoVo);
        }

        Comparator<UserRoleInfoVo> userRoleInfoVoComparator = new RoleAndInfoByCreateDate();

        Collections.sort(userRoleInfoVos, userRoleInfoVoComparator);


        UserInfo currentUser = tokenAnalysis.getTokenUser();
        Integer currentUserId = currentUser.getUserId();
        List<Integer> role = userRoleService.getRole(currentUserId);

        List<UserRoleInfoVo> userRoleInfoVoList = userRoleInfoVos;
        if (role.contains(18)) {
            // 过滤用户的权限 ( roleId == 18 和 19 )
            userRoleInfoVoList = userRoleInfoVos
                    .stream()
                    .filter(roleAndInfoPackage -> {
                        List<Role> roleList = roleAndInfoPackage.getRoles();
                        for (Role role1 : roleList) {
                            if (18 == role1.getRoleId()) {
                                return false;
                            }
                            if (19 == role1.getRoleId()) {
                                return false;
                            }
                        }
                        return true;
                    })
                    .collect(toList());
        }

        if (role.contains(19)) {
            // 过滤用户的权限 ( roleId == 19 )
            userRoleInfoVoList = userRoleInfoVos
                    .stream()
                    .filter(roleAndInfoPackage -> {
                        List<Role> roleList = roleAndInfoPackage.getRoles();
                        for (Role role1 : roleList) {
                            if (19 == role1.getRoleId()) {
                                return false;
                            }
                        }
                        return true;
                    })
                    .collect(toList());
        }

        return userRoleInfoVoList;
    }
}

