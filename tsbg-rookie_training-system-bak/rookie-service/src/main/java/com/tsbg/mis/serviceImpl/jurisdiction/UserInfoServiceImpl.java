package com.tsbg.mis.serviceImpl.jurisdiction;

import com.tsbg.mis.jurisdiction.model.UserInfo;
import com.tsbg.mis.dao.jurisdiction.UserInfoDao;
import com.tsbg.mis.service.jurisdiction.UserInfoService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoDao userInfoDao;

    @Override
    public Integer selectuidbyuserCode(String accountName) {
        return userInfoDao.selectuidbyuserCode(accountName);
    }

    @Override
    public Integer selectCountByUserCode(String accountName) {
        return userInfoDao.selectCountByUserCode(accountName);
    }

    @Override
    public Integer selectStatusByUserCode(String accountName) {
        return userInfoDao.selectStatusByUserCode(accountName);
    }

    @Override
    public String selectSaltByUserCode(String accountName) {
        return userInfoDao.selectSaltByUserCode(accountName);
    }

    @Override
    public int judgeIfExistUserByUserPwd(String accountName, String userPwd) {
        return userInfoDao.judgeIfExistUserByUserPwd(accountName, userPwd);
    }

    @Override
    public int resetUserSalt(String salt, String accountName) {
        return userInfoDao.resetUserSalt(salt, accountName);
    }

    @Override
    public int modifyPasswordByUsername(String userPwd, String accountName) {
        return userInfoDao.modifyPasswordByUsername(userPwd, accountName);
    }

    @Override
    public UserInfo selectByUserCode(String accountName) {
        return userInfoDao.selectByUserCode(accountName);
    }

    @Override
    public String selectUserNameByUserCode(String accountName) {
        return userInfoDao.selectUserNameByUserCode(accountName);
    }

    @Override
    public int selectIfExistThisUser(Integer userId) {
        return userInfoDao.selectIfExistThisUser(userId);
    }

    @Override
    public String selectPermListByUseId(Integer userId) {
        return userInfoDao.selectPermListByUseId(userId);
    }

    @Override
    public String selectPermListByUseId2(Integer userId) {
        return userInfoDao.selectPermListByUseId2(userId);
    }

    @Override
    public UserInfo selectUserMsgbyUserId(String userId) {
        return userInfoDao.selectUserMsgbyUserId(userId);
    }

    @Override
    public int selectisExistUserCodeByStaffCode(String userCode) {
        return userInfoDao.selectisExistUserCodeByStaffCode(userCode);
    }

    @Override
    public int selectisExistByAccountName(String accountName) {
        return userInfoDao.selectisExistByAccountName(accountName);
    }

    @Override
    public int insertSelective(UserInfo record) {
        return userInfoDao.insertSelective(record);
    }

    @Override
    public int modifyPassword(String salt, String userPwd, String accountName) {
        return userInfoDao.modifyPassword(salt, userPwd, accountName);
    }

}
