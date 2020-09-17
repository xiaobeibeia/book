package com.tsbg.mis.dao.rookie;

import com.tsbg.mis.base.BuList;
import com.tsbg.mis.rookie.model.ClassGradeInfo;
import com.tsbg.mis.rookie.model.ClassSquadList;
import com.tsbg.mis.rookie.model.ClassStudentInfo;
import com.tsbg.mis.rookie.vo.StudentCommitteeVo;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface ClassStudentInfoDao {

    //查詢菁干班所有成員(只传studentId时为查询单个成员详情)
    List<ClassStudentInfo> selectAll(ClassStudentInfo classStudentInfo);

    //查詢所有屆別
    List<String> selectClassPeriod();

    //根據屆別查詢所有班級信息
    List<ClassGradeInfo> selectGradeInfo(String classPeriod);

    //根据時間查出菁干班人员信息
    List<ClassStudentInfo> selectAllStudentByClassPeriod(String classPeriod);

    //根據屆別查已有班級名稱
    List<String> selectGradeNameByClassPeriod(String classPeriod);

    //修改班級名稱
    int modifyGradeName(ClassGradeInfo classGradeInfo);

    //根據gradeId刪除班級
    int delGradeInfo(ClassGradeInfo classGradeInfo);

    //(删除班级)根據gradeId重置学生信息
    int resetStudentInfo(ClassGradeInfo classGradeInfo);

    //設定班級 -> 插入 class_grade_info 表
    int divideIntoClasses(ClassGradeInfo classGradeInfo);

    //根據gradeId查詢這個班級所在的屆別
    String selectClassPeriodByGradeId(Integer gradeId);

    //查詢這屆有多少個班
    int selectCountGrade(String classPeriod);

    //查當前屆別的班級id
    List<Integer> selectGradeIdByClassPeriod(String classPeriod);

    //分班后根據工號修改其班級id
    void updateGradeId(Integer gradeId, String staffCode);

    //重置分班
    int resetClassMembers(String classPeriod);

    //創建分組
    int addSquad(ClassSquadList classSquadList);

    //修改分組
    int modifySLeaderCode(ClassSquadList classSquadList);

    //根据gradeId查询这个班已有班委(包含组长)
    List<StudentCommitteeVo> selectStudentCommittee(Integer gradeId);

    //根据gradeId查询这个班已有班委(不包含组长)
    List<StudentCommitteeVo> selectClassCommittee(Integer gradeId);

    //根据工号和gradeId,将班委职位置为普通学生61
    int modifyClassCommittee(String staffCode, Integer gradeId);

    //將重新指定的學生設定為班委 --> 根據工號修改班委
    int modifyStudentTypeByStaffCode(ClassStudentInfo classStudentInfo);

    //查询class_student_info表中squadId = 0的（还未分组）
    List<ClassStudentInfo> selectStudentByGradeId(Integer gradeId);

    //查询当前届别下此班级有多少个分组
    List<Integer> selectSquadByGrade(Integer gradeId);

    //查每个分组中组长的信息 studentType = 66
    ClassSquadList selectSLeaderByStudentType(Integer gradeId, Integer squadId);

    //查每个分组中其他组成员的信息 studentType = 61
    List<ClassStudentInfo> selectGroupStudentByStudentType(Integer gradeId, Integer squadId);

    //修改分組成員
    int updateSquadIdAndStudentType(Integer squadId);

    //根據工號修改學生類別
    void updateStudentTypeByGradeIdAndStaffCode(Integer squadId, String staffCode, Integer studentType);

    //修改（刪除）組成員
    int modifySquadMembers(String staffCode,Integer squadId,Integer studentType);

    //刪除整個分組
    int delSquad(Integer squad);

    //查询所有处级单位信息
    List<BuList> selectAllBuList();
}
