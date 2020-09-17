package com.tsbg.mis.service.jurisdiction;

import com.tsbg.mis.jurisdiction.model.UserInfo;

public interface UserInfoService {

    //根据用户工号查询对应的uid
    Integer selectuidbyuserCode(String accountName);

    //根据工号查询是否存在该用户
    Integer selectCountByUserCode(String accountName);

    //通过工号查询用户状态
    Integer selectStatusByUserCode(String accountName);

    //根据工号查询密码盐
    String selectSaltByUserCode(String accountName);

    //根据工号来查询此用户用户名
    String selectUserNameByUserCode(String accountName);

    //通过工号和密码来判断是否存在此用户
    int judgeIfExistUserByUserPwd(String accountName, String userPwd);

    //重置用户密码盐
    int resetUserSalt(String salt, String accountName);

    //修改密码
    int modifyPasswordByUsername(String userPwd, String accountName);

    UserInfo selectByUserCode(String userCode);

    //根据userId查询是否存在该用户
    int selectIfExistThisUser(Integer userId);

    //根据用户编号查询生态权限列表
    String selectPermListByUseId(Integer userId);

    //根据用户编号查询固资权限列表
    String selectPermListByUseId2(Integer userId);

    //根据用户编号查询用户个人信息
    UserInfo selectUserMsgbyUserId(String userId);

    //员工注册之前需要去用户表判断该账号是否已经注册
    int selectisExistUserCodeByStaffCode(String userCode);

    //非集团员工注册时判断账号是否是集团已有员工工号
    int selectisExistByAccountName(String accountName);

    //註冊
    int insertSelective(UserInfo record);

    int modifyPassword(String newSalt, String newPwd, String staffCode);
}
