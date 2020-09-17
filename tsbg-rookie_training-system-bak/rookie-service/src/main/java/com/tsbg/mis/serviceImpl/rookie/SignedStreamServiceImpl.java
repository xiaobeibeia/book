package com.tsbg.mis.serviceImpl.rookie;

import com.tsbg.mis.annotation.SysLog;
import com.tsbg.mis.dao.rookie.ProductionLineListDao;
import com.tsbg.mis.dao.rookie.RookieApprovalInfoDao;
import com.tsbg.mis.dao.signed.BusinessListDao;
import com.tsbg.mis.dao.signed.SignedNodeDao;
import com.tsbg.mis.dao.signed.SignedWorkflowDao;
import com.tsbg.mis.master.StaffInfo;
import com.tsbg.mis.rookie.model.RookieApprovalInfo;
import com.tsbg.mis.rookie.vo.SignedProgressVo;
import com.tsbg.mis.signed.bag.SignedNodeComparator;
import com.tsbg.mis.signed.bag.SignedPackage;
import com.tsbg.mis.service.rookie.SignedStreamService;
import com.tsbg.mis.signed.model.BusinessList;
import com.tsbg.mis.signed.model.SignedNode;
import com.tsbg.mis.signed.model.SignedWorkflow;
import com.tsbg.mis.signed.vo.SignedWorkflowVo;
import com.tsbg.mis.util.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 汪永晖
 */
@Service
public class SignedStreamServiceImpl implements SignedStreamService {

    private final Integer SIGN_TYPE_ID = 6;

    private final Integer PROJ_ID = 10;

    private final Integer USED_STATUS = 1;

    private final Integer NOT_STOP = 0;

    private final Integer UN_PASS = 0;

    private final Integer PRODUCTION_LINE_INTERNSHIP_DAILY = 9;

    private final Integer PRODUCTION_LINE_INTERNSHIP_WEEKLY = 10;

    private final Integer PRODUCTION_LINE_INTERNSHIP_MONTHLY = 11;

    private final Integer PROPOSAL_IMPROVEMENT_REPORT = 12;

    private final Integer DEPARTMENTAL_INTERNSHIP_DAILY = 13;

    private final Integer DEPARTMENTAL_INTERNSHIP_WEEKLY = 14;

    private final Integer DEPARTMENTAL_INTERNSHIP_MONTHLY = 15;

    private final Integer MAJOR_PROJECT_REPORT_AND_OTHER_SUMMARY = 16;

    private final Integer DAILY_PRODUCTION_LINE_APPROVAL = 1;

    private final Integer WEEKLY_PRODUCTION_LINE_APPROVAL = 2;

    private final Integer MONTHLY_PRODUCTION_LINE_APPROVAL = 3;

    private final Integer PROPOSAL_TO_IMPROVE_SIGNING = 4;

    private final Integer DAILY_DEPART_REPORT = 7;

    private final Integer WEEK_DEPART_REPORT = 8;

    private final Integer MONTH_DEPART_REPORT = 9;

    private final SignedNodeDao signedNodeDao;

    private final BusinessListDao businessListDao;

    private final SignedWorkflowDao signedWorkflowDao;

    private final RookieApprovalInfoDao rookieApprovalInfoDao;

    private final ProductionLineListDao productionLineListDao;

    public SignedStreamServiceImpl(SignedWorkflowDao signedWorkflowDao, BusinessListDao businessListDao, SignedNodeDao signedNodeDao, RookieApprovalInfoDao rookieApprovalInfoDao, ProductionLineListDao productionLineListDao) {
        this.signedWorkflowDao = signedWorkflowDao;
        this.businessListDao = businessListDao;
        this.signedNodeDao = signedNodeDao;
        this.rookieApprovalInfoDao = rookieApprovalInfoDao;
        this.productionLineListDao = productionLineListDao;
    }

