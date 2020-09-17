package com.tsbg.mis.service.rookie;

import com.tsbg.mis.rookie.model.ClassStudentInfo;
import com.tsbg.mis.rookie.vo.ProductionLineInfoVo;
import com.tsbg.mis.rookie.vo.ProductionLineListVo;
import com.tsbg.mis.util.ResultUtils;

import java.util.List;


public interface ProductionLineListService {
    ResultUtils insertLine(ProductionLineListVo productionLineListVo);

    ResultUtils fuzzySearchLine(ProductionLineListVo productionLineListVo);

    ResultUtils redactLine(ProductionLineListVo productionLineListVo);

    ResultUtils selectStudentInStudentInfo();

    ResultUtils selectUserInfo();

    ResultUtils deleteByLineId(Integer lineId);

    ResultUtils selectStudentWhereUndistributedByDate(ProductionLineListVo productionLineListVo);

    ResultUtils selectStaffInfoByCode(String staffCode);

    ResultUtils selectUserInfoByRole(Integer roleId);

    ResultUtils queryProductionLineRecord(ProductionLineInfoVo productionLineInfoVo);

    ResultUtils updateStudentInfo(List<ClassStudentInfo> classStudentInfo);

    ResultUtils querydepartmentInternshipRecord(String staffCode);

    ResultUtils queryMyProductionLineRecord(ProductionLineInfoVo productionLineInfoVo);

    ResultUtils queryMydepartmentInternshipRecord(ProductionLineInfoVo productionLineInfoVo);

    ResultUtils queryInternshipRecord(ProductionLineInfoVo productionLineInfoVo);
}
