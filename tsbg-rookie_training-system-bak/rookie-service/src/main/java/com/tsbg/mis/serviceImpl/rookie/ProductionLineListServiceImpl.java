package com.tsbg.mis.serviceImpl.rookie;

import com.tsbg.mis.annotation.SysLog;
import com.tsbg.mis.dao.rookie.ProductionLineListDao;
import com.tsbg.mis.jurisdiction.model.UserInfo;
import com.tsbg.mis.master.StaffInfo;
import com.tsbg.mis.rookie.model.ClassStudentInfo;
import com.tsbg.mis.rookie.vo.ClassStudentInfoVo;
import com.tsbg.mis.rookie.vo.ProductionLineInfoVo;
import com.tsbg.mis.rookie.vo.ProductionLineListVo;
import com.tsbg.mis.service.rookie.ProductionLineListService;
import com.tsbg.mis.serviceImpl.jurisdiction.base.TokenAnalysis;
import com.tsbg.mis.util.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class ProductionLineListServiceImpl implements ProductionLineListService {

    @Autowired
    ProductionLineListDao productionLineListDao;
    @Autowired
    TokenAnalysis tokenAnalysis;

    @Override
    //新增产线
    @SysLog("新增产线")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ResultUtils insertLine(ProductionLineListVo productionLineListVo){
        String userStaffCode = tokenAnalysis.getTokenUser().getStaffCode();
        productionLineListVo.setCreatorCode(userStaffCode);
        productionLineListVo.setUpdateCode(userStaffCode);

        //校验传过来的人是否都有合适的角色
        String lineGroupLeaderCode = productionLineListVo.getLineGroupLeaderCode();//产线组长工号
        String lineLeaderCode = productionLineListVo.getLineLeaderCode();//产线线长工号
        String lineManagerCode = productionLineListVo.getLineManagerCode();//产线负责人工号
        List<Integer> lineGroupLeaderRoles = productionLineListDao.selectRoleByStaffCode(lineGroupLeaderCode);
        List<Integer> lineLeaderRoles = productionLineListDao.selectRoleByStaffCode(lineLeaderCode);
        List<Integer> lineManagerRoles = productionLineListDao.selectRoleByStaffCode(lineManagerCode);
        if(null == lineGroupLeaderRoles || !lineGroupLeaderRoles.contains(11)){
            return new ResultUtils(500,"此人不是產線組長");
        }
        if(null == lineLeaderRoles || !lineLeaderRoles.contains(10)){
            return new ResultUtils(500,"此人不是線長");
        }
        if(null == lineManagerRoles || !lineManagerRoles.contains(12)){
            return new ResultUtils(500,"此人不是產線負責人");
        }


        List<ClassStudentInfo> studentInfos = productionLineListVo.getStudentInfos();//获取传过来的学生列表(应该只有学生id
        //放在这里判断防止有问题的时候产线已经新增完毕
        if(studentInfos!=null && !studentInfos.isEmpty()){
            Date internshipStartDate = productionLineListVo.getInternshipStartDate();
            Date internshipEndDate = productionLineListVo.getInternshipEndDate();
            if(internshipStartDate==null || internshipEndDate==null){
                return new ResultUtils(500,"請設置實習時間段");
            }
        }

        ProductionLineListVo productionLineListVo1 = disposeName(productionLineListVo);//处理名字方法(获取名字并找出工号和email)

        Integer insertCount = productionLineListDao.insertLine(productionLineListVo1);//添加产线
        if(null == insertCount || insertCount<=0){
            return new ResultUtils(500,"新增產線失敗");
        }
        //添加学生list
        Integer lineId = productionLineListVo.getLineId();//获取刚添加的lineId
        productionLineListVo.setLineId(lineId);

        if(studentInfos!=null && !studentInfos.isEmpty()){
            for (ClassStudentInfo studentInfo: studentInfos ) {
                String studentCode = studentInfo.getStaffCode();//获取学生id
                productionLineListVo.setStudentCode(studentCode);
                Integer insertStudentCount = productionLineListDao.insertStudentInfo(productionLineListVo);//添加学生
                if(null == insertStudentCount || insertStudentCount<=0){
                    return new ResultUtils(500,"新增學生失敗");
                }
            }
        }
        return new ResultUtils(101,"新增成功");
    }

    @Override
    //模糊搜索产线
    public ResultUtils fuzzySearchLine(ProductionLineListVo productionLineListVo){
        List<ProductionLineListVo> productionLineListVos = productionLineListDao.fuzzySearchLine(productionLineListVo);
        //查询学生信息并返回
        if(productionLineListVos!=null && !productionLineListVos.isEmpty()){
            for (ProductionLineListVo productionLineList : productionLineListVos ) {
                List<ClassStudentInfo> studentInfos = productionLineListDao.selectStudentInfoByLineId(productionLineList.getLineId());
                productionLineList.setStudentInfos(studentInfos);
            }
            return new ResultUtils(100,"查找成功",productionLineListVos);
        }else if(productionLineListVos !=null && productionLineListVos.isEmpty()){
            return new ResultUtils(100,"查找為空",productionLineListVos);
        }else {
            return new ResultUtils(500,"查找失敗");
        }
    }

    //编辑产线
    @Override
    @SysLog("编辑产线")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ResultUtils redactLine(ProductionLineListVo productionLineListVo){
        String userStaffCode = tokenAnalysis.getTokenUser().getStaffCode();
        productionLineListVo.setCreatorCode(userStaffCode);
        productionLineListVo.setUpdateCode(userStaffCode);
        Integer lineId = productionLineListVo.getLineId();
        productionLineListDao.updateLineStudentStatusByLineId(lineId,null);//把当前产线的学生status都设为0无效
        List<ClassStudentInfo> studentInfos = productionLineListVo.getStudentInfos();
        if(studentInfos!=null && !studentInfos.isEmpty()){
            for (ClassStudentInfo studentInfo: studentInfos ) {
                String studentCode = studentInfo.getStaffCode();//获取学生code
                productionLineListVo.setStudentCode(studentCode);
                Integer insertStudentCount = productionLineListDao.insertStudentInfo(productionLineListVo);//添加学生
                if(null == insertStudentCount || insertStudentCount<=0){
                    return new ResultUtils(500,"新增學生失敗");
                }
            }
        }

        //处理传过来的名字(线长等
        ProductionLineListVo productionLineListVo1 = disposeName(productionLineListVo);
        Integer updateCount = productionLineListDao.updateLineInfo(productionLineListVo1);//修改产线信息
        if(null == updateCount || updateCount<0){
            return new ResultUtils(500,"編輯產線失敗");
        }
        return new ResultUtils(101,"編輯產線成功",productionLineListVo);
    }

    @Override
    //查找class_student_info里所有有效的学生
    public ResultUtils selectStudentInStudentInfo(){
        List<ClassStudentInfoVo> classStudentInfoVos = productionLineListDao.selectStudentInStudentInfo();
        if(classStudentInfoVos!=null && !classStudentInfoVos.isEmpty()){
            return new ResultUtils(100,"查找成功",classStudentInfoVos);
        }else if(classStudentInfoVos!=null && classStudentInfoVos.isEmpty()){
            return new ResultUtils(100,"查找為空",classStudentInfoVos);
        }else {
            return new ResultUtils(500,"查找失敗");
        }
    }

    @Override
    //查找所有user_info里有效的user
    public ResultUtils selectUserInfo(){
        List<UserInfo> userInfos = productionLineListDao.selectUserInfo();
        if(userInfos!=null && !userInfos.isEmpty()){
            return new ResultUtils(100,"查找成功",userInfos);
        }else if(userInfos!=null && userInfos.isEmpty()){
            return new ResultUtils(100,"查找為空",userInfos);
        }else {
            return new ResultUtils(500,"查找失敗");
        }
    }

    @Override
    //删除产线(一并删除底下成员
    @SysLog("删除产线(一并删除底下的成员)")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ResultUtils deleteByLineId(Integer lineId){
        productionLineListDao.updateLineStatusByLineId(lineId);
        productionLineListDao.updateLineStudentStatusByLineId(lineId,null);
        return new ResultUtils(101,"刪除成功");
    }

    @Override
    //查找未分配学生
    public ResultUtils selectStudentWhereUndistributedByDate(ProductionLineListVo productionLineListVo){
        //查出已分配的学生
        List<ClassStudentInfoVo> classStudentInfoVos = productionLineListDao.selectStudentWhereUndistributedByDate(productionLineListVo);

        return new ResultUtils(100,"查找未分配成員成功",classStudentInfoVos);
    }


    @Override
    //根据工号查询员工信息
    public ResultUtils selectStaffInfoByCode(String staffCode){
        StaffInfo staffInfo = productionLineListDao.selectStaffInfoByCode(staffCode);
        if(staffInfo!=null){
            return new ResultUtils(100,"查詢成功",staffInfo);
        }else {
            return new ResultUtils(500,"查詢失敗");
        }
    }

    //处理工号(根据工号查找相关信息并塞进去)
    public ProductionLineListVo disposeName(ProductionLineListVo productionLineListVo){

        //获取工号
        String lineLeaderCode = productionLineListVo.getLineLeaderCode();
        String lineGroupLeaderCode = productionLineListVo.getLineGroupLeaderCode();
        String lineManagerCode = productionLineListVo.getLineManagerCode();
        //根据工号查找信息
        StaffInfo lineLeaderInfo = productionLineListDao.selectStaffInfoByCode(lineLeaderCode);
        StaffInfo lineGroupLeaderInfo = productionLineListDao.selectStaffInfoByCode(lineGroupLeaderCode);
        StaffInfo lineManagerInfo = productionLineListDao.selectStaffInfoByCode(lineManagerCode);
        //set回对象到时候作为sql参数
        if(lineLeaderInfo !=null){
            productionLineListVo.setLineLeaderName(lineLeaderInfo.getStaffName());
            productionLineListVo.setLineLeaderEmail(lineLeaderInfo.getEmail());
        }
        if(lineGroupLeaderInfo!=null){
            productionLineListVo.setLineGroupLeaderName(lineGroupLeaderInfo.getStaffName());
            productionLineListVo.setLineGroupLeaderEmail(lineGroupLeaderInfo.getEmail());
        }
        if(lineManagerInfo !=null){
            productionLineListVo.setLineManagerName(lineManagerInfo.getStaffName());
            productionLineListVo.setLineManagerEmail(lineManagerInfo.getEmail());
        }

        return productionLineListVo;
    }

    @Override
    //根据角色带出信息
    public ResultUtils selectUserInfoByRole(Integer roleId){
        List<UserInfo> userInfos = productionLineListDao.selectUserInfoByRole(roleId);
        if(null != userInfos && !userInfos.isEmpty()){
            return new ResultUtils(100,"查詢成功",userInfos);
        }else if(null != userInfos && userInfos.isEmpty()){
            return new ResultUtils(100,"查詢為空",userInfos);
        }else {
            return new ResultUtils(500,"查詢失敗");
        }
    }

    //修改学生信息
    @Override
    public ResultUtils updateStudentInfo(List<ClassStudentInfo> classStudentInfos){
        if(null != classStudentInfos && !classStudentInfos.isEmpty()){
            for (ClassStudentInfo classStudentInfo:classStudentInfos ) {
                productionLineListDao.updateStudentInfo(classStudentInfo);
            }
            return new ResultUtils(100,"更新成功");
        }else {
            return new ResultUtils(500,"更新失败");
        }
    }

    //查询产线实习记录详情
    @Override
    public ResultUtils queryProductionLineRecord(ProductionLineInfoVo productionLineInfoVo) {
        //取得当前登录人的工号
        String staffCode  = tokenAnalysis.getTokenUser().getStaffCode();
        productionLineInfoVo.setStaffCode(staffCode);
        //根据学生工号查询查询产线组长、线长等信息
        List<ProductionLineInfoVo> queryProductionLineRecords = productionLineListDao.queryProductionLineRecord(productionLineInfoVo);
        if(queryProductionLineRecords==null){
            return new ResultUtils(100, "查询为空", queryProductionLineRecords);
        }
        //根据学生工号查询学生的组长、线长电话
        for(ProductionLineInfoVo productionLineInfoVo1:queryProductionLineRecords){
            UserInfo userInfo1 = productionLineListDao.queryUserInfo(productionLineInfoVo1.getLineLeaderCode());
            if(userInfo1!=null){
                productionLineInfoVo1.setLinePhoneNumber(userInfo1.getPhoneNumber());
            }
            UserInfo userInfo2 = productionLineListDao.queryUserInfo(productionLineInfoVo1.getLineGroupLeaderCode());
            if(userInfo2!=null){
                productionLineInfoVo1.setLineGroupPhoneNumber(userInfo2.getPhoneNumber());
            }
            UserInfo userInfo3 =productionLineListDao.queryUserInfo(productionLineInfoVo1.getLineManagerCode());
            if(userInfo3!=null){
                productionLineInfoVo1.setLineManagerPhoneNumber(userInfo3.getPhoneNumber());
            }
        }
        if (null != queryProductionLineRecords && !queryProductionLineRecords.isEmpty()) {
            return new ResultUtils(100, "查询成功", queryProductionLineRecords);
        }else {
            return new ResultUtils(500, "查询失败");
        }
    }

    //查询部门实习记录详情
    @Override
    public ResultUtils querydepartmentInternshipRecord(String staffCode) {
        //根据学生工号查询查询产线组长、线长等信息
        List<ProductionLineInfoVo> querydepartmentInternshipRecord = productionLineListDao.querydepartmentInternshipRecord(staffCode);
        if(querydepartmentInternshipRecord==null){
            return new ResultUtils(100, "查询为空", querydepartmentInternshipRecord);
        }
        //根据学生工号查询教练员、直属主管、课级主管、部门主管邮箱分机号。
        for(ProductionLineInfoVo productionLineInfoVo:querydepartmentInternshipRecord){
            UserInfo userInfo1 = productionLineListDao.queryUserInfo(productionLineInfoVo.getCoachCode());
            if(userInfo1!=null){
                productionLineInfoVo.setCoachuserExt(userInfo1.getUserExt());
                productionLineInfoVo.setCoachemailAddress(userInfo1.getEmailAddress());
            }
            UserInfo userInfo2 = productionLineListDao.queryUserInfo(productionLineInfoVo.getDirectManagerCode());
            if(userInfo2!=null){
                productionLineInfoVo.setDirectManageruserExt(userInfo2.getUserExt());
                productionLineInfoVo.setDirectManageremailAddress(userInfo2.getEmailAddress());
            }
            UserInfo userInfo3 =productionLineListDao.queryUserInfo(productionLineInfoVo.getDepartManagerCode());
            if(userInfo3!=null){
                productionLineInfoVo.setDepartManageruserExt(userInfo3.getUserExt());
                productionLineInfoVo.setDepartManageremailAddress(userInfo3.getEmailAddress());
            }

            UserInfo userInfo4 =productionLineListDao.queryUserInfo(productionLineInfoVo.getUnitManagerCode());
            if(userInfo4!=null){
                productionLineInfoVo.setUnitManageruserExt(userInfo4.getUserExt());
                productionLineInfoVo.setUnitManageremailAddress(userInfo4.getEmailAddress());
            }
        }
        if (null != querydepartmentInternshipRecord && !querydepartmentInternshipRecord.isEmpty()) {
            return new ResultUtils(100, "查询成功", querydepartmentInternshipRecord);
        }else {
            return new ResultUtils(500, "查询失败");
        }
    }

    //查询我的实习记录-产线实习记录
    @Override
    public ResultUtils queryMyProductionLineRecord(ProductionLineInfoVo productionLineInfoVo) {
        String userStaffCode = tokenAnalysis.getTokenUser().getStaffCode();
        productionLineInfoVo.setStaffCode(userStaffCode);
        List<ProductionLineInfoVo> productionLineInfoVos = productionLineListDao.queryMyProductionLineRecord(productionLineInfoVo);
        if(productionLineInfoVos==null){
            return new ResultUtils(100, "查询为空", productionLineInfoVos);
        }
        for(ProductionLineInfoVo productionLineInfoVo1:productionLineInfoVos){
            productionLineInfoVo1.setTypeId(7);
            productionLineInfoVo1.setTypeName("產線實習");
        }
        if (!productionLineInfoVos.isEmpty()) {
            return new ResultUtils(100, "查询成功", productionLineInfoVos);
        } else if (productionLineInfoVos.isEmpty()) {
            return new ResultUtils(100, "查询为空", productionLineInfoVos);
        } else {
            return new ResultUtils(500, "查询失败");
        }
    }

    //查询我的实习记录、部门实习记录
    @Override
    public ResultUtils queryMydepartmentInternshipRecord(ProductionLineInfoVo productionLineInfoVo) {
        String userStaffCode = tokenAnalysis.getTokenUser().getStaffCode();
        productionLineInfoVo.setStaffCode(userStaffCode);
        List<ProductionLineInfoVo> productionLineInfoVos = productionLineListDao.queryMyProductionLineRecord(productionLineInfoVo);
        if(productionLineInfoVos ==null){
            return new ResultUtils(100, "查询为空", productionLineInfoVos);
        }
        //清空List集合
        productionLineInfoVos.clear();
        for(ProductionLineInfoVo productionLineInfoVo1:productionLineInfoVos){
            productionLineInfoVo1.setTypeId(8);
            productionLineInfoVo1.setTypeName("部門實習");
            productionLineInfoVo1.setInternshipStartDate(null);
            productionLineInfoVo1.setInternshipEndDate(null);
        }
        if (!productionLineInfoVos.isEmpty()) {
            return new ResultUtils(100, "查询成功", productionLineInfoVos);
        }else if (productionLineInfoVos.isEmpty()) {
            return new ResultUtils(100, "查询为空", productionLineInfoVos);
        }else{
            return new ResultUtils(500, "查询失败");
        }
    }

    //查询我的实习记录、部门、产线实习记录（传产线实习的开始时间-和部门的结束时间进行查询）
    @Override
    public ResultUtils queryInternshipRecord(ProductionLineInfoVo productionLineInfoVo) {
        String userStaffCode = tokenAnalysis.getTokenUser().getStaffCode();
        productionLineInfoVo.setStaffCode(userStaffCode);
        List<ProductionLineInfoVo> productionLineInfoVos = productionLineListDao.queryMyProductionLineRecord(productionLineInfoVo);
        if(productionLineInfoVos==null){
            return new ResultUtils(100, "查询为空", productionLineInfoVos);
        }
        for(ProductionLineInfoVo productionLineInfoVo1:productionLineInfoVos){
            productionLineInfoVo1.setTypeId(7);
            productionLineInfoVo1.setTypeName("產線實習");
        }
        if (!productionLineInfoVos.isEmpty()) {
            return new ResultUtils(100, "查询成功", productionLineInfoVos);
        }else if (productionLineInfoVos.isEmpty()) {
            return new ResultUtils(100, "查询为空", productionLineInfoVos);
        }else{
            return new ResultUtils(500, "查询失败");
        }
    }
}
