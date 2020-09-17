package com.tsbg.mis.serviceImpl.rookie;

import com.tsbg.mis.annotation.SysLog;
import com.tsbg.mis.base.BuList;
import com.tsbg.mis.jurisdiction.model.UserInfo;
import com.tsbg.mis.dao.rookie.ClassStudentInfoDao;
import com.tsbg.mis.rookie.model.ClassGradeInfo;
import com.tsbg.mis.rookie.model.ClassSquadList;
import com.tsbg.mis.rookie.model.ClassStudentInfo;
import com.tsbg.mis.rookie.vo.ClassSquadListVo;
import com.tsbg.mis.rookie.vo.CommitteeVo;
import com.tsbg.mis.rookie.vo.StudentCommitteeVo;
import com.tsbg.mis.rookie.vo.GradeStudentInfoVo;
import com.tsbg.mis.serviceImpl.jurisdiction.base.TokenAnalysis;
import com.tsbg.mis.util.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * @Author F1337200
 **/
@Service
public class ClassManagementService {

    @Autowired
    private ClassStudentInfoDao classStudentInfoDao;
    @Autowired
    private TokenAnalysis tokenAnalysis;

    //查詢菁干班所有成員(只传studentId时为查询单个成员详情)
    public ResultUtils selectAll(ClassStudentInfo classStudentInfo){
        if(classStudentInfo.getClassPeriod() == null || classStudentInfo.getClassPeriod().trim().equals("")){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
            String classPeriod = sdf.format(new Date()) + "屆";
            classStudentInfo.setClassPeriod(classPeriod);
        }
        List<ClassStudentInfo> classStudentInfos = classStudentInfoDao.selectAll(classStudentInfo);
        if(classStudentInfos != null && !classStudentInfos.isEmpty()){
            return new ResultUtils(100,"查詢成功",classStudentInfos);
        }
        return new ResultUtils(100,"暫無數據");
    }


    //查詢所有屆別
    public ResultUtils selectClassPeriod(){
        List<String> classPeriods = classStudentInfoDao.selectClassPeriod();
        if(classPeriods !=null && classPeriods.size()>0){
            return new ResultUtils(100,"屆別查詢成功",classPeriods);
        }
        return new ResultUtils(100,"暫無數據");
    }



    //根據屆別查詢所有班級信息
    public ResultUtils selectGradeInfo(String classPeriod) {
        //查詢班級信息
        if(classPeriod == null || classPeriod.trim().equals("")){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
            classPeriod = sdf.format(new Date()) + "屆";
        }
        List<ClassGradeInfo> classGradeInfos = classStudentInfoDao.selectGradeInfo(classPeriod);
        if (classGradeInfos != null && !classGradeInfos.isEmpty()) {
            return new ResultUtils(100, "查詢成功", classGradeInfos);
        }
        return new ResultUtils(100, "暫無數據");
    }


    //班级管理-->設定班級
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @SysLog(value = "設定班級")
    public ResultUtils placement(List<ClassGradeInfo> classGradeInfo){
        UserInfo user = tokenAnalysis.getTokenUser();
        Date date = new Date();

        //分班 --> 接收分班信息插入到班级信息表中
        if(classGradeInfo != null){
            //菁干班届别名称
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
            String period = sdf.format(new Date()) +"屆";
            long count = classGradeInfo.stream().distinct().count();
            if(count<classGradeInfo.size()){
              return new ResultUtils(500,"班級名重複,請重新輸入");
            }
            for (ClassGradeInfo classGradeInfos: classGradeInfo) {
                if(classGradeInfos.getGradeName() !=null && !classGradeInfos.getGradeName().trim().equals("")){
                    //设置菁干班届别名称
                    classGradeInfos.setClassPeriod(period);
                    //设置创建人工号、时间   修改人工号、时间
                    classGradeInfos.setCreatorCode(user.getStaffCode());
                    classGradeInfos.setCreateDate(date);
                    classGradeInfos.setUpdateCode(user.getStaffCode());
                    classGradeInfos.setUpdateDate(date);
                    classStudentInfoDao.divideIntoClasses(classGradeInfos);
                }else {
                    return new ResultUtils(500,"請輸入班級名稱");
                }
            }
            return new ResultUtils(101,"設定班級成功",classGradeInfo);
        }
        return new ResultUtils(500,"請輸入班級名稱");
    }


