package com.tsbg.mis.dao.jurisdiction;

import com.tsbg.mis.jurisdiction.bag.RoleAndProJPackage;
import com.tsbg.mis.jurisdiction.model.Role;
import com.tsbg.mis.jurisdiction.model.UserInfo;
import com.tsbg.mis.jurisdiction.model.UserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Mapper
@Component
public interface UserRoleDao {
    int deleteByPrimaryKey(Integer uroleId);

    int insert(UserRole record);

    int insertSelective(UserRole record);

    UserRole selectByPrimaryKey(Integer uroleId);

    int updateByPrimaryKeySelective(UserRole record);

    int updateByPrimaryKey(UserRole record);

    //插入数据到user_role
    int insertData(@Param("uid") Integer userId, @Param("rid") Integer roleId, @Param("creatorId") Integer creatorId, @Param("createDate") Date date, @Param("projId") Integer projId);

    //查询当前登录用户所拥有的项目情况
    List<RoleAndProJPackage> selectProJMsgByUid(Integer uid);

    //根据用户编号、角色编号和项目编号查找条数
    int selectRoleCountByCondition(@Param("uid") Integer uid, @Param("roleId") Integer roleId, @Param("projId") Integer projId);

    //根据用户编号查询项目编号
    List<Integer> selectProJIdByUserId(Integer uid);

    //根据用户的userId查询出其所拥有的角色插入到字段role_list
    List<Integer> getRole(Integer uid);

    //根据用户id查询用户角色id
    List<Integer> selectRoleId(@Param("userId") Integer userId);

    /**
     * 根据工号查询手机号
     *
     * @param staffCode
     * @return
     */
    String selectPhoneNumberByStaffCode(String staffCode);

    /**
     * 根据角色查询所有用户
     *
     * @param roleId
     * @return
     */
    List<String> selectStaffCodeByRoleId(Integer roleId);

    /**
     * 查询员工姓名
     *
     * @param approveStaffCode
     * @return
     */
    String selectStaffNameByStaffCode(String approveStaffCode);

    /**
     * 根据 roleIds 查询角色详细信息
     *
     * @param roleIds
     * @return
     */
    List<Role> queryRoleInfoWithRoleIds(List<Integer> roleIds);

    /**
     * 根据角色查询所有用户
     *
     * @param roleId
     * @return
     */
    List<UserInfo> selectReviewerByRoleId(Integer roleId);
}