    /**
     * 新增签核流程
     *
     * @param signedPackage
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @SysLog(value = "新增签核流程")
    public ResultUtils insert(SignedPackage signedPackage) {
        // 插入到 business_list 表中
        BusinessList business = BusinessList.builder()
                .businessName(signedPackage.getBusinessName())
                .signTypeId(SIGN_TYPE_ID)
                .projId(PROJ_ID)
                .proName("菁干班培训系统")
                .status(USED_STATUS)
                .build();
        businessListDao.insert(business);

        // 插入到 signed_node 和 signed_workflow 表中
        Integer businessId = business.getBusinessId();
        List<SignedWorkflow> signedWorkflowList = signedPackage.getSignedWorkflowList();
        if (signedWorkflowList == null || CollectionUtils.isEmpty(signedWorkflowList)) {
            return new ResultUtils(500, "新增失敗");
        }
        signedWorkflowList.sort(new SignedNodeComparator());
        for (int i = 0; i < signedWorkflowList.size(); i++) {
            SignedWorkflow signedWorkflow = signedWorkflowList.get(i);
            // 插入到 signed_node
            SignedNode signedNode = SignedNode.builder()
                    .nodeName(signedWorkflow.getNodeName())
                    .businessId(businessId)
                    .isStop(NOT_STOP)
                    .build();
            signedNodeDao.insert(signedNode);

            //  插入到 signed_workflow 表中
            Integer signNodeId = signedNode.getSignNodeId();
            SignedWorkflow workflow = SignedWorkflow.builder()
                    .signTypeId(SIGN_TYPE_ID)
                    .businessId(businessId)
                    .signSequence(signedWorkflow.getSignSequence())
                    .signNodeId(signNodeId)
                    .nodeName(signedWorkflow.getNodeName())
                    .roleId(signedWorkflow.getRoleId())
                    .projId(PROJ_ID)
                    .status(USED_STATUS)
                    .build();
            signedWorkflowDao.insert(workflow);
        }

        return new ResultUtils(101, "成功新增簽核流程");
    }

    /**
     * 查询流程
     *
     * @return
     */
    @Override
    public ResultUtils inquire() {
        // 查询所有business
        List<SignedWorkflowVo> signedWorkflowVoList = businessListDao.inquire();
        if (signedWorkflowVoList == null || CollectionUtils.isEmpty(signedWorkflowVoList)) {
            return new ResultUtils(501, "查詢失敗");
        }
        List<Integer> businessIds = signedWorkflowVoList.stream()
                .map(SignedWorkflowVo::getBusinessId)
                .collect(Collectors.toList());
        // 查询所有签核流程的具体信息
        if (CollectionUtils.isEmpty(businessIds)) {
            return new ResultUtils(100, "暫無數據");
        }
        List<SignedWorkflow> signedWorkflowList = signedWorkflowDao.queryAllByBusinessIds(businessIds);
        if (signedWorkflowList == null || CollectionUtils.isEmpty(signedWorkflowList)) {
            return new ResultUtils(501, "查詢失敗");
        }
        for (SignedWorkflowVo signedWorkflowVo : signedWorkflowVoList) {
            Integer businessId = signedWorkflowVo.getBusinessId();
            List<SignedWorkflow> signedList = signedWorkflowList.stream()
                    .filter(signedWorkflow ->
                            (businessId.equals(signedWorkflow.getBusinessId()))
                    )
                    .collect(Collectors.toList());
            signedWorkflowVo.setSignedWorkflowList(signedList);
        }
        return new ResultUtils(100, "成功查詢所有簽核流程", signedWorkflowVoList);
    }

