package com.tsbg.mis.serviceImpl.rookie;

import com.alibaba.fastjson.JSONObject;
import com.tsbg.mis.annotation.SysLog;
import com.tsbg.mis.dao.rookie.ProductionLineExamineDao;
import com.tsbg.mis.dao.rookie.ReportCreationListDao;
import com.tsbg.mis.jurisdiction.model.UserInfo;
import com.tsbg.mis.rookie.model.ExamineInternshipGrade;
import com.tsbg.mis.rookie.model.ProductionLineList;
import com.tsbg.mis.rookie.model.ReportCreationList;
import com.tsbg.mis.rookie.model.ReportProductionWeekQuestion;
import com.tsbg.mis.rookie.vo.*;
import com.tsbg.mis.service.rookie.ProductionLineExamineService;
import com.tsbg.mis.service.rookie.ReportCreationListService;
import com.tsbg.mis.serviceImpl.jurisdiction.base.TokenAnalysis;
import com.tsbg.mis.util.ResultUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author :张梦雅
 * @description :
 * @create :2020-07-22 15:05:00
 */
@Service
@Slf4j
public class ProductionLineExamineServiceImpl implements ProductionLineExamineService {
    @Autowired
    TokenAnalysis tokenAnalysis;
    @Autowired
    ProductionLineExamineDao productionLineExamineDao;
    @Autowired
    ReportCreationListDao reportCreationListDao;
    @Autowired
    ReportCreationListService reportCreationListService;


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ResultUtils inquireStudentscalendar() {
        //获取当前用户信息
        UserInfo currentUser = tokenAnalysis.getTokenUser();
        String staffCode = currentUser.getStaffCode();
        log.info("获取当前用户信息 ->{}",currentUser);
        List<ProductionLineExamineVo> list = new ArrayList<>();//返回前端结果集
        //判断当前用户的角色
        ArrayList<Integer>  userRoleList = productionLineExamineDao.queryUserRole(staffCode);
        log.info("当前用户角色 ->{}",userRoleList);
        //从任务表找到新任务
        List<ReportCreationList> enableReportList = reportCreationListDao.selectEnableReport();
        if (enableReportList == null || enableReportList.isEmpty()) {
            return new ResultUtils(100, "沒有可以啟用任務", enableReportList);
        }
        for (ReportCreationList reportCreationList : enableReportList) {
            Integer creationId = reportCreationList.getCreationId();
            //新任务里有产线实习月度考核
            log.info("找到产线实习月度考核的可用任务 ->{}",reportCreationList);
            for (Integer roleId : userRoleList) {
                if (roleId == 11) { //当前用户为产线组长
                    //找到产线组长旗下所有菁干班成员
                    ArrayList<ExamineStudentVo> studentList = selectLeaderUnderStudents(staffCode);
                    log.info("当前产线组长旗下所有菁干班成员 ->{}",studentList);
                    if (studentList != null && !studentList.isEmpty()) {
                        for (ExamineStudentVo examineStudentVo : studentList) {
                            List<ExamineInternshipGrade> internshipGradeList = productionLineExamineDao.selectPendingApproval(creationId, examineStudentVo.getStaffCode());
                            log.info("当前角色(组长)旗下菁干班成员在考核评分记录表的数据 ->{}",internshipGradeList);
                            ProductionLineExamineVo productionLineExamineVo = getStudentExamineRusult(reportCreationList.getCreationId(),reportCreationList, internshipGradeList, examineStudentVo.getStaffCode(),examineStudentVo.getStaffName());
                            if(productionLineExamineVo.getMissionTypeName() != null &&  !"".equals(productionLineExamineVo.getMissionTypeName()) ){
                                list.add(productionLineExamineVo);
                            }
                        }
                    }
                } else if (roleId == 12) { //当前用户为产线负责人
                    //找到产线负责人下所有菁干班成员
                    ArrayList<ExamineStudentVo> studentList = selectManagerUnderStudents(staffCode);
                    log.info("当前角色为产线负责人,旗下菁干班所有成员 ->",studentList);
                    if (studentList != null && !studentList.isEmpty()) {
                        for (ExamineStudentVo examineStudentVo : studentList) {
                            List<ExamineInternshipGrade> internshipGradeList = productionLineExamineDao.selectPendingApprovalForManager(creationId,examineStudentVo.getStaffCode());
                            log.info("当前角色(产线负责人)旗下菁干班成员在考核评分记录表的数据 ->{}",internshipGradeList);
                            ProductionLineExamineVo productionLineExamineVo = getStudentExamineRusultForManager(creationId,reportCreationList, internshipGradeList, examineStudentVo.getStaffCode(),examineStudentVo.getStaffName());
                            if(productionLineExamineVo.getMissionTypeName() != null &&  !"".equals(productionLineExamineVo.getMissionTypeName()) ){
                                list.add(productionLineExamineVo);
                            }
                        }
                    }
                }

            }
        }
        return new ResultUtils(100, "成功查詢所有信息", list);
    }

    private Boolean screenData(ProductionLineExamineVo productionLineExamineVo) {
        Boolean isFillingInTime = false;
        Date startTime = productionLineExamineVo.getStartTime();
        Date endTime = productionLineExamineVo.getEndTime();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = sdf.parse(sdf.format(date));
            if(startTime != null ){
                if(endTime != null){
                    if (date.getTime() >= startTime.getTime() && date.getTime() <= endTime.getTime()) {
                        isFillingInTime = true;
                    }
                }else {
                    if (date.getTime() >= startTime.getTime()) {
                        isFillingInTime = true;
                    }
                }

            }


        } catch (ParseException e) {
            e.printStackTrace();
        }