    //根據id修改班級名稱  &  刪除班級
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @SysLog(value = "修改/刪除班級")
    public ResultUtils modifyGrade(ClassGradeInfo classGradeInfo){
        UserInfo user = tokenAnalysis.getTokenUser();
        if(classGradeInfo.getGradeName() !=null && !classGradeInfo.getGradeName().trim().equals("") && classGradeInfo.getGradeId() !=null){
            //判斷名稱是否有重複
            //根據gradeId查這個班級所在的屆別
            String classPeriodName = classStudentInfoDao.selectClassPeriodByGradeId(classGradeInfo.getGradeId());
            //根據classPeriodName查詢這屆所有的班級名稱
            List<String> gradeNames = classStudentInfoDao.selectGradeNameByClassPeriod(classPeriodName);
            if(gradeNames != null && gradeNames.contains(classGradeInfo.getGradeName())){
                return new ResultUtils(500,"此屆菁干班班級名稱重複，請重新輸入班級名稱");
            }
            //修改班級名稱
            classGradeInfo.setUpdateCode(user.getStaffCode());
            classGradeInfo.setUpdateDate(new Date());
            int i = classStudentInfoDao.modifyGradeName(classGradeInfo);
            if(i>0){
                return new ResultUtils(101,"修改成功",classGradeInfo);
            }else {
                return new ResultUtils(500,"修改失敗");
            }
        }
        //刪除班級 -> 將class_grade_info表中的status置為0,将class_student_info表中为该班级的学生信息重置
        if(classGradeInfo.getGradeId() != null){
            classGradeInfo.setUpdateCode(user.getStaffCode());
            classGradeInfo.setUpdateDate(new Date());
            classGradeInfo.setStatus(0);
            //删除班级
            int i = classStudentInfoDao.delGradeInfo(classGradeInfo);
            //重置已分班学生信息
            int j = classStudentInfoDao.resetStudentInfo(classGradeInfo);
            if(i>0 && j>=0){
                return new ResultUtils(101,"刪除班級成功",classGradeInfo);
            }else {
                return new ResultUtils(500,"刪除班級失敗");
            }
        }
        return new ResultUtils(500,"操作失敗");
    }


    //班级管理-->給班級添加成員  自動隨機分班
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @SysLog(value = "隨機分班")
    public ResultUtils addStudentIntoGradeAuto(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        String classPeriod = sdf.format(new Date()) + "屆";
        //先根據屆別查出所有菁干班成員
        List<ClassStudentInfo> classStudentInfos = classStudentInfoDao.selectAllStudentByClassPeriod(classPeriod);
        if(classStudentInfos == null || classStudentInfos.isEmpty()){
            return new ResultUtils(500,"此屆別暫無菁干班成員");
        }
        for (ClassStudentInfo studentInfo :classStudentInfos) {
            if(studentInfo.getStudentType() !=null && studentInfo.getStudentType() != 63){
                return new ResultUtils(500,"学生中有班委职位，无法分班");
            }
        }
        //查詢這屆有多少個班
        int count = classStudentInfoDao.selectCountGrade(classPeriod);
        //查當前屆別的班級id
        List<Integer> integers = classStudentInfoDao.selectGradeIdByClassPeriod(classPeriod);

        //隨機分班
        //arr為A班，剩下的classStudentInfos為B班，若為1個班級或2個以上班級也適用
        int i = classStudentInfos.size()/count;
        int num;
        if(integers == null || integers.isEmpty()){
           return new ResultUtils(500,"此屆別沒有設置班級");
        }
        for(int k=1; k<count; k++){
            if(count%2 == 0){
                num = 1;
            }else {
                num = 0;
            }
            List<String> arr = new ArrayList<>();
            while (num <= i){
                if(classStudentInfos.size() == 0){
                    break;
                }
                int index=(int)(Math.random()*classStudentInfos.size());
                String n =classStudentInfos.get(index).getStaffCode();
                classStudentInfos.remove(index);
                arr.add(n);
                num++;
            }
            //修改class_student_info表里的班級狀態
            for (String staffCode: arr) {
                classStudentInfoDao.updateGradeId(integers.get(k-1),staffCode);
            }
        }
        //剩下的classStudentInfos修改為B班
        for (ClassStudentInfo obj:classStudentInfos) {
            classStudentInfoDao.updateGradeId(integers.get(integers.size()-1),obj.getStaffCode());
        }

        return new ResultUtils(101,"隨機分班成功");
    }


