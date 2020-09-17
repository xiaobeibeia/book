package com.tsbg.mis.dao.rookie;

import com.tsbg.mis.jurisdiction.bag.Key;
import com.tsbg.mis.jurisdiction.bag.RoleAndInfoPackage;
import com.tsbg.mis.jurisdiction.model.Role;
import com.tsbg.mis.jurisdiction.vo.UserInfoVo;
import com.tsbg.mis.signed.model.ManagerNode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 人资权限管理
 *
 * @author 汪永晖
 */
@Mapper
public interface UserPowerDao {

    /**
     * 查询人资系统所有用户
     *
     * @return
     */
    List<RoleAndInfoPackage> inquireManager();

    /**
     * 人资系统新增用户——搜索用户
     * 查找 user_info 表
     *
     * @param key 模糊搜索关键字——工号
     * @return
     */
    List<UserInfoVo> fuzzySearchByUserCode(String key);

    /**
     * 人资系统新增用户——搜索用户
     * 查找 user_info 表
     *
     * @param key 模糊搜索关键字——姓名
     * @return
     */
    List<UserInfoVo> fuzzySearchByUserName(String key);

    /**
     * 搜索人资系统的用户
     *
     * @param key 模糊搜索关键字——工号
     * @return
     */
    List<RoleAndInfoPackage> fuzzySearchUserRoleByUserCode(String key);

    /**
     * 搜索人资系统的用户
     *
     * @param key 模糊搜索关键字——姓名
     * @return
     */
    List<RoleAndInfoPackage> fuzzySearchUserRoleByUserName(String key);

    /**
     * 判断该用户是否已经是人资系统的用户
     *
     * @return
     */
    Integer selectCountByUserId(Integer userId);

    /**
     * 添加数据至user_role
     *
     * @param userId
     * @param createCode
     * @param createDate
     * @return
     */
    int insertDateToUserRole(@Param("userId") Integer userId,
                             @Param("createCode") String createCode,
                             @Param("createDate") Date createDate,
                             @Param("lastUpdateDate") Date lastUpdateDate);

    /**
     * 查询人资系统所有角色信息
     *
     * @return
     */
    List<Role> inquireRole();

    /**
     * 根据 userId 查询用户的角色信息
     *
     * @param userId
     * @return
     */
    List<RoleAndInfoPackage> inquireRoleByUserId(Integer userId);

    /**
     * 批量删除角色
     *
     * @param deleteList
     * @return
     */
    Integer batchDeletion(List<Integer> deleteList);

    /**
     * @param exists
     * @return
     */
    Integer batchAdd(ArrayList<RoleAndInfoPackage> exists);

    /**
     * 判断用户的基本角色是否被停用
     *
     * @param userId
     * @return
     */
    Integer selectBasicRoleByUserId(Integer userId);

    /**
     * 启用用户
     *
     * @param userId
     * @return
     */
    Integer updateUserAllRole(Integer userId);

    /**
     * 停用用户
     *
     * @param userId
     * @return
     */
    Integer updateUserAllRoleDisable(Integer userId);

    /**
     * 根据 userId 查询用户所有的角色信息
     *
     * @param userId
     * @return
     */
    List<RoleAndInfoPackage> inquireRoleByUser(Integer userId);

    /**
     * 批量修改用户角色
     *
     * @param notexists2
     * @return
     */
    Integer batchUpdate(ArrayList<RoleAndInfoPackage> notexists2);

    /**
     * 新增用户——模糊搜索查所有
     *
     * @return
     */
    List<UserInfoVo> selectAllUserInfo();

    /**
     * 搜索人资系统用户——模糊搜索查所有
     *
     * @return
     */
    List<RoleAndInfoPackage> searchAllUserAndRole();

    /**
     * 人资系统新增用户——搜索用户
     *
     * @param key
     * @return
     */
    List<UserInfoVo> fuzzySearchByUserNameAndUserCode(Key key);

    /**
     * 搜索人资系统的用户
     *
     * @param key
     * @return
     */
    List<RoleAndInfoPackage> fuzzySearchUserRoleByUserNameAndUserRole(Key key);

    /**
     * 判断该用户 roleid = 17 or 21 and status = 1
     *
     * @param userId
     * @return
     */
    List<RoleAndInfoPackage> inquireUserRoleByUserId(Integer userId);

    /**
     * 查询该用户在 manage_node 表中的状态
     *
     * @param userCode
     * @return
     */
    ManagerNode inquireManagerNodeByUserCode(String userCode);

    /**
     * 查出该用户的详细信息
     *
     * @param userId
     * @return
     */
    List<RoleAndInfoPackage> selectUserByUserId(Integer userId);

    /**
     * 新增 manager_node
     *
     * @param managerCode
     * @param managerName
     * @param managerMailAddress
     * @return
     */
    int insertManagerNode(@Param("managerCode") String managerCode,
                          @Param("managerName") String managerName,
                          @Param("managerMailAddress") String managerMailAddress);

    /**
     * 设置 status = 1
     *
     * @return
     */
    Integer updateManagerNode(String managerCode);

    /**
     * 设置 status = 0
     *
     * @return
     */
    Integer updateManagerNodeStatus(String managerCode);
}
