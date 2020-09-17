package com.tsbg.mis.serviceImpl.rookie;

import com.tsbg.mis.annotation.SysLog;
import com.tsbg.mis.dao.jurisdiction.UserRoleDao;
import com.tsbg.mis.dao.rookie.ClassStudentInfoDao;
import com.tsbg.mis.dao.rookie.ReportProductionListDao;
import com.tsbg.mis.dao.rookie.RookieApprovalInfoDao;
import com.tsbg.mis.dao.signed.SignedWorkflowDao;
import com.tsbg.mis.jurisdiction.model.UserInfo;
import com.tsbg.mis.rookie.model.ClassStudentInfo;
import com.tsbg.mis.rookie.model.ProductionLineList;
import com.tsbg.mis.rookie.model.ReportProductionList;
import com.tsbg.mis.rookie.model.RookieApprovalInfo;
import com.tsbg.mis.service.rookie.StudentTaskService;
import com.tsbg.mis.serviceImpl.jurisdiction.base.TokenAnalysis;
import com.tsbg.mis.signed.bag.SignedNodeComparator;
import com.tsbg.mis.signed.model.SignedWorkflow;
import com.tsbg.mis.util.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 汪永晖
 */
@Service
@Slf4j
public class StudentTaskServiceImpl implements StudentTaskService {

    private final Integer USED_STATUS = 1;

    private final Integer DAILY_REPORT_PRODUCTION = 1;

    private final Integer WEEK_REPORT_PRODUCTION = 2;

    private final Integer MONTH_REPORT_PRODUCTION = 3;

    private final Integer CHANGE_REPORT_PRODUCTION = 4;

    private final Integer DAILY_DEPART_REPORT = 7;

    private final Integer WEEK_DEPART_REPORT = 8;

    private final Integer MONTH_DEPART_REPORT = 9;

    private final Integer PROJ_ID = 10;

    private final Integer SUBMIT_STATUS = 2;

    private final Integer UN_AUDIT_STATUS = 3;

    private final String INVEST_TABLE_NAME = "report_production_list";

    private final String INVEST_FIELD_NAME = "report_id";

    private final String DEPART_INVEST_TABLE_NAME = "report_depart_list";

    private final String DEPART_INVEST_FIELD_NAME = "report_id";

    private final Integer STUDENT_ROLE_ID = 13;

    private final TokenAnalysis tokenAnalysis;

    private final ReportProductionListDao reportProductionListDao;

    private final SignedWorkflowDao signedWorkflowDao;

    private final RookieApprovalInfoDao rookieApprovalInfoDao;

    private final UserRoleDao userRoleDao;

    private final ClassStudentInfoDao classStudentInfoDao;

    public StudentTaskServiceImpl(ReportProductionListDao reportProductionListDao, SignedWorkflowDao signedWorkflowDao, RookieApprovalInfoDao rookieApprovalInfoDao, TokenAnalysis tokenAnalysis, UserRoleDao userRoleDao, ClassStudentInfoDao classStudentInfoDao) {
        this.reportProductionListDao = reportProductionListDao;
        this.signedWorkflowDao = signedWorkflowDao;
        this.rookieApprovalInfoDao = rookieApprovalInfoDao;
        this.tokenAnalysis = tokenAnalysis;
        this.userRoleDao = userRoleDao;
        this.classStudentInfoDao = classStudentInfoDao;
    }

