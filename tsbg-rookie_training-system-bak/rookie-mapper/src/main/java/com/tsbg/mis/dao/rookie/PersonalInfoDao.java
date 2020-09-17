package com.tsbg.mis.dao.rookie;

import com.tsbg.mis.rookie.bag.UserPersonalInfoPackage;
import com.tsbg.mis.rookie.model.ClassStudentInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface PersonalInfoDao {

    //查询我的组织信息
   List<ClassStudentInfo> queryOrganizationInfo(String staffCode);

   //修改个人信息
   Integer UpdateUserPersonalInfo(UserPersonalInfoPackage userPersonalInfoPackage);

   //修改FileInfo表中的信息
   Integer UpdateFileInfo(UserPersonalInfoPackage userPersonalInfoPackage);

   List<UserPersonalInfoPackage> queryUserPersonalInfo(String connectFieldsValue);

   Integer insertUserPersonalInfo(UserPersonalInfoPackage userPersonalInfoPackage);

   UserPersonalInfoPackage queryPersonalPortraitPath(UserPersonalInfoPackage userPersonalInfoPackage);

   //根据工号查询用户个人信息
   UserPersonalInfoPackage selectStaffInfoByCode(String staffCode);

   //修改个人信息修改stafInfo表
   Integer UpdatePersonalInfo(UserPersonalInfoPackage userPersonalInfoPackage);
}
