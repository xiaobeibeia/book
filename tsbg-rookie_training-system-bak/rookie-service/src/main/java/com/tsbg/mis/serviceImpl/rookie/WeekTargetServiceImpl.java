package com.tsbg.mis.serviceImpl.rookie;

import com.tsbg.mis.annotation.SysLog;
import com.tsbg.mis.dao.jurisdiction.UserInfoDao;
import com.tsbg.mis.dao.jurisdiction.UserRoleDao;
import com.tsbg.mis.dao.rookie.DepartmentTargetDao;
import com.tsbg.mis.dao.rookie.RookieApprovalInfoDao;
import com.tsbg.mis.dao.signed.SignedWorkflowDao;
import com.tsbg.mis.jurisdiction.model.UserInfo;
import com.tsbg.mis.rookie.bag.MonthlyTargetPackage;
import com.tsbg.mis.rookie.model.DepartTargetList;
import com.tsbg.mis.rookie.model.RookieApprovalInfo;
import com.tsbg.mis.rookie.vo.MonthTargetApprovalVo;
import com.tsbg.mis.rookie.vo.RookieApprovalInfoVo;
import com.tsbg.mis.rookie.vo.TargetVo;
import com.tsbg.mis.service.rookie.WeekTargetService;
import com.tsbg.mis.serviceImpl.jurisdiction.base.TokenAnalysis;
import com.tsbg.mis.signed.bag.SignedNodeComparator;
import com.tsbg.mis.signed.model.SignedWorkflow;
import com.tsbg.mis.util.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 汪永晖
 */
@Service
@Slf4j
public class WeekTargetServiceImpl implements WeekTargetService {

    private final Integer HR_ROLE_ID = 18;

    private final Integer USED_STATUS = 1;

    private final Integer DEPART_MONTHLY_GOAL = 6;

    private final Integer PROJ_ID = 10;

    private final Integer SUBMIT_STATUS = 2;

    private final Integer UN_AUDIT_STATUS = 3;

    private final String INVEST_TABLE_NAME = "depart_target_list";

    private final String INVEST_FIELD_NAME = "target_num";

    private final UserRoleDao userRoleDao;

    private final TokenAnalysis tokenAnalysis;

    private final SignedWorkflowDao signedWorkflowDao;

    private final RookieApprovalInfoDao rookieApprovalInfoDao;

    private final UserInfoDao userInfoDao;

    private final DepartmentTargetDao departmentTargetDao;

    public WeekTargetServiceImpl(UserRoleDao userRoleDao, TokenAnalysis tokenAnalysis, SignedWorkflowDao signedWorkflowDao, RookieApprovalInfoDao rookieApprovalInfoDao, UserInfoDao userInfoDao, DepartmentTargetDao departmentTargetDao) {
        this.userRoleDao = userRoleDao;
        this.tokenAnalysis = tokenAnalysis;
        this.signedWorkflowDao = signedWorkflowDao;
        this.rookieApprovalInfoDao = rookieApprovalInfoDao;
        this.userInfoDao = userInfoDao;
        this.departmentTargetDao = departmentTargetDao;
    }

    /**
     * 查询月目标所有审核人
     *
     * @return
     */
    @Override
    public ResultUtils queryReviewer() {
        List<UserInfo> userInfoList = userRoleDao.selectReviewerByRoleId(HR_ROLE_ID);
        return new ResultUtils(100, "成功查询月目标所有审核人", userInfoList);
    }