    /**
     * 修改签核流程
     *
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @SysLog(value = "修改签核流程")
    public ResultUtils update(SignedPackage signedPackage) {
        // 修改 business_list
        businessListDao.update(BusinessList.builder()
                .businessName(signedPackage.getBusinessName())
                .businessId(signedPackage.getBusinessId())
                .build());

        // 修改 signed_node 和 signed_workflow 表
        signedNodeDao.deleteByBusinessId(signedPackage.getBusinessId());
        signedWorkflowDao.deleteByBusinessId(signedPackage.getBusinessId());
        List<SignedWorkflow> signedWorkflowList = signedPackage.getSignedWorkflowList();
        if (signedWorkflowList == null || CollectionUtils.isEmpty(signedWorkflowList)) {
            return new ResultUtils(500, "修改失敗");
        }
        Integer businessId = signedPackage.getBusinessId();
        signedWorkflowList.sort(new SignedNodeComparator());
        for (int i = 0; i < signedWorkflowList.size(); i++) {
            SignedWorkflow signedWorkflow = signedWorkflowList.get(i);
            // 插入到 signed_node
            SignedNode signedNode = SignedNode.builder()
                    .nodeName(signedWorkflow.getNodeName())
                    .businessId(businessId)
                    .isStop(NOT_STOP)
                    .build();
            signedNodeDao.insert(signedNode);

            //  插入到 signed_workflow 表中
            Integer signNodeId = signedNode.getSignNodeId();
            SignedWorkflow workflow = SignedWorkflow.builder()
                    .signTypeId(SIGN_TYPE_ID)
                    .businessId(businessId)
                    .signSequence(signedWorkflow.getSignSequence())
                    .signNodeId(signNodeId)
                    .nodeName(signedWorkflow.getNodeName())
                    .roleId(signedWorkflow.getRoleId())
                    .projId(PROJ_ID)
                    .status(USED_STATUS)
                    .build();
            signedWorkflowDao.insert(workflow);
        }
        return new ResultUtils(101, "簽核流程修改成功");
    }

    /**
     * 删除签核流程
     *
     * @param businessId
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @SysLog(value = "删除签核流程")
    public ResultUtils delete(Integer businessId) {
        businessListDao.deleteById(businessId);
        signedNodeDao.deleteByBusinessId(businessId);
        signedWorkflowDao.deleteByBusinessId(businessId);
        return new ResultUtils(101, "成功刪除簽核節點");
    }

    /**
     * 根据ID查询签核流程
     *
     * @param businessId
     * @return
     */
    @Override
    public ResultUtils selectBusiness(Integer businessId) {
        SignedWorkflowVo signedWorkflowVo = businessListDao.inquireById(businessId);
        if (signedWorkflowVo == null) {
            return new ResultUtils(501, "查詢失敗");
        }
        List<SignedWorkflow> signedWorkflowList = signedWorkflowDao.queryAll(SignedWorkflow.builder()
                .businessId(businessId)
                .status(USED_STATUS)
                .projId(PROJ_ID)
                .build());
        if (signedWorkflowList == null || CollectionUtils.isEmpty(signedWorkflowList)) {
            return new ResultUtils(501, "查詢失敗");
        }
        List<SignedWorkflow> signedList = signedWorkflowList.stream()
                .filter(signedWorkflow ->
                        (businessId.equals(signedWorkflow.getBusinessId()))
                )
                .collect(Collectors.toList());
        signedWorkflowVo.setSignedWorkflowList(signedList);
        return new ResultUtils(100, "成功查詢簽核流程", signedWorkflowVo);
    }

    /**
     * 根据报告ID查询签核进度
     *
     * @param id
     * @return
     */
    @Override
    public ResultUtils selectApprovalInfo(Integer id) {
        List<RookieApprovalInfo> rookieApprovalInfoList = rookieApprovalInfoDao.queryAll(RookieApprovalInfo.builder()
                .investFieldValue(String.valueOf(id))
                .build());
        return new ResultUtils(100, "成功查詢簽核流程", rookieApprovalInfoList);
    }

