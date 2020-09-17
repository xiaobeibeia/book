package com.tsbg.mis.dao.jurisdiction;

import com.tsbg.mis.jurisdiction.model.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Mapper
@Component
@Repository
public interface UserInfoDao {

    int deleteByPrimaryKey(Integer userId);

    int insert(UserInfo record);

    int insertSelective(UserInfo record);

    UserInfo selectByPrimaryKey(Integer userId);

    int updateByPrimaryKeySelective(UserInfo record);

    int updateByPrimaryKey(UserInfo record);

    //员工注册之前需要去用户表判断该账号是否已经注册
    int selectisExistUserCodeByStaffCode(String accountName);

    //非集团员工注册时判断账号是否是集团已有员工工号
    int selectisExistByAccountName(String accountName);

    //根据用户工号查询对应的uid
    Integer selectuidbyuserCode(String accountName);

    //根据user_id修改用户的perm_list
    int modifyPermListByuserId(@Param("permlist") String permlist, @Param("uid") Integer userId);

    //根据工号查询是否存在该用户
    Integer selectCountByUserCode(String accountName);

    //通过工号查询用户状态
    Integer selectStatusByUserCode(String accountName);

    //根据工号查询密码盐
    String selectSaltByUserCode(String accountName);

    //根据工号来查询此用户用户名
    String selectUserNameByUserCode(String accountName);

     //管理员停用和启用用户
    int setEcoUserByUserCode(@Param("status") int status, @Param("accountName") String accountName);

    //根据工号查询用户个人信息
    UserInfo selectUserMsgbyUserCode(String accountName);

    //根据工号修改个人信息
    int updateByUserCodeSelective(UserInfo userInfo);

    //修改密码
    int modifyPasswordByUsername(@Param("userPwd") String userPwd, @Param("accountName") String accountName);

    UserInfo selectByUserCode(@Param("accountName") String accountName);

    //通过工号和密码来判断是否存在此用户
    int judgeIfExistUserByUserPwd(@Param("accountName") String accountName, @Param("userPwd") String userPwd);

    //管理员重置用户密码
    int reSetPwdByUserCode(@Param("userPwd") String userPwd, @Param("accountName") String accountName);

    //根据工号查询当前用户身份是否为管理员
    int selectIdentityByUserCode(String accountName);

    //重置用户密码盐
    int resetUserSalt(@Param("salt") String salt, @Param("accountName") String accountName);

    //根据工号查询权限列表
    String selectPowerByUserCode(String accountName);

    //根據工號查詢郵箱，用於忘記密碼，驗證
    String selectEmailByUserCode(String accountName);

    //根据userId查询是否存在该用户
    int selectIfExistThisUser(Integer userId);

    //問題反饋根據工號查詢反饋者的相關信息
    UserInfo selectFeedbackUserByUserCode(String accountName);

    //處理問題反饋根據工號查詢處理者的相關信息
    UserInfo selectHandleUserByUserCode(String accountName);

    //問題反饋根據工號修改反饋者的相關信息
    int updateFeedbackUserByUserCode(UserInfo userInfo);

    //處理問題反饋根據工號修改處理者的相關信息
    int updateHandleUserByUserCode(UserInfo userInfo);

    //根据用户编号查询生态权限列表
    String selectPermListByUseId(Integer userId);

    //根据用户编号查询固资权限列表
    String selectPermListByUseId2(Integer userId);

    //根据用户编号查询用户个人信息
    UserInfo selectUserMsgbyUserId(String userId);

    UserInfo selectCreatorIdAndName(String accountName);//根据后台获取的工号查询userId和userName

    // 修改密码和密码盐
    int modifyPassword(@Param("salt") String salt, @Param("userPwd") String userPwd, @Param("accountName") String accountName);
}