    /**
     * 提交产线周和月报报告
     *
     * @param reportId
     * @param reportType
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @SysLog(value = "提交报告")
    public ResultUtils submitReport(Integer reportId, Integer reportType, Integer lineId) {
        Integer signedWorkflowBusinessId = null;
        String approveStaffName = null;
        String approveStaffCode = null;

        // 根据 line_id 查询该用户对应的产线所有领导
        UserInfo currentUser = tokenAnalysis.getTokenUser();
        ProductionLineList productionLineList = reportProductionListDao.inquireWithStaffCode(lineId);

        switch (reportType) {
            case 10: // 周报
                signedWorkflowBusinessId = WEEK_REPORT_PRODUCTION;
                break;
            case 11: // 月报
                signedWorkflowBusinessId = MONTH_REPORT_PRODUCTION;
                break;
        }
        // 查询签核流程表
        List<SignedWorkflow> signedWorkflowList = signedWorkflowDao.queryAll(SignedWorkflow.builder()
                .businessId(signedWorkflowBusinessId)
                .status(USED_STATUS)
                .build());
        signedWorkflowList.sort(new SignedNodeComparator());
        if (signedWorkflowBusinessId == null || signedWorkflowList.size() < 2) {
            return new ResultUtils(500, "還沒有對應的簽核流程");
        }
        // 查询第一个签核节点的角色和具体的人员
        SignedWorkflow submitSigned = signedWorkflowList.get(0);
        SignedWorkflow firstSigned = signedWorkflowList.get(1);
        Integer roleId = firstSigned.getRoleId();
        Integer businessId = firstSigned.getBusinessId();
        log.info("{} 对应签核的第一个签核节点的角色是 -> {}", currentUser.getStaffCode(), roleId);

        switch (roleId) {
            case 10: // 线长
                approveStaffName = productionLineList.getLineLeaderName();
                approveStaffCode = productionLineList.getLineLeaderCode();
                break;
            case 11: // 组长
                approveStaffName = productionLineList.getLineGroupLeaderName();
                approveStaffCode = productionLineList.getLineGroupLeaderCode();
                break;
            case 12: // 产线负责人
                approveStaffName = productionLineList.getLineManagerName();
                approveStaffCode = productionLineList.getLineManagerCode();
                break;
        }

        log.info("对应签核的第一个签核节点是 -> {}", approveStaffCode);

        // 往签核信息记录表插入提交节点和第一个节点
        RookieApprovalInfo submit = RookieApprovalInfo.builder()
                .businessId(businessId)
                .nodeId(submitSigned.getSignNodeId())
                .nodeName(submitSigned.getNodeName())
                .approveStaffName(currentUser.getUserName())
                .approveStaffCode(currentUser.getStaffCode())
                .projId(PROJ_ID)
                .approveSequence(submitSigned.getSignSequence())
                .investTableName(INVEST_TABLE_NAME)
                .investFieldName(INVEST_FIELD_NAME)
                .investFieldValue(reportId.toString())
                .status(USED_STATUS)
                .isPass(SUBMIT_STATUS)
                .build();
        rookieApprovalInfoDao.insert(submit);
        RookieApprovalInfo first = RookieApprovalInfo.builder()
                .businessId(businessId)
                .nodeId(firstSigned.getSignNodeId())
                .nodeName(firstSigned.getNodeName())
                .approveStaffName(approveStaffName)
                .approveStaffCode(approveStaffCode)
                .projId(PROJ_ID)
                .approveSequence(firstSigned.getSignSequence())
                .investTableName(INVEST_TABLE_NAME)
                .investFieldName(INVEST_FIELD_NAME)
                .investFieldValue(reportId.toString())
                .status(USED_STATUS)
                .isPass(UN_AUDIT_STATUS)
                .build();
        rookieApprovalInfoDao.insert(first);
        // 修改报告的状态
        reportProductionListDao.update(ReportProductionList.builder().reportId(reportId).stateId(UN_AUDIT_STATUS).build());
        return new ResultUtils(101, "成功提交報告");
    }

    /**
     * 提交提案改善报告
     *
     * @param proposalId
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @SysLog(value = "提交提案改善报告")
    public ResultUtils improveReport(Integer proposalId, Integer lineId) {

        UserInfo currentUser = tokenAnalysis.getTokenUser();

        // 根据 line_id 查询该用户对应的产线所有领导
        ProductionLineList productionLineList = reportProductionListDao.inquireWithStaffCode(lineId);
        Map<String, String> approveMap = new HashMap<>();
        approveMap.put(productionLineList.getLineLeaderCode(), productionLineList.getLineLeaderName());
        approveMap.put(productionLineList.getLineGroupLeaderCode(), productionLineList.getLineGroupLeaderName());
        approveMap.put(productionLineList.getLineManagerCode(), productionLineList.getLineManagerName());

        // 查询签核流程表
        List<SignedWorkflow> signedWorkflowList = signedWorkflowDao.queryAll(SignedWorkflow.builder()
                .businessId(CHANGE_REPORT_PRODUCTION)
                .status(USED_STATUS)
                .build());
        signedWorkflowList.sort(new SignedNodeComparator());
        if (signedWorkflowList.size() < 2) {
            return new ResultUtils(500, "還沒有對應的簽核流程");
        }
        // 查询第一个签核节点的角色和具体的人员
        SignedWorkflow submitSigned = signedWorkflowList.get(0);
        SignedWorkflow firstSigned = signedWorkflowList.get(1);
        Integer roleId = firstSigned.getRoleId();
        Integer businessId = firstSigned.getBusinessId();
        log.info("{} 对应签核的第一个签核节点的角色是 -> {}", currentUser.getStaffCode(), roleId);

        // 查询角色下的所有人工号
        List<String> staffCodeList = userRoleDao.selectStaffCodeByRoleId(roleId);
        String approveStaffName = null;
        String approveStaffCode = null;
        for (String code : staffCodeList) {
            if (approveMap.containsKey(code)) {
                approveStaffCode = code;
                approveStaffName = approveMap.get(code);
                break;
            }
        }
        log.info("对应签核的第一个签核节点是 -> {}", approveStaffCode);

        // 往签核信息记录表插入提交节点和第一个节点
        RookieApprovalInfo submit = RookieApprovalInfo.builder()
                .businessId(businessId)
                .nodeId(submitSigned.getSignNodeId())
                .nodeName(submitSigned.getNodeName())
                .approveStaffName(currentUser.getUserName())
                .approveStaffCode(currentUser.getStaffCode())
                .projId(PROJ_ID)
                .approveSequence(submitSigned.getSignSequence())
                .investTableName("report_production_proposal_list")
                .investFieldName("proposal_id")
                .investFieldValue(proposalId.toString())
                .status(USED_STATUS)
                .isPass(SUBMIT_STATUS)
                .build();
        rookieApprovalInfoDao.insert(submit);
        RookieApprovalInfo first = RookieApprovalInfo.builder()
                .businessId(businessId)
                .nodeId(firstSigned.getSignNodeId())
                .nodeName(firstSigned.getNodeName())
                .approveStaffName(approveStaffName)
                .approveStaffCode(approveStaffCode)
                .projId(PROJ_ID)
                .approveSequence(firstSigned.getSignSequence())
                .investTableName("report_production_proposal_list")
                .investFieldName("proposal_id")
                .investFieldValue(proposalId.toString())
                .status(USED_STATUS)
                .isPass(UN_AUDIT_STATUS)
                .build();
        rookieApprovalInfoDao.insert(first);
        // 修改提案改善报告的状态
        reportProductionListDao.updateProposal(proposalId);
        return new ResultUtils(101, "成功提交報告");
    }

    /**
     * 提交部门报告
     *
     * @param reportId
     * @param reportType
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @SysLog(value = "提交部门报告")
    public ResultUtils submitDepartReport(Integer reportId, Integer reportType) {
        Integer signedWorkflowBusinessId = null;
        String approveStaffName = null;
        String approveStaffCode = null;
        UserInfo currentUser = tokenAnalysis.getTokenUser();
        List<Integer> roleIds = userRoleDao.selectRoleId(currentUser.getUserId());
        if (roleIds == null || CollectionUtils.isEmpty(roleIds)) {
            return new ResultUtils(500, "該用戶沒有角色");
        }
        if (!roleIds.contains(STUDENT_ROLE_ID)) {
            return new ResultUtils(500, "非菁幹班成員不能提交部門報告");
        }

        switch (reportType) {
            case 13: // 部門實習日報
                signedWorkflowBusinessId = DAILY_DEPART_REPORT;
                break;
            case 14: // 部門實習週報
                signedWorkflowBusinessId = WEEK_DEPART_REPORT;
                break;
            case 15: // 部門實習月報
                signedWorkflowBusinessId = MONTH_DEPART_REPORT;
                break;
        }

        // 查询签核流程表
        List<SignedWorkflow> signedWorkflowList = signedWorkflowDao.queryAll(SignedWorkflow.builder()
                .businessId(signedWorkflowBusinessId)
                .status(USED_STATUS)
                .build());
        signedWorkflowList.sort(new SignedNodeComparator());
        if (signedWorkflowBusinessId == null || signedWorkflowList.size() < 2) {
            return new ResultUtils(500, "還沒有對應的簽核流程");
        }

        // 根据 staff_code 去 class_student_info 查询该菁干班成员的所有领导
        ClassStudentInfo c = new ClassStudentInfo();
        c.setStaffCode(currentUser.getStaffCode());
        ClassStudentInfo classStudentInfo = classStudentInfoDao.selectAll(c).get(0);

        // 查询第一个签核节点的角色和具体的人员
        SignedWorkflow submitSigned = signedWorkflowList.get(0);
        SignedWorkflow firstSigned = signedWorkflowList.get(1);
        Integer roleId = firstSigned.getRoleId();
        Integer businessId = firstSigned.getBusinessId();
        log.info("{} 对应签核的第一个签核节点的角色是 -> {}", currentUser.getStaffCode(), roleId);

        switch (roleId) {
            case 14: // 教练员
                if (classStudentInfo.getCoachCode() == null)
                    return new ResultUtils(500, "您的教練員為空，不能提交審核");
                approveStaffName = classStudentInfo.getCoachName();
                approveStaffCode = classStudentInfo.getCoachCode();
                break;
            case 15: // 直属主管
                if (classStudentInfo.getDirectManagerCode() == null)
                    return new ResultUtils(500, "您的直屬主管為空，不能提交審核");
                approveStaffName = classStudentInfo.getDirectManagerName();
                approveStaffCode = classStudentInfo.getDirectManagerCode();
                break;
            case 16: // 处级主管
                if (classStudentInfo.getUnitManagerCode() == null)
                    return new ResultUtils(500, "您的處級主管為空，不能提交審核");
                approveStaffName = classStudentInfo.getUnitManagerName();
                approveStaffCode = classStudentInfo.getUnitManagerCode();
                break;
            case 17: // 部门主管
                if (classStudentInfo.getDepartManagerCode() == null)
                    return new ResultUtils(500, "您的部門主管為空，不能提交審核");
                approveStaffName = classStudentInfo.getDepartManagerName();
                approveStaffCode = classStudentInfo.getDepartManagerCode();
                break;
        }
        log.info("对应签核的第一个签核节点是 -> {}", approveStaffCode);

        // 往签核信息记录表插入提交节点和第一个节点
        RookieApprovalInfo submit = RookieApprovalInfo.builder()
                .businessId(businessId)
                .nodeId(submitSigned.getSignNodeId())
                .nodeName(submitSigned.getNodeName())
                .approveStaffName(currentUser.getUserName())
                .approveStaffCode(currentUser.getStaffCode())
                .projId(PROJ_ID)
                .approveSequence(submitSigned.getSignSequence())
                .investTableName(DEPART_INVEST_TABLE_NAME)
                .investFieldName(DEPART_INVEST_FIELD_NAME)
                .investFieldValue(reportId.toString())
                .status(USED_STATUS)
                .isPass(SUBMIT_STATUS)
                .build();
        rookieApprovalInfoDao.insert(submit);
        RookieApprovalInfo first = RookieApprovalInfo.builder()
                .businessId(businessId)
                .nodeId(firstSigned.getSignNodeId())
                .nodeName(firstSigned.getNodeName())
                .approveStaffName(approveStaffName)
                .approveStaffCode(approveStaffCode)
                .projId(PROJ_ID)
                .approveSequence(firstSigned.getSignSequence())
                .investTableName(DEPART_INVEST_TABLE_NAME)
                .investFieldName(DEPART_INVEST_FIELD_NAME)
                .investFieldValue(reportId.toString())
                .status(USED_STATUS)
                .isPass(UN_AUDIT_STATUS)
                .build();
        rookieApprovalInfoDao.insert(first);

        // 修改报告的状态
        reportProductionListDao.updateUnAudit(first);
        return new ResultUtils(101, "成功提交報告");
    }


}