        return isFillingInTime;
    }

    //考核评分列表
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ResultUtils inquireStudent() {
        //获取当前用户信息
        UserInfo currentUser = tokenAnalysis.getTokenUser();
        String staffCode = currentUser.getStaffCode();
        log.info("当前用户信息 ->{}",currentUser);


        List<ProductionLineExamineVo> list = new ArrayList<>();//返回前端结果集
        List<ExamineInternshipGrade> internshipGradeList = new ArrayList<>();//考核表记录数据
        //判断当前用户的角色
        ArrayList<ExamineStudentVo> studentList = new ArrayList<>();
        ArrayList<Integer>  userRoleList = productionLineExamineDao.queryUserRole(staffCode);
        log.info("当前用户角色 ->{}",userRoleList);
        //从任务表找到新任务
        List<ReportCreationList> enableReportList = reportCreationListDao.selectEnableReport();
        if (enableReportList == null || enableReportList.isEmpty()) {
            return new ResultUtils(100, "沒有可啟用任務", enableReportList);
        }
        for (ReportCreationList reportCreationList : enableReportList) {
            log.info("找到产线实习月度考核的可用任务 ->{}",reportCreationList);
            Integer creationId = reportCreationList.getCreationId();
            //新任务里有产线实习月度考核
            for (Integer roleId : userRoleList) {
                if (roleId == 11) { //当前用户为产线组长
                    //找到产线组长旗下所有菁干班成员
                    studentList = selectLeaderUnderStudents(staffCode);
                    log.info("当前角色(产线组长)旗下菁干班成员 ->{}",studentList);
                    if (studentList != null && !studentList.isEmpty()) {
                        for (ExamineStudentVo examineStudentVo : studentList) {
                            //  根据角色,如果是组长,在待批人员就是实习期考核细项评分结果表里无数据人员
                            internshipGradeList = productionLineExamineDao.selectPendingApproval(creationId, examineStudentVo.getStaffCode());
                            log.info("当前角色(组长)旗下菁干班成员在考核评分记录表的数据: ->{}",internshipGradeList);
                            ProductionLineExamineVo productionLineExamineVo = getStudentExamineRusult(creationId,reportCreationList, internshipGradeList, examineStudentVo.getStaffCode(),examineStudentVo.getStaffName());
                            Boolean isFillingInTime = screenData(productionLineExamineVo);//用当前时间来筛选在开放填写时间内
                            if(isFillingInTime){//在任务填写时间内则展示
                                if(productionLineExamineVo.getMissionTypeName() != null &&  !"".equals(productionLineExamineVo.getMissionTypeName()) ){
                                    list.add(productionLineExamineVo);
                                }
                            }

                        }
                    }
                }else if (roleId == 12) { //当前用户为产线负责人
                    //找到产线负责人下所有菁干班成员
                    studentList = selectManagerUnderStudents(staffCode);
                    log.info("当前角色(产线负责人)旗下菁干班成员 ->{}",studentList);
                    if (studentList != null && !studentList.isEmpty()) {
                        for (ExamineStudentVo examineStudentVo : studentList) {
                            internshipGradeList = productionLineExamineDao.selectPendingApprovalForManager(creationId,examineStudentVo.getStaffCode());
                            log.info("当前角色(产线负责人)旗下菁干班成员在考核评分记录表的数据: ->{}",internshipGradeList);
                            ProductionLineExamineVo productionLineExamineVo = getStudentExamineRusultForManager(creationId,reportCreationList, internshipGradeList, examineStudentVo.getStaffCode(),examineStudentVo.getStaffName());
                            Boolean isFillingInTime = screenData(productionLineExamineVo);//用当前时间来筛选在开放填写时间内
                            if(isFillingInTime){//在任务填写时间内则展示
                                if(productionLineExamineVo.getMissionTypeName() != null &&  !"".equals(productionLineExamineVo.getMissionTypeName()) ){
                                    list.add(productionLineExamineVo);
                                }
                            }

                        }
                    }
                }else if (roleId == 18){//人资
                    //找最新一届菁干班的同学
                    studentList = productionLineExamineDao.selectNewStudents();
                    log.info("当前角色(人资)旗下菁干班成员人数 ->{}",studentList.size());
                    if (studentList != null && !studentList.isEmpty()) {
                        for (ExamineStudentVo examineStudentVo : studentList) {
                            internshipGradeList = productionLineExamineDao.selectPendingApprovalForHR(creationId,examineStudentVo.getStaffCode());
                            log.info("当前角色(人资)旗下菁干班成员在考核评分记录表的数据: ->{}",internshipGradeList);
                            ProductionLineExamineVo productionLineExamineVo = getStudentExamineRusultForHR(reportCreationList.getCreationId(),reportCreationList, internshipGradeList, examineStudentVo.getStaffCode(),examineStudentVo.getStaffName());
                            Boolean isFillingInTime = screenData(productionLineExamineVo);//用当前时间来筛选在开放填写时间内
                            if(isFillingInTime){//在任务填写时间内则展示
                                if(productionLineExamineVo.getMissionTypeName() != null &&  !"".equals(productionLineExamineVo.getMissionTypeName()) ){
                                    list.add(productionLineExamineVo);
                                }
                            }
                        }
                    }
                }
            }

        }
        return new ResultUtils(100, "成功查詢所有信息", list);
    }

    private ProductionLineExamineVo getStudentExamineRusultForHR(Integer creationId, ReportCreationList reportCreationList, List<ExamineInternshipGrade> internshipGradeList, String staffCode, String staffName) {
        ProductionLineExamineVo productionLineExamineVo ;
        if(internshipGradeList.size() == 0){
            //角色为人资时,如果考核记录表中无此同学记录,就视为"待评"
            productionLineExamineVo =ProductionLineExamineVo.builder()
                    .studentName(staffName)
                    .stateId(1)
                    .stateName("待評")
                    .missionType(98)
                    .missionTypeName("產線實習月度考核")
                    .creationId(reportCreationList.getCreationId())
                    .startTime(reportCreationList.getStartTime())
                    .endTime(reportCreationList.getEndTime())
                    .isEnable(reportCreationList.getIsEnable())
                    .studentCode(staffCode)
                    .build();
        }else {
            productionLineExamineVo =ProductionLineExamineVo.builder()
                    .studentName(staffName)
                    .stateId(2)
                    .stateName("已評")
                    .missionType(98)
                    .missionTypeName("產線實習月度考核")
                    .creationId(reportCreationList.getCreationId())
                    .startTime(reportCreationList.getStartTime())
                    .endTime(reportCreationList.getEndTime())
                    .isEnable(reportCreationList.getIsEnable())
                    .studentCode(staffCode)
                    .build();
        }
        return productionLineExamineVo;
    }

    //找出产线负责人旗下所有菁干班学生人员名单
    private ArrayList<ExamineStudentVo> selectManagerUnderStudents(String staffCode) {
        ArrayList<ExamineStudentVo> examineStudentVos = new ArrayList<>();
        //找出菁干班同学所对应的最新的产线信息
        List<NewLineForStudentsVo> newLineForStudentsList = productionLineExamineDao.selectNewLineForStudents();
        if(newLineForStudentsList != null && newLineForStudentsList.size() > 0){
            for (NewLineForStudentsVo newLineForStudentsVo : newLineForStudentsList) {
                if(staffCode.equals(newLineForStudentsVo.getLineManageCode())){
                    ExamineStudentVo examineStudentVo = new ExamineStudentVo();
                    examineStudentVo.setStaffCode(newLineForStudentsVo.getStaffCode());
                    examineStudentVo.setStaffName(newLineForStudentsVo.getStaffName());
                    examineStudentVos.add(examineStudentVo);
                }
            }
        }
        return examineStudentVos;
    }

    //查找产线组长旗下菁干班学生人员名单
    private ArrayList<ExamineStudentVo> selectLeaderUnderStudents(String staffCode) {
        ArrayList<ExamineStudentVo> examineStudentVos = new ArrayList<>();
        //找出菁干班同学所对应的最新的产线信息
        List<NewLineForStudentsVo> newLineForStudentsList = productionLineExamineDao.selectNewLineForStudents();
        if(newLineForStudentsList != null && newLineForStudentsList.size() > 0){
            for (NewLineForStudentsVo newLineForStudentsVo : newLineForStudentsList) {
                if(staffCode.equals(newLineForStudentsVo.getLineGroupLeaderCode())){
                    ExamineStudentVo examineStudentVo = new ExamineStudentVo();
                    examineStudentVo.setStaffCode(newLineForStudentsVo.getStaffCode());
                    examineStudentVo.setStaffName(newLineForStudentsVo.getStaffName());
                    examineStudentVos.add(examineStudentVo);
                }
            }
        }
        return examineStudentVos;

    }

    private ProductionLineExamineVo getStudentExamineRusultForManager(int creationId,ReportCreationList reportCreationList, List<ExamineInternshipGrade> internshipGradeList, String studentStaffCode,String studentStaffName) {
        ProductionLineExamineVo productionLineExamineVo =ProductionLineExamineVo.builder().build();
        int isNoReExamine = 0;//初评次数
        int isReExamine = 0;//复评次数
        if (internshipGradeList != null && !internshipGradeList.isEmpty()){
            for (ExamineInternshipGrade examineInternshipGrade : internshipGradeList) {
                if(examineInternshipGrade.getCreationId() == creationId && examineInternshipGrade.getExamineTypeId() == 3
                        && examineInternshipGrade.getIsReExamine() == 0){
                    //必须符合以上条件才算已初评
                    isNoReExamine = isNoReExamine + 1;
                }else if(examineInternshipGrade.getCreationId() == creationId && examineInternshipGrade.getExamineTypeId() == 3
                        && examineInternshipGrade.getIsReExamine() == 1){
                    isReExamine = isReExamine +1;
                }
            }
            if(isNoReExamine > 0 && isReExamine == 0){
                productionLineExamineVo =ProductionLineExamineVo.builder()
                        .studentName(studentStaffName)
                        .stateId(1)
                        .stateName("待評")
                        .missionType(98)
                        .missionTypeName("產線實習月度考核")
                        .creationId(reportCreationList.getCreationId())
                        .startTime(reportCreationList.getStartTime())
                        .endTime(reportCreationList.getEndTime())
                        .isEnable(reportCreationList.getIsEnable())
                        .studentCode(studentStaffCode)
                        .build();
            }
            if(isNoReExamine > 0 && isReExamine > 0){
                //产线负责人已评人员 ->初评次数 大于0 ,复评次数大于0
                productionLineExamineVo =ProductionLineExamineVo.builder()
                        .studentName(studentStaffName)
                        .stateId(2)
                        .stateName("已評")
                        .missionType(98)
                        .missionTypeName("產線實習月度考核")
                        .creationId(reportCreationList.getCreationId())
                        .startTime(reportCreationList.getStartTime())
                        .endTime(reportCreationList.getEndTime())
                        .isEnable(reportCreationList.getIsEnable())
                        .studentCode(studentStaffCode)
                        .build();
            }

        }
        return productionLineExamineVo;
    }

    /**
     * 产线实习 带出菁干班个人信息 实习内容及评语
     * 一个结果分为4个部分展示,分别为
     * 学生信息
     * 实习内容和评语信息
     * 产线组长个人潜力考核信息
     * 产线负责人个人潜力考核信息
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ResultUtils getStudentInformation(JSONObject json) {
        log.info("产线实习,个人潜力信息展示 入参 ->{}",json);
        String staffCode = tokenAnalysis.getTokenUser().getStaffCode();
        String studentStaffName = json.getString("studentStaffName") ;
        String studentStaffCode = json.getString("studentStaffCode") ;
        Integer creationId = 0;
        List<ReportCreationList> enableReportList = reportCreationListDao.selectEnableReport();
        if (enableReportList == null || enableReportList.isEmpty()) {
            return new ResultUtils(100, "沒有可啟用任務", enableReportList);
        }
        for (ReportCreationList reportCreationList : enableReportList) {
            if (reportCreationList.getMissionType() == 98) {
                creationId = reportCreationList.getCreationId();
            }
        }

        ProductionLineAndPotentialData productionLineAndPotentialData = new ProductionLineAndPotentialData();
        //判断当前用户角色
        ArrayList<Integer>  userRoleList = productionLineExamineDao.queryUserRole(staffCode);
        log.info("当前用户角色 ->{}",userRoleList);
        for (Integer roleId : userRoleList) {
            if (roleId == 11 || roleId == 12 || roleId == 18) {//当前用户为产线组长或者负责人
                //获取学生信息 及最新产线信息
                List<StudentInformationVo> studentInformationVoList = productionLineExamineDao.queryStudentInformation(staffCode,studentStaffCode,studentStaffName);

                log.info("产线实习,个人潜力页面,返回学生信息 ->{}",studentInformationVoList);
                InternshipCommentInformationVo internshipCommentInformationVo = InternshipCommentInformationVo.builder().build();
                //获取月报得分
                String monthlyReportGrade = "0";
                List<String> monthlyReportGradeList  = productionLineExamineDao.getDataFromExamine(creationId,studentStaffCode,0,6);
                if(monthlyReportGradeList.size() == 0){//如果考核记录表里没有此人记录就去月报表里去查
                    List<String> monthlyReportGrade1 = productionLineExamineDao.getMonthlyReportGrade(studentStaffCode);
                    if(monthlyReportGrade1.size() > 0){
                        monthlyReportGrade = String.valueOf(Double.valueOf(monthlyReportGrade1.get(0)) * 0.5);
                    }

                }
                log.info("产线实习,个人潜力页面,返回月报得分 ->{}",monthlyReportGradeList);
                if(monthlyReportGradeList.size() != 0){
                    monthlyReportGrade = monthlyReportGradeList.get(0);
                }

                internshipCommentInformationVo.setMonthlyReportGrade(monthlyReportGrade);
                //提案改善报告得分
                String proposalReportGrade = "0";
                List<String> dataFromExamine = productionLineExamineDao.getDataFromExamine(creationId, studentStaffCode,0, 7);
                if(dataFromExamine.size() == 0){//如果考核记录表里没有此人记录就去提案改善表里去查
                    List<String> proposalReportGrade1 = productionLineExamineDao.getProposalReportGrade(studentStaffCode);
                    if(proposalReportGrade1.size() > 0){
                        proposalReportGrade = String.valueOf(Double.valueOf(proposalReportGrade1.get(0)) * 0.1);
                    }

                }else {
                    proposalReportGrade = dataFromExamine.get(0);
                }
                log.info("产线实习,个人潜力页面,返回提案改善报告得分 ->{}",proposalReportGrade);

                internshipCommentInformationVo.setProposalReportGrade(proposalReportGrade);
                //实习产线总得分
                String productionLineInternshipGrade = "0";
                List<String> productionLineInternshipGradeList = productionLineExamineDao.getDataFromExamine(creationId,studentStaffCode,0,2);
                log.info("产线实习,个人潜力页面,返回实习产线总得分 ->{}",productionLineInternshipGrade);
                if(productionLineInternshipGradeList.size() > 0){
                    productionLineInternshipGrade = productionLineInternshipGradeList.get(0);
                }
                internshipCommentInformationVo.setProductionLineInternshipGrade(productionLineInternshipGrade);
                //获取实习内容和评语信息
                InternshipCommentInformationVo internshipCommentInformationVo2 = productionLineExamineDao.queryInternshipCommentInformation(creationId,staffCode,studentStaffCode,studentStaffName);
                log.info("产线实习,个人潜力页面,返回实习内容和评语信息 ->{}",internshipCommentInformationVo2);
                if(internshipCommentInformationVo2 != null){
                    internshipCommentInformationVo.setInternshipComment(internshipCommentInformationVo2.getInternshipComment());
                    internshipCommentInformationVo.setComment(internshipCommentInformationVo2.getComment());
                }

                //产线组长个人潜力考核信息
                PersonalPotentialByLineGroupLeaderVo personalPotentialByLineGroupLeaderVo = new PersonalPotentialByLineGroupLeaderVo();
                    List<ExamineInternshipGrade> examineInternshipGradeList = productionLineExamineDao.queryPersonalPotentialByLineGroupLeader(creationId,staffCode,studentStaffCode,studentStaffName);
                for (ExamineInternshipGrade examineInternshipGrade : examineInternshipGradeList) {
                    if(examineInternshipGrade.getExamineTypeId()==3){//初评总得分
                        personalPotentialByLineGroupLeaderVo.setPotentialGradeByLineGroupLeader(examineInternshipGrade.getExamineGrade());
                    }else if (examineInternshipGrade.getExamineTypeId() == 10 || examineInternshipGrade.getExamineTypeId() == 11
                            || examineInternshipGrade.getExamineTypeId() == 12 || examineInternshipGrade.getExamineTypeId() == 13){//组长品行信用评分
                        ConductCredit conductCredit = ConductCredit.builder()
                                .examineTypeId(examineInternshipGrade.getExamineTypeId())
                                .grade(examineInternshipGrade.getExamineGrade())
                                .build();
                        personalPotentialByLineGroupLeaderVo.setConductCredit(conductCredit);
                    }else if (examineInternshipGrade.getExamineTypeId() == 14 || examineInternshipGrade.getExamineTypeId() == 15 ||
                            examineInternshipGrade.getExamineTypeId() == 16 || examineInternshipGrade.getExamineTypeId() == 17 ){//产线组长个人潜质评分
                        PersonalPotential personalPotential = PersonalPotential.builder()
                                .examineTypeId(examineInternshipGrade.getExamineTypeId())
                                .grade(examineInternshipGrade.getExamineGrade())
                                .build();
                        personalPotentialByLineGroupLeaderVo.setPersonalPotential(personalPotential);
                    }
                }
                log.info("产线实习,个人潜力页面,返回产线组长个人潜力考核评分信息 ->{}",personalPotentialByLineGroupLeaderVo);
                //产线负责人个人潜力考核信息
                PersonalPotentialByLineManagerVo personalPotentialByLineManagerVo =  new PersonalPotentialByLineManagerVo();
                List<ExamineInternshipGrade> examineInternshipGradeList2 = productionLineExamineDao.queryPersonalPotentialByLineManager(creationId,staffCode,studentStaffCode,studentStaffName);
                for (ExamineInternshipGrade examineInternshipGrade : examineInternshipGradeList2) {
                    if(examineInternshipGrade.getExamineTypeId()==3){//复评总得分
                        personalPotentialByLineManagerVo.setPotentialGradeByLineManager(examineInternshipGrade.getExamineGrade());
                    }else if (examineInternshipGrade.getExamineTypeId() == 10 || examineInternshipGrade.getExamineTypeId() == 11
                            || examineInternshipGrade.getExamineTypeId() == 12 || examineInternshipGrade.getExamineTypeId() == 13){//负责人品行信用评分
                        ConductCredit conductCredit = ConductCredit.builder()
                                .examineTypeId(examineInternshipGrade.getExamineTypeId()).grade(examineInternshipGrade.getExamineGrade()).build();
                        personalPotentialByLineManagerVo.setConductCredit(conductCredit);
                    }else if (examineInternshipGrade.getExamineTypeId() == 14 || examineInternshipGrade.getExamineTypeId() == 15 ||
                            examineInternshipGrade.getExamineTypeId() == 16 || examineInternshipGrade.getExamineTypeId() == 17 ){//产线负责人个人潜质评分
                        PersonalPotential personalPotential = PersonalPotential.builder()
                                .examineTypeId(examineInternshipGrade.getExamineTypeId()).grade(examineInternshipGrade.getExamineGrade()).build();
                        personalPotentialByLineManagerVo.setPersonalPotential(personalPotential);
                    }
                }
                log.info("产线实习,个人潜力页面,返回产线负责人个人潜力考核评分信息 ->{}",personalPotentialByLineManagerVo);

                if(studentInformationVoList.size() >= 1){
                    productionLineAndPotentialData.setStudentInformationVo(studentInformationVoList.get(0));
                }
                productionLineAndPotentialData.setInternshipCommentInformationVo(internshipCommentInformationVo);
                productionLineAndPotentialData.setPersonalPotentialByLineGroupLeaderVo(personalPotentialByLineGroupLeaderVo);
                productionLineAndPotentialData.setPersonalPotentialByLineManagerVo(personalPotentialByLineManagerVo);
            }

        }

        return new ResultUtils(100,"菁幹班同學個人信息及考核信息",productionLineAndPotentialData);
    }

    @Override
    @SysLog(value = "新增产线实习,产线组长个人潜力评分考核")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ResultUtils insertExamineByLinneGroupLeader(ProductionLineInternshipVo productionLineInternshipVo) {
        log.info("产线实习,个人潜力 考核内容提交 入参 ->{}",productionLineInternshipVo);
        String staffCode = tokenAnalysis.getTokenUser().getStaffCode();
        log.info("当前用户信息 ->{}",tokenAnalysis.getTokenUser());
        String studentStaffCode = productionLineInternshipVo.getStudentStaffCode();
        Integer lineId = productionLineExamineDao.selectLineIdByStaffCode(studentStaffCode);
        Integer creationId = 0;
        List<ReportCreationList> enableReportList = reportCreationListDao.selectEnableReport();
        if (enableReportList == null || enableReportList.isEmpty()) {
            return new ResultUtils(100, "沒有可啟用任務", enableReportList);
        }
        for (ReportCreationList reportCreationList : enableReportList) {
            if (reportCreationList.getMissionType() == 98) {
                creationId = reportCreationList.getCreationId();
            }
        }
        List<ExamineInternshipGrade> examineInternshipGradeList = productionLineExamineDao.selectSameCommentDate(creationId,studentStaffCode);
        if(examineInternshipGradeList.size() > 0){//此同学之前实习内容及评语已填
            for (ExamineInternshipGrade examineInternshipGrade : examineInternshipGradeList) {
                //将状态改为0 无效
                examineInternshipGrade.setUpdateDate(new Date());
                examineInternshipGrade.setUpdateCode(staffCode);
                productionLineExamineDao.updateSameCommentDate(examineInternshipGrade);
            }
        }

        //产线实习得分(月报*0.5得分)插入对象
        ExamineInternshipGrade grade5 = ExamineInternshipGrade.builder()
                .staffCode(studentStaffCode)
                .creationId(creationId)
                .internshipType(1)
                .lineId(lineId)
                .examineTypeId(6)
                .examineGrade(productionLineInternshipVo.getMonthlyReportGrade())
                .isReExamine(0)
                .createCode(staffCode)
                .createDate(new Date())
                .status(1)
                .build();

        //提案改善报告得分插入对象
        ExamineInternshipGrade grade6 = ExamineInternshipGrade.builder()
                .staffCode(studentStaffCode)
                .creationId(creationId)
                .internshipType(1)
                .lineId(lineId)
                .examineTypeId(7)
                .examineGrade(productionLineInternshipVo.getProposalReportGrade())
                .isReExamine(0)
                .createCode(staffCode)
                .createDate(new Date())
                .status(1)
                .build();

        //产线实习总得分插入对象(包括实习内容和评语)
        ExamineInternshipGrade grade7 = ExamineInternshipGrade.builder()
                .staffCode(studentStaffCode)
                .pid(0)
                .creationId(creationId)
                .internshipType(1)
                .lineId(lineId)
                .internshipComment(productionLineInternshipVo.getInternshipComment())
                .comment(productionLineInternshipVo.getComment())
                .examineTypeId(2)
                .examineGrade(productionLineInternshipVo.getProductionLineInternshipGrade())
                .isReExamine(0)
                .createCode(staffCode)
                .createDate(new Date())
                .status(1)
                .build();
        List<ExamineInternshipGrade> gradeList = productionLineExamineDao.selectSameExamineInternshipGradeDate(creationId,studentStaffCode);
        if(gradeList.size() > 0){
            for (ExamineInternshipGrade examineInternshipGrade : gradeList){
                examineInternshipGrade.setUpdateDate(new Date());
                examineInternshipGrade.setUpdateCode(staffCode);
                productionLineExamineDao.updateSameExamineInternshipGradeDate(examineInternshipGrade);
            }


        }
        Integer grade7Count = productionLineExamineDao.insertExamineByLinneGroupLeader(grade7);//产线实习总得分插入对象
        grade5.setPid(grade7.getExamineId());
        grade6.setPid(grade7.getExamineId());
        Integer grade5Count = productionLineExamineDao.insertExamineByLinneGroupLeader(grade5);//产线实习得分(月报*0.5得分)插入对象
        Integer grade6Count = productionLineExamineDao.insertExamineByLinneGroupLeader(grade6);//提案改善报告得分插入对象

        //产线组长品行信用评分插入对象
        String conductCredit = productionLineInternshipVo.getConductCreditByLineGroupLeader();//产线组长品行信用评分
        ExamineInternshipGrade grade2 = ExamineInternshipGrade.builder()
                .staffCode(studentStaffCode)
                .creationId(creationId)
                .internshipType(1)
                .lineId(lineId)
                .examineGrade(conductCredit)
                .isReExamine(0)
                .createCode(staffCode)
                .createDate(new Date())
                .status(1)
                .build();
        Double conductCreditGrade = Double.valueOf(conductCredit);
        if(conductCreditGrade <= 10 && conductCreditGrade >= 8.1){
            grade2.setExamineTypeId(10);
        }else if(conductCreditGrade < 8.1 && conductCreditGrade >= 6.1){
            grade2.setExamineTypeId(11);
        }else if(conductCreditGrade < 6.1 && conductCreditGrade >= 4.1){
            grade2.setExamineTypeId(12);
        }else if(conductCreditGrade <4.1 && conductCreditGrade >= 2.1){
            grade2.setExamineTypeId(13);
        }
        //产线组长个人潜质得分 插入对象
        String personalPotential = productionLineInternshipVo.getPersonalPotentialByLineGroupLeader();//个人潜质得分
        ExamineInternshipGrade grade3 = ExamineInternshipGrade.builder()
                .staffCode(studentStaffCode)
                .creationId(creationId)
                .internshipType(1)
                .lineId(lineId)
                .examineGrade(personalPotential)
                .isReExamine(0)
                .createCode(staffCode)
                .createDate(new Date())
                .status(1)
                .build();
        Double personalPotentialGrade = Double.valueOf(personalPotential);
        if(personalPotentialGrade <= 10 && personalPotentialGrade >= 8.1){
            grade3.setExamineTypeId(14);
        }else if(personalPotentialGrade < 8.1 && personalPotentialGrade >= 6.1){
            grade3.setExamineTypeId(15);
        }else if(personalPotentialGrade < 6.1 && personalPotentialGrade >= 4.1){
            grade3.setExamineTypeId(16);
        }else if(personalPotentialGrade < 4.1 && personalPotentialGrade >= 2.1){
            grade3.setExamineTypeId(17);
        }

        //个人潜力初评总得分插入对象
        ExamineInternshipGrade grade4 = ExamineInternshipGrade.builder()
                .staffCode(studentStaffCode)
                .pid(0)
                .creationId(creationId)
                .internshipType(1)
                .lineId(lineId)
                .examineTypeId(3)
                .examineGrade(productionLineInternshipVo.getPotentialGradeByLineGroupLeader())
                .isReExamine(0)
                .createCode(staffCode)
                .createDate(new Date())
                .status(1)
                .build();
        List<ExamineInternshipGrade> internshipGradeDateList = productionLineExamineDao.selectSameInternshipGradeDate(0,creationId,studentStaffCode);
        if(internshipGradeDateList.size() > 0){
            for (ExamineInternshipGrade examineInternshipGrade : internshipGradeDateList) {
                examineInternshipGrade.setUpdateDate(new Date());
                examineInternshipGrade.setUpdateCode(staffCode);
                productionLineExamineDao.updateSameInternshipGradeDate(examineInternshipGrade);
            }
        }

        Integer grade4Count = productionLineExamineDao.insertExamineByLinneGroupLeader(grade4);//个人潜力初评总得分插入对象
        grade2.setPid(grade4.getExamineId());
        grade3.setPid(grade4.getExamineId());
        Integer grade2Count = productionLineExamineDao.insertExamineByLinneGroupLeader(grade2);//产线组长品行信用评分插入对象
        Integer grade3Count = productionLineExamineDao.insertExamineByLinneGroupLeader(grade3);//产线组长个人潜质得分 插入对象
        if(grade2Count <= 0 || grade3Count <= 0 || grade4Count <= 0 || grade5Count <= 0 || grade6Count <= 0 || grade7Count <= 0){
            return new ResultUtils(500,"插入實習期考核細項評分結果表失敗");
        }
        
        return new ResultUtils(100,"插入成功");
    }



    /**
     * 获取菁干班同学出勤与奖惩内容
     * 1:找到产线实习月度考核任务(mission_type = 98)
     * 2:根据任务的实习开始时间来判断要找哪个月的出勤与奖惩记录
     * 3:在出勤表中根据日期及examine_type_id来确认各个类型的次数(奖惩表类似)
     * 4:出勤考核中无事由说明则返回空,有事由说明,多个则按时间排序展示
     * @param json
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ResultUtils inquireAttendaceRewardsAndPunishments(JSONObject json) throws ParseException {
         String studentStaffCode = json.getString("studentStaffCode");
        String studentStaffName = json.getString("studentStaffName") ;
        List<AttendanceVo> attendanceVoList =new ArrayList<>();
        List<OtherRewardsAndPunishmentsVo> rewardsAndPunishmentsVoList =new ArrayList<>();
        List<ReportCreationList> enableReportList = reportCreationListDao.selectEnableReport();
        if (enableReportList == null || enableReportList.isEmpty()) {
            return new ResultUtils(100, "沒有可啟用任務", enableReportList);
        }
        for (ReportCreationList reportCreationList : enableReportList) {
            //新任务里有产线实习月度考核
            if (reportCreationList.getMissionType() == 98) {
                log.info("找到产线实习月度考核的可用任务 ->{}",reportCreationList);
                //先去实习期考核细项评分结果表 查有无记录
                List<ExamineInternshipGrade>  attendaceInternshipGradeList= productionLineExamineDao.getAttendaceByParams(reportCreationList.getCreationId(),studentStaffCode);
                if(attendaceInternshipGradeList.size() > 0){
                    attendanceVoList = getDataFromExamineInternshipGrade(attendaceInternshipGradeList);
                }else {
                    attendanceVoList = getDataFromAttendanceRecord(studentStaffCode,studentStaffName,reportCreationList);
                }
                List<ExamineInternshipGrade>  rewardsAndPunishInternshipGradeList= productionLineExamineDao.getRewardsAndPunishByParams(reportCreationList.getCreationId(),studentStaffCode);
                if(rewardsAndPunishInternshipGradeList.size() > 0){
                    rewardsAndPunishmentsVoList = getRewardsDataFromExamineInternshipGrade(rewardsAndPunishInternshipGradeList);
                }else {
                    rewardsAndPunishmentsVoList = getRewardsAndPunishDataFromAttendanceRecord(studentStaffCode,studentStaffName,reportCreationList);
                }

            }
        }
        AttendaceRewardsAndPunishmentsVo attendaceRewardsAndPunishmentsVo = new AttendaceRewardsAndPunishmentsVo();
        attendaceRewardsAndPunishmentsVo.setStudentStaffName(studentStaffName);
        attendaceRewardsAndPunishmentsVo.setStudentCode(studentStaffCode);
        attendaceRewardsAndPunishmentsVo.setAttendanceVoList(attendanceVoList);
        attendaceRewardsAndPunishmentsVo.setOtherRewardsAndPunishmentsVoList(rewardsAndPunishmentsVoList);

        return new ResultUtils(100,"查詢成功",attendaceRewardsAndPunishmentsVo);
    }

    //从奖惩记录表中获取奖惩记录
    private List<OtherRewardsAndPunishmentsVo> getRewardsAndPunishDataFromAttendanceRecord(String studentStaffCode, String studentStaffName, ReportCreationList reportCreationList) throws ParseException {
        List<OtherRewardsAndPunishmentsVo> rewardsAndPunishmentsVoList =new ArrayList<>();
        Date internshipStart = reportCreationList.getInternshipStart();//获取实习开始时间
        Date internshipEnd = reportCreationList.getInternshipEnd();//获取实习结束时间
        //获取事業群活動次数
        int businessGroupActivitiesNumber = productionLineExamineDao.getRewardsAndPunishNumber(studentStaffCode,studentStaffName,25,internshipStart,internshipEnd);
        //获取 事业群活动次数分数
        double businessGroupActivitiesGrade = businessGroupActivitiesNumber * (1);
        OtherRewardsAndPunishmentsVo otherRewardsAndPunishmentsVo = OtherRewardsAndPunishmentsVo.builder()
                .examineType("事業群活動")
                .content("+1")
                .countNum(businessGroupActivitiesNumber)
                .examineGrade(String.valueOf(businessGroupActivitiesGrade))
                .build();
        rewardsAndPunishmentsVoList.add(otherRewardsAndPunishmentsVo);
        //集体活动次数
        int groupActivitiesNumber = productionLineExamineDao.getRewardsAndPunishNumber(studentStaffCode,studentStaffName,26,internshipStart,internshipEnd);
        //集体活动分数
        double groupActivitiesGrade = groupActivitiesNumber * (2);
        OtherRewardsAndPunishmentsVo otherRewardsAndPunishmentsVo2 = OtherRewardsAndPunishmentsVo.builder()
                .examineType("集體活動")
                .content("+2")
                .countNum(groupActivitiesNumber)
                .examineGrade(String.valueOf(groupActivitiesGrade))
                .build();
        rewardsAndPunishmentsVoList.add(otherRewardsAndPunishmentsVo2);
        //座谈会发言次数
        int forumSpeechNumber = productionLineExamineDao.getRewardsAndPunishNumber(studentStaffCode,studentStaffName,27,internshipStart,internshipEnd);
        double forumSpeechGrade = forumSpeechNumber * (0.5);
        OtherRewardsAndPunishmentsVo otherRewardsAndPunishmentsVo3 = OtherRewardsAndPunishmentsVo.builder()
                .examineType("座談會發言")
                .content("+0.5")
                .countNum(forumSpeechNumber)
                .examineGrade(String.valueOf(forumSpeechGrade))
                .build();
        rewardsAndPunishmentsVoList.add(otherRewardsAndPunishmentsVo3);
        //担任班长次数
        int asMonitorNumber = productionLineExamineDao.getRewardsAndPunishNumber(studentStaffCode,studentStaffName,28,internshipStart,internshipEnd);
        double asMonitorGrade = asMonitorNumber * (1);
        OtherRewardsAndPunishmentsVo otherRewardsAndPunishmentsVo4 = OtherRewardsAndPunishmentsVo.builder()
                .examineType("擔任班長")
                .content("+1")
                .countNum(asMonitorNumber)
                .examineGrade(String.valueOf(asMonitorGrade))
                .build();
        rewardsAndPunishmentsVoList.add(otherRewardsAndPunishmentsVo4);
        //《鴻橋》發表 次数
        int hongQiaoPublishNumber = productionLineExamineDao.getRewardsAndPunishNumber(studentStaffCode,studentStaffName,29,internshipStart,internshipEnd);
        double hongQiaoPublishGrade = hongQiaoPublishNumber * (1);
        OtherRewardsAndPunishmentsVo otherRewardsAndPunishmentsVo5 = OtherRewardsAndPunishmentsVo.builder()
                .examineType("《鴻橋》發表")
                .content("+1")
                .countNum(hongQiaoPublishNumber)
                .examineGrade(String.valueOf(hongQiaoPublishGrade))
                .build();
        rewardsAndPunishmentsVoList.add(otherRewardsAndPunishmentsVo5);
        //嘉奖次数
        int commendationNumber = productionLineExamineDao.getRewardsAndPunishNumber(studentStaffCode,studentStaffName,30,internshipStart,internshipEnd);
        double commendationGrade = commendationNumber * (3);
        OtherRewardsAndPunishmentsVo otherRewardsAndPunishmentsVo6 = OtherRewardsAndPunishmentsVo.builder()
                .examineType("嘉獎")
                .content("+3")
                .countNum(commendationNumber)
                .examineGrade(String.valueOf(commendationGrade))
                .build();
        rewardsAndPunishmentsVoList.add(otherRewardsAndPunishmentsVo6);
        //小功次数
        int mourningDressNumber = productionLineExamineDao.getRewardsAndPunishNumber(studentStaffCode,studentStaffName,31,internshipStart,internshipEnd);
        double mourningDressGrade = mourningDressNumber * (5);
        OtherRewardsAndPunishmentsVo otherRewardsAndPunishmentsVo7 = OtherRewardsAndPunishmentsVo.builder()
                .examineType("小功")
                .content("+5")
                .countNum(mourningDressNumber)
                .examineGrade(String.valueOf(mourningDressGrade))
                .build();
        rewardsAndPunishmentsVoList.add(otherRewardsAndPunishmentsVo7);
        //大功次数
        int greatAchievementsNumber = productionLineExamineDao.getRewardsAndPunishNumber(studentStaffCode,studentStaffName,32,internshipStart,internshipEnd);
        double greatAchievementsGrade = greatAchievementsNumber * (9);
        OtherRewardsAndPunishmentsVo otherRewardsAndPunishmentsVo8 = OtherRewardsAndPunishmentsVo.builder()
                .examineType("大功")
                .content("+9")
                .countNum(greatAchievementsNumber)
                .examineGrade(String.valueOf(greatAchievementsGrade))
                .build();
        rewardsAndPunishmentsVoList.add(otherRewardsAndPunishmentsVo8);
        //警告次数
        int warnNumber = productionLineExamineDao.getRewardsAndPunishNumber(studentStaffCode,studentStaffName,33,internshipStart,internshipEnd);
        double warnGrade = warnNumber * (-3);
        OtherRewardsAndPunishmentsVo otherRewardsAndPunishmentsVo9 = OtherRewardsAndPunishmentsVo.builder()
                .examineType("警告")
                .content("-3")
                .countNum(warnNumber)
                .examineGrade(String.valueOf(warnGrade))
                .build();
        rewardsAndPunishmentsVoList.add(otherRewardsAndPunishmentsVo9);
        //小過次数
        int minorMistakesNumber = productionLineExamineDao.getRewardsAndPunishNumber(studentStaffCode,studentStaffName,34,internshipStart,internshipEnd);
        double minorMistakesGrade = minorMistakesNumber * (-5);
        OtherRewardsAndPunishmentsVo otherRewardsAndPunishmentsVo10 = OtherRewardsAndPunishmentsVo.builder()
                .examineType("小過")
                .content("-5")
                .countNum(minorMistakesNumber)
                .examineGrade(String.valueOf(minorMistakesGrade))
                .build();
        rewardsAndPunishmentsVoList.add(otherRewardsAndPunishmentsVo10);
        //大過次数
        int seriousOffenseNumber = productionLineExamineDao.getRewardsAndPunishNumber(studentStaffCode,studentStaffName,35,internshipStart,internshipEnd);
        double seriousOffenseGrade = seriousOffenseNumber * (-9);
        OtherRewardsAndPunishmentsVo otherRewardsAndPunishmentsVo11 = OtherRewardsAndPunishmentsVo.builder()
                .examineType("大過")
                .content("-9")
                .countNum(seriousOffenseNumber)
                .examineGrade(String.valueOf(seriousOffenseGrade))
                .build();
        rewardsAndPunishmentsVoList.add(otherRewardsAndPunishmentsVo11);

        return rewardsAndPunishmentsVoList;
    }

    //从从实习期考核细项评分结果表中获取出奖惩记录
    private List<OtherRewardsAndPunishmentsVo> getRewardsDataFromExamineInternshipGrade(List<ExamineInternshipGrade> rewardsAndPunishInternshipGradeList) {
        List<OtherRewardsAndPunishmentsVo> rewardsAndPunishmentsVoList =new ArrayList<>();
        OtherRewardsAndPunishmentsVo otherRewardsAndPunishmentsVo = OtherRewardsAndPunishmentsVo.builder()
                .examineType("事業群活動")
                .content("+1")
                .countNum(0)
                .examineGrade("0")
                .build();
        OtherRewardsAndPunishmentsVo otherRewardsAndPunishmentsVo2 = OtherRewardsAndPunishmentsVo.builder()
                .examineType("集體活動")
                .content("+2")
                .countNum(0)
                .examineGrade("0")
                .build();
        OtherRewardsAndPunishmentsVo otherRewardsAndPunishmentsVo3 = OtherRewardsAndPunishmentsVo.builder()
                .examineType("座談會發言")
                .content("+0.5")
                .countNum(0)
                .examineGrade("0")
                .build();
        OtherRewardsAndPunishmentsVo otherRewardsAndPunishmentsVo4 = OtherRewardsAndPunishmentsVo.builder()
                .examineType("擔任班長")
                .content("+1")
                .countNum(0)
                .examineGrade("0")
                .build();
        OtherRewardsAndPunishmentsVo otherRewardsAndPunishmentsVo5 = OtherRewardsAndPunishmentsVo.builder()
                .examineType("《鴻橋》發表")
                .content("+1")
                .countNum(0)
                .examineGrade("0")
                .build();
        OtherRewardsAndPunishmentsVo otherRewardsAndPunishmentsVo6 = OtherRewardsAndPunishmentsVo.builder()
                .examineType("嘉獎")
                .content("+3")
                .countNum(0)
                .examineGrade("0")
                .build();
        OtherRewardsAndPunishmentsVo otherRewardsAndPunishmentsVo7 = OtherRewardsAndPunishmentsVo.builder()
                .examineType("小功")
                .content("+5")
                .countNum(0)
                .examineGrade("0")
                .build();
        OtherRewardsAndPunishmentsVo otherRewardsAndPunishmentsVo8 = OtherRewardsAndPunishmentsVo.builder()
                .examineType("大功")
                .content("+9")
                .countNum(0)
                .examineGrade("0")
                .build();
        OtherRewardsAndPunishmentsVo otherRewardsAndPunishmentsVo9 = OtherRewardsAndPunishmentsVo.builder()
                .examineType("警告")
                .content("-3")
                .countNum(0)
                .examineGrade("0")
                .build();
        OtherRewardsAndPunishmentsVo otherRewardsAndPunishmentsVo10 = OtherRewardsAndPunishmentsVo.builder()
                .examineType("小過")
                .content("-5")
                .countNum(0)
                .examineGrade("0")
                .build();
        OtherRewardsAndPunishmentsVo otherRewardsAndPunishmentsVo11 = OtherRewardsAndPunishmentsVo.builder()
                .examineType("大過")
                .content("-9")
                .countNum(0)
                .examineGrade("0")
                .build();
        for (ExamineInternshipGrade examineInternshipGrade : rewardsAndPunishInternshipGradeList) {
            if(examineInternshipGrade.getExamineTypeId() == 25 ){//事業群活動
                otherRewardsAndPunishmentsVo.setCountNum(examineInternshipGrade.getCountNum());
                otherRewardsAndPunishmentsVo.setExamineGrade(examineInternshipGrade.getExamineGrade());
            }else if(examineInternshipGrade.getExamineTypeId() == 26 ){//集體活動
                otherRewardsAndPunishmentsVo2.setCountNum(examineInternshipGrade.getCountNum());
                otherRewardsAndPunishmentsVo2.setExamineGrade(examineInternshipGrade.getExamineGrade());
            }else if(examineInternshipGrade.getExamineTypeId() == 27 ){//座談會發言
                otherRewardsAndPunishmentsVo3.setCountNum(examineInternshipGrade.getCountNum());
                otherRewardsAndPunishmentsVo3.setExamineGrade(examineInternshipGrade.getExamineGrade());
            }else if(examineInternshipGrade.getExamineTypeId() == 28 ){//擔任班長
                otherRewardsAndPunishmentsVo4.setCountNum(examineInternshipGrade.getCountNum());
                otherRewardsAndPunishmentsVo4.setExamineGrade(examineInternshipGrade.getExamineGrade());
            }else if(examineInternshipGrade.getExamineTypeId() == 29 ){//《鴻橋》發表
                otherRewardsAndPunishmentsVo5.setCountNum(examineInternshipGrade.getCountNum());
                otherRewardsAndPunishmentsVo5.setExamineGrade(examineInternshipGrade.getExamineGrade());
            }else if(examineInternshipGrade.getExamineTypeId() == 30 ){//嘉獎
                otherRewardsAndPunishmentsVo6.setCountNum(examineInternshipGrade.getCountNum());
                otherRewardsAndPunishmentsVo6.setExamineGrade(examineInternshipGrade.getExamineGrade());
            }else if(examineInternshipGrade.getExamineTypeId() == 31 ){//小功
                otherRewardsAndPunishmentsVo7.setCountNum(examineInternshipGrade.getCountNum());
                otherRewardsAndPunishmentsVo7.setExamineGrade(examineInternshipGrade.getExamineGrade());
            }else if(examineInternshipGrade.getExamineTypeId() == 32 ){//大功
                otherRewardsAndPunishmentsVo8.setCountNum(examineInternshipGrade.getCountNum());
                otherRewardsAndPunishmentsVo8.setExamineGrade(examineInternshipGrade.getExamineGrade());
            }else if(examineInternshipGrade.getExamineTypeId() == 33 ){//警告
                otherRewardsAndPunishmentsVo9.setCountNum(examineInternshipGrade.getCountNum());
                otherRewardsAndPunishmentsVo9.setExamineGrade(examineInternshipGrade.getExamineGrade());
            }else if(examineInternshipGrade.getExamineTypeId() == 34 ){//小過
                otherRewardsAndPunishmentsVo10.setCountNum(examineInternshipGrade.getCountNum());
                otherRewardsAndPunishmentsVo10.setExamineGrade(examineInternshipGrade.getExamineGrade());
            }else if(examineInternshipGrade.getExamineTypeId() == 35 ){//大過
                otherRewardsAndPunishmentsVo11.setCountNum(examineInternshipGrade.getCountNum());
                otherRewardsAndPunishmentsVo11.setExamineGrade(examineInternshipGrade.getExamineGrade());
            }
        }
        rewardsAndPunishmentsVoList.add(otherRewardsAndPunishmentsVo);
        rewardsAndPunishmentsVoList.add(otherRewardsAndPunishmentsVo2);
        rewardsAndPunishmentsVoList.add(otherRewardsAndPunishmentsVo3);
        rewardsAndPunishmentsVoList.add(otherRewardsAndPunishmentsVo4);
        rewardsAndPunishmentsVoList.add(otherRewardsAndPunishmentsVo5);
        rewardsAndPunishmentsVoList.add(otherRewardsAndPunishmentsVo6);
        rewardsAndPunishmentsVoList.add(otherRewardsAndPunishmentsVo7);
        rewardsAndPunishmentsVoList.add(otherRewardsAndPunishmentsVo8);
        rewardsAndPunishmentsVoList.add(otherRewardsAndPunishmentsVo9);
        rewardsAndPunishmentsVoList.add(otherRewardsAndPunishmentsVo10);
        rewardsAndPunishmentsVoList.add(otherRewardsAndPunishmentsVo11);
        return rewardsAndPunishmentsVoList;
    }

    //从实习期考核细项评分结果表中获取出勤记录
    private List<AttendanceVo> getDataFromExamineInternshipGrade(List<ExamineInternshipGrade> internshipGradeList) {
        List<AttendanceVo> attendanceVoList =new ArrayList<>();
        AttendanceVo attendanceVo = AttendanceVo.builder()
                .examineType("遲到")
                .content("-1")
                .countNum(0)
                .examineGrade(String.valueOf(0))
                .reason("")
                .build();
        AttendanceVo attendanceVo2 = AttendanceVo.builder()
                .examineType("早退")
                .content("-1")
                .countNum(0)
                .examineGrade(String.valueOf(0))
                .reason("")
                .build();
        AttendanceVo attendanceVo3 = AttendanceVo.builder()
                .examineType("病假")
                .content("-0.5")
                .countNum(0)
                .examineGrade(String.valueOf(0))
                .reason("")
                .build();
        AttendanceVo attendanceVo4 = AttendanceVo.builder()
                .examineType("事假")
                .content("-1")
                .countNum(0)
                .examineGrade(String.valueOf(0))
                .reason("")
                .build();
        AttendanceVo attendanceVo5 = AttendanceVo.builder()
                .examineType("曠工")
                .content("-2")
                .countNum(0)
                .examineGrade(String.valueOf(0))
                .reason("")
                .build();
        for (ExamineInternshipGrade examineInternshipGrade : internshipGradeList) {
            if(examineInternshipGrade.getExamineTypeId() == 20){//迟到
                attendanceVo.setCountNum(examineInternshipGrade.getCountNum());
                attendanceVo.setExamineGrade(examineInternshipGrade.getExamineGrade());
                attendanceVo.setReason(examineInternshipGrade.getReason());
            }else if(examineInternshipGrade.getExamineTypeId() == 21){//早退
                attendanceVo2.setCountNum(examineInternshipGrade.getCountNum());
                attendanceVo2.setExamineGrade(examineInternshipGrade.getExamineGrade());
                attendanceVo2.setReason(examineInternshipGrade.getReason());
            }else if(examineInternshipGrade.getExamineTypeId() == 22){//病假
                attendanceVo3.setCountNum(examineInternshipGrade.getCountNum());
                attendanceVo3.setExamineGrade(examineInternshipGrade.getExamineGrade());
                attendanceVo3.setReason(examineInternshipGrade.getReason());
            }else if(examineInternshipGrade.getExamineTypeId() == 23){//事假
                attendanceVo4.setCountNum(examineInternshipGrade.getCountNum());
                attendanceVo4.setExamineGrade(examineInternshipGrade.getExamineGrade());
                attendanceVo4.setReason(examineInternshipGrade.getReason());
            }else if(examineInternshipGrade.getExamineTypeId() == 24) {//曠工
                attendanceVo5.setCountNum(examineInternshipGrade.getCountNum());
                attendanceVo5.setExamineGrade(examineInternshipGrade.getExamineGrade());
                attendanceVo5.setReason(examineInternshipGrade.getReason());
            }
        }
        attendanceVoList.add(attendanceVo);
        attendanceVoList.add(attendanceVo2);
        attendanceVoList.add(attendanceVo3);
        attendanceVoList.add(attendanceVo4);
        attendanceVoList.add(attendanceVo5);
        return attendanceVoList;
    }

    //从考勤表中获取出勤记录
    private List<AttendanceVo> getDataFromAttendanceRecord(String studentStaffCode , String studentStaffName, ReportCreationList reportCreationList) throws ParseException {
        List<AttendanceVo> attendanceVoList =new ArrayList<>();

        Date internshipStart = reportCreationList.getInternshipStart();//获取实习开始时间
        Date internshipEnd = reportCreationList.getInternshipEnd();//获取实习结束时间
        //获取迟到次数
        int lateNumber = productionLineExamineDao.getAttendaceNumber(studentStaffCode,studentStaffName,20,internshipStart,internshipEnd);
        //获取迟到得分
        double lateExamineGrade = lateNumber * (-1);
        AttendanceVo attendanceVo = AttendanceVo.builder()
                .examineType("遲到")
                .content("-1")
                .countNum(lateNumber)
                .examineGrade(String.valueOf(lateExamineGrade))
                .reason("")
                .build();
        attendanceVoList.add(attendanceVo);
        //获取早退次数
        int earlyLeaveNumber = productionLineExamineDao.getAttendaceNumber(studentStaffCode,studentStaffName,21,internshipStart,internshipEnd);
        //获取早退得分
        double earlyLeaveExamineGrade = earlyLeaveNumber * (-1);
        AttendanceVo attendanceVo2 = AttendanceVo.builder()
                .examineType("早退")
                .content("-1")
                .countNum(earlyLeaveNumber)
                .examineGrade(String.valueOf(earlyLeaveExamineGrade))
                .reason("")
                .build();
        attendanceVoList.add(attendanceVo2);
        //获取病假次数
        int sickNumber = productionLineExamineDao.getAttendaceNumber(studentStaffCode,studentStaffName,22,internshipStart,internshipEnd);
        //获取病假得分
        double sickExamineGrade = sickNumber * (0.5);
        AttendanceVo attendanceVo3 = AttendanceVo.builder()
                .examineType("病假")
                .content("-0.5")
                .countNum(sickNumber)
                .examineGrade(String.valueOf(sickExamineGrade))
                .reason("")
                .build();
        attendanceVoList.add(attendanceVo3);
        //获取事假次数
        int compassionateNumber = productionLineExamineDao.getAttendaceNumber(studentStaffCode,studentStaffName,23,internshipStart,internshipEnd);
        //获取事假得分
        double compassionateExamineGrade = compassionateNumber * (-1);
        AttendanceVo attendanceVo4 = AttendanceVo.builder()
                .examineType("事假")
                .content("-1")
                .countNum(compassionateNumber)
                .examineGrade(String.valueOf(compassionateExamineGrade))
                .reason("")
                .build();
        attendanceVoList.add(attendanceVo4);
        //获取旷工次数
        int absenceNumber = productionLineExamineDao.getAttendaceNumber(studentStaffCode,studentStaffName,24,internshipStart,internshipEnd);
        //获取旷工得分
        double absenceExamineGrade = absenceNumber * (-2);
        AttendanceVo attendanceVo5 = AttendanceVo.builder()
                .examineType("曠工")
                .content("-2")
                .countNum(absenceNumber)
                .examineGrade(String.valueOf(absenceExamineGrade))
                .reason("")
                .build();
        attendanceVoList.add(attendanceVo5);
        return attendanceVoList;
    }

    //修改菁干班出勤与考勤内容
    @Override
    @SysLog(value = "产线考核-修改菁干班出勤与考核")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ResultUtils updateAttendaceRewardsAndPunishments(AttendaceRewardsAndPunishmentsVo attendaceRewardsAndPunishmentsVo) {
        log.info("出勤与奖惩信息提交 入参 ->{}",attendaceRewardsAndPunishmentsVo);
        String staffCode = tokenAnalysis.getTokenUser().getStaffCode();
        log.info("当前用户信息 ->{}",tokenAnalysis.getTokenUser());
        ExamineInternshipGrade examineInternshipGrade =  ExamineInternshipGrade.builder().build();
        //String studentStaffName = attendaceRewardsAndPunishmentsVo.getStudentStaffName();
        String studentCode = attendaceRewardsAndPunishmentsVo.getStudentCode();
        Integer lineId = productionLineExamineDao.selectLineIdByStaffCode(studentCode);
        Integer creationId = 0;
        List<ReportCreationList> enableReportList = reportCreationListDao.selectEnableReport();
        if (enableReportList == null || enableReportList.isEmpty()) {
            return new ResultUtils(100, "沒有可啟用任務", enableReportList);
        }
        for (ReportCreationList reportCreationList : enableReportList) {
            if (reportCreationList.getMissionType() == 98) {
                creationId = reportCreationList.getCreationId();
            }
        }
        //是否此同学是否在考核表中已有记录,有则修改状态,无则新增
        List<ExamineInternshipGrade> sameAttendanceList = productionLineExamineDao.selectSameAttendanceDate(creationId,studentCode);
        if(sameAttendanceList.size() > 0){
            for (ExamineInternshipGrade internshipGrade : sameAttendanceList) {
                internshipGrade.setStatus(0);
                internshipGrade.setUpdateDate(new Date());
                internshipGrade.setUpdateCode(staffCode);
                productionLineExamineDao.updateSameAttendanceDate(internshipGrade);
            }
        }
        String attendanceAndRewardsAndPunishGrade = attendaceRewardsAndPunishmentsVo.getAttendanceAndRewardsAndPunishGrade();
        ExamineInternshipGrade examineInternshipGrade1 = new  ExamineInternshipGrade();
        examineInternshipGrade1.setStaffCode(studentCode);
        examineInternshipGrade1.setExamineTypeId(4);
        examineInternshipGrade1.setPid(0);
        examineInternshipGrade1.setInternshipType(1);
        examineInternshipGrade1.setLineId(lineId);
        examineInternshipGrade1.setCreationId(creationId);
        examineInternshipGrade1.setExamineGrade(attendanceAndRewardsAndPunishGrade);
        examineInternshipGrade1.setIsReExamine(0);
        examineInternshipGrade1.setCreateCode(staffCode);
        examineInternshipGrade1.setCreateDate(new Date());
        examineInternshipGrade1.setStatus(1);
        productionLineExamineDao.insertAttendaceRewardsAndPunishments(examineInternshipGrade1);//考勤与奖惩总得分插入数据库
        List<AttendanceVo> attendanceVoList = attendaceRewardsAndPunishmentsVo.getAttendanceVoList();
        ExamineInternshipGrade examineInternshipGrade2 = new  ExamineInternshipGrade();
        for (AttendanceVo attendanceVo : attendanceVoList) {
            examineInternshipGrade2.setStaffCode(studentCode);
            examineInternshipGrade2.setPid(examineInternshipGrade1.getExamineId());
            examineInternshipGrade2.setInternshipType(1);
            examineInternshipGrade2.setLineId(lineId);
            examineInternshipGrade2.setCreationId(creationId);
            examineInternshipGrade2.setExamineGrade(attendanceVo.getExamineGrade());
            if(attendanceVo.getCountNum() >= 0){
                examineInternshipGrade2.setCountNum(attendanceVo.getCountNum());
            }
            examineInternshipGrade2.setReason(attendanceVo.getReason());
            examineInternshipGrade2.setIsReExamine(0);
            examineInternshipGrade2.setCreateCode(staffCode);
            examineInternshipGrade2.setCreateDate(new Date());
            examineInternshipGrade2.setStatus(1);
            if("出勤狀況總得分".equals(attendanceVo.getExamineType())){
                examineInternshipGrade2.setExamineTypeId(18);
                examineInternshipGrade2.setExamineGrade(examineInternshipGrade2.getExamineGrade());
                productionLineExamineDao.insertAttendaceRewardsAndPunishments(examineInternshipGrade2);
            }

        }
        ExamineInternshipGrade examineInternshipGrade3 = new  ExamineInternshipGrade();
        for (AttendanceVo attendanceVo : attendanceVoList) {
            examineInternshipGrade3.setStaffCode(studentCode);
            examineInternshipGrade3.setPid(examineInternshipGrade2.getExamineId());
            examineInternshipGrade3.setInternshipType(1);
            examineInternshipGrade3.setLineId(lineId);
            examineInternshipGrade3.setCreationId(creationId);
            examineInternshipGrade3.setExamineGrade(attendanceVo.getExamineGrade());
            if(attendanceVo.getCountNum() >= 0){
                examineInternshipGrade3.setCountNum(attendanceVo.getCountNum());
            }
            examineInternshipGrade3.setReason(attendanceVo.getReason());
            examineInternshipGrade3.setIsReExamine(0);
            examineInternshipGrade3.setCreateCode(staffCode);
            examineInternshipGrade3.setCreateDate(new Date());
            examineInternshipGrade3.setStatus(1);
            if("遲到".equals(attendanceVo.getExamineType())){
                examineInternshipGrade3.setExamineTypeId(20);
                productionLineExamineDao.insertAttendaceRewardsAndPunishments(examineInternshipGrade3);
            }else if("早退".equals(attendanceVo.getExamineType())){
                examineInternshipGrade3.setExamineTypeId(21);
                productionLineExamineDao.insertAttendaceRewardsAndPunishments(examineInternshipGrade3);
            }else if("病假".equals(attendanceVo.getExamineType())){
                examineInternshipGrade3.setExamineTypeId(22);
                productionLineExamineDao.insertAttendaceRewardsAndPunishments(examineInternshipGrade3);
            }else if("事假".equals(attendanceVo.getExamineType())){
                examineInternshipGrade3.setExamineTypeId(23);
                productionLineExamineDao.insertAttendaceRewardsAndPunishments(examineInternshipGrade3);
            }else if("曠工".equals(attendanceVo.getExamineType())){
                examineInternshipGrade3.setExamineTypeId(24);
                productionLineExamineDao.insertAttendaceRewardsAndPunishments(examineInternshipGrade3);
            }

        }
        List<OtherRewardsAndPunishmentsVo> otherRewardsAndPunishmentsVoList = attendaceRewardsAndPunishmentsVo.getOtherRewardsAndPunishmentsVoList();
        ExamineInternshipGrade examineInternshipGrade4 = new  ExamineInternshipGrade();
        if(otherRewardsAndPunishmentsVoList.size() > 0){
            for (OtherRewardsAndPunishmentsVo otherRewardsAndPunishmentsVo : otherRewardsAndPunishmentsVoList) {
                examineInternshipGrade4.setStaffCode(studentCode);
                examineInternshipGrade4.setInternshipType(1);
                examineInternshipGrade4.setLineId(lineId);
                examineInternshipGrade4.setPid(examineInternshipGrade1.getExamineId());
                examineInternshipGrade4.setCreationId(creationId);
                examineInternshipGrade4.setExamineGrade(otherRewardsAndPunishmentsVo.getExamineGrade());
                if(otherRewardsAndPunishmentsVo.getCountNum() >= 0){
                    examineInternshipGrade4.setCountNum(otherRewardsAndPunishmentsVo.getCountNum());
                }
                examineInternshipGrade4.setIsReExamine(0);
                examineInternshipGrade4.setCreateCode(staffCode);
                examineInternshipGrade4.setCreateDate(new Date());
                examineInternshipGrade4.setStatus(1);
                if("其他獎懲總得分".equals(otherRewardsAndPunishmentsVo.getExamineType())){
                    examineInternshipGrade4.setExamineTypeId(19);
                    productionLineExamineDao.insertAttendaceRewardsAndPunishments(examineInternshipGrade4);
                }
            }

            for (OtherRewardsAndPunishmentsVo otherRewardsAndPunishmentsVo : otherRewardsAndPunishmentsVoList) {
                examineInternshipGrade.setStaffCode(studentCode);
                examineInternshipGrade.setPid(examineInternshipGrade4.getExamineId());
                examineInternshipGrade.setInternshipType(1);
                examineInternshipGrade.setLineId(lineId);
                examineInternshipGrade.setPid(19);
                examineInternshipGrade.setCreationId(creationId);
                examineInternshipGrade.setExamineGrade(otherRewardsAndPunishmentsVo.getExamineGrade());
                if(otherRewardsAndPunishmentsVo.getCountNum() >= 0){
                    examineInternshipGrade.setCountNum(otherRewardsAndPunishmentsVo.getCountNum());
                }
                examineInternshipGrade.setIsReExamine(0);
                examineInternshipGrade.setCreateCode(staffCode);
                examineInternshipGrade.setCreateDate(new Date());
                examineInternshipGrade.setStatus(1);
                if("事業群活動".equals(otherRewardsAndPunishmentsVo.getExamineType())){
                    examineInternshipGrade.setExamineTypeId(25);
                    productionLineExamineDao.insertAttendaceRewardsAndPunishments(examineInternshipGrade);
                }else if("集體活動".equals(otherRewardsAndPunishmentsVo.getExamineType())){
                    examineInternshipGrade.setExamineTypeId(26);
                    productionLineExamineDao.insertAttendaceRewardsAndPunishments(examineInternshipGrade);
                }else if("座談會發言".equals(otherRewardsAndPunishmentsVo.getExamineType())){
                    examineInternshipGrade.setExamineTypeId(27);
                    productionLineExamineDao.insertAttendaceRewardsAndPunishments(examineInternshipGrade);
                }else if("擔任班長".equals(otherRewardsAndPunishmentsVo.getExamineType())){
                    examineInternshipGrade.setExamineTypeId(28);
                    productionLineExamineDao.insertAttendaceRewardsAndPunishments(examineInternshipGrade);
                }else if("《鴻橋》發表".equals(otherRewardsAndPunishmentsVo.getExamineType())){
                    examineInternshipGrade.setExamineTypeId(29);
                    productionLineExamineDao.insertAttendaceRewardsAndPunishments(examineInternshipGrade);
                }else if("嘉獎".equals(otherRewardsAndPunishmentsVo.getExamineType())){
                    examineInternshipGrade.setExamineTypeId(30);
                    productionLineExamineDao.insertAttendaceRewardsAndPunishments(examineInternshipGrade);
                }else if("小功".equals(otherRewardsAndPunishmentsVo.getExamineType())){
                    examineInternshipGrade.setExamineTypeId(31);
                    productionLineExamineDao.insertAttendaceRewardsAndPunishments(examineInternshipGrade);
                }else if("大功".equals(otherRewardsAndPunishmentsVo.getExamineType())){
                    examineInternshipGrade.setExamineTypeId(32);
                    productionLineExamineDao.insertAttendaceRewardsAndPunishments(examineInternshipGrade);
                }else if("警告".equals(otherRewardsAndPunishmentsVo.getExamineType())){
                    examineInternshipGrade.setExamineTypeId(33);
                    productionLineExamineDao.insertAttendaceRewardsAndPunishments(examineInternshipGrade);
                }else if("小過".equals(otherRewardsAndPunishmentsVo.getExamineType())){
                    examineInternshipGrade.setExamineTypeId(34);
                    productionLineExamineDao.insertAttendaceRewardsAndPunishments(examineInternshipGrade);
                }else if("大過".equals(otherRewardsAndPunishmentsVo.getExamineType())){
                    examineInternshipGrade.setExamineTypeId(35);
                    productionLineExamineDao.insertAttendaceRewardsAndPunishments(examineInternshipGrade);
                }

            }
        }
        return new ResultUtils(100 ,"新增成功");
    }

    //查询评分情况
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ResultUtils inquireRating() {
        List<RatingVo> ratingVoList = new ArrayList<>();
        RatingVo ratingVo = RatingVo.builder()
                .examinedContent("優")
                .prescribedProportion("10%")
                .build();
        RatingVo ratingVo2 = RatingVo.builder()
                .examinedContent("甲")
                .prescribedProportion("30%")
                .build();
        RatingVo ratingVo3 = RatingVo.builder()
                .examinedContent("乙")
                .prescribedProportion("40%")
                .build();
        RatingVo ratingVo4 = RatingVo.builder()
                .examinedContent("丙")
                .prescribedProportion("15%")
                .build();
        RatingVo ratingVo5 = RatingVo.builder()
                .examinedContent("丁")
                .prescribedProportion("5%")
                .build();

        String examineTypeId = "";
        DecimalFormat df = new DecimalFormat("0");
        Integer creationId = 0;
        List<ReportCreationList> enableReportList = reportCreationListDao.selectEnableReport();
        if (enableReportList == null || enableReportList.isEmpty()) {
            return new ResultUtils(100, "沒有可啟用任務", enableReportList);
        }
        for (ReportCreationList reportCreationList : enableReportList) {
            if (reportCreationList.getMissionType() == 98) {
                creationId = reportCreationList.getCreationId();
            }
        }
        //获取考核记录表内菁干班所有评分等级情况
        List<StudentsRatingVo> studentsRatingList = productionLineExamineDao.selectAllStudents(creationId,examineTypeId);
        if(studentsRatingList != null && studentsRatingList.size() > 0 ){
            Long studentsRatingSize = Long.valueOf(studentsRatingList.size());
            //获取考核记录表内 优 评分情况
            List<StudentsRatingVo> excellentList =productionLineExamineDao.selectAllStudents(creationId, "38");
            if(excellentList != null && excellentList.size() > 0  ){
                String  currentPeople = String.valueOf(excellentList.size());//现有人数
                Long excellentSize = Long.valueOf(excellentList.size());
                String currentShare = df.format((excellentSize*1.0 /studentsRatingSize ) * 100) + "%";//现有占比
                ratingVo.setCurrentShare(currentShare);
                ratingVo.setCurrentPeople(currentPeople);
            }
            //获取考核记录表内  甲 评分情况
            List<StudentsRatingVo> firstList =productionLineExamineDao.selectAllStudents(creationId, "39");
            if(firstList != null && firstList.size() > 0 ){
                String  currentPeople = String.valueOf(firstList.size());//现有人数
                Long firstSize = Long.valueOf(firstList.size());
                String currentShare = df.format((firstSize*1.0 /studentsRatingSize ) * 100) + "%";//现有占比
                ratingVo2.setCurrentShare(currentShare);
                ratingVo2.setCurrentPeople(currentPeople);
            }
            //获取考核记录表内  乙 评分情况
            List<StudentsRatingVo> secondList =productionLineExamineDao.selectAllStudents(creationId, "40");
            if(secondList != null && secondList.size() > 0  ){
                String  currentPeople = String.valueOf(secondList.size());//现有人数
                Long secondSize = Long.valueOf(secondList.size());
                String currentShare = df.format((secondSize*1.0 /studentsRatingSize ) * 100) + "%";//现有占比
                ratingVo3.setCurrentShare(currentShare);
                ratingVo3.setCurrentPeople(currentPeople);
            }
            //获取考核记录表内 丙 评分情况
            List<StudentsRatingVo> thirdList =productionLineExamineDao.selectAllStudents(creationId, "41");
            if(thirdList != null && thirdList.size() > 0 ){
                String  currentPeople = String.valueOf(thirdList.size());//现有人数
                Long thirdSize = Long.valueOf(thirdList.size());
                String currentShare = df.format((thirdSize*1.0 /studentsRatingSize ) * 100) + "%";//现有占比
                ratingVo4.setCurrentShare(currentShare);
                ratingVo4.setCurrentPeople(currentPeople);
            }
            //获取考核记录表内 丁 评分情况
            List<StudentsRatingVo> fourthList =productionLineExamineDao.selectAllStudents(creationId, "42");
            if(fourthList != null && fourthList.size() > 0  ){
                String  currentPeople = String.valueOf(fourthList.size());//现有人数
                Long fourthSize = Long.valueOf(fourthList.size());
                String currentShare = df.format((fourthSize*1.0 /studentsRatingSize ) * 100) + "%";//现有占比
                ratingVo5.setCurrentShare(currentShare);
                ratingVo5.setCurrentPeople(currentPeople);
            }
        }
        ratingVoList.add(ratingVo);
        ratingVoList.add(ratingVo2);
        ratingVoList.add(ratingVo3);
        ratingVoList.add(ratingVo4);
        ratingVoList.add(ratingVo5);
        return new ResultUtils(100,"查詢成功",ratingVoList);
    }

    //查询考评情况详细名单
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ResultUtils inquireRatingDetailedList(JSONObject json) {
        log.info("產線考评情况详细名单 入参 ->{}",json);
        List<StudentsRatingVo> studentsRatingVoList = new ArrayList<>();
        try{
            Integer creationId = 0;
            List<ReportCreationList> enableReportList = reportCreationListDao.selectEnableReport();
            if (enableReportList == null || enableReportList.isEmpty()) {
                return new ResultUtils(100, "沒有可啟用任務", enableReportList);
            }
            for (ReportCreationList reportCreationList : enableReportList) {
                if (reportCreationList.getMissionType() == 98) {
                    creationId = reportCreationList.getCreationId();
                }
            }
            String examinedContent = json.getString("examinedContent");
            if("優".equals(examinedContent)){
                studentsRatingVoList =productionLineExamineDao.selectAllStudents(creationId, "38");

            }else if("甲".equals(examinedContent)){
                studentsRatingVoList =productionLineExamineDao.selectAllStudents(creationId, "39");
            }else if("乙".equals(examinedContent)){
                studentsRatingVoList =productionLineExamineDao.selectAllStudents(creationId, "40");
            }else if("丙".equals(examinedContent)){
                studentsRatingVoList =productionLineExamineDao.selectAllStudents(creationId, "41");
            }else if("丁".equals(examinedContent)){
                studentsRatingVoList =productionLineExamineDao.selectAllStudents(creationId, "42");
            }
        }catch (Exception e) {
            return new ResultUtils(501,"缺少入参,查询失败");
        }


        return new ResultUtils(100,"查詢成功",studentsRatingVoList);
    }

    //总评分提交
    @Override
    @SysLog(value = "产线考核--总评分提交")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ResultUtils insertTotalGrade(ProductionLineExamineTotalGradeVo productionLineExamineTotalGradeVo) {
        log.info("总评分提交 入参 ->{}",productionLineExamineTotalGradeVo);
        String staffCode = tokenAnalysis.getTokenUser().getStaffCode();
        log.info("当前用户信息 ->{}",tokenAnalysis.getTokenUser());
        String studentStaffCode = productionLineExamineTotalGradeVo.getStaffCode();
        Integer lineId = productionLineExamineDao.selectLineIdByStaffCode(studentStaffCode);
        ExamineInternshipGrade examineInternshipGrade =  ExamineInternshipGrade.builder().
                pid(1)
                .internshipType(1)
                .lineId(lineId)
                .examineTypeId(5)
                .examineGrade(productionLineExamineTotalGradeVo.getTotalGrade())
                .isReExamine(0)
                .createCode(staffCode)
                .createDate(new Date())
                .status(1)
                .build();
        log.info("菁干班同学{},产线实习月度考核总评分 入库 :{}",studentStaffCode,examineInternshipGrade);
        Integer insertCount1 = productionLineExamineDao.insertExamineByLinneGroupLeader(examineInternshipGrade);
        ExamineInternshipGrade examineInternshipGrade2 =  ExamineInternshipGrade.builder().
                pid(37)
                .internshipType(1)
                .lineId(lineId)
                .examineTypeId(productionLineExamineTotalGradeVo.getRank())
                .isReExamine(0)
                .createCode(staffCode)
                .createDate(new Date())
                .status(1)
                .build();
        log.info("菁干班同学{},产线实习月度考核获得等级 入库 :{}",studentStaffCode,examineInternshipGrade2);
        Integer insertCount2 = productionLineExamineDao.insertExamineByLinneGroupLeader(examineInternshipGrade2);
        if( insertCount1<=0 || insertCount2 <= 0 ){
            return new ResultUtils(500,"插入實習期考核細項評分結果表失敗");
        }
        return new ResultUtils(101,"新增成功");
    }

    @Override
    @SysLog(value = "产线考核--产线负责人个人潜力考核提交")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ResultUtils insertExamineByLinneManager(ProductionLineInternshipVo productionLineInternshipVo) {
        log.info("产线负责人 个人潜力内容提交 入参:{}",productionLineInternshipVo);
        String staffCode = tokenAnalysis.getTokenUser().getStaffCode();
        log.info("当前用户信息 ->{}",tokenAnalysis.getTokenUser());
        String studentStaffCode = productionLineInternshipVo.getStudentStaffCode();
        Integer lineId = productionLineExamineDao.selectLineIdByStaffCode(studentStaffCode);
        Integer creationId = 0;
        List<ReportCreationList> enableReportList = reportCreationListDao.selectEnableReport();
        if (enableReportList == null || enableReportList.isEmpty()) {
            return new ResultUtils(100, "沒有可啟用任務", enableReportList);
        }
        for (ReportCreationList reportCreationList : enableReportList) {
            if (reportCreationList.getMissionType() == 98) {
                creationId = reportCreationList.getCreationId();
            }
        }
        //产线负责人品行信用评分插入对象
        String conductCredit = productionLineInternshipVo.getConductCreditByLineManager();//产线负责人品行信用分
        ExamineInternshipGrade grade1 = ExamineInternshipGrade.builder()
                .staffCode(studentStaffCode)
                .creationId(creationId)
                .internshipType(1)
                .lineId(lineId)
                .creationId(creationId)
                .examineGrade(conductCredit)
                .isReExamine(1)
                .createCode(staffCode)
                .createDate(new Date())
                .status(1)
                .build();
        Double conductCreditGrade = Double.valueOf(conductCredit);
        if(conductCreditGrade <= 10 && conductCreditGrade >= 8.1){
            grade1.setExamineTypeId(10);
        }else if(conductCreditGrade < 8.1 && conductCreditGrade >= 6.1){
            grade1.setExamineTypeId(11);
        }else if(conductCreditGrade < 6.1 && conductCreditGrade >= 4.1){
            grade1.setExamineTypeId(12);
        }else if(conductCreditGrade < 4.1 && conductCreditGrade >= 2.1){
            grade1.setExamineTypeId(13);
        }

        //产线负责人个人潜质得分 插入对象
        String personalPotential = productionLineInternshipVo.getPersonalPotentialByLineManager();//个人潜质得分
        ExamineInternshipGrade grade2 = ExamineInternshipGrade.builder()
                .staffCode(studentStaffCode)
                .creationId(creationId)
                .internshipType(1)
                .lineId(lineId)
                .examineGrade(personalPotential)
                .isReExamine(1)
                .createCode(staffCode)
                .createDate(new Date())
                .status(1)
                .build();
        Double personalPotentialGrade = Double.valueOf(personalPotential);
        if(personalPotentialGrade <= 10 && personalPotentialGrade >= 8.1){
            grade2.setExamineTypeId(14);
        }else if(personalPotentialGrade < 8.1 && personalPotentialGrade >= 6.1){
            grade2.setExamineTypeId(15);
        }else if(personalPotentialGrade < 6.1 && personalPotentialGrade >= 4.1){
            grade2.setExamineTypeId(16);
        }else if(personalPotentialGrade < 4.1 && personalPotentialGrade >= 2.1){
            grade2.setExamineTypeId(17);
        }

        //个人潜力复评总得分插入对象
        ExamineInternshipGrade grade3 = ExamineInternshipGrade.builder()
                .staffCode(studentStaffCode)
                .pid(0)
                .creationId(creationId)
                .internshipType(1)
                .lineId(lineId)
                .examineTypeId(3)
                .examineGrade(productionLineInternshipVo.getPotentialGradeByLineManager())
                .isReExamine(1)
                .createCode(staffCode)
                .createDate(new Date())
                .status(1)
                .build();
        List<ExamineInternshipGrade> internshipGradeDateList = productionLineExamineDao.selectSameInternshipGradeDate(1,creationId,studentStaffCode);
        if(internshipGradeDateList.size() > 0){
            for (ExamineInternshipGrade examineInternshipGrade : internshipGradeDateList) {
                examineInternshipGrade.setUpdateDate(new Date());
                examineInternshipGrade.setUpdateCode(staffCode);
                productionLineExamineDao.updateSameInternshipGradeDate(examineInternshipGrade);
            }
        }

        Integer grade3Count = productionLineExamineDao.insertExamineByLinneGroupLeader(grade3);//个人潜力复评总得分插入对象
        grade2.setPid(grade3.getExamineId());
        grade1.setPid(grade3.getExamineId());
        Integer grade2Count = productionLineExamineDao.insertExamineByLinneGroupLeader(grade2);//产线负责人个人潜质得分 插入对象
        Integer grade1Count = productionLineExamineDao.insertExamineByLinneGroupLeader(grade1);//产线负责人品行信用评分插入对象
        if( grade1Count<=0 || grade2Count <= 0 || grade3Count <= 0 ){
            return new ResultUtils(500,"插入實習期考核細項評分結果表失敗");
        }
        return new ResultUtils(100,"提交成功");
    }

    //根据条件查询考核评分列表
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ResultUtils inquireStudentsByParams(InquireStudentsByParamsVo inquireStudentsByParamsVo) {
        log.info("根据条件查询考核评分列表 入参->{}",inquireStudentsByParamsVo);
        List<ProductionLineExamineVo> list = new ArrayList<>();//返回前端结果集
        String examinStatus = inquireStudentsByParamsVo.getExaminStatus();
        String studentStaffCode = "";
        String studentStaffName = "";
        String studentStaffNameOrCode = inquireStudentsByParamsVo.getStudentStaffNameOrCode();
        if( checkIsStaffCode(studentStaffNameOrCode)){
            studentStaffCode = studentStaffNameOrCode;
        }else {
            studentStaffName = studentStaffNameOrCode;
        }
        
        String currentStaffCode = tokenAnalysis.getTokenUser().getStaffCode();
        log.info("当前用户信息 ->{}",tokenAnalysis.getTokenUser());
        //判断当前用户的角色
        ArrayList<ExamineStudentVo> studentList = new ArrayList<>();
        ArrayList<Integer>  userRoleList = productionLineExamineDao.queryUserRole(currentStaffCode);
        log.info("当前用户角色 ->{}",userRoleList);
        //从任务表找到新任务
        List<ReportCreationList> enableReportList = reportCreationListDao.selectEnableReport();
        if (enableReportList == null || enableReportList.isEmpty()) {
            return new ResultUtils(100, "没有可启用任务", enableReportList);
        }
        for (ReportCreationList reportCreationList : enableReportList) {
            Integer creationId = reportCreationList.getCreationId();
            //新任务里有产线实习月度考核
            log.info("找到 产线实习月度考核的可启用任务 ->{}",reportCreationList);
            for (Integer roleId : userRoleList) {
                if (roleId == 11) { //当前用户为产线组长
                    //找到产线组长旗下所有菁干班成员或者某个成员
                    studentList = selectLeaderUnderStudentsByParam(currentStaffCode,studentStaffName,studentStaffCode);
                    log.info("找到产线组长旗下所有菁干班成员或者某个成员 ->{}",studentList);
                    if (studentList != null && !studentList.isEmpty()) {
                        for (ExamineStudentVo examineStudentVo : studentList) {
                            List<ExamineInternshipGrade> examineInternshipGrades = productionLineExamineDao.selectPendingApproval(creationId, examineStudentVo.getStaffCode());
                            log.info("当前角色(产线组长)旗下菁干班成员考核评分表记录 ->{}",examineInternshipGrades);
                            ProductionLineExamineVo productionLineExamineVo = getStudentExamineRusultByParam(creationId,reportCreationList, examineInternshipGrades, examineStudentVo.getStaffCode(), examineStudentVo.getStaffName(), examinStatus);
                            Boolean isFillingInTime = screenData(productionLineExamineVo);//用当前时间来筛选在开放填写时间内
                            if(isFillingInTime){//在任务填写时间内则展示
                                if(productionLineExamineVo.getMissionTypeName() != null &&  !"".equals(productionLineExamineVo.getMissionTypeName()) ){
                                    list.add(productionLineExamineVo);
                                }
                            }

                        }
                    }
                }else if (roleId == 12){//当前用户为产线负责人
                    //找到产线负责人下所有菁干班成员
                    studentList = selectManagerUnderStudentsByParam(currentStaffCode,studentStaffName,studentStaffCode,reportCreationList);
                    log.info("找到负责人旗下所有菁干班成员或者某个成员 ->{}",studentList);
                    if (studentList != null && !studentList.isEmpty()) {
                        for (ExamineStudentVo examineStudentVo : studentList) {
                            List<ExamineInternshipGrade> internshipGradeList = productionLineExamineDao.selectPendingApprovalForManager(creationId,examineStudentVo.getStaffCode());
                            log.info("当前角色(产线组长)旗下菁干班成员考核评分表记录 ->{}",internshipGradeList);
                            ProductionLineExamineVo productionLineExamineVo = getStudentExamineRusultForManagerByParam(creationId,reportCreationList, internshipGradeList ,examineStudentVo.getStaffCode(),examineStudentVo.getStaffName(),examinStatus);
                            Boolean isFillingInTime = screenData(productionLineExamineVo);//用当前时间来筛选在开放填写时间内
                            if(isFillingInTime){//在任务填写时间内则展示
                                if(productionLineExamineVo.getMissionTypeName() != null &&  !"".equals(productionLineExamineVo.getMissionTypeName()) ){
                                    list.add(productionLineExamineVo);
                                }
                            }
                        }
                    }
                }else if (roleId == 18){//当前用户为人资
                    //找到产线负责人下所有菁干班成员
                    studentList = productionLineExamineDao.selectNewStudentsByParam(studentStaffName,studentStaffCode);
                    log.info("当前角色为人资,旗下菁干班所有成员数 ->{}",studentList.size());
                    if (studentList != null && !studentList.isEmpty()) {
                        for (ExamineStudentVo examineStudentVo : studentList) {
                            List<ExamineInternshipGrade> examineInternshipGrades = productionLineExamineDao.selectPendingApprovalForHR(creationId,examineStudentVo.getStaffCode());
                            log.info("当前角色(人资)旗下菁干班成员考核评分表记录 ->{}",examineInternshipGrades);
                            ProductionLineExamineVo productionLineExamineVo = getStudentExamineRusultForHRByParam(reportCreationList.getCreationId(),reportCreationList, examineInternshipGrades, examineStudentVo.getStaffCode(), examineStudentVo.getStaffName(), examinStatus);
                            Boolean isFillingInTime = screenData(productionLineExamineVo);//用当前时间来筛选在开放填写时间内
                            if(isFillingInTime){//在任务填写时间内则展示
                                if(productionLineExamineVo.getMissionTypeName() != null &&  !"".equals(productionLineExamineVo.getMissionTypeName()) ){
                                    list.add(productionLineExamineVo);
                                }
                            }

                        }
                    }
                }
            }
        }
        return new ResultUtils(100, "成功查詢所有信息", list);
    }

    private ProductionLineExamineVo getStudentExamineRusultForHRByParam(Integer creationId, ReportCreationList reportCreationList, List<ExamineInternshipGrade> examineInternshipGrades, String staffCode, String staffName, String examinStatus) {
        ProductionLineExamineVo productionLineExamineVo =ProductionLineExamineVo.builder().build();
        if("待評".equals(examinStatus) ){//人资未评,考核表中无数据
            if(examineInternshipGrades.isEmpty()){
                productionLineExamineVo =ProductionLineExamineVo.builder()
                        .studentName(staffName)
                        .stateId(1)
                        .stateName("待評")
                        .missionType(98)
                        .missionTypeName("產線實習月度考核")
                        .creationId(reportCreationList.getCreationId())
                        .startTime(reportCreationList.getStartTime())
                        .endTime(reportCreationList.getEndTime())
                        .isEnable(reportCreationList.getIsEnable())
                        .studentCode(staffCode)
                        .build();
            }

        } else if ("已評".equals(examinStatus) ){//人资已评 ->考核表中有数据
            if(!examineInternshipGrades.isEmpty()){
                productionLineExamineVo =ProductionLineExamineVo.builder()
                        .studentName(staffName)
                        .stateId(2)
                        .stateName("已評")
                        .missionType(98)
                        .missionTypeName("產線實習月度考核")
                        .creationId(reportCreationList.getCreationId())
                        .startTime(reportCreationList.getStartTime())
                        .endTime(reportCreationList.getEndTime())
                        .isEnable(reportCreationList.getIsEnable())
                        .studentCode(staffCode)
                        .build();
            }

        }else {
            productionLineExamineVo = getStudentExamineRusultForHR(creationId,reportCreationList, examineInternshipGrades, staffCode,staffName);
        }
        return productionLineExamineVo;

    }


    private ArrayList<ExamineStudentVo> selectManagerUnderStudentsByParam(String currentStaffCode, String studentStaffName,String studentStaffCode,ReportCreationList reportCreationList) {
        ArrayList<ExamineStudentVo> examineStudentVos = new ArrayList<>();
        //找出菁干班同学所对应的最新的产线信息
        List<NewLineForStudentsVo> newLineForStudentsList = productionLineExamineDao.selectNewLineForStudents();
        if(newLineForStudentsList != null && newLineForStudentsList.size() > 0){
            for (NewLineForStudentsVo newLineForStudentsVo : newLineForStudentsList) {
                if(currentStaffCode.equals(newLineForStudentsVo.getLineManageCode())){
                    ExamineStudentVo examineStudentVo = new ExamineStudentVo();
                    examineStudentVo.setStaffCode(newLineForStudentsVo.getStaffCode());
                    examineStudentVo.setStaffName(newLineForStudentsVo.getStaffName());
                    examineStudentVos.add(examineStudentVo);
                }
            }
        }
        return examineStudentVos;
    }

    private ArrayList<ExamineStudentVo> selectLeaderUnderStudentsByParam(String currentStaffCode, String studentStaffName,String studentStaffCode) {
        ArrayList<ExamineStudentVo> examineStudentVos = new ArrayList<>();
        //找出最新菁干班同学所对应的最新的产线信息
        List<NewLineForStudentsVo> newLineForStudentsList = productionLineExamineDao.selectNewLineForStudentsByParam(studentStaffName,studentStaffCode);
        if(newLineForStudentsList != null && newLineForStudentsList.size() > 0){
            for (NewLineForStudentsVo newLineForStudentsVo : newLineForStudentsList) {
                if(currentStaffCode.equals(newLineForStudentsVo.getLineGroupLeaderCode())){
                    ExamineStudentVo examineStudentVo = new ExamineStudentVo();
                    examineStudentVo.setStaffCode(newLineForStudentsVo.getStaffCode());
                    examineStudentVo.setStaffName(newLineForStudentsVo.getStaffName());
                    examineStudentVos.add(examineStudentVo);
                }
            }
        }



        return examineStudentVos;
    }

    //查看当前菁干班同学产线实习月报
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ResultUtils getMonthlyReport(ReportProductionListVo reportProductionListVo) {
        log.info("查看当前菁干班同学产线实习月报 入参 ->{}",reportProductionListVo);
        String staffCode = reportProductionListVo.getStaffCode();
        List<ReportProductionListVo> list = productionLineExamineDao.getMonthlyReport(staffCode);
        if(list.size() > 0){
            reportProductionListVo = list.get(0);
            String staffName = productionLineExamineDao.getStaffNameByStaffCode(staffCode);
            String internshipState = reportProductionListVo.getInternshipState();
            String internshipStateName = null;
            List<String> internshipStateList = new ArrayList<>();
            if (internshipState != null && !internshipState.equals("")) {
                String[] split = internshipState.split(",");
                for (String state : split) {
                    if (null != state) {
                        internshipStateList.add(productionLineExamineDao.getTypeNameById(state));
                    }
                }
                internshipStateName = StringUtils.join(internshipStateList.toArray(), ",");
            }
            String lineName = productionLineExamineDao.getLineNameById(reportProductionListVo.getLineId());
            reportProductionListVo.setStaffName(staffName);
            reportProductionListVo.setInternshipState(internshipStateName);
            reportProductionListVo.setLineName(lineName);
            reportProductionListVo.setMissionTypeName("產線實習月度考核");
            reportProductionListVo.setReportTypeName("產線實習月報");
        }

        return new ResultUtils(100, "成功查詢當前菁幹班同學所有產線實習月報", list);
    }

    //查看当前菁干班同学产线实习周报
    @SneakyThrows
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ResultUtils getWeeklyReport(WeeklyReportProductionListVo weeklyReportProductionListVo)  {
        log.info("查看当前菁干班同学产线实习周报 入参 ->{}",weeklyReportProductionListVo);
        String staffCode = weeklyReportProductionListVo.getStaffCode();
        List<ReportCreationList> enableReportList = reportCreationListDao.selectEnableReport();
        if (enableReportList == null || enableReportList.isEmpty()) {
            return new ResultUtils(100, "没有可启用任务", enableReportList);
        }
        Date internshipStart = enableReportList.get(0).getInternshipStart();
        HashMap<String, Date> firstAndEndDate = getFirstAndEndDate(internshipStart);
        Date firstDate = firstAndEndDate.get("firstDate");
        Date lastDate = firstAndEndDate.get("lastDate");

        //根据工号和产线实习任务报告的实习时间来确定周报
        List<WeeklyReportProductionListVo> list = productionLineExamineDao.getWeeklyReport(staffCode,firstDate,lastDate);
        if(list != null && list.size() > 0){
            for (WeeklyReportProductionListVo reportProductionListVo : list) {
                reportProductionListVo.setMissionTypeName("產線實習月度考核");
                reportProductionListVo.setReportTypeName("產線實習週報");
                String mentalState = productionLineExamineDao.getTypeNameById(reportProductionListVo.getMentalState());//本周状态
                reportProductionListVo.setMentalState(mentalState);
                String attendance = productionLineExamineDao.getTypeNameById(reportProductionListVo.getAttendance());//本周考勤
                reportProductionListVo.setAttendance(attendance);
                Integer reportId = reportProductionListVo.getReportId();
                List<ReportProductionWeekQuestion> reportProductionWeekQuestionList = productionLineExamineDao.getWeekQuestions(reportId);
                reportProductionListVo.setReportProductionWeekQuestionList(reportProductionWeekQuestionList);
            }
        }

        return new ResultUtils(100, "成功查詢當前菁幹班同學所有產線實習週報", list);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ResultUtils inquireTotalGrade(ProductionLineExamineTotalGradeVo productionLineExamineTotalGradeVo) {
        String currentStaffCode = tokenAnalysis.getTokenUser().getStaffCode();
        log.info("查看总评分 入参 ->{}",productionLineExamineTotalGradeVo);
        String staffCode = productionLineExamineTotalGradeVo.getStaffCode();//菁干班同学工号
        //String staffName = productionLineExamineTotalGradeVo.getStaffName();//菁干班同学姓名
        Integer lineId = productionLineExamineDao.selectLineIdByStaffCode(staffCode);
        String totalGrade = null;//本月考核总得分
        Integer rank = null;//本月考核等级
        Integer creationId = 0;
        List<ReportCreationList> enableReportList = reportCreationListDao.selectEnableReport();
        if (enableReportList == null || enableReportList.isEmpty()) {
            return new ResultUtils(100, "沒有可啟用任務", enableReportList);
        }
        for (ReportCreationList reportCreationList : enableReportList) {
            if (reportCreationList.getMissionType() == 98) {
                creationId = reportCreationList.getCreationId();
            }
        }

        //查看此同学任务下有没有在考核记录表插入总分及等级,有则直接返回,无则四个自分数相加
        List<ExamineInternshipGrade> totalGradeList = productionLineExamineDao.getProductionLineInternshipGrade( staffCode,creationId);//获取产线考核总分数
        List<ExamineInternshipGrade> rankList = productionLineExamineDao.inquireRank(staffCode, creationId);
        if(totalGradeList.size() > 0 && rankList.size() > 0 ){
            ExamineInternshipGrade totalGradeObj = totalGradeList.get(0);
            String totalGradeData = totalGradeObj.getExamineGrade();//获取产线考核总分数
            ExamineInternshipGrade rankObj = rankList.get(0);
            String rankdata = String.valueOf(rankObj.getExamineTypeId());//本月考核等级

            HashMap<String,String> gradeRankMap =selectGradeRank(creationId,staffCode,lineId,currentStaffCode);
            String totalGradeMap = gradeRankMap.get("totalGrade");
            String rankMap = gradeRankMap.get("rank");
            if(totalGradeData.equals(totalGradeMap) && rankdata.equals(rankMap)){
                //如果可以直接获取到产线总分数及等级,并且和四个子分数相加一致,就直接返回
                totalGrade = totalGradeData;
                rank = Integer.valueOf(rankdata);
            }else {
                //如果可以直接获取到产线总分数及等级,并且和四个子分数不一致,则返回四个子分数相加,入库,并将之前的数据状态置为0
                totalGrade = totalGradeMap;
                if(rankMap != null ){
                    rank = Integer.valueOf(rankMap);
                }
                totalGradeObj.setUpdateCode(currentStaffCode);
                totalGradeObj.setUpdateDate(new Date());
                totalGradeObj.setStatus(0);
                productionLineExamineDao.update(totalGradeObj);
                rankObj.setUpdateCode(currentStaffCode);
                rankObj.setUpdateDate(new Date());
                rankObj.setStatus(0);
                productionLineExamineDao.update(rankObj);
                ExamineInternshipGrade examineInternshipGrade6 =  ExamineInternshipGrade.builder()
                        .staffCode(staffCode)
                        .pid(0)
                        .examineTypeId(36)
                        .creationId(creationId)
                        .internshipType(1)
                        .lineId(lineId)
                        .examineTypeId(36)
                        .examineGrade(totalGrade)
                        .isReExamine(0)
                        .createCode(currentStaffCode)
                        .createDate(new Date())
                        .status(1)
                        .build();
                log.info("菁干班同学{},产线实习月度考核总评分 入库 :{}",staffCode,examineInternshipGrade6);
                productionLineExamineDao.insertExamineByLinneGroupLeader(examineInternshipGrade6);
                ExamineInternshipGrade examineInternshipGrade7 =  ExamineInternshipGrade.builder()
                        .staffCode(staffCode)
                        .pid(0)
                        .examineTypeId(37)
                        .creationId(creationId)
                        .internshipType(1)
                        .lineId(lineId)
                        .examineTypeId(rank)
                        .isReExamine(0)
                        .createCode(currentStaffCode)
                        .createDate(new Date())
                        .status(1)
                        .build();
                log.info("菁干班同学{},产线实习月度考核获得等级 入库 :{}",staffCode,examineInternshipGrade7);
                productionLineExamineDao.insertExamineByLinneGroupLeader(examineInternshipGrade7);

            }
        }else {
            //如果没有直接获取到产线总分及等级,就返回四个子分数相加,并入库
            HashMap<String,String> gradeRankMap =selectGradeRank(creationId,staffCode,lineId,currentStaffCode);
            totalGrade = gradeRankMap.get("totalGrade");
            if(gradeRankMap.get("rank") != null){
                rank = Integer.valueOf(gradeRankMap.get("rank"));
            }
            if(totalGrade != null && rank != null){
                ExamineInternshipGrade examineInternshipGrade6 =  ExamineInternshipGrade.builder()
                        .staffCode(staffCode)
                        .pid(0)
                        .examineTypeId(36)
                        .creationId(creationId)
                        .internshipType(1)
                        .lineId(lineId)
                        .examineTypeId(36)
                        .examineGrade(totalGrade)
                        .isReExamine(0)
                        .createCode(currentStaffCode)
                        .createDate(new Date())
                        .status(1)
                        .build();
                log.info("菁干班同学{},产线实习月度考核总评分 入库 :{}",staffCode,examineInternshipGrade6);
                productionLineExamineDao.insertExamineByLinneGroupLeader(examineInternshipGrade6);
                ExamineInternshipGrade examineInternshipGrade7 =  ExamineInternshipGrade.builder()
                        .staffCode(staffCode)
                        .pid(0)
                        .creationId(creationId)
                        .internshipType(1)
                        .lineId(lineId)
                        .examineTypeId(rank)
                        .isReExamine(0)
                        .createCode(currentStaffCode)
                        .createDate(new Date())
                        .status(1)
                        .build();
                log.info("菁干班同学{},产线实习月度考核获得等级 入库 :{}",staffCode,examineInternshipGrade7);
                productionLineExamineDao.insertExamineByLinneGroupLeader(examineInternshipGrade7);
            }


        }

        productionLineExamineTotalGradeVo.setTotalGrade(totalGrade);
        productionLineExamineTotalGradeVo.setRank(rank);
        return new ResultUtils(100,"查询成功",productionLineExamineTotalGradeVo);
    }

    private HashMap<String,String> selectGradeRank(Integer creationId,String staffCode,Integer lineId,String currentStaffCode) {
        String totalGrade = null;//本月考核总得分
        String rank = null;//本月考核等级
        String examineGrade = null;//获得产线实习总得分
        String examineGrade3 = null;//获得个人潜力复评总得分
        String examineGrade4 = null;//获得出勤奖惩总得分
        HashMap<String,String> gradeRankMap = new HashMap<>();
        ExamineInternshipGrade examineInternshipGrade =  productionLineExamineDao.inquireGrade(creationId,staffCode,"2");//获得产线实习总得分
        if(examineInternshipGrade != null && examineInternshipGrade.getExamineGrade() != null &&  !"".equals(examineInternshipGrade.getExamineGrade())){
            examineGrade =  examineInternshipGrade.getExamineGrade();//获得产线实习总得分
        }
        ExamineInternshipGrade examineInternshipGrade3 = productionLineExamineDao.inquirePersonalPotentialGrade(creationId,staffCode,"1");

        if(examineInternshipGrade3 != null && examineInternshipGrade3.getExamineGrade() != null && !"".equals(examineInternshipGrade3.getExamineGrade())){
            examineGrade3 =  examineInternshipGrade3.getExamineGrade();//获得个人潜力复评总得分
        }
        ExamineInternshipGrade examineInternshipGrade4 =  productionLineExamineDao.inquireGrade(creationId,staffCode,"4");//获得出勤考核奖惩总得分
        if(examineInternshipGrade4 != null && examineInternshipGrade4.getExamineGrade() != null && !"".equals(examineInternshipGrade4.getExamineGrade())){
            examineGrade4 =  examineInternshipGrade4.getExamineGrade();//获得出勤考核奖惩总得分
        }

        if(examineGrade != null && examineGrade3 != null && examineGrade4 != null ){
            //须得产线实习总分,个人潜力复评总得分,出勤奖惩总得分, 三个都有分 才出本月考核总得分
            totalGrade = String.valueOf(new BigDecimal(examineInternshipGrade.getExamineGrade()).add(new BigDecimal(examineInternshipGrade3.getExamineGrade()))
                    .add(new BigDecimal(examineInternshipGrade4.getExamineGrade())));
            log.info("产线实习总分数:{}",totalGrade);
            if(Double.valueOf(totalGrade) >= 95){
                rank = "38";
            }else if(Double.valueOf(totalGrade) >= 85 && Double.valueOf(totalGrade) < 95){
                rank = "39";
            }else if(Double.valueOf(totalGrade) >= 75 && Double.valueOf(totalGrade) < 85){
                rank = "40";
            }else if(Double.valueOf(totalGrade) >= 65 && Double.valueOf(totalGrade) < 75){
                rank = "41";
            }else if(Double.valueOf(totalGrade) < 65 ){
                rank = "42";
            }
            gradeRankMap.put("totalGrade",totalGrade);
            gradeRankMap.put("rank",rank);

        }
        return gradeRankMap;

    }

    private ProductionLineExamineVo getStudentExamineRusultForManagerByParam(Integer creationId, ReportCreationList reportCreationList, List<ExamineInternshipGrade> internshipGradeList, String studentStaffCode, String studentStaffName, String examinStatus) {
        ProductionLineExamineVo productionLineExamineVo =ProductionLineExamineVo.builder().build();
        int isNoReExamine = 0;//初评次数
        int isReExamine = 0;//复评次数
        if (internshipGradeList != null && !internshipGradeList.isEmpty()){
            for (ExamineInternshipGrade examineInternshipGrade : internshipGradeList) {
                if(examineInternshipGrade.getExamineTypeId() == 3  && examineInternshipGrade.getIsReExamine() == 0){
                    //必须符合以上条件才算已初评
                    isNoReExamine = isNoReExamine + 1;
                }else if(examineInternshipGrade.getExamineTypeId() == 3  && examineInternshipGrade.getIsReExamine() == 1){
                    isReExamine = isReExamine +1;
                }
            }
            //产线负责人要找待评人员 ->初评次数大于0,复评次数=0
            if("待評".equals(examinStatus) ){
                if(isNoReExamine > 0 && isReExamine == 0){
                    productionLineExamineVo =ProductionLineExamineVo.builder()
                            .studentName(studentStaffName)
                            .stateId(1)
                            .stateName("待評")
                            .missionType(98)
                            .missionTypeName("產線實習月度考核")
                            .creationId(reportCreationList.getCreationId())
                            .startTime(reportCreationList.getStartTime())
                            .endTime(reportCreationList.getEndTime())
                            .isEnable(reportCreationList.getIsEnable())
                            .studentCode(studentStaffCode)
                            .build();
                }
            }else if("已評".equals(examinStatus) ){
                if(isNoReExamine > 0 && isReExamine > 0){
                    //产线负责人已评人员 ->初评次数 大于0 ,复评次数大于0
                    productionLineExamineVo =ProductionLineExamineVo.builder()
                            .studentName(studentStaffName)
                            .stateId(2)
                            .stateName("已評")
                            .missionType(98)
                            .missionTypeName("產線實習月度考核")
                            .creationId(reportCreationList.getCreationId())
                            .startTime(reportCreationList.getStartTime())
                            .endTime(reportCreationList.getEndTime())
                            .isEnable(reportCreationList.getIsEnable())
                            .studentCode(studentStaffCode)
                            .build();
                }

            }else {
                productionLineExamineVo = getStudentExamineRusultForManager(creationId,reportCreationList, internshipGradeList, studentStaffCode,studentStaffName);
            }


        }
        return productionLineExamineVo;
    }

    private ProductionLineExamineVo getStudentExamineRusultByParam(int creationId, ReportCreationList reportCreationList, List<ExamineInternshipGrade> internshipGradeList, String studentStaffCode, String studentStaffName, String examinStatus) {
        ProductionLineExamineVo productionLineExamineVo =ProductionLineExamineVo.builder().build();
        int isNoReExamine = 0;//初评次数
        int isReExamine = 0;//复评次数
        if (internshipGradeList != null && !internshipGradeList.isEmpty()) {
            for (ExamineInternshipGrade examineInternshipGrade : internshipGradeList) {
                if (examineInternshipGrade.getCreationId() == creationId && examineInternshipGrade.getExamineTypeId() == 3  && examineInternshipGrade.getIsReExamine() == 0) {
                    //必须符合以上条件才算已初评
                    isNoReExamine = isNoReExamine + 1;
                } else if (examineInternshipGrade.getCreationId() == creationId && examineInternshipGrade.getExamineTypeId() == 3  && examineInternshipGrade.getIsReExamine() == 1) {
                    isReExamine = isReExamine + 1;
                }
            }
        }
            //产线组长要找待评人员 ->初评次数 = 0,复评次数=0
        if("待評".equals(examinStatus)){
            if(isNoReExamine == 0 && isReExamine == 0){
                productionLineExamineVo =ProductionLineExamineVo.builder()
                        .studentName(studentStaffName)
                        .stateId(1)
                        .stateName("待評")
                        .missionType(98)
                        .missionTypeName("產線實習月度考核")
                        .creationId(reportCreationList.getCreationId())
                        .startTime(reportCreationList.getStartTime())
                        .endTime(reportCreationList.getEndTime())
                        .isEnable(reportCreationList.getIsEnable())
                        .studentCode(studentStaffCode)
                        .build();
            }
        } else if ("已評".equals(examinStatus)){//产线负责人已评 ->初评次数 大于0
            if(isNoReExamine > 0 ){
                productionLineExamineVo =ProductionLineExamineVo.builder()
                        .studentName(studentStaffName)
                        .stateId(2)
                        .stateName("已評")
                        .missionType(98)
                        .missionTypeName("產線實習月度考核")
                        .creationId(reportCreationList.getCreationId())
                        .startTime(reportCreationList.getStartTime())
                        .endTime(reportCreationList.getEndTime())
                        .isEnable(reportCreationList.getIsEnable())
                        .studentCode(studentStaffCode)
                        .build();
            }
        }else {
            productionLineExamineVo = getStudentExamineRusult(creationId,reportCreationList, internshipGradeList, studentStaffCode,studentStaffName);
        }
        return productionLineExamineVo;
    }

    private ProductionLineExamineVo getStudentExamineRusult(int creationId,ReportCreationList reportCreationList,List<ExamineInternshipGrade> internshipGradeList ,String studentStaffCode,String studentStaffName) {
        ProductionLineExamineVo productionLineExamineVo =ProductionLineExamineVo.builder().build();
        int isNoReExamine = 0;//初评次数
        int isReExamine = 0;//复评次数
        if(internshipGradeList != null && !internshipGradeList.isEmpty()){
            for (ExamineInternshipGrade examineInternshipGrade : internshipGradeList) {
                if(examineInternshipGrade.getCreationId() == creationId && examineInternshipGrade.getExamineTypeId() == 3
                        && examineInternshipGrade.getIsReExamine() == 0){
                    //必须符合以上条件才算已初评
                    isNoReExamine = isNoReExamine + 1;
                }else if(examineInternshipGrade.getCreationId() == creationId && examineInternshipGrade.getExamineTypeId() == 3
                        && examineInternshipGrade.getIsReExamine() == 1){
                    isReExamine = isReExamine +1;
                }
            }
        }

        //产线组长待评 初评次数=0 复评次数=0
        if(isNoReExamine == 0 && isReExamine == 0){
            productionLineExamineVo =ProductionLineExamineVo.builder()
                    .studentName(studentStaffName)
                    .stateId(1)
                    .stateName("待評")
                    .missionType(98)
                    .missionTypeName("產線實習月度考核")
                    .creationId(reportCreationList.getCreationId())
                    .startTime(reportCreationList.getStartTime())
                    .endTime(reportCreationList.getEndTime())
                    .isEnable(reportCreationList.getIsEnable())
                    .studentCode(studentStaffCode)
                    .build();
        }
        //产线组长已评 ->初评次数大于0
        if(isNoReExamine > 0){
            productionLineExamineVo =ProductionLineExamineVo.builder()
                    .studentName(studentStaffName)
                    .stateId(2)
                    .stateName("已評")
                    .missionType(98)
                    .missionTypeName("產線實習月度考核")
                    .creationId(reportCreationList.getCreationId())
                    .startTime(reportCreationList.getStartTime())
                    .endTime(reportCreationList.getEndTime())
                    .isEnable(reportCreationList.getIsEnable())
                    .studentCode(studentStaffCode)
                    .build();
        }

        return productionLineExamineVo;
    }

    //检查传入字段是否为工号
    private boolean checkIsStaffCode(String studentStaffNameOrCode) {
        String reg = "(?i)^(?!([a-z]*|\\d*)$)[a-z\\d]+$";
        boolean matches = studentStaffNameOrCode.matches(reg);
        return matches;
    }

    //根据Date获取年月初及年月末的Date时间
    public HashMap<String,Date> getFirstAndEndDate(Date dateTime) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTime);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        String firstday = format.format(calendar.getTime());
        Date firstDate = format.parse(firstday);
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 0);
        String lastday = format.format(calendar.getTime());
        Date lastDate = format.parse(lastday);
        HashMap<String,Date> timeMap = new HashMap<>();
        timeMap.put("firstDate",firstDate);
        timeMap.put("lastDate",lastDate);
        return timeMap;
    }





}