    /**
     * 创建完月目标，提交到人资去审核
     *
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @SysLog(value = "提交月目标")
    public ResultUtils reviewMonthlyTarget(MonthlyTargetPackage monthlyTargetPackage) {
        UserInfo currentUser = tokenAnalysis.getTokenUser();
        List<String> targetNumList = monthlyTargetPackage.getTargetNum();
        String staffCode = monthlyTargetPackage.getStaffCode();

        // 查询签核流程表
        List<SignedWorkflow> signedWorkflowList = signedWorkflowDao.queryAll(SignedWorkflow.builder()
                .businessId(DEPART_MONTHLY_GOAL)
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

        // 查询第二个签核节点
        UserInfo userInfo = userInfoDao.selectByUserCode(staffCode);
        List<String> staffCodeList = userRoleDao.selectStaffCodeByRoleId(roleId);
        if (!staffCodeList.contains(staffCode)) {
            return new ResultUtils(500, "該審核人沒有人資角色，無法審核");
        }
        String approveStaffName = userInfo.getUserName();
        log.info("对应签核的第二个签核节点是 -> {}", staffCode);

        for (String targetNum : targetNumList) {
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
                    .investFieldValue(targetNum)
                    .status(USED_STATUS)
                    .isPass(SUBMIT_STATUS)
                    .build();
            rookieApprovalInfoDao.insert(submit);
            RookieApprovalInfo first = RookieApprovalInfo.builder()
                    .businessId(businessId)
                    .nodeId(firstSigned.getSignNodeId())
                    .nodeName(firstSigned.getNodeName())
                    .approveStaffName(approveStaffName)
                    .approveStaffCode(staffCode)
                    .projId(PROJ_ID)
                    .approveSequence(firstSigned.getSignSequence())
                    .investTableName(INVEST_TABLE_NAME)
                    .investFieldName(INVEST_FIELD_NAME)
                    .investFieldValue(targetNum)
                    .status(USED_STATUS)
                    .isPass(UN_AUDIT_STATUS)
                    .build();
            rookieApprovalInfoDao.insert(first);

            // 修改月目标的状态
            departmentTargetDao.updateTargetInfosByTargetNum(targetNum);
        }

        return new ResultUtils(101, "提交成功");
    }

    /**
     * 查询所有的月目标审核任务
     *
     * @return
     */
    @Override
    public ResultUtils queryMonthTarget() {
        UserInfo currentUser = tokenAnalysis.getTokenUser();
        String staffCode = currentUser.getStaffCode();

        // 查询所有月目标审核任务
        List<MonthTargetApprovalVo> monthTargetApprovalVoList = rookieApprovalInfoDao.queryMonthTargetApproval(staffCode);

        // 去重
        Map<Integer, List<MonthTargetApprovalVo>> map = new HashMap<>();
        Integer count = 1;
        if (monthTargetApprovalVoList == null) {
            return new ResultUtils(501, "查詢失敗，月目標審核任務為空");
        }
        for (MonthTargetApprovalVo rookie : monthTargetApprovalVoList) {
            List<MonthTargetApprovalVo> r = monthTargetApprovalVoList.stream()
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
            for (Map.Entry<Integer, List<MonthTargetApprovalVo>> entry : map.entrySet()) {
                monthTargetApprovalVoList.removeAll(entry.getValue());
                List<MonthTargetApprovalVo> rookieApprovalInfoVos = entry.getValue().stream()
                        .filter(rookieApprovalInfoVo1 -> {
                            if (rookieApprovalInfoVo1 != null && UN_AUDIT_STATUS.equals(rookieApprovalInfoVo1.getIsPass())) {
                                return true;
                            }
                            return false;
                        })
                        .collect(Collectors.toList());
                if (rookieApprovalInfoVos != null) {
                    MonthTargetApprovalVo rookieApprovalInfoVo = rookieApprovalInfoVos.size() > 0 ? rookieApprovalInfoVos.get(0) : entry.getValue().get(0);
                    monthTargetApprovalVoList.add(rookieApprovalInfoVo);
                }
            }
        }

        return new ResultUtils(100, "成功查詢所有的審核任務", monthTargetApprovalVoList);
    }

    /**
     * 查询个人所有的目标（周和月）
     *
     * @return
     */
    @Override
    public ResultUtils queryAllTarget() {
        UserInfo currentUser = tokenAnalysis.getTokenUser();
        String staffCode = currentUser.getStaffCode();

        // 查询当前登录用户的目标
        List<TargetVo> targetVoList = departmentTargetDao.queryAllTarget(staffCode);
        if (targetVoList == null || targetVoList.isEmpty()) {
            return new ResultUtils(501, "暫無數據");
        }
        //根据单号查询详情
        for (TargetVo targetVo : targetVoList) {
            String targetNum = targetVo.getTargetNum();
            log.info(targetNum);
            List<String> targetNumList = new ArrayList<>();
            targetNumList.add(targetNum);
            List<DepartTargetList> departTargetLists = departmentTargetDao.queryTargetDetailsInfo(targetNumList);
            targetVo.setDepartTargetLists(departTargetLists);
        }
        return new ResultUtils(100, "成功查詢所有目標", targetVoList);
    }
}
