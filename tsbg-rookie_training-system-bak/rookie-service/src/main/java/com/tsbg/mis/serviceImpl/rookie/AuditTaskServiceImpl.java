package com.tsbg.mis.serviceImpl.rookie;

import com.tsbg.mis.annotation.SysLog;
import com.tsbg.mis.dao.jurisdiction.UserRoleDao;
import com.tsbg.mis.dao.rookie.ClassStudentInfoDao;
import com.tsbg.mis.dao.rookie.DepartTargetListDao;
import com.tsbg.mis.dao.rookie.ReportProductionListDao;
import com.tsbg.mis.dao.rookie.RookieApprovalInfoDao;
import com.tsbg.mis.dao.signed.BusinessListDao;
import com.tsbg.mis.dao.signed.SignedWorkflowDao;
import com.tsbg.mis.jurisdiction.model.UserInfo;
import com.tsbg.mis.rookie.bag.MonthTargetApprovalInfoPackage;
import com.tsbg.mis.rookie.model.*;
import com.tsbg.mis.rookie.vo.RookieApprovalInfoVo;
import com.tsbg.mis.service.rookie.AuditTaskService;
import com.tsbg.mis.serviceImpl.jurisdiction.base.TokenAnalysis;
import com.tsbg.mis.signed.bag.RookieApprovalInfoPackage;
import com.tsbg.mis.signed.bag.SignedNodeComparator;
import com.tsbg.mis.signed.model.BusinessList;
import com.tsbg.mis.signed.model.SignedWorkflow;
import com.tsbg.mis.util.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 汪永晖
 */
@Service
@Slf4j
public class AuditTaskServiceImpl implements AuditTaskService {

    private final Integer USED_STATUS = 1;

    private final Integer DAILY_REPORT_PRODUCTION = 1;

    private final Integer PROJ_ID = 10;

    private final Integer SUBMIT_STATUS = 2;

    private final Integer UN_AUDIT_STATUS = 3;

    private final Integer REJECT = 0;

    private final Integer PASS = 1;

    private final Integer UN_PASS = 0;

    private final Integer STATE_PASS = 4;

    private final Integer STATE_PASS_AUDIT = 3;

    private final String INVEST_TABLE_NAME = "report_production_list";

    private final String INVEST_FIELD_NAME = "report_id";

    private final TokenAnalysis tokenAnalysis;

    private final RookieApprovalInfoDao rookieApprovalInfoDao;

    private final BusinessListDao businessListDao;

    private final SignedWorkflowDao signedWorkflowDao;

    private final ReportProductionListDao reportProductionListDao;

    private final UserRoleDao userRoleDao;

    private final DepartTargetListDao departTargetListDao;

    private final ClassStudentInfoDao classStudentInfoDao;

    public AuditTaskServiceImpl(TokenAnalysis tokenAnalysis, RookieApprovalInfoDao rookieApprovalInfoDao, BusinessListDao businessListDao, SignedWorkflowDao signedWorkflowDao, ReportProductionListDao reportProductionListDao, UserRoleDao userRoleDao, DepartTargetListDao departTargetListDao, ClassStudentInfoDao classStudentInfoDao) {
        this.tokenAnalysis = tokenAnalysis;
        this.rookieApprovalInfoDao = rookieApprovalInfoDao;
        this.businessListDao = businessListDao;
        this.signedWorkflowDao = signedWorkflowDao;
        this.reportProductionListDao = reportProductionListDao;
        this.userRoleDao = userRoleDao;
        this.departTargetListDao = departTargetListDao;
        this.classStudentInfoDao = classStudentInfoDao;
    }

