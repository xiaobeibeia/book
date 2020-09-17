package com.tsbg.mis.dao.rookie;

import com.tsbg.mis.jurisdiction.model.UserInfo;
import com.tsbg.mis.master.StaffInfo;
import com.tsbg.mis.rookie.model.ClassStudentInfo;
import com.tsbg.mis.rookie.vo.ClassStudentInfoVo;
import com.tsbg.mis.rookie.vo.ProductionLineInfoVo;
import com.tsbg.mis.rookie.vo.ProductionLineListVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Component
@Repository
public interface ProductionLineListDao {


    Integer insertLine(ProductionLineListVo productionLineListVo);

    Integer insertStudentInfo(ProductionLineListVo productionLineListVo);

    List<ProductionLineListVo> fuzzySearchLine(ProductionLineListVo productionLineListVo);

    StaffInfo selectStaffInfoByCode(String staffName);

    List<ClassStudentInfo> selectStudentInfoByLineId(Integer lineId);

    Integer updateLineInfo(ProductionLineListVo productionLineListVo);

    List<ClassStudentInfoVo> selectStudentInStudentInfo();

    List<UserInfo> selectUserInfo();

    Integer updateLineStatusByLineId(Integer lineId);

    List<ClassStudentInfoVo> selectStudentWhereUndistributedByDate(ProductionLineListVo productionLineListVo);

    List<UserInfo> selectUserInfoByRole(Integer roleId);

    List<Integer> selectRoleByStaffCode(String staffCode);

    Integer updateStudentInfo(ClassStudentInfo classStudentInfo);

    //查询产线实习记录详情
    List<ProductionLineInfoVo> queryProductionLineRecord(ProductionLineInfoVo productionLineInfoVo);

    //删除----↓
    Integer deleteStudentInClassStudentInfo(String staffCode);//删除学生表某个学生

    Integer updateLineStudentStatusByLineId(@Param("lineId") Integer lineId, @Param("staffCode") String staffCode);//把当前产线的学生status全部设为0(无效

    //根据工号查询其是否为小组组长
    Integer selectIsSquadLeader(String staffCode);

    //根据工号删除小组组长信息
    Integer delSquadLeader(String staffCode);

    UserInfo queryUserInfo(String staffCode);
    //查询部门实习记录详情
    List<ProductionLineInfoVo> querydepartmentInternshipRecord(String staffCode);

    //查询我的实习记录-产线实习记录
    List<ProductionLineInfoVo> queryMyProductionLineRecord(ProductionLineInfoVo productionLineInfoVo);

    //查询我的实习记录-部门实习记录
    List<ProductionLineInfoVo> queryMydepartmentInternshipRecord(ProductionLineInfoVo productionLineInfoVo);

    //查询我的实习记录-部门、产线实习记录
    List<ProductionLineInfoVo> queryInternshipRecord(ProductionLineInfoVo productionLineInfoVo);
}