    //手動分班（修改&微調）
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @SysLog(value = "手動分班")
    public ResultUtils modifyStudentIntoGrade(List<String> staffCodes, Integer gradeId){
        //接收前端傳來的數據,循環修改所在班級id
        //需要調整的學生  以及  班級id
        if(!staffCodes.isEmpty() && gradeId != null){
            for (String staffcode:staffCodes) {
                classStudentInfoDao.updateGradeId(gradeId,staffcode);
            }
            return new ResultUtils(101,"已調整學生班級",staffCodes,gradeId);
        }

        return new ResultUtils(500,"請選擇需要調整的學生和班級");
    }


    //重置分班
    @SysLog(value = "重置分班")
    public ResultUtils resetClassMembers(){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        String classPeriod = sdf.format(date) + "屆";
        //将class_student_info表中的grade_id置为0,squad_id = 0,student_type = 63
        int i = classStudentInfoDao.resetClassMembers(classPeriod);
        if(i > 0){
            return new ResultUtils(101,"已重置分班");
        }
        return new ResultUtils(500,"操作失敗");
    }


    //查询class_student_info表中squadId = 0的（还未分组）
    public ResultUtils selectStudentByGradeId(Integer gradeId){
        if(gradeId != null){
            List<ClassStudentInfo> classStudentInfos = classStudentInfoDao.selectStudentByGradeId(gradeId);
            if(classStudentInfos !=null && !classStudentInfos.isEmpty()){
                return new ResultUtils(100,"查询该班级未分组成员成功",classStudentInfos);
            }
        }
        return new ResultUtils(100,"暫無數據");
    }


    //班級成員分組  &  修改分組成員
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @SysLog(value = "班級成員分組&修改分組成員")
    public ResultUtils addStudentInfoSquad(ClassSquadListVo classSquadListVo){
        UserInfo user = tokenAnalysis.getTokenUser();
        Date date = new Date();
        //創建分組
        if(classSquadListVo != null && classSquadListVo.getClassSquadList().getSquadId() == null){
            classSquadListVo.getClassSquadList().setCreatorCode(user.getStaffCode());
            classSquadListVo.getClassSquadList().setCreateDate(date);
            classSquadListVo.getClassSquadList().setUpdateCode(user.getStaffCode());
            classSquadListVo.getClassSquadList().setUpdateDate(date);
            String squadName = classSquadListVo.getClassSquadList().getSquadName();
            if(squadName != null && !squadName.trim().equals("")){
                for (ClassStudentInfo classStudentInfo: classSquadListVo.getClassStudentInfos()) {
                    if(classStudentInfo.getStaffCode() != null && classStudentInfo.getStaffCode().equals(classSquadListVo.getClassSquadList().getsLeaderCode())){
                        return new ResultUtils(500,"該分組請勿將組長重複添加到組員中");
                    }
                }
                int i = classStudentInfoDao.addSquad(classSquadListVo.getClassSquadList());
                if(i > 0){
                    //创建分组成功,返回主键gradeId
                    Integer squadId = classSquadListVo.getClassSquadList().getSquadId();
                    //添加組長
                    classStudentInfoDao.updateStudentTypeByGradeIdAndStaffCode(squadId,classSquadListVo.getClassSquadList().getsLeaderCode(),classSquadListVo.getClassSquadList().getStudentType());
                    //批量添加成员 --> update class_student_info表中的squad_id
                    for (ClassStudentInfo classStudentInfo: classSquadListVo.getClassStudentInfos()) {
                        if(classStudentInfo.getStudentType() != null && classStudentInfo.getStudentType() == 63){
                            classStudentInfoDao.updateStudentTypeByGradeIdAndStaffCode(squadId,classStudentInfo.getStaffCode(),null);
                            continue;
                        }
                        classStudentInfoDao.updateStudentTypeByGradeIdAndStaffCode(squadId,classStudentInfo.getStaffCode(),classStudentInfo.getStudentType());
                    }
                    return new ResultUtils(101,"添加分組成員成功",classSquadListVo);
                }else {
                    return new ResultUtils(500,"創建分組失敗");
                }
            }
        }else if(classSquadListVo != null && classSquadListVo.getClassSquadList().getSquadId() != null) { //修改分組成員  根据是否有小组id来判断
            Integer squadId = classSquadListVo.getClassSquadList().getSquadId();
            //清空原成員分組信息
            int i = classStudentInfoDao.updateSquadIdAndStudentType(squadId);
            if(i>0){
                //修改組長
                classSquadListVo.getClassSquadList().setUpdateCode(user.getStaffCode());
                classSquadListVo.getClassSquadList().setUpdateDate(date);
                //修改class_squad_list表
                classStudentInfoDao.modifySLeaderCode(classSquadListVo.getClassSquadList());
                //根據squadId修改s_leader_code
                classStudentInfoDao.updateStudentTypeByGradeIdAndStaffCode(squadId,classSquadListVo.getClassSquadList().getsLeaderCode(),classSquadListVo.getClassSquadList().getStudentType());
                //批量修改成员 --> update class_student_info表中的squad_id
                for (ClassStudentInfo classStudentInfo: classSquadListVo.getClassStudentInfos()) {
                    classStudentInfoDao.updateStudentTypeByGradeIdAndStaffCode(squadId,classStudentInfo.getStaffCode(),classStudentInfo.getStudentType());
                }
                return new ResultUtils(101,"修改分組成員成功",classSquadListVo);
            }
            return new ResultUtils(500,"修改分組成員失敗");
        }
        return new ResultUtils(500,"請填寫完整信息");
    }