    /**
     * 查询所有的审核任务
     *
     * @return
     */
    @Override
    public ResultUtils queryAll() {
        UserInfo currentUser = tokenAnalysis.getTokenUser();
        String staffCode = currentUser.getStaffCode();
        List<RookieApprovalInfoVo> rookieApprovalInfoVoList = rookieApprovalInfoDao.queryAllInfoVo(RookieApprovalInfo.builder()
                .approveStaffCode(staffCode)
                .build());

        List<RookieApprovalInfoVo> rookieProposalList = rookieApprovalInfoDao.queryAllInfoVoFromProposal(RookieApprovalInfo.builder()
                .approveStaffCode(staffCode)
                .build());

        List<RookieApprovalInfoVo> rookieDepartList = rookieApprovalInfoDao.queryAllInfoVoFromDepart(RookieApprovalInfo.builder()
                .approveStaffCode(staffCode)
                .build());

        if (rookieApprovalInfoVoList == null) {
            return new ResultUtils(500, "查詢失敗");
        }
        rookieApprovalInfoVoList.addAll(rookieProposalList);
        rookieApprovalInfoVoList.addAll(rookieDepartList);

        // 去重
        Map<Integer, List<RookieApprovalInfoVo>> map = new HashMap<>();
        Integer count = 1;
        for (RookieApprovalInfoVo rookie : rookieApprovalInfoVoList) {
            List<RookieApprovalInfoVo> r = rookieApprovalInfoVoList.stream()
                    .filter(rookieApprovalInfoVo -> {
                        if (rookie != null && rookie.getInvestTableName() != null && rookie.getInvestFieldName() != null && rookie.getInvestFieldValue() != null &&
                                rookie.getInvestTableName().equals(rookieApprovalInfoVo.getInvestTableName()) &&
                                rookie.getInvestFieldName().equals(rookieApprovalInfoVo.getInvestFieldName()) &&
                                rookie.getInvestFieldValue().equals(rookieApprovalInfoVo.getInvestFieldValue())) {
                            return true;
                        }
                        return false;
                    })
                    .collect(Collectors.toList());
            if (r != null && r.size() > 1) {
                map.put(count++, r);
            }
        }

        if (map.size() > 0) {
            for (Map.Entry<Integer, List<RookieApprovalInfoVo>> entry : map.entrySet()) {
                rookieApprovalInfoVoList.removeAll(entry.getValue());
                List<RookieApprovalInfoVo> rookieApprovalInfoVos = entry.getValue().stream()
                        .filter(rookieApprovalInfoVo1 -> {
                            if (rookieApprovalInfoVo1 != null && STATE_PASS_AUDIT.equals(rookieApprovalInfoVo1.getIsPass())) {
                                return true;
                            }
                            return false;
                        })
                        .collect(Collectors.toList());
                if (rookieApprovalInfoVos != null) {
                    RookieApprovalInfoVo rookieApprovalInfoVo = rookieApprovalInfoVos.size() > 0 ? rookieApprovalInfoVos.get(0) : entry.getValue().get(0);
                    rookieApprovalInfoVoList.add(rookieApprovalInfoVo);
                }
            }
        }
        return new ResultUtils(100, "成功查詢所有的審核任務訂閱", rookieApprovalInfoVoList);
    }

    /**
     * 查询所有报告类型
     *
     * @return
     */
    @Override
    public ResultUtils inquireBusiness() {
        List<BusinessList> businessLists = businessListDao.queryAll(BusinessList.builder()
                .status(USED_STATUS)
                .projId(PROJ_ID)
                .build());
        return new ResultUtils(100, "成功查詢所有報告類型", businessLists);
    }

