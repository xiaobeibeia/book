package com.tsbg.mis.serviceImpl.rookie;

import cn.hutool.core.util.IdUtil;
import com.tsbg.mis.annotation.SysLog;
import com.tsbg.mis.dao.jurisdiction.UserRoleDao;
import com.tsbg.mis.dao.rookie.ClassStudentInfoDao;
import com.tsbg.mis.dao.rookie.DepartmentTargetDao;
import com.tsbg.mis.jurisdiction.bag.RoleAndInfoPackage;
import com.tsbg.mis.jurisdiction.model.UserInfo;
import com.tsbg.mis.master.StaffInfo;
import com.tsbg.mis.rookie.bag.DepartTargetComparator;
import com.tsbg.mis.rookie.bag.DepartTargetSetInfoPackage;
import com.tsbg.mis.rookie.bag.WeekDepartTargetPackage;
import com.tsbg.mis.rookie.model.*;
import com.tsbg.mis.rookie.vo.DepartTargetSetInfoList;
import com.tsbg.mis.rookie.vo.WeekDepartTargetVo;
import com.tsbg.mis.service.rookie.DepartmentTargetService;
import com.tsbg.mis.serviceImpl.jurisdiction.base.TokenAnalysis;
import com.tsbg.mis.util.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DepartmentTargetServiceImpl implements DepartmentTargetService {

    private final Integer COACH_ROLE_ID = 14;

    private final Integer WEEK_TARGET_TYPE_ID = 1;

    private final Integer WEEK_GOAL_CAN_BE_MODIFIED = 4;

    @Autowired
    TokenAnalysis tokenAnalysis;

    @Autowired
    DepartmentTargetDao departmentTargetDao;

    @Autowired
    private UserRoleDao userRoleDao;

    @Autowired
    private ClassStudentInfoDao classStudentInfoDao;

    //查詢type_list表中所有數據
    @Override
    public ResultUtils queryAllType() {
        List<TypeList> typeLists = departmentTargetDao.queryAllType();
        if (typeLists != null) {
            return new ResultUtils(100, "查詢成功", typeLists);
        } else {
            return new ResultUtils(100, "查詢成功", new ArrayList<>());
        }
    }

    //根据工号查询员工信息
    @Override
    public ResultUtils queryStaffInfo() {
        //从token中取出员工工号
        String staffCode = tokenAnalysis.getTokenUser().getStaffCode();
        //再根据工号查询员工信息
        StaffInfo queryStaffInfo = departmentTargetDao.queryStaffInfo(staffCode);
        if (queryStaffInfo != null) {
            return new ResultUtils(100, "查詢成功", queryStaffInfo);
        } else {
            return new ResultUtils(100, "查詢成功", new ArrayList<>());
        }
    }

    //查询所有届别
    @Override
    public ResultUtils queryClassPeriod() {
        List<ReportCreationList> reportCreationLists = departmentTargetDao.queryClassPeriod();
        if (reportCreationLists != null) {
            return new ResultUtils(100, "查詢成功", reportCreationLists);
        } else {
            return new ResultUtils(100, "查詢成功", new ArrayList<>());
        }
    }

    //根据届别查询当前届别下的所有报告任务
    @Override
    public ResultUtils queryReportCreation(String classPeriod) {
        List<ReportCreationList> reportCreationLists = departmentTargetDao.queryReportCreation(classPeriod);
        if (reportCreationLists != null) {
            return new ResultUtils(100, "查詢成功", reportCreationLists);
        } else {
            return new ResultUtils(100, "查詢成功", new ArrayList<>());
        }
    }

    //根据查询当前设立人所在部门下的所有菁干班成员信息
    @Override
    public ResultUtils queryClassStudentInfo(ClassStudentInfo classStudentInfo) {
        List<ClassStudentInfo> classStudentInfos = new ArrayList<>();
        //从token取出当前登录人的工号
        String StaffCode = tokenAnalysis.getTokenUser().getStaffCode();
        //再根据工号查询当前登录人的事业群、事业处、部门、课等信息
        StaffInfo staffInfos = departmentTargetDao.queryStaffInfo(StaffCode);
        //循环遍历根据当前登录人的事业处去查询
        if (staffInfos != null) {
            classStudentInfo.setBuName(staffInfos.getBUName());
        }
        if (classStudentInfo != null) {
            classStudentInfos = departmentTargetDao.queryClassStudentInfo(classStudentInfo);
        }
        if (classStudentInfos != null) {
            return new ResultUtils(100, "查詢成功", classStudentInfos);
        } else {
            return new ResultUtils(100, "查詢成功", new ArrayList<>());
        }
    }

    //设立部门月、周目标
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @SysLog(value = "设立部门月、周目标")
    public ResultUtils insertDepartmentTargetInfo(DepartTargetSetInfoPackage departTargetSetInfoPackage) {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String dateString = format.format(date);
        String targetNum;
        //从token中获取当前登录用户的工号
        String StaffCode = tokenAnalysis.getTokenUser().getStaffCode();
        List<DepartTargetList> departTargetLists = departTargetSetInfoPackage.getDepartTargetList();
        List<DepartTargetStudent> departTargetStudents = departTargetSetInfoPackage.getDepartTargetStudent();
        List<RoleAndInfoPackage> roleAndInfoPackages = departmentTargetDao.queryUserRole(StaffCode);
        if (roleAndInfoPackages == null) {
            roleAndInfoPackages = new ArrayList<>();
        }
        Integer roleId = null;
        Integer departTargetStudentInfo = 0;
        //设置目标权重默认为100
        Integer targetWeight = 100;
        //创建一个临时变量用于存储
        for (RoleAndInfoPackage roleAndInfoPackage : roleAndInfoPackages) {
            roleId = roleAndInfoPackage.getRoleId();
        }
        if (departTargetLists == null) {
            return new ResultUtils(500, "新增失敗");
        }
        //创建一个集合将每次新增成功之后的单号存放在里面
        List<String> list = new ArrayList<>();
        if (roleId != null) {
            //判断用户角色如果是部门主管或者教练员才可以设立月、周目标
            if (roleId == 17 || roleId == 14) {
                //第一步先新增depart_target_set_info表
                int countTotalWeight = 0;
                for (int i = 0; i < departTargetLists.size(); i++) {
                    if (null == departTargetLists.get(i) ||
                            null == departTargetLists.get(i).getTargetWeight() ||
                            departTargetLists.get(i).getTargetWeight().isEmpty()) {
                        return new ResultUtils(500, "權重總和應為100%");
                    } else {
                        countTotalWeight += Integer.parseInt(departTargetLists.get(i).getTargetWeight());
                        if (i == departTargetLists.size() - 1 && countTotalWeight != targetWeight) {
                            return new ResultUtils(500, "權重總和應為100%");
                        }
                    }
                }
                //第二步新增depart_target_list表前为其设置信息
                for (DepartTargetList departTargetList : departTargetLists) {
                    //根据creationId查询对应报告任务的开始时间和结束时间
                    DepartTargetList departTargetList1 = departmentTargetDao.queryReportCreationList(departTargetList.getCreationId());
                    //根据查询出的报告任务的开始时间和结束时间将他设置为目标的生效时间和失效时间
                    if (departTargetList1 != null) {
                        departTargetList.setTargetStartDate(departTargetList1.getTargetStartDate());
                        departTargetList.setTargetEndDate(departTargetList1.getTargetEndDate());
                    }
                    //将创建和修改时间设置为当前时间
                    departTargetList.setCreateDate(date);
                    departTargetList.setUpdateDate(date);
                    //获取权重拼接一个%在权重的后面
                    departTargetList.setTargetWeight(departTargetList.getTargetWeight());
                }

                //遍历判断目标对象不为空
                if (departTargetStudents != null) {
                    for (DepartTargetStudent departTargetStudent : departTargetStudents) {
                        //如果先查询表中是否有目标单号
                        String lastTargetNum = departmentTargetDao.queryTargetNum();
                        //如果没有目标单号就给他设置一个默认值
                        if (lastTargetNum == null) {
                            lastTargetNum = "MT" + dateString + "000";
                        }
                        String lastTime = lastTargetNum.substring(2, 10),
                                lastNum = lastTargetNum.substring(10);
                        int lastNumInt = Integer.parseInt(lastNum);
                        if (lastNumInt >= 999) {
                            return new ResultUtils(500, "對不起，今天能設立的目標已到達上限，目標新增失敗");
                        }
                        if (lastTime.equals(dateString)) {
                            targetNum = "MT" + dateString + (lastNumInt < 100 ? (lastNumInt < 9 ? "00" + (lastNumInt + 1) : "0" + (lastNumInt + 1)) : (lastNumInt + 1) + "");
                        } else {
                            targetNum = "MT" + dateString + "001";
                        }
                        if (departTargetLists != null) {
                            for (DepartTargetList departTargetList : departTargetLists) {
                                departTargetList.setTargetNum(targetNum);
                                departTargetList.setExamineState(0);
                                //如果是部门月目标则直接进行新增、如果是周目标则月目标新增成功之后将周目标的pid修改成月目标的id
                                if (departTargetList.getTypeId() != null && departTargetList.getTypeId() == 4) {
                                    //月目标的Pid是0
                                    departTargetList.setPid(0);
                                    departmentTargetDao.insertDepartTargetLists(departTargetList);
                                } else {
                                    //取前端传过来的TargetId将周目标的Pid设置成月目标的主键id
                                    departTargetList.setPid(departTargetList.getTargetId());
                                    departTargetList.setExamineState(4);
                                    //如果是周目标则月目标新增成功之后将周目标的pid修改成月目标的id
                                    departmentTargetDao.insertDepartTargetLists(departTargetList);
                                }
                            }
                        }
                        //新增将创建时间和修改时间设为当前时间
                        departTargetStudent.setCreateDate(date);
                        departTargetStudent.setUpdateDate(date);
                        departTargetStudent.setTargetNum(targetNum);
                        list.add(targetNum);
                        departTargetStudentInfo = departmentTargetDao.insertDepartTargetStudent(departTargetStudent);
                    }
                }
            }
        } else {
            return new ResultUtils(500, "對不起，您無權限新增目標，請聯繫管理員添加權限");
        }
        if (null != departTargetStudentInfo && departTargetStudentInfo > 0) {
            return new ResultUtils(100, "新增成功", list);
        } else {
            return new ResultUtils(500, "新增失敗");
        }
    }

    //根据目标单号查询目标详情
    @Override
    public ResultUtils queryTargetDetailsInfo(DepartTargetSetInfoPackage departTargetSetInfoPackage) {
        if (departTargetSetInfoPackage != null) {
            List<String> targetNumList = departTargetSetInfoPackage.getTargetNumList();
            if (targetNumList != null) {
                List<DepartTargetList> departTargetLists = departmentTargetDao.queryTargetDetailsInfo(targetNumList);
                List<DepartTargetStudent> departTargetStudent = departmentTargetDao.queryTargetStudentInfo(targetNumList);
                departTargetSetInfoPackage.setDepartTargetList(departTargetLists);
                departTargetSetInfoPackage.setDepartTargetStudent(departTargetStudent);
            }
            return new ResultUtils(100, "查詢成功", departTargetSetInfoPackage);
        }else {
            return new ResultUtils(100, "查詢成功", new ArrayList<>());
        }
    }

    /*//根据targetId编辑目标
    @Override
    public ResultUtils updateTargetInfo(DepartTargetSetInfoPackage departTargetSetInfoPackage) {
        List<DepartTargetList> departTargetLists = departTargetSetInfoPackage.getDepartTargetList();
        int count = 0;
        if (departTargetLists != null) {
            for (DepartTargetList departTargetList : departTargetLists) {
                if (departTargetList.getTargetId() != null) {
                    departTargetList.setTargetWeight(departTargetList.getTargetWeight());
                    count = departmentTargetDao.updateTargetInfo(departTargetList);
                } else {
                    return new ResultUtils(500, "修改失敗");
                }
            }
        }
        if (count != 0) {
            return new ResultUtils(100, "修改成功");
        } else {
            return new ResultUtils(500, "修改失敗");
        }
    }*/

    //部门月目标查询
    @Override
    public ResultUtils queryDepartmentMonthTargetInfo() {
        //1获取当前登录人的信息
        String staffCodeInfo = tokenAnalysis.getTokenUser().getStaffCode();
        //2.根据当前登录人工号去查询登录人的处级单位名称BuName
        StaffInfo staffInfo = departmentTargetDao.queryStaffInfo(staffCodeInfo);
        if (staffInfo == null) {
            return new ResultUtils(500, "查无此人");
        }
        DepartTargetSetInfoList departTargetSetInfoList1 = new DepartTargetSetInfoList();
        departTargetSetInfoList1.setBuName(staffInfo.getBUName());
        List<DepartTargetSetInfoList> departTargetSetInfoListsss = new ArrayList<>();
        //3.根据当前登录人事业处下面的所有学生工号去查询报告任务
        List<DepartTargetSetInfoList> departTargetSetInfoLists = departmentTargetDao.selectMonthReportCreationList(departTargetSetInfoList1);
        if (departTargetSetInfoLists == null) {
            departTargetSetInfoLists = new ArrayList<>();
        }
        for (DepartTargetSetInfoList departTargetSetInfoListone : departTargetSetInfoLists) {
            //4.根据报告任务生效和失效时间去查询目标信息
            List<DepartTargetSetInfoList> departTargetSetInfoListstwo = departmentTargetDao.queryTargetInfo(departTargetSetInfoListone);
            if (departTargetSetInfoListstwo != null && !departTargetSetInfoListstwo.isEmpty()) {
                for (int i = 0; i < departTargetSetInfoListstwo.size(); i++) {
                    departTargetSetInfoListstwo.get(i).setInvestFieldValue(departTargetSetInfoListstwo.get(i).getTargetNum());
                    departTargetSetInfoListstwo.get(i).setInvestTableName("depart_target_list");
                    departTargetSetInfoListstwo.get(i).setInvestFieldName("target_num");
                    //5.根据目标信息去查询目标审核人信息
                    DepartTargetSetInfoList departTargetSetInfoListss = departmentTargetDao.queryApprovalInfo(departTargetSetInfoListstwo.get(i));
                    //6.当查询的目标信息不为空就把审核人信息添加到目标信息中
                    if (null != departTargetSetInfoListss) {
                        departTargetSetInfoListstwo.get(i).setApproveStaffCode(departTargetSetInfoListss.getApproveStaffCode());
                        departTargetSetInfoListstwo.get(i).setApproveStaffName(departTargetSetInfoListss.getApproveStaffName());
                        departTargetSetInfoListstwo.get(i).setSignedDate(departTargetSetInfoListss.getSignedDate());
                        departTargetSetInfoListsss.add(departTargetSetInfoListstwo.get(i));
                    }
                }
            } else {
                departTargetSetInfoListone.setExamineState(6);
                departTargetSetInfoListsss.add(departTargetSetInfoListone);
            }
        }
        //如果查询到数据就直接返回数据给页面
        departTargetSetInfoListsss.sort(new DepartTargetComparator());
        return new ResultUtils(100, "查詢成功", departTargetSetInfoListsss);

    }

    //部门周目标查询
    @Override
    public ResultUtils queryDepartmentWeekTargetInfo() {
        //1获取当前登录人的信息
        String staffCodeInfo = tokenAnalysis.getTokenUser().getStaffCode();
        //2.根据当前登录人工号去查询登录人的处级单位名称BuName
        StaffInfo staffInfo = departmentTargetDao.queryStaffInfo(staffCodeInfo);
        if (staffInfo == null) {
            return new ResultUtils(500, "查无此人");
        }
        DepartTargetSetInfoList departTargetSetInfoList1 = new DepartTargetSetInfoList();
        departTargetSetInfoList1.setBuName(staffInfo.getBUName());
        List<DepartTargetSetInfoList> departTargetSetInfoListsss = new ArrayList<>();
        //3.根据当前登录人事业处下面的所有学生工号去查询报告任务
        List<DepartTargetSetInfoList> departTargetSetInfoLists = departmentTargetDao.selectMonthReportCreationList(departTargetSetInfoList1);
        if (departTargetSetInfoLists == null) {
            departTargetSetInfoLists = new ArrayList<>();
        }
        for (DepartTargetSetInfoList departTargetSetInfoListone : departTargetSetInfoLists) {
            //4.根据报告任务生效和失效时间去查询目标信息
            List<DepartTargetSetInfoList> departTargetSetInfoListstwo = departmentTargetDao.queryTargetInfo(departTargetSetInfoListone);
            if (departTargetSetInfoListstwo != null && !departTargetSetInfoListstwo.isEmpty()) {
                for (int i = 0; i < departTargetSetInfoListstwo.size(); i++) {
                    departTargetSetInfoListstwo.get(i).setInvestFieldValue(departTargetSetInfoListstwo.get(i).getTargetNum());
                    departTargetSetInfoListstwo.get(i).setInvestTableName("depart_target_list");
                    departTargetSetInfoListstwo.get(i).setInvestFieldName("target_num");
                    //5.根据目标信息去查询目标审核人信息
                    DepartTargetSetInfoList departTargetSetInfoListss = departmentTargetDao.queryApprovalInfo(departTargetSetInfoListstwo.get(i));
                    //6.当查询的目标信息不为空就把审核人信息添加到目标信息中
                    if (null != departTargetSetInfoListss) {
                        departTargetSetInfoListstwo.get(i).setApproveStaffCode(departTargetSetInfoListss.getApproveStaffCode());
                        departTargetSetInfoListstwo.get(i).setApproveStaffName(departTargetSetInfoListss.getApproveStaffName());
                        departTargetSetInfoListstwo.get(i).setSignedDate(departTargetSetInfoListss.getSignedDate());
                        departTargetSetInfoListsss.add(departTargetSetInfoListstwo.get(i));
                    }
                }
            } else {
                departTargetSetInfoListone.setExamineState(6);
                departTargetSetInfoListsss.add(departTargetSetInfoListone);
            }
        }
        //过滤掉审核通过的月目标(0待审核；1审核通过（可修改）；2审核通过（不可修改）；3被驳回)
        List<DepartTargetSetInfoList> departTargetSetInfoListssss = departTargetSetInfoListsss.stream().filter(d -> (2 == d.getExamineState() || 1 == d.getExamineState())).collect(Collectors.toList());
        if (departTargetSetInfoListssss != null) {
            for (DepartTargetSetInfoList departTargetSetInfoList : departTargetSetInfoListssss) {
                if (departTargetSetInfoList.getExamineState() == 1) {
                    departTargetSetInfoList.setExamineState(11);
                }
                if (departTargetSetInfoList.getExamineState() == 2) {
                    departTargetSetInfoList.setExamineState(12);
                }
            }
        }
        if (departTargetSetInfoListssss != null) {
            departTargetSetInfoListsss.sort(new DepartTargetComparator());
            return new ResultUtils(100, "查詢成功", departTargetSetInfoListssss);
        } else {
            return new ResultUtils(100, "查詢成功", new ArrayList<>());
        }
    }


    //查询月报、周报
    @Override
    public ResultUtils queryMonthlyWeekReportInfo(ReportCreationList reportCreationList) {
        List<ReportCreationList> reportCreationLists = departmentTargetDao.queryMonthlyWeekReportInfo(reportCreationList);
        if (reportCreationLists != null) {
            return new ResultUtils(100, "查詢成功", reportCreationLists);
        } else {
            return new ResultUtils(100, "查詢成功", new ArrayList<>());
        }
    }


    @Override
    //批量編輯月目標
    @SysLog("編輯月目標")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ResultUtils updateMonthlyTarget(DepartTargetSetInfoPackage departTargetSetInfoPackage) throws Exception {
        if (departTargetSetInfoPackage == null) {
            return new ResultUtils(500, "参数错误");
        }
        Date date = new Date();
        List<DepartTargetList> departTargetLists = departTargetSetInfoPackage.getDepartTargetList();
        List<DepartTargetStudent> departTargetStudents = departTargetSetInfoPackage.getDepartTargetStudent();
        List<String> targetNumList = new ArrayList<>();
        Integer totalWeight = 0;
        if (null == departTargetLists || departTargetLists.isEmpty()) {
            return new ResultUtils(500, "目標列表不能為空");
        }
        if (null == departTargetStudents || departTargetStudents.isEmpty()) {
            return new ResultUtils(500, "學生不能為空");
        }
        for (DepartTargetList departTargetList : departTargetLists) {
            String targetWeight = departTargetList.getTargetWeight();
            if (null != targetWeight) {
                Integer weightInt = Integer.parseInt(targetWeight);
                totalWeight += weightInt;
            }
        }
        if (totalWeight != 100) {
            return new ResultUtils(500, "權重總和應為100%");
        }
        for (DepartTargetStudent departTargetStudent : departTargetStudents) {
            departTargetStudent.setUpdateDate(date);
            Integer status = departTargetStudent.getStatus();
            String targetNum = departTargetStudent.getTargetNum();

            if (null != status && status == 0) {//如果是編輯後刪除的學生
                //則直接把目標信息和學生關聯表信息刪除
                departmentTargetDao.deleteTargetInfo(targetNum);//把目标单号下的所有目标先删除
                departmentTargetDao.deleteStudentTarget(targetNum);//删除目标学生关联表
            } else {//如果是被編輯的學生(未刪除的)/或者新增的學生(status = null)
                //只刪除已保存的目標信息
                if (targetNum != null) {
                    departmentTargetDao.deleteTargetInfo(targetNum);//把目标单号下的所有目标先删除
                }
                //再重新添加，如果是原来就有的学生，单号不变，新加的学生则重新生成单号
                for (DepartTargetList departTargetList : departTargetLists) {
                    departTargetList.setPid(0);
                    departTargetList.setUpdateDate(date);
                    departTargetList.setCreateDate(date);
                    //根据creationId查询对应报告任务的开始时间和结束时间
                    DepartTargetList departTargetList1 = departmentTargetDao.queryReportCreationList(departTargetList.getCreationId());
                    //根据查询出的报告任务的开始时间和结束时间将他设置为目标的生效时间和失效时间
                    if (departTargetList1 != null) {
                        departTargetList.setTargetStartDate(departTargetList1.getTargetStartDate());
                        departTargetList.setTargetEndDate(departTargetList1.getTargetEndDate());
                    }
                    if (targetNum == null) {//没有targetNum的是新加的学生
                        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
                        String dateString = format.format(date);
                        //如果先查询表中是否有目标单号
                        String lastTargetNum = departmentTargetDao.queryTargetNum();
                        //如果没有目标单号就给他设置一个默认值
                        if (lastTargetNum == null) {
                            lastTargetNum = "MT" + dateString + "000";
                        }
                        String lastTime = lastTargetNum.substring(2, 10),
                                lastNum = lastTargetNum.substring(10);
                        int lastNumInt = Integer.parseInt(lastNum);
                        if (lastNumInt >= 999) {
                            return new ResultUtils(500, "對不起，今天能設立的目標已到達上限，目標新增失敗");
                        }
                        if (lastTime.equals(dateString)) {
                            targetNum = "MT" + dateString + (lastNumInt < 100 ? (lastNumInt < 9 ? "00" + (lastNumInt + 1) : "0" + (lastNumInt + 1)) : (lastNumInt + 1) + "");
                        } else {
                            targetNum = "MT" + dateString + "001";
                        }
                        departTargetList.setTargetNum(targetNum);
                        departTargetList.setExamineState(0);
                        Integer typeId = departTargetList.getTypeId();
                        //如果是部门月目标则直接进行新增、如果是周目标则月目标新增成功之后将周目标的pid修改成月目标的id
                        if (null == typeId) {
                            throw new Exception("typeId不能為空");
                        }
                        if (typeId == 4) {
                            //月目标的Pid是0
                            departTargetList.setPid(0);
                            Integer count1 = departmentTargetDao.insertDepartTargetLists(departTargetList);
                            if (null == count1 || count1 <= 0) {
                                throw new Exception("編輯目標失敗");
                            }
                        } else {
                            //取前端传过来的TargetId将周目标的Pid设置成月目标的主键id
                            departTargetList.setPid(departTargetList.getTargetId());
                            //如果是周目标则月目标新增成功之后将周目标的pid修改成月目标的id
                            Integer count1 = departmentTargetDao.insertDepartTargetLists(departTargetList);
                            if (null == count1 || count1 <= 0) {
                                throw new Exception("編輯目標失敗");
                            }
                        }
                        departTargetStudent.setCreateDate(date);
                        departTargetStudent.setUpdateDate(date);
                        departTargetStudent.setTargetNum(targetNum);
                        Integer insertStudentCount = departmentTargetDao.insertDepartTargetStudent(departTargetStudent);
                        if (null == insertStudentCount || insertStudentCount <= 0) {
                            throw new Exception("添加学生失败");
                        }
                    } else {//有targetNum的是原来就有的学生

                        DepartTargetList targetCreateDate = departmentTargetDao.selectCreateDateByTargetNum(targetNum);
                        Date createDate = targetCreateDate.getCreateDate();

                        departTargetList.setCreateDate(createDate);
                        departTargetList.setTargetNum(targetNum);
                        departTargetList.setExamineState(0);
                        Integer count = departmentTargetDao.insertDepartTargetLists(departTargetList);
                        if (null == count || count <= 0) {
                            throw new Exception("編輯目標失敗");
                        }
                    }
                }
                targetNumList.add(targetNum);
            }
        }
        return new ResultUtils(101, "編輯成功", targetNumList);
    }

    //查询是否已创建周目标信息
    @Override
    public ResultUtils queryCreateTargetInfo(WeekDepartTargetPackage weekDepartTargetPackage) {
        //根据creationId查询对应的月报的时间
        DepartTargetList departTargetLists = departmentTargetDao.queryReportCreationInfo(weekDepartTargetPackage);
        //根据月报的时间查询周报
        List<WeekDepartTargetVo> weekDepartTargetVoList = departmentTargetDao.queryWeekReportInfo(departTargetLists);
        if (weekDepartTargetVoList != null) {
            // 根据targetNum 和 creationId 查询周目标
            List<String> targetNumList = weekDepartTargetPackage.getTargetNumList();
            for (WeekDepartTargetVo weekDepartTargetVo : weekDepartTargetVoList) {
                Integer creationId = weekDepartTargetVo.getCreationId();
                List<DepartTargetList> targetLists = departmentTargetDao.queryWeekTargetExist(weekDepartTargetPackage.getCreationId(), creationId, targetNumList);
                if (targetLists == null) {
                    targetLists = new ArrayList<>();
                }
                // 存在周目标
                if (targetLists != null && !targetLists.isEmpty()) {
                    weekDepartTargetVo.setExamineState(targetLists.get(0).getExamineState());
                    weekDepartTargetVo.setDepartTargetLists(targetLists);
                }
                if (targetLists.isEmpty()) {
                    weekDepartTargetVo.setExamineState(6);
                }
            }
        }
        return new ResultUtils(100, "成功查詢週目標列表", weekDepartTargetVoList);
    }

    /**
     * 单个创建周目标
     *
     * @param departTargetSetInfoPackage
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ResultUtils createWeekTarget(DepartTargetSetInfoPackage departTargetSetInfoPackage) {
        String studentStaffCode = departTargetSetInfoPackage.getDepartTargetStudent().get(0).getStaffCode();
        List<DepartTargetList> departTargetList = departTargetSetInfoPackage.getDepartTargetList();
        log.info("要给 -> {}, 创建周目标 -> {}", studentStaffCode, departTargetList);

        // 校验当前登录人是否能创建周目标
        UserInfo currentUser = tokenAnalysis.getTokenUser();
        List<Integer> roleIds = userRoleDao.selectRoleId(currentUser.getUserId());
        if (roleIds == null || CollectionUtils.isEmpty(roleIds)) {
            return new ResultUtils(500, "該用戶沒有角色");
        }
        if (!roleIds.contains(COACH_ROLE_ID)) {
            return new ResultUtils(500, "非教練員不能創建週目標");
        }
        // 根据 student_staff_code 去 class_student_info 查询该菁干班成员的所有领导
        ClassStudentInfo c = new ClassStudentInfo();
        c.setStaffCode(studentStaffCode);
        ClassStudentInfo classStudentInfo = classStudentInfoDao.selectAll(c).get(0);
        if (!currentUser.getStaffCode().equals(classStudentInfo.getCoachCode())) {
            return new ResultUtils(500, "您不是該菁幹班成員的教練員，不能創建週目標");
        }
        // 生成的是不带-的字符串，类似于：b17f24ff026d40949c85a24f4f375d42
        String simpleUUID = IdUtil.simpleUUID();
        log.info("此学生 -> {} 的周目标单号是 -> {}", studentStaffCode, simpleUUID);
        // 插入目标
        for (DepartTargetList departTarget : departTargetList) {
            departTarget.setTargetNum(simpleUUID);
            departTarget.setStaffCode(currentUser.getStaffCode());
            departTarget.setClassPeriod(classStudentInfo.getClassPeriod());
            departTarget.setTypeId(WEEK_TARGET_TYPE_ID);
            departTarget.setEndDate(new Date());
            departTarget.setCreateDate(new Date());
            departTarget.setUpdateDate(new Date());
            departTarget.setExamineState(WEEK_GOAL_CAN_BE_MODIFIED);
            departmentTargetDao.insertDepartTargetLists(departTarget);
        }
        departmentTargetDao.insertDepartTargetStudent(DepartTargetStudent.builder()
                .staffCode(studentStaffCode)
                .targetNum(simpleUUID)
                .createDate(new Date())
                .updateDate(new Date())
                .build());

        // 修改月目标的状态
        String monthTargetNum = departTargetSetInfoPackage.getMonthTargetNum();
        departmentTargetDao.updateMonthTargetState(monthTargetNum);
        return new ResultUtils(101, "成功創建週目標");
    }

    /**
     * 修改周目标
     *
     * @param departTargetSetInfoPackage
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ResultUtils updateWeekTarget(DepartTargetSetInfoPackage departTargetSetInfoPackage) throws Exception {
        String weekTargetNum = departTargetSetInfoPackage.getWeekTargetNum();
        // 删除之前的周目标
        departmentTargetDao.deleteTargetInfo(weekTargetNum);

        // 校验当前登录人是否能创建周目标
        UserInfo currentUser = tokenAnalysis.getTokenUser();
        List<Integer> roleIds = userRoleDao.selectRoleId(currentUser.getUserId());
        if (roleIds == null || CollectionUtils.isEmpty(roleIds)) {
            return new ResultUtils(500, "該用戶沒有角色");
        }
        if (!roleIds.contains(COACH_ROLE_ID)) {
            return new ResultUtils(500, "非教練員不能創建週目標");
        }

        // 新增周目标
        List<DepartTargetList> departTargetList = departTargetSetInfoPackage.getDepartTargetList();
        if (null == departTargetList || departTargetList.isEmpty()) {
            throw new Exception("departTargetList不能為空");
        }
        // 根据 student_staff_code 去 class_student_info 查询该菁干班成员的所有领导
        ClassStudentInfo c = new ClassStudentInfo();
        c.setStaffCode(departTargetSetInfoPackage.getStudentStaffCode());
        ClassStudentInfo classStudentInfo = null;
        List<ClassStudentInfo> classStudentInfos = classStudentInfoDao.selectAll(c);
        if (null != classStudentInfos && !classStudentInfos.isEmpty()) {
            classStudentInfo = classStudentInfos.get(0);
            if (!currentUser.getStaffCode().equals(classStudentInfo.getCoachCode())) {
                return new ResultUtils(500, "您不是該菁幹班成員的教練員，不能創建週目標");
            }
        }
        if (classStudentInfo == null) {
            return new ResultUtils(500, "該菁幹班成員信息為空");
        }
        for (DepartTargetList departTarget : departTargetList) {
            departTarget.setTargetNum(weekTargetNum);
            departTarget.setStaffCode(currentUser.getStaffCode());
            departTarget.setClassPeriod(classStudentInfo.getClassPeriod());
            departTarget.setTypeId(WEEK_TARGET_TYPE_ID);
            departTarget.setEndDate(new Date());
            departTarget.setCreateDate(new Date());
            departTarget.setUpdateDate(new Date());
            departTarget.setExamineState(WEEK_GOAL_CAN_BE_MODIFIED);
            departmentTargetDao.insertDepartTargetLists(departTarget);
        }
        return new ResultUtils(101, "成功修改週目標");
    }
}