    //查询当前届别中的某个班级下的各分组成员信息
    public ResultUtils selectStudentByGradeAndSquad(Integer gradeId){
        //先查询当前届别下此班级有多少个分组
        List<Integer> squads = classStudentInfoDao.selectSquadByGrade(gradeId);
        if(squads !=null && squads.size()>0){
            //查询每个分组下的成员信息 返回classSquadListVo
            GradeStudentInfoVo gradeStudentInfoVo = new GradeStudentInfoVo();
            gradeStudentInfoVo.setClassSquadListVos(new ArrayList<>());
            for (Integer squadId: squads) {
                if(squadId == 0){
                    continue;
                }
                ClassSquadListVo classSquadListVo = new ClassSquadListVo();
                //查每个分组中组长的信息 studentType = 68
                ClassSquadList classSquadList = classStudentInfoDao.selectSLeaderByStudentType(gradeId, squadId);
                //查每个分组中其他组成员的信息 studentType != 68
                List<ClassStudentInfo> classStudentInfos = classStudentInfoDao.selectGroupStudentByStudentType(gradeId, squadId);
                classSquadListVo.setClassSquadList(classSquadList);
                classSquadListVo.setClassStudentInfos(classStudentInfos);
                if(classSquadList == null && (classStudentInfos==null || classStudentInfos.size() == 0)){
                    continue;
                }
                gradeStudentInfoVo.getClassSquadListVos().add(classSquadListVo);
            }
            return new ResultUtils(100,"查詢各分組成員信息成功",gradeStudentInfoVo);
        }
        return new ResultUtils(100,"暫無數據");
    }


    //查询班级设定的班委
    public ResultUtils selectStudentCommittee(Integer gradeId){
        if(gradeId != null){
            List<StudentCommitteeVo> studentCommitteeVos = classStudentInfoDao.selectStudentCommittee(gradeId);
            if(studentCommitteeVos != null && !studentCommitteeVos.isEmpty()){
                return new ResultUtils(100,"查詢班委成功",studentCommitteeVos);
            }
            return new ResultUtils(100,"暫無數據");
        }
        return new ResultUtils(500,"系統參數錯誤，請重試");
    }