    /**
     * 查询签核进度
     *
     * @param reportId
     * @param reportType
     * @return
     */
    @Override
    public ResultUtils selectProgress(Integer reportId, Integer reportType) {
        // 确定business类型
        Integer businessId = null;
        String investTableName = "report_production_list";
        switch (reportType) {
            case 9:
                businessId = DAILY_PRODUCTION_LINE_APPROVAL;
                break;
            case 10:
                businessId = WEEKLY_PRODUCTION_LINE_APPROVAL;
                break;
            case 11:
                businessId = MONTHLY_PRODUCTION_LINE_APPROVAL;
                break;
            case 12:
                businessId = PROPOSAL_TO_IMPROVE_SIGNING;
                investTableName = "report_production_proposal_list";
                break;
            case 13: // 部門實習日報
                businessId = DAILY_DEPART_REPORT;
                investTableName = "report_depart_list";
                break;
            case 14: // 部門實習週報
                businessId = WEEK_DEPART_REPORT;
                investTableName = "report_depart_list";
                break;
            case 15: // 部門實習月報
                businessId = MONTH_DEPART_REPORT;
                investTableName = "report_depart_list";
                break;
        }

        // 根据 businessId 查询签核流程
        List<SignedWorkflow> signedWorkflowList = signedWorkflowDao.queryAll(SignedWorkflow.builder()
                .businessId(businessId)
                .status(USED_STATUS)
                .projId(PROJ_ID)
                .build());
        if (signedWorkflowList == null || CollectionUtils.isEmpty(signedWorkflowList)) {
            return new ResultUtils(501, "查詢失敗1");
        }
        Integer finalBusinessId = businessId;
        List<SignedWorkflow> signedList = signedWorkflowList.stream()
                .filter(signedWorkflow ->
                        (signedWorkflow.getBusinessId().equals(finalBusinessId))
                )
                .collect(Collectors.toList());
        if (signedList == null || CollectionUtils.isEmpty(signedList)) {
            return new ResultUtils(501, "查詢失敗2");
        }
        List<SignedProgressVo> signedProgressVoList = new ArrayList<>();
        for (SignedWorkflow signedWorkflow : signedList) {
            signedProgressVoList.add(SignedProgressVo.builder()
                    .nodeName(signedWorkflow.getNodeName())
                    .build());
        }

        if (reportId == null) {
            return new ResultUtils(100, "成功返回簽核進度", signedProgressVoList);
        }

        // 根据报告ID查询签核进度
        List<RookieApprovalInfo> rookieApprovalInfoList = rookieApprovalInfoDao.queryAll(RookieApprovalInfo.builder()
                .investFieldValue(String.valueOf(reportId))
                .investTableName(investTableName)
                .build());
        if (rookieApprovalInfoList == null || CollectionUtils.isEmpty(rookieApprovalInfoList)) {
            return new ResultUtils(100, "成功返回簽核進度", signedProgressVoList);
        }
        List<SignedProgressVo> signedProgressVos = new ArrayList<>();
        for (RookieApprovalInfo rookieApprovalInfo : rookieApprovalInfoList) {
            // 根据主管工号查询联系方式
            String approveStaffCode = rookieApprovalInfo.getApproveStaffCode();
            StaffInfo staffInfo = productionLineListDao.selectStaffInfoByCode(approveStaffCode);
            if (staffInfo == null) {
                return new ResultUtils(501, "查詢主管联系方式失敗");
            }

            // 根据 levelType 查询评级并且进行截断
            String levelTypeString = null;
            if (rookieApprovalInfo.getLevelType() != null) {
                String levelTypeName = rookieApprovalInfoDao.queryLevelTypeName(rookieApprovalInfo.getLevelType());
                if (levelTypeName == null) {
                    return new ResultUtils(501, "查詢评级失敗");
                }
                int count = levelTypeName.indexOf("=");
                if (count > -1) {
                    levelTypeString = levelTypeName.substring(count - 2, count);
                }
            }
            signedProgressVos.add(SignedProgressVo.builder()
                    .nodeName(rookieApprovalInfo.getNodeName())
                    .isPass(rookieApprovalInfo.getIsPass())
                    .approveStaffName(rookieApprovalInfo.getApproveStaffName())
                    .approveStaffCode(rookieApprovalInfo.getApproveStaffCode())
                    .approveOpinion(rookieApprovalInfo.getApproveOpinion())
                    .grade(rookieApprovalInfo.getGrade())
                    .approveStaffMail(staffInfo.getEmail())
                    .approveStaffPhoneNumber(staffInfo.getPhoneNum())
                    .levelType(rookieApprovalInfo.getLevelType())
                    .levelTypeName(levelTypeString)
                    .fulfillmentOfSchedule(rookieApprovalInfo.getFulfillmentOfSchedule())
                    .targetComment(rookieApprovalInfo.getTargetComment())
                    .build());
        }
        String progress = signedProgressVos.get(signedProgressVos.size() - 1).getNodeName();
        // 补齐签核记录
        String finalProgress = progress;
        SignedWorkflow signedWorkflow1 = signedWorkflowList.stream()
                .filter(signedWorkflow -> finalProgress.equals(signedWorkflow.getNodeName()))
                .findFirst()
                .get();
        Integer processIndex = signedWorkflowList.indexOf(signedWorkflow1) + 1;
        // 驳回之后从头开始审核
        if (UN_PASS.equals(signedProgressVos.get(signedProgressVos.size() - 1).getIsPass())) {
            processIndex = 0;
        }

        for (int i = processIndex; i < signedWorkflowList.size(); i++) {
            signedProgressVos.add(SignedProgressVo.builder()
                    .nodeName(signedWorkflowList.get(i).getNodeName())
                    .build());
        }
        return new ResultUtils(100, "成功返回簽核進度", signedProgressVos);
    }

}