    /**
     * 产线主管签核报告
     *
     * @param rookieApprovalInfoPackage
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @SysLog(value = "主管签核报告")
    public ResultUtils auditReport(RookieApprovalInfoPackage rookieApprovalInfoPackage) {
        UserInfo currentUser = tokenAnalysis.getTokenUser();
        String staffCode = currentUser.getStaffCode();

        RookieApprovalInfo rookieApprovalInfo = rookieApprovalInfoDao.queryById(rookieApprovalInfoPackage.getRookieApprovalId());
        if (rookieApprovalInfo == null || !staffCode.equals(rookieApprovalInfo.getApproveStaffCode())) {
            return new ResultUtils(500, "不能審核該報告");
        }
        if (!UN_AUDIT_STATUS.equals(rookieApprovalInfo.getIsPass())) {
            return new ResultUtils(500, "該報告不是待審核狀態");
        }

        String approveOpinion = rookieApprovalInfoPackage.getApproveOpinion();
        String grade = rookieApprovalInfoPackage.getGrade();
        Integer isPass = rookieApprovalInfoPackage.getIsPass();

        // 通过
        if (PASS.equals(isPass)) {
            // 更新签核记录表
            rookieApprovalInfoDao.update(RookieApprovalInfo.builder()
                    .rookieApprovalId(rookieApprovalInfoPackage.getRookieApprovalId())
                    .signedDate(new Date())
                    .approveOpinion(approveOpinion)
                    .grade(grade)
                    .isPass(isPass)
                    .levelType(rookieApprovalInfoPackage.getLevelType())
                    .build());

            // 查询签核流程表——下一个节点
            List<SignedWorkflow> signedWorkflowList = signedWorkflowDao.queryAll(SignedWorkflow.builder()
                    .businessId(rookieApprovalInfo.getBusinessId())
                    .status(USED_STATUS)
                    .build());
            if (signedWorkflowList == null || CollectionUtils.isEmpty(signedWorkflowList)) {
                return new ResultUtils(500, "该签核流程为空");
            }
            signedWorkflowList.sort(new SignedNodeComparator());

            Integer approveSequence = rookieApprovalInfo.getApproveSequence() + 1;
            Integer reportId = Integer.valueOf(rookieApprovalInfo.getInvestFieldValue());
            log.info("reportId -> {}", reportId);
            List<SignedWorkflow> signed = signedWorkflowList.stream()
                    .filter(signedWorkflow -> approveSequence.equals(signedWorkflow.getSignSequence()))
                    .collect(Collectors.toList());
            // 是最后一个签核节点
            if (CollectionUtils.isEmpty(signed)) {
                // 修改报告的状态
                reportProductionListDao.updateReport(rookieApprovalInfo);
                return new ResultUtils(101, "成功審核該報告");
            }

            // 下一个节 点
            SignedWorkflow next = signed.get(0);
            Integer roleId = next.getRoleId();
            log.info("下一个节点是 -> {}", roleId);
            String submitStaffCode = rookieApprovalInfoPackage.getStaffCode();
            log.info("submitStaffCode -> {}", submitStaffCode);

            // 根据提交人的工号查询该用户对应的产线所有领导
            ProductionLineList productionLineList = reportProductionListDao.inquireWithStaffCode(rookieApprovalInfoPackage.getLineId());
            Map<String, String> approveMap = new HashMap<>();
            approveMap.put(productionLineList.getLineLeaderCode(), productionLineList.getLineLeaderName());
            approveMap.put(productionLineList.getLineGroupLeaderCode(), productionLineList.getLineGroupLeaderName());
            approveMap.put(productionLineList.getLineManagerCode(), productionLineList.getLineManagerName());
            String approveStaffName = null;
            String approveStaffCode = null;

            // 查询角色下的所有人工号
            List<String> staffCodeList = userRoleDao.selectStaffCodeByRoleId(roleId);
            for (String code : staffCodeList) {
                if (approveMap.containsKey(code)) {
                    approveStaffCode = code;
                    approveStaffName = approveMap.get(code);
                    break;
                }
            }
            log.info("下一个签核节点是 -> {}", approveStaffCode);

            RookieApprovalInfo nextApprovalInfo = RookieApprovalInfo.builder()
                    .businessId(next.getBusinessId())
                    .nodeId(next.getSignNodeId())
                    .nodeName(next.getNodeName())
                    .approveStaffName(approveStaffName)
                    .approveStaffCode(approveStaffCode)
                    .projId(PROJ_ID)
                    .approveSequence(next.getSignSequence())
                    .investTableName(rookieApprovalInfo.getInvestTableName())
                    .investFieldName(rookieApprovalInfo.getInvestFieldName())
                    .investFieldValue(rookieApprovalInfo.getInvestFieldValue())
                    .status(USED_STATUS)
                    .isPass(UN_AUDIT_STATUS)
                    .build();
            rookieApprovalInfoDao.insert(nextApprovalInfo);
            return new ResultUtils(101, "成功通过該報告");
        }

        // 不通过
        if (UN_PASS.equals(isPass)) {
            // 更新签核记录表
            rookieApprovalInfoDao.update(RookieApprovalInfo.builder()
                    .rookieApprovalId(rookieApprovalInfoPackage.getRookieApprovalId())
                    .signedDate(new Date())
                    .approveOpinion(approveOpinion)
                    .grade(grade)
                    .isPass(isPass)
                    .levelType(rookieApprovalInfoPackage.getLevelType())
                    .build());

            // 修改报告状态 -> 被驳回
            reportProductionListDao.updateReportUnPass(rookieApprovalInfo);
            return new ResultUtils(101, "成功驳回該報告");
        }
        return new ResultUtils(500, "不能審核該報告");
    }

    /**
     * 人资批量签核部门主管的月目标
     *
     * @param monthTargetApprovalInfoPackage
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @SysLog(value = "人资批量签核部门主管的月目标")
    public ResultUtils auditMonthTarget(MonthTargetApprovalInfoPackage monthTargetApprovalInfoPackage) {
        if (monthTargetApprovalInfoPackage.getTargetNumList() == null) {
            return new ResultUtils(500, "月目標單號為空，簽核失敗");
        }
        if (monthTargetApprovalInfoPackage.getTargetNumList().size() > 1) {
            if (!this.checkSameMonthGoal(monthTargetApprovalInfoPackage.getTargetNumList())) {
                return new ResultUtils(500, "月目標不同，不能同時簽核");
            }
        }

        UserInfo currentUser = tokenAnalysis.getTokenUser();
        String staffCode = currentUser.getStaffCode();
        List<Integer> rookieApprovalInfoIds = monthTargetApprovalInfoPackage.getRookieApprovalInfoList();
        String approveOpinion = monthTargetApprovalInfoPackage.getApproveOpinion();
        Integer isPass = monthTargetApprovalInfoPackage.getIsPass();

        if (rookieApprovalInfoIds == null) {
            return new ResultUtils(500, "簽核記錄列表為空");
        }
        boolean flag = true;
        for (Integer rookieApprovalId : rookieApprovalInfoIds) {
            RookieApprovalInfo rookieApprovalInfo = rookieApprovalInfoDao.queryById(rookieApprovalId);
            if (rookieApprovalInfo == null || !staffCode.equals(rookieApprovalInfo.getApproveStaffCode())) {
                return new ResultUtils(500, "不能審核該報告");
            }
            if (!UN_AUDIT_STATUS.equals(rookieApprovalInfo.getIsPass())) {
                return new ResultUtils(500, "該報告不是待審核狀態");
            }

            // 通过
            if (PASS.equals(isPass)) {
                // 更新签核记录表
                rookieApprovalInfoDao.update(RookieApprovalInfo.builder()
                        .rookieApprovalId(rookieApprovalId)
                        .signedDate(new Date())
                        .approveOpinion(approveOpinion)
                        .isPass(isPass)
                        .build());

                // 修改报告的状态
                reportProductionListDao.updateTarget(rookieApprovalInfo);
            }

            // 不通过
            if (UN_PASS.equals(isPass)) {
                // 更新签核记录表
                rookieApprovalInfoDao.update(RookieApprovalInfo.builder()
                        .rookieApprovalId(rookieApprovalId)
                        .signedDate(new Date())
                        .approveOpinion(approveOpinion)
                        .isPass(isPass)
                        .build());

                // 修改报告状态 -> 被驳回
                reportProductionListDao.updateTargetUnPass(rookieApprovalInfo);
                flag = false;
            }
        }

        return flag ? new ResultUtils(101, "成功通过該報告") : new ResultUtils(101, "成功驳回該報告");
    }

    /**
     * 查询同一批次的所有月目标
     *
     * @param targetNum
     * @return
     */
    @Override
    public ResultUtils querySameMonthTarget(String targetNum) {
        List<DepartTargetList> departTargetLists = departTargetListDao.checkSameMonthGoal(targetNum);
        List<String> targetNums = departTargetLists.stream()
                .map(DepartTargetList::getTargetNum)
                .collect(Collectors.toList());
        log.info("从数据库中查出来的所有同一批次创建的月目标 -> {}", targetNums);

        Set<String> targets = new HashSet<>(targetNums);
        log.info("经过去重之后的所有月目标单号 -> {}", targets);
        return new ResultUtils(100, "成功查詢同一批次的所有月目標", targets);
    }

