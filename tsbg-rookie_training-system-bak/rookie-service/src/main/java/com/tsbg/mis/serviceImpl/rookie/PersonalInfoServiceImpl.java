package com.tsbg.mis.serviceImpl.rookie;

import com.tsbg.mis.annotation.SysLog;
import com.tsbg.mis.dao.rookie.PersonalInfoDao;
import com.tsbg.mis.jurisdiction.model.UserInfo;
import com.tsbg.mis.rookie.bag.UserPersonalInfoPackage;
import com.tsbg.mis.rookie.model.ClassStudentInfo;
import com.tsbg.mis.service.rookie.PersonalInfoService;
import com.tsbg.mis.serviceImpl.jurisdiction.base.TokenAnalysis;
import com.tsbg.mis.util.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class PersonalInfoServiceImpl implements PersonalInfoService {

    @Autowired
    PersonalInfoDao personalInfoDao;

    @Autowired
    TokenAnalysis tokenAnalysis;

    @Override
    public ResultUtils queryOrganizationInfo(String staffCode) {
        //根据学生工号查询学生的组织信息
        List<ClassStudentInfo> queryOrganizationInfos = personalInfoDao.queryOrganizationInfo(staffCode);
        if (null != queryOrganizationInfos && !queryOrganizationInfos.isEmpty()) {
            return new ResultUtils(100, "查询成功", queryOrganizationInfos);
        } else if (null != queryOrganizationInfos && queryOrganizationInfos.isEmpty()) {
            return new ResultUtils(100, "查询为空", queryOrganizationInfos);
        } else {
            return new ResultUtils(500, "查询失败");
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @SysLog(value = "个人中心、修改个人信息")
    public ResultUtils UpdateUserPersonalInfo(UserPersonalInfoPackage userPersonalInfoPackage) {
        //获取当前登录用户工号
        String userStaffCode = tokenAnalysis.getTokenUser().getStaffCode();
        userPersonalInfoPackage.setStaffCode(userStaffCode);
        //根据传进来的值进行修改先修改userInfo表
        Integer sum;
//        Integer sum1;
        Integer sum2;
        if (userPersonalInfoPackage.getPhoneNumber() != null && userPersonalInfoPackage.getEmailAddress() != null) {
            //第一步先修改userInfo表中的信息
            sum = personalInfoDao.UpdateUserPersonalInfo(userPersonalInfoPackage);
            if(null==sum || sum<=0){
                return new ResultUtils(500,"修改失敗");
            }
           /* //第二步先修改StaffInfo表中的信息
            userPersonalInfoPackage.setEmail(userPersonalInfoPackage.getEmailAddress());
            userPersonalInfoPackage.setPhoneNum(userPersonalInfoPackage.getPhoneNumber());
            sum1 =personalInfoDao.UpdatePersonalInfo(userPersonalInfoPackage);
            if(null==sum1 || sum1<=0){
                return new ResultUtils(500,"修改失敗");
            }*/
            List<UserPersonalInfoPackage> userPersonalInfoPackages = personalInfoDao.queryUserPersonalInfo(userStaffCode);
            userPersonalInfoPackage.setProjId(10);
            userPersonalInfoPackage.setConnectTableName("user_info");
            userPersonalInfoPackage.setConnectFieldsName("user_img");
            userPersonalInfoPackage.setConnectFieldsValue(userStaffCode);
            //如果查询到用户已有头像就进行修改
            boolean flag = false;
            if (userPersonalInfoPackages!=null && userPersonalInfoPackages.size()!=0) {
                sum2 = personalInfoDao.UpdateFileInfo(userPersonalInfoPackage);
                if(null==sum2 || sum2<=0){
                    return new ResultUtils(500,"修改失敗");
                }
            }else{
                   if((userPersonalInfoPackage.getFileName()==null || userPersonalInfoPackage.getFileName().equals("")) || (userPersonalInfoPackage.getFilePath()==null || userPersonalInfoPackage.getFilePath().equals(""))){
                        flag=true;
                   }else {
                       //如果查询到用户没有头像则进行新增
                       sum2 = personalInfoDao.insertUserPersonalInfo(userPersonalInfoPackage);
                       if(null==sum2 || sum2<=0){
                           return new ResultUtils(500,"修改失敗");
                       }}
                   }
        }
        return new ResultUtils(101, "修改成功");
    }


    @Override
    public ResultUtils selectStaffInfoByCode() {
        //获取当前登录用户工号
//        String staffCode = tokenAnalysis.getTokenUser().getStaffCode();
        UserInfo userInfo = tokenAnalysis.getTokenUser();
        //根据用户工号查询用户邮箱、电话、组织信息
        UserPersonalInfoPackage userPersonalInfoPackages = personalInfoDao.selectStaffInfoByCode(userInfo.getStaffCode());
        if(userPersonalInfoPackages==null){
            return new ResultUtils(500, "暫無數據");
        }
        UserPersonalInfoPackage userPersonalInfoPackage1 = new UserPersonalInfoPackage();
        userPersonalInfoPackage1.setConnectTableName("user_info");
        userPersonalInfoPackage1.setConnectFieldsName("user_img");
        userPersonalInfoPackage1.setConnectFieldsValue(userInfo.getStaffCode());
        //根据员工工号、对应表的名称和字段名去查询FileInfo表中用户上传头像的Path和ViewPath
        UserPersonalInfoPackage userPersonalInfoPackage = personalInfoDao.queryPersonalPortraitPath(userPersonalInfoPackage1);
        if(userPersonalInfoPackage==null){
            userPersonalInfoPackage = new UserPersonalInfoPackage();
        }
        userPersonalInfoPackages.setFileId(userPersonalInfoPackage.getFileId());
        userPersonalInfoPackages.setFilePath(userPersonalInfoPackage.getFilePath());
        userPersonalInfoPackages.setViewPath(userPersonalInfoPackage.getViewPath());
        userPersonalInfoPackages.setStaffName(userInfo.getUserName());
        return new ResultUtils(100, "查詢成功",userPersonalInfoPackages);
    }

}