package com.tsbg.mis.service.rookie;

import com.tsbg.mis.rookie.bag.UserPersonalInfoPackage;
import com.tsbg.mis.util.ResultUtils;

public interface PersonalInfoService {

    //查询我的组织信息
    ResultUtils queryOrganizationInfo(String staffCode);
    //修改个人信息
    ResultUtils UpdateUserPersonalInfo(UserPersonalInfoPackage userPersonalInfoPackage);

    //根据工号查询用户个人信息
    ResultUtils selectStaffInfoByCode();
}