    /**
     * 检查一组 targetNum 是否是同一个月目标
     *
     * @param targetNumList
     * @return
     */
    @Override
    public ResultUtils checkSameMonthTarget(List<String> targetNumList) {
        return new ResultUtils(100, "成功校驗targetNumList", checkSameMonthGoal(targetNumList));
    }

    /**
     * 部门主管签核报告
     *
     * @param rookieApprovalInfoPackage
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @SysLog(value = "部门主管签核报告")
    public ResultUtils departAudit(RookieApprovalInfoPackage rookieApprovalInfoPackage) {
        UserInfo currentUser = tokenAnalysis.getTokenUser();
        String staffCode = currentUser.getStaffCode();

        RookieApprovalInfo rookieApprovalInfo = rookieApprovalInfoDao.queryById(rookieApprovalInfoPackage.getRookieApprovalId());
        if (rookieApprovalInfo == null || !staffCode.equals(rookieApprovalInfo.getApproveStaffCode())) {
            return new ResultUtils(500, "不能審核該報告");
        }
        if (!UN_AUDIT_STATUS.equals(rookieApprovalInfo.getIsPass())) {
            return new ResultUtils(500, "該報告不是待審核狀態");
        }

        String approveOpinion = rookieApprovalInfoPackage.getApproveOpinion();
        String grade = rookieApprovalInfoPackage.getGrade();
        Integer isPass = rookieApprovalInfoPackage.getIsPass();

        // 通过
        if (PASS.equals(isPass)) {
            // 更新签核记录表
            rookieApprovalInfoDao.update(RookieApprovalInfo.builder()
                    .rookieApprovalId(rookieApprovalInfoPackage.getRookieApprovalId())
                    .signedDate(new Date())
                    .approveOpinion(approveOpinion)
                    .grade(grade)
                    .isPass(isPass)
                    .fulfillmentOfSchedule(rookieApprovalInfoPackage.getFulfillmentOfSchedule())
                    .targetComment(rookieApprovalInfoPackage.getTargetComment())
                    .build());

            // 查询签核流程表——下一个节点
            List<SignedWorkflow> signedWorkflowList = signedWorkflowDao.queryAll(SignedWorkflow.builder()
                    .businessId(rookieApprovalInfo.getBusinessId())
                    .status(USED_STATUS)
                    .build());
            if (signedWorkflowList == null || CollectionUtils.isEmpty(signedWorkflowList)) {
                return new ResultUtils(500, "该签核流程为空");
            }
            signedWorkflowList.sort(new SignedNodeComparator());

            Integer approveSequence = rookieApprovalInfo.getApproveSequence() + 1;
            Integer reportId = Integer.valueOf(rookieApprovalInfo.getInvestFieldValue());
            log.info("reportId -> {}", reportId);
            List<SignedWorkflow> signed = signedWorkflowList.stream()
                    .filter(signedWorkflow -> approveSequence.equals(signedWorkflow.getSignSequence()))
                    .collect(Collectors.toList());
            // 是最后一个签核节点
            if (CollectionUtils.isEmpty(signed)) {
                // 修改报告的状态
                reportProductionListDao.updateReport(rookieApprovalInfo);
                return new ResultUtils(101, "成功審核該報告");
            }

            // 下一个节 点
            SignedWorkflow next = signed.get(0);
            Integer roleId = next.getRoleId();
            log.info("下一个节点是 -> {}", roleId);
            String submitStaffCode = rookieApprovalInfoPackage.getStaffCode();
            log.info("submitStaffCode -> {}", submitStaffCode);

            // 根据 staff_code 去 class_student_info 查询该菁干班成员的所有领导
            ClassStudentInfo c = new ClassStudentInfo();
            c.setStaffCode(submitStaffCode);
            ClassStudentInfo classStudentInfo = classStudentInfoDao.selectAll(c).get(0);

            // 查询下一个签核节点的角色和具体的人员
            String approveStaffName = null;
            String approveStaffCode = null;
            switch (roleId) {
                case 14: // 教练员
                    approveStaffName = classStudentInfo.getCoachName();
                    approveStaffCode = classStudentInfo.getCoachCode();
                    break;
                case 15: // 直属主管
                    approveStaffName = classStudentInfo.getDirectManagerName();
                    approveStaffCode = classStudentInfo.getDirectManagerCode();
                    break;
                case 16: // 处级主管
                    approveStaffName = classStudentInfo.getUnitManagerName();
                    approveStaffCode = classStudentInfo.getUnitManagerCode();
                    break;
                case 17: // 部门主管
                    approveStaffName = classStudentInfo.getDepartManagerName();
                    approveStaffCode = classStudentInfo.getDepartManagerCode();
                    break;
            }

            log.info("下一个签核节点是 -> {}", approveStaffCode);

            RookieApprovalInfo nextApprovalInfo = RookieApprovalInfo.builder()
                    .businessId(next.getBusinessId())
                    .nodeId(next.getSignNodeId())
                    .nodeName(next.getNodeName())
                    .approveStaffName(approveStaffName)
                    .approveStaffCode(approveStaffCode)
                    .projId(PROJ_ID)
                    .approveSequence(next.getSignSequence())
                    .investTableName(rookieApprovalInfo.getInvestTableName())
                    .investFieldName(rookieApprovalInfo.getInvestFieldName())
                    .investFieldValue(rookieApprovalInfo.getInvestFieldValue())
                    .status(USED_STATUS)
                    .isPass(UN_AUDIT_STATUS)
                    .build();
            rookieApprovalInfoDao.insert(nextApprovalInfo);
            return new ResultUtils(101, "成功通过該報告");
        }

        // 不通过
        if (UN_PASS.equals(isPass)) {
            // 更新签核记录表
            rookieApprovalInfoDao.update(RookieApprovalInfo.builder()
                    .rookieApprovalId(rookieApprovalInfoPackage.getRookieApprovalId())
                    .signedDate(new Date())
                    .approveOpinion(approveOpinion)
                    .grade(grade)
                    .isPass(isPass)
                    .fulfillmentOfSchedule(rookieApprovalInfoPackage.getFulfillmentOfSchedule())
                    .targetComment(rookieApprovalInfoPackage.getTargetComment())
                    .build());

            // 修改报告状态 -> 被驳回
            reportProductionListDao.updateReportUnPass(rookieApprovalInfo);
            return new ResultUtils(101, "成功驳回該報告");
        }
        return new ResultUtils(500, "不能審核該報告");
    }


    /**
     * 检查一组 targetNum 是否是同一个月目标
     *
     * @param targetNumList 一组targetNum
     * @return true -> 同一个月目标
     */
    public boolean checkSameMonthGoal(List<String> targetNumList) {
        List<DepartTargetList> departTargetLists = departTargetListDao.checkSameMonthGoal(targetNumList.get(0));
        List<String> targetNums = departTargetLists.stream()
                .map(DepartTargetList::getTargetNum)
                .collect(Collectors.toList());
        log.info("从数据库中查出来的所有同一批次创建的月目标 -> {}", targetNums);

        Set<String> targets = new HashSet<>(targetNums);
        log.info("经过去重之后的所有月目标单号 -> {}", targets);

        return targets.containsAll(new HashSet<>(targetNumList));
    }

}