    //修改班委信息
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @SysLog(value = "设立/修改班委")
    public ResultUtils modifyStudentCommittee(CommitteeVo committeeVo){
        if(committeeVo.getGradeId() != null && committeeVo.getClassStudentInfos() != null && committeeVo.getClassStudentInfos().size() > 0){
            HashSet<String> set = new HashSet<>();
            List<ClassStudentInfo> classStudentInfos = committeeVo.getClassStudentInfos();
            if(classStudentInfos == null || classStudentInfos.isEmpty()){
                return new ResultUtils(500,"請選擇需要設立的班委成員");
            }
            for(int j=0; j<classStudentInfos.size(); j++ ){
                set.add(classStudentInfos.get(j).getStaffCode());
            }
            Boolean result = set.size() == classStudentInfos.size() ? true : false;
            if(!result){
                return new ResultUtils(500,"同一學生請勿安排多個班委");
            }
            //根据gradeId查这个班所有班委成员(studentType !=63 && !=68)
            List<StudentCommitteeVo> studentCommitteeVos = classStudentInfoDao.selectClassCommittee(committeeVo.getGradeId());
            if(studentCommitteeVos != null && studentCommitteeVos.size()>0){
                for (StudentCommitteeVo studentComminttees:studentCommitteeVos) {
                    //根据工号和gradeId,将这些班委职位置为普通学生63(相当于删除旧数据)
                    int i = classStudentInfoDao.modifyClassCommittee(studentComminttees.getStaffCode(),studentComminttees.getGradeId());
                    if(i > 0){
                        continue;
                    }else {
                        return new ResultUtils(500,"修改班委失敗");
                    }
                }
            }
            //重新设立班委(gradeId,staffCode,studentType)
            for (ClassStudentInfo classStudentInfo: classStudentInfos) {
                int i = classStudentInfoDao.modifyStudentTypeByStaffCode(classStudentInfo);
                if(i > 0){
                    continue;
                }else {
                    return new ResultUtils(500,"設立班委失敗");
                }
            }
            return new ResultUtils(100,"操作成功",committeeVo);
        }
        return new ResultUtils(500,"系統參數錯誤，請重試");
    }


    //刪除組成員  &  删除整个分组
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @SysLog(value = "刪除組成員/删除整个分组")
    public ResultUtils modifySquadMembers(List<String> staffCodes,Integer squadId){
        // 刪除組成員  ->  將class_student_info表中的squad_id置為0
        if(staffCodes != null && staffCodes.size() > 0){
            for (String staffCode : staffCodes) {
               int i = classStudentInfoDao.modifySquadMembers(staffCode,squadId,null);
               if(i > 0){
                   continue;
               }
               return new ResultUtils(500,"刪除組成員失敗");
            }
            return new ResultUtils(101,"刪除組成員成功",staffCodes);
        }
        /*删除整个分组 ->
        * 1、將class_student_info表中的squad_id置為0
        * 2、將class_squad_list表中為squadId的status置為0
        */
        if(squadId !=null){
            //根據squadId查此小組的組長
            ClassSquadList classSquadList = classStudentInfoDao.selectSLeaderByStudentType(null, squadId);
            //擔任班委只移除分組，不修改student_type
            int c = 0;
            if(classSquadList !=null && classSquadList.getStudentType() == 68){
                String staffCode = classSquadList.getsLeaderCode();
                c = classStudentInfoDao.modifySquadMembers(staffCode,squadId,63);
            }
            int a = classStudentInfoDao.modifySquadMembers(null,squadId,null);
            int b = classStudentInfoDao.delSquad(squadId);
            if(a>0 || (c>0 && b>0)){
                return new ResultUtils(101,"刪除分組成功",squadId);
            }
        }
        return new ResultUtils(500,"刪除失敗");
    }


    //查询所有处级单位信息
    public  ResultUtils selectAllBuList(){
        List<BuList> buLists = classStudentInfoDao.selectAllBuList();
        if(buLists != null && buLists.size() > 0){
            return new ResultUtils(100,"查询成功",buLists);
        }
        return new ResultUtils(500,"暫無數據");
    }

}
