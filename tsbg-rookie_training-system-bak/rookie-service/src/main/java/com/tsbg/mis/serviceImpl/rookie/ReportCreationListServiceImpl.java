package com.tsbg.mis.serviceImpl.rookie;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tsbg.mis.annotation.SysLog;
import com.tsbg.mis.dao.rookie.ProductionLineListDao;
import com.tsbg.mis.dao.rookie.ReportCreationListDao;
import com.tsbg.mis.rookie.model.*;
import com.tsbg.mis.service.rookie.ReportCreationListService;
import com.tsbg.mis.serviceImpl.jurisdiction.base.TokenAnalysis;
import com.tsbg.mis.util.ResultUtils;
import com.tsbg.mis.util.WeekDayUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Component
@Slf4j
public class ReportCreationListServiceImpl implements ReportCreationListService {

    @Autowired
    private ReportCreationListDao reportCreationListDao;
    @Autowired
    private ProductionLineListDao productionLineListDao;
    @Autowired
    private TokenAnalysis tokenAnalysis;

    //创建任务报告
    @Override
    @SysLog("创建任务报告")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ResultUtils creationReport(ReportCreationList reportCreationList) throws Exception {
        if(reportCreationList==null){
            return new ResultUtils(500,"参数错误");
        }

        Integer missionType = reportCreationList.getMissionType();
        if(null == missionType){
            return new ResultUtils(500,"missionType不能为空");
        }

        String classPeriod = reportCreationList.getClassPeriod();
        if(null == classPeriod){
            return new ResultUtils(500,"届别不能为空");
        }

        //设置添加者工号
        String staffCode = tokenAnalysis.getTokenUser().getStaffCode();
        reportCreationList.setCreatorCode(staffCode);

        Date internshipStart = reportCreationList.getInternshipStart();//获取实习开始时间
        Date internshipEnd = reportCreationList.getInternshipEnd();//获取实习结束时间

        //默认值
        if (reportCreationList.getDefaultStartTime() == null) {
            reportCreationList.setDefaultStartTime(6);
        }
        if (reportCreationList.getDefaultEndTime() == null) {
            reportCreationList.setDefaultEndTime(9);
        }

        if(missionType==98 || missionType==99 || missionType==100){//如果是考核

            //拼接missionName
            String missionName = getMissionName(missionType);
            reportCreationList.setMissionName(missionName);

            reportCreationList.setMissionStartTime(internshipStart);//设置任务开始时间(默认是考核任务的
            reportCreationList.setMissionEndTime(internshipEnd);//设置任务结束时间(默认是考核任务的

            if (reportCreationList.getDefaultStartTime() == 6) {
                reportCreationList.setStartTime(internshipStart);
            } else if (reportCreationList.getDefaultStartTime() == 7) {
                //当前日期加一天
                reportCreationList.setStartTime(conversion(internshipStart, 1));
            }

            //设置关闭填写时间(如果为10，未填写前不关闭填写入口，那就不需要设置关闭时间)
            if (reportCreationList.getDefaultEndTime() == 8) {
                //关闭填写时间为任务结束日期后5天
                reportCreationList.setEndTime(conversion(internshipEnd, 5));
            } else if (reportCreationList.getDefaultEndTime() == 9) {
                //关闭填写时间为任务结束日期后10天
                reportCreationList.setEndTime(conversion(internshipEnd, 10));
            }


            Integer assessCount = reportCreationListDao.creationReport(reportCreationList);
            if(null == assessCount || assessCount<=0){
                return new ResultUtils(500,"創建失敗");
            }
            return new ResultUtils(101,"創建成功");
        }else {//报告任务
            
            Integer dailyType;//日报的类型id
            Integer weeklyType;//周报的类型id
            Integer monthlyType;//月报的类型id
            if (reportCreationList.getMissionType() == 7) {//产线实习的报告类型
                dailyType = 9;
                weeklyType = 10;
                monthlyType = 11;
            } else {//部门实习的报告类型
                dailyType = 13;
                weeklyType = 14;
                monthlyType = 15;
            }
            String missionNameDaily = getMissionName(dailyType);
            String missionNameWeek = getMissionName(weeklyType);
            String missionNameMonth = getMissionName(monthlyType);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String internshipStartStr = sdf.format(internshipStart);
            String internshipEndStr = sdf.format(internshipEnd);

            //获取实习时间段内星期一到星期四的日期(只新增日报
            String[] monAndThursDates = WeekDayUtil.getDates(internshipStartStr, internshipEndStr, "星期一|星期二|星期三|星期四");
            for (String monAndThursDateString : monAndThursDates) {
                //新增日报
                ReportCreationList rcl = createDaily(monAndThursDateString, reportCreationList, dailyType);//新增日报方法（设置各种时间及报告类型
                rcl.setMissionName(missionNameDaily);
                Integer count = reportCreationListDao.creationReport(rcl);
                if (count == null || count <= 0) {
                    throw new Exception("創建日報失敗");
                }
            }

            //然后找出实习时间段内星期五的日期(新增日报和周报
            String[] friDates = WeekDayUtil.getDates(internshipStartStr, internshipEndStr, "星期五");
            for (String friDateString : friDates) {
                //新增日报
                ReportCreationList rclDaily = createDaily(friDateString, reportCreationList, dailyType);
                rclDaily.setMissionName(missionNameDaily);
                Integer dailyCount = reportCreationListDao.creationReport(rclDaily);
                if (dailyCount == null || dailyCount <= 0) {
                    throw new Exception("創建週五日報失敗");
                }
                //添加周报
                ReportCreationList rclWeekly = createWeekly(friDateString, reportCreationList, weeklyType);
                rclWeekly.setMissionName(missionNameWeek);
                Integer weeklyCount = reportCreationListDao.creationReport(rclWeekly);
                if (weeklyCount == null || weeklyCount <= 0) {
                    throw new Exception("創建週報失敗");
                }
            }
            //如果实习类型是产线实习，添加提案改善类报告
            if (reportCreationList.getMissionType() == 7) {
                reportCreationList.setMissionStartTime(internshipStart);
                reportCreationList.setMissionEndTime(internshipEnd);
                reportCreationList.setReportType(12);//12是提案改善类报告
                reportCreationList.setStartTime(conversion(internshipEnd, -4));
                if (reportCreationList.getDefaultEndTime() == 8) {
                    //关闭填写时间为任务结束日期后5天
                    reportCreationList.setEndTime(conversion(internshipEnd, 1));
                } else if (reportCreationList.getDefaultEndTime() == 9) {
                    //关闭填写时间为任务结束日期后10天
                    reportCreationList.setEndTime(conversion(internshipEnd, 6));
                }
                String missionNameProposal = getMissionName(12);
                reportCreationList.setMissionName(missionNameProposal);
                Integer proposalReportCount = reportCreationListDao.creationReport(reportCreationList);
                if (null == proposalReportCount || proposalReportCount <= 0) {
                    throw new Exception("創建提案改善報告任務失敗");
                }
            }

            //如果实习类型是部门实习，添加重大专案类报告
            if (reportCreationList.getMissionType() == 8) {
                reportCreationList.setMissionStartTime(internshipStart);
                reportCreationList.setMissionEndTime(internshipEnd);
                reportCreationList.setReportType(16);//16是重大专案类报告
                reportCreationList.setStartTime(conversion(internshipEnd, -4));
                if (reportCreationList.getDefaultEndTime() == 8) {
                    //关闭填写时间为任务结束日期后5天
                    reportCreationList.setEndTime(conversion(internshipEnd, 1));
                } else if (reportCreationList.getDefaultEndTime() == 9) {
                    //关闭填写时间为任务结束日期后10天
                    reportCreationList.setEndTime(conversion(internshipEnd, 6));
                }
                String missionNameMajor = getMissionName(16);
                reportCreationList.setMissionName(missionNameMajor);
                Integer proposalReportCount = reportCreationListDao.creationReport(reportCreationList);
                if (null == proposalReportCount || proposalReportCount <= 0) {
                    throw new Exception("創建重大专案報告任務失敗");
                }
            }

            //添加月报
            //找出时间段内一共有多少天
            String[] allDates = WeekDayUtil.getDates(internshipStartStr, internshipEndStr, "星期一|星期二|星期三|星期四|星期五|星期六|星期日");
            Integer dayNum = allDates.length;//一共有dayNum天
            Integer createCount = dayNum / 31;//需要添加的月报数
            if (dayNum % 31 != 0) {
                createCount += 1;
            }
            reportCreationList.setReportType(monthlyType);//设置报告类型
            for (int i = 1; i <= createCount; i++) {
                reportCreationList.setMissionStartTime(internshipStart);//每个月报的任务开始时间
                Calendar cl = Calendar.getInstance();
                cl.setTime(internshipStart);
                cl.add(Calendar.MONTH, 1);//加一个月
                cl.setTime(cl.getTime());
                cl.add(Calendar.DATE, -1);//减一天

                if (internshipEnd.getTime() > cl.getTime().getTime()) {
                    reportCreationList.setMissionEndTime(cl.getTime());//每个月报的任务结束时间
                    //默认设置月报开放填写时间提前4天，开放
                    reportCreationList.setStartTime(conversion(cl.getTime(), -4));//设置开放填写时间为这一轮任务结束时间的前4天
                    if (reportCreationList.getDefaultEndTime() != 10) {
                        reportCreationList.setEndTime(conversion(cl.getTime(), 6));//设置关闭填写时间为任务结束时间后6天，总共开放10天
                    }
                } else {//如果实习结束时间小于算出来的任务结束时间，则任务结束时间直接取实习结束时间
                    reportCreationList.setMissionEndTime(internshipEnd);
                    //默认设置月报开放填写时间提前4天开放
                    reportCreationList.setStartTime(conversion(internshipEnd, -4));
                    if (reportCreationList.getDefaultEndTime() == 8) {
                        //关闭填写时间为任务结束日期后5天
                        reportCreationList.setEndTime(conversion(internshipEnd, 1));
                    } else if (reportCreationList.getDefaultEndTime() == 9) {
                        //关闭填写时间为任务结束日期后10天
                        reportCreationList.setEndTime(conversion(internshipEnd, 6));
                    }
                }

                reportCreationList.setMissionName(missionNameMonth);
                Integer monthlyCount = reportCreationListDao.creationReport(reportCreationList);
                if (monthlyCount == null || monthlyCount <= 0) {
                    throw new Exception("創建月報失敗");
                }

                //把后一轮的任务开始时间设成前一轮的任务结束时间加一天
                cl.setTime(cl.getTime());
                cl.add(Calendar.DATE,1);
                internshipStart = cl.getTime();
            }

            return new ResultUtils(101, "創建成功");
        }
    }

    @Override
    //根据tab查找state_list表
    public ResultUtils selectStateListByTab(Integer tab) {
        List<StateList> stateLists = reportCreationListDao.selectStateListByTab(tab);
        if (stateLists != null && !stateLists.isEmpty()) {
            return new ResultUtils(100, "查找成功", stateLists);
        } else if (stateLists != null && stateLists.isEmpty()) {
            return new ResultUtils(100, "查找為空", stateLists);
        } else {
            return new ResultUtils(500, "查找失敗");
        }
    }

    @Override
    //根据tab查找type_list表
    public ResultUtils selectTypeListByTab(Integer tab, Integer pid) {
        List<TypeList> typeLists = reportCreationListDao.selectTypeListByTab(tab, pid);

        if (typeLists != null && !typeLists.isEmpty()) {
            return new ResultUtils(100, "查找成功", typeLists);
        } else if (typeLists != null && typeLists.isEmpty()) {
            return new ResultUtils(100, "查找為空", typeLists);
        } else {
            return new ResultUtils(500, "查找失敗");
        }
    }

    @Override
    //模糊搜索报告任务
    public ResultUtils fuzzySearchReport(ReportCreationList reportCreationList) {
        List<ReportCreationList> reportCreationLists = reportCreationListDao.fuzzySearchReport(reportCreationList);
        if (reportCreationLists != null && !reportCreationLists.isEmpty()) {
            return new ResultUtils(100, "查詢成功", reportCreationLists);
        } else if (reportCreationLists != null && reportCreationLists.isEmpty()) {
            return new ResultUtils(100, "查詢為空", reportCreationLists);
        } else {
            return new ResultUtils(500, "查詢失敗");
        }
    }

    //填写产线实习报告
    @Override
    @SysLog("填写产线实习报告")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ResultUtils insertLineReport(ReportProductionList reportProductionList) throws Exception {
        if (reportProductionList == null) {
            return new ResultUtils(500, "參數錯誤");
        }
        if (null == reportProductionList.getReportType()) {
            return new ResultUtils(500, "請傳報告類型(reportType)");
        }
        Integer lineId = reportProductionList.getLineId();
        if (null == lineId) {
            return new ResultUtils(500, "lineId不能為空");
        }
        //查询此任务是否已关闭填写
        try {
            boolean isStop = isStop(reportProductionList);
            if (isStop) {
                return new ResultUtils(500, "此任務已關閉填寫");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Integer state = 2;
        if (reportProductionList.getReportType() == 9) {
            state = 4;
        }
        //获取工号并set
        String staffCode = tokenAnalysis.getTokenUser().getStaffCode();
        //String staffCode = "F1338000";
        reportProductionList.setStaffCode(staffCode);
        reportProductionList.setStateId(state);//固定保存到草稿箱

        Integer isHaveProblem = reportProductionList.getIsHaveProblem();
        if (isHaveProblem == null) {
            isHaveProblem = 0;
            reportProductionList.setIsHaveProblem(isHaveProblem);
        }

        //校验
        if (reportProductionList.getReportType() == 9 || reportProductionList.getReportType() == 10) {
            String internshipContent = reportProductionList.getInternshipContent();
            if (null == internshipContent || internshipContent.equals("")) {
                return new ResultUtils(500, "實習內容不能為空");
            }
        }
        String internshipHarvest = reportProductionList.getInternshipHarvest();
        if (null == internshipHarvest || internshipHarvest.equals("")) {
            return new ResultUtils(500, "實習心得(總結)不能為空");
        }

        //当传进来是产线实习周报的时候
        if (reportProductionList.getReportType() == 10) {
            if (reportProductionList.getMentalStateList() == null || reportProductionList.getMentalStateList().isEmpty()) {
                return new ResultUtils(500, "本週狀態不能為空");
            } else {
                List<Integer> mentalStateList = reportProductionList.getMentalStateList();
                if (null == mentalStateList || mentalStateList.isEmpty()) {
                    return new ResultUtils(500, "本週狀態不能為空");
                }
                if (mentalStateList.contains(17) || mentalStateList.contains(18)) {
                    if (mentalStateList.contains(19) || mentalStateList.contains(20) || mentalStateList.contains(21)) {
                        return new ResultUtils(500, "本週狀態選擇衝突");
                    }
                } else if (mentalStateList.contains(19) || mentalStateList.contains(20) || mentalStateList.contains(21)) {
                    if (mentalStateList.contains(17) || mentalStateList.contains(18)) {
                        return new ResultUtils(500, "本週狀態選擇衝突");
                    }
                }
                StringBuilder mentalStateStr = new StringBuilder();
                for (Integer mentalState : mentalStateList) {
                    mentalStateStr.append(mentalState).append(",");
                }
                reportProductionList.setMentalState(mentalStateStr.substring(0, mentalStateStr.length() - 1));
            }
        }

        //当传进来的是产线实习月报的时候
        if (reportProductionList.getReportType() == 11) {
            //获取improve_category列表，如果不为空则拼接再set
            if (null == reportProductionList.getImproveCategoryList() || reportProductionList.getImproveCategoryList().isEmpty()) {
                return new ResultUtils(500, "改善範疇不能為空");
            } else {
                List<Integer> improveCategoryList = reportProductionList.getImproveCategoryList();
                if (null == improveCategoryList || improveCategoryList.isEmpty()) {
                    return new ResultUtils(500, "改善範疇不能為空");
                }
                StringBuilder improveCategoryStr = new StringBuilder();
                for (Integer improveCategory : improveCategoryList) {
                    improveCategoryStr.append(improveCategory).append(",");
                }
                reportProductionList.setImproveCategory(improveCategoryStr.substring(0, improveCategoryStr.length() - 1));
            }

            //拼接internship_state(实习阶段)
            if (null == reportProductionList.getInternshipStateList() || reportProductionList.getInternshipStateList().isEmpty()) {
                return new ResultUtils(500, "實習階段不能為空");
            } else {
                List<Integer> internshipStateList = reportProductionList.getInternshipStateList();
                if (null == internshipStateList || internshipStateList.isEmpty()) {
                    return new ResultUtils(500, "實習階段不能為空");
                }
                StringBuilder internshipStateStr = new StringBuilder();
                for (Integer internshipState : internshipStateList) {
                    internshipStateStr.append(internshipState).append(",");
                }
                reportProductionList.setInternshipState(internshipStateStr.substring(0, internshipStateStr.length() - 1));
            }

            if (reportProductionList.getQuestionDescription() == null || reportProductionList.getQuestionDescription().equals("")) {
                return new ResultUtils(500, "發現問題不能為空");
            }
            if (reportProductionList.getQuestionSuggestion() == null || reportProductionList.getQuestionSuggestion().equals("")) {
                return new ResultUtils(500, "解決方案不能為空");
            }
        }

        //保存報告(产线实习)
        Integer productionCount = reportCreationListDao.insertLineReport(reportProductionList);//保存产线实习报告的Dao
        if (null == productionCount || productionCount <= 0) {
            throw new Exception("保存失敗");
        } else {
            Integer reportId = reportProductionList.getReportId();
            if (reportId == null) {
                throw new Exception("插入文件失敗");
            }
            //添加文件
            FileInfo fileInfo = reportProductionList.getFileInfo();
            if (fileInfo != null) {
                fileInfo.setConnectTableName("report_production_list");
                fileInfo.setConnectFieldsName("report_id");
                fileInfo.setConnectFieldsValue(reportId.toString());
                fileInfo.setUpdateUserCode(staffCode);
                fileInfo.setProjId(10);
                Integer count = reportCreationListDao.insertFileInfo(fileInfo);
                if (count == null || count <= 0) {
                    throw new Exception("插入文件失敗");
                }
            }


            //问题与建议
            if (isHaveProblem == 1) {
                List<ReportProductionWeekQuestion> reportProductionWeekQuestionList
                        = reportProductionList.getReportProductionWeekQuestionList();
                if (reportProductionWeekQuestionList == null || reportProductionWeekQuestionList.isEmpty()) {
                    throw new Exception("問題和建議不能為空");
                } else {
                    for (ReportProductionWeekQuestion reportProductionWeekQuestion : reportProductionWeekQuestionList) {
                        reportProductionWeekQuestion.setReportId(reportId);
                        Integer questionCount = reportCreationListDao.insertProductionQuestion(reportProductionWeekQuestion);
                        if (null == questionCount || questionCount <= 0) {
                            throw new Exception("添加問題與建議失敗");
                        }
                    }
                }
            }
        }
        return new ResultUtils(101, "保存成功", reportProductionList);
    }

    @SysLog("填写提案改善类报告")
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ResultUtils insertProposalReport(ReportProductionProposalList reportProductionProposalList) throws Exception {
        if (reportProductionProposalList == null) {
            return new ResultUtils(500, "參數錯誤");
        }
        //校验
        String proposalTheme = reportProductionProposalList.getProposalTheme();
        if (null == proposalTheme || proposalTheme.equals("")) {
            return new ResultUtils(500, "提案主體不能為空");
        }
        String analyzeContent = reportProductionProposalList.getAnalyzeContent();
        if (null == analyzeContent || analyzeContent.equals("")) {
            return new ResultUtils(500, "分析內容不能為空");
        }
        String iPurposeDescription = reportProductionProposalList.getiPurposeDescription();
        if (null == iPurposeDescription || iPurposeDescription.equals("")) {
            return new ResultUtils(500, "目的描述不能為空");
        }
        String improveMethod = reportProductionProposalList.getImproveMethod();
        if (null == improveMethod || improveMethod.equals("")) {
            return new ResultUtils(500, "改善對策不能為空");
        }
        String executePlan = reportProductionProposalList.getExecutePlan();
        if (null == executePlan || executePlan.equals("")) {
            return new ResultUtils(500, "執行計劃不能為空");
        }
        String performanceDescription = reportProductionProposalList.getPerformanceDescription();
        if (null == performanceDescription || performanceDescription.equals("")) {
            return new ResultUtils(500, "可達績效描述不能為空");
        }
        Integer yearCostSaving = reportProductionProposalList.getYearCostSaving();
        if (yearCostSaving == null) {
            return new ResultUtils(500, "按年省不能為空");
        }
        Integer onceCostSaving = reportProductionProposalList.getOnceCostSaving();
        if (null == onceCostSaving) {
            return new ResultUtils(500, "一次性節省不能為空");
        }
        String costSavingReason = reportProductionProposalList.getCostSavingReason();
        if (null == costSavingReason) {
            return new ResultUtils(500, "節省的理由和依據不能為空");
        }

        String staffCode = tokenAnalysis.getTokenUser().getStaffCode();
        reportProductionProposalList.setStateId(2);

        if (null == reportProductionProposalList.getProposalTypeList() || reportProductionProposalList.getProposalTypeList().isEmpty()) {
            return new ResultUtils(500, "提案類別不能為空");
        } else {
            List<Integer> proposalTypeList = reportProductionProposalList.getProposalTypeList();
            if (null == proposalTypeList || proposalTypeList.isEmpty()) {
                return new ResultUtils(500, "提案類別不能為空");
            }
            StringBuilder proposalTypeStr = new StringBuilder();
            for (Integer proposalType : proposalTypeList) {
                proposalTypeStr.append(proposalType).append(",");
            }
            reportProductionProposalList.setProposalType(proposalTypeStr.substring(0, proposalTypeStr.length() - 1));
        }

        if (null == reportProductionProposalList.getAnalyzeMethodList() || reportProductionProposalList.getAnalyzeMethodList().isEmpty()) {
            return new ResultUtils(500, "分析手法不能為空");
        } else {
            List<Integer> analyzeMethodList = reportProductionProposalList.getAnalyzeMethodList();
            if (null == analyzeMethodList || analyzeMethodList.isEmpty()) {
                return new ResultUtils(500, "分析手法不能為空");
            }
            StringBuilder analyzeMethodStr = new StringBuilder();
            for (Integer analyzeMethod : analyzeMethodList) {
                analyzeMethodStr.append(analyzeMethod).append(",");
            }
            reportProductionProposalList.setAnalyzeMethod(analyzeMethodStr.substring(0, analyzeMethodStr.length() - 1));
        }

        if (null == reportProductionProposalList.getImproveCategoryList() || reportProductionProposalList.getImproveCategoryList().isEmpty()) {
            return new ResultUtils(500, "改善範疇不能為空");
        } else {
            List<Integer> improveCategoryList = reportProductionProposalList.getImproveCategoryList();
            if (null == improveCategoryList || improveCategoryList.isEmpty()) {
                return new ResultUtils(500, "改善範疇不能為空");
            }
            StringBuilder improveCategoryStr = new StringBuilder();
            for (Integer improveCategory : improveCategoryList) {
                improveCategoryStr.append(improveCategory).append(",");
            }
            reportProductionProposalList.setImproveCategory(improveCategoryStr.substring(0, improveCategoryStr.length() - 1));
        }

        //保存提案改善的Dao
        Integer proposalReportCount = reportCreationListDao.insertProposalReport(reportProductionProposalList);
        if (null == proposalReportCount || proposalReportCount <= 0) {
            return new ResultUtils(500, "新增提案改善報告失敗");
        } else {
            Integer proposalId = reportProductionProposalList.getProposalId();
            //添加文件
            FileInfo fileInfo = reportProductionProposalList.getFileInfo();
            if (fileInfo != null) {
                fileInfo.setConnectTableName("report_production_proposal_list");
                fileInfo.setConnectFieldsName("proposal_id");
                fileInfo.setConnectFieldsValue(proposalId.toString());
                fileInfo.setUpdateUserCode(staffCode);
                fileInfo.setProjId(10);
                Integer count = reportCreationListDao.insertFileInfo(fileInfo);
                if (count == null || count <= 0) {
                    throw new Exception("插入文件失敗");
                }
            }
            return new ResultUtils(101, "保存成功", reportProductionProposalList);
        }
    }

    //填写部门实习报告
    @Override
    @SysLog("填写部门实习报告")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ResultUtils insertDepartReport(ReportDepartList reportDepartList) throws Exception {
        if (reportDepartList == null) {
            return new ResultUtils(500, "參數錯誤");
        }
        Integer reportType = reportDepartList.getReportType();
        if (reportType == null) {
            return new ResultUtils(500, "請選擇報告類型");
        }
        if (reportType == 14 || reportType == 15) {
            if (reportDepartList.getWorkHarvest() == null || reportDepartList.getWorkHarvest().equals("")) {
                return new ResultUtils(500, "工作心得分享不能為空");
            }
            if (reportDepartList.getLifeHarvest() == null || reportDepartList.getLifeHarvest().equals("")) {
                return new ResultUtils(500, "生活心得分享不能為空");
            }
            if (reportDepartList.getWorkSuggestion() == null || reportDepartList.getWorkSuggestion().equals("")) {
                return new ResultUtils(500, "工作中發現的問題不能為空");
            }
        }


        String staffCode = tokenAnalysis.getTokenUser().getStaffCode();
        reportDepartList.setStateId(2);
        Integer count = reportCreationListDao.insertDepartReport(reportDepartList);
        if (null == count || count <= 0) {
            throw new Exception("保存報告失敗");
        }
        Integer reportId = reportDepartList.getReportId();
        List<ReportDepartWork> reportDepartWorkList = reportDepartList.getReportDepartWorkList();
        if (null != reportDepartWorkList && !reportDepartWorkList.isEmpty()) {
            for (ReportDepartWork reportDepartWork : reportDepartWorkList) {
                String fulfillmentOfSchedule = reportDepartWork.getFulfillmentOfSchedule();
                if(null == reportDepartWork.getPid()){
                    reportDepartWork.setPid(0);
                }
                String workContent = reportDepartWork.getWorkContent();
                if(null == workContent || workContent.equals("")){
                    return new ResultUtils(500,"工作內容不能為空");
                }

                //校验
                Integer tab = reportDepartWork.getTab();
                if (null != tab) {
                    List<Integer> tabModel = Arrays.asList(0,2,3,4,8,9,10);
                    if(tabModel.contains(tab)){
                        if (reportDepartWork.getFulfillmentOfSchedule() == null || reportDepartWork.getFulfillmentOfSchedule().equals("")) {
                            throw new Exception("完成進度不能為空");
                        }
                        //為周报月报时做以下判断
                        String totalTime = reportDepartWork.getTotalTime();
                        if(null == totalTime || totalTime.equals("")){
                            return new ResultUtils(500,"完成用時不能為空");
                        }
                        if(null != fulfillmentOfSchedule && fulfillmentOfSchedule.equals("100")){
                            String finishProof = reportDepartWork.getFinishProof();
                            if(null == finishProof || finishProof.equals("")){
                                return new ResultUtils(500,"證據支持不能為空");
                            }
                        }else if(null != fulfillmentOfSchedule && !fulfillmentOfSchedule.equals("100")){
                            String unfinishedReason = reportDepartWork.getUnfinishedReason();
                            if(null == unfinishedReason || unfinishedReason.equals("")){
                                return new ResultUtils(500,"未完成原因不能為空");
                            }
                            if(reportType == 14 || reportType == 15){
                                Integer isContinue = reportDepartWork.getIsContinue();
                                if(null == isContinue){
                                    return new ResultUtils(500,"請選擇是否繼續");
                                }
                                if(isContinue == 1){
                                    String nextWeekWork = reportDepartWork.getNextWeekWork();
                                    if(null == nextWeekWork || nextWeekWork.equals("")){
                                        return new ResultUtils(500,"下週待做不能為空");
                                    }
                                }
                            }
                        }
                    }
                }


                //如果本周待做任务不为空，则为上周遗留下来的任务
                if(null != reportDepartWork.getToDoWork() && !reportDepartWork.getToDoWork().equals("")){
                    //如果完成进度是100,已完成
                    if(null != fulfillmentOfSchedule && fulfillmentOfSchedule.equals("100")){
                        reportDepartWork.setIsContinue(0);
                        Integer pid = reportDepartWork.getPid();
                        Integer workId;
                        if(pid == null){
                            pid = 0;
                        }
                        //则把这条任务之前没做完的任务是否继续设为0
                        do {
                            //查找workId是这条记录的pid的work(找遗留下来的父work)
                            ReportDepartWork pidWork = reportCreationListDao.selectWorkPid(pid);
                            if(null != pidWork){
                                workId = pidWork.getWorkId();
                                pid = pidWork.getPid();
                                reportCreationListDao.updateWorkIsContinue(workId,0);
                            }
                            if(pid == null){
                                pid = 0;
                            }
                        }while (pid!=0);
                    }
                }

                reportDepartWork.setReportId(reportId);

                //新增工作信息
                Integer workCount = reportCreationListDao.insertDepartWork(reportDepartWork);
                if (null == workCount || workCount <= 0) {
                    throw new Exception("添加工作信息失敗");
                }

                FileInfo fileInfo = reportDepartWork.getFileInfo();
                if (fileInfo != null) {
                    fileInfo.setConnectTableName("report_depart_work");
                    fileInfo.setConnectFieldsName("work_id");
                    Integer workId = reportDepartWork.getWorkId();
                    if (workId == null) {
                        return new ResultUtils(500, "workId不能为空");
                    }
                    fileInfo.setConnectFieldsValue(workId.toString());
                    fileInfo.setUpdateUserCode(staffCode);
                    fileInfo.setProjId(10);
                    Integer fileCount = reportCreationListDao.insertFileInfo(fileInfo);
                    if (fileCount == null || fileCount <= 0) {
                        throw new Exception("插入文件失敗");
                    }
                }
            }
        }

        return new ResultUtils(101, "保存成功", reportDepartList);
    }

    //编辑报告任务
    @Override
    @SysLog("编辑报告任务")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ResultUtils updateReport(ReportCreationList reportCreationList) {
        Integer updateCount = reportCreationListDao.updateReport(reportCreationList);
        if (null != updateCount && updateCount > 0) {
            if (reportCreationList.getStatus() != null && reportCreationList.getStatus() == 0) {
                return new ResultUtils(101, "刪除成功");
            } else {
                return new ResultUtils(101, "編輯成功");
            }
        } else {
            return new ResultUtils(500, "編輯失敗");
        }
    }

    @Override
    //查询当前学生所有报告
    public ResultUtils selectAllReportByStaffCode() {
        String staffCode = tokenAnalysis.getTokenUser().getStaffCode();
        List<ReportCreationList> notInReports = reportCreationListDao.selectReportNotFillIn(staffCode);//首先查出所有未填写的报告
        if (notInReports != null && !notInReports.isEmpty()) {
            for (ReportCreationList notInReport : notInReports) {
                notInReport.setStateId(1);
                notInReport.setStateName("未填寫");
            }
        } else if (null == notInReports) {
            notInReports = new ArrayList<>();
        }
        List<ReportCreationList> productionListReports = reportCreationListDao.selectReportFillInProductionList(staffCode);//再查出所有的已填产线实习报告
        List<ReportCreationList> departReports = reportCreationListDao.selectReportFillInDepart(staffCode);//再查出所有部门实习已填的实习报告
        List<ReportCreationList> proposalReports = reportCreationListDao.selectReportFillInProposal(staffCode);//查出所有已填的提案改善報告
        //再把三个集合合并起来返给前端
        if (productionListReports != null && !productionListReports.isEmpty()) {
            notInReports.addAll(productionListReports);
        }
        if (departReports != null && !departReports.isEmpty()) {
            notInReports.addAll(departReports);
        }
        if (null != proposalReports && !proposalReports.isEmpty()) {
            for (ReportCreationList r : proposalReports) {
                r.setMissionTypeName("產線實習");
                r.setMissionType(7);
            }
            notInReports.addAll(proposalReports);
        }

        for (int i = notInReports.size() - 1; i >= 0; i--) {
            Integer isEnable = notInReports.get(i).getIsEnable();
            Date startTime = notInReports.get(i).getStartTime();
            Date endTime = notInReports.get(i).getEndTime();
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if (isEnable == 0) {
                notInReports.remove(i);
            } else {
                try {
                    date = sdf.parse(sdf.format(date));
                    if (date.getTime() < startTime.getTime()) {
                        notInReports.remove(i);
                    }
                    if (null != endTime) {
                        if (date.getTime() > endTime.getTime()) {
                            notInReports.remove(i);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return new ResultUtils(100, "查詢成功", notInReports);
    }

    //日期计算方法（加减日期
    public Date conversion(Date date, Integer changeDay) {//date:要计算的日期;changeDay:要加减的天数，加为正数，减为负数
        Calendar cl = Calendar.getInstance();
        cl.setTime(date);
        cl.add(Calendar.DATE, changeDay);
        return cl.getTime();
    }

    //新增日报
    public ReportCreationList createDaily(String dateString, ReportCreationList reportCreationList, Integer reportType) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//格式化，String类型日期转Date类型
        try {
            Date monAndThursDate = sdf.parse(dateString);
            reportCreationList.setReportType(reportType);//设置报告类型，9是日报
            //任务开始时间和结束时间都是这天
            reportCreationList.setMissionStartTime(monAndThursDate);
            reportCreationList.setMissionEndTime(monAndThursDate);
            //设置开始填写时间
            if (reportCreationList.getDefaultStartTime() == 6) {
                reportCreationList.setStartTime(monAndThursDate);
            } else if (reportCreationList.getDefaultStartTime() == 7) {
                //当前日期加一天
                reportCreationList.setStartTime(conversion(monAndThursDate, 1));
            }

            //设置关闭填写时间(如果为10，未填写前不关闭填写入口，那就不需要设置关闭时间)
            if (reportCreationList.getDefaultEndTime() == 8) {
                //关闭填写时间为任务结束日期后5天
                reportCreationList.setEndTime(conversion(monAndThursDate, 5));
            } else if (reportCreationList.getDefaultEndTime() == 9) {
                //关闭填写时间为任务结束日期后10天
                reportCreationList.setEndTime(conversion(monAndThursDate, 10));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return reportCreationList;
        }
    }

    //新增周报
    public ReportCreationList createWeekly(String dateString, ReportCreationList reportCreationList, Integer reportType) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//格式化，String类型日期转Date类型
        try {
            Date FriDate = sdf.parse(dateString);
            reportCreationList.setReportType(reportType);//设置报告类型
            //任务开始时间是周一（周五减四天、结束时间是这周五这天
            if (conversion(FriDate, -4).getTime() < reportCreationList.getInternshipStart().getTime()) {
                reportCreationList.setMissionStartTime(reportCreationList.getInternshipStart());
            } else {
                reportCreationList.setMissionStartTime(conversion(FriDate, -4));
            }
            reportCreationList.setMissionEndTime(FriDate);
            //设置开始填写时间
            if (reportCreationList.getDefaultStartTime() == 6) {
                reportCreationList.setStartTime(FriDate);
            } else if (reportCreationList.getDefaultStartTime() == 7) {
                //当前日期加一天
                reportCreationList.setStartTime(conversion(FriDate, 1));
            }

            //设置关闭填写时间(如果为14，未填写前不关闭填写入口，那就不需要设置关闭时间)
            if (reportCreationList.getDefaultEndTime() == 8) {
                //关闭填写时间为任务结束日期后5天
                reportCreationList.setEndTime(conversion(FriDate, 5));
            } else if (reportCreationList.getDefaultEndTime() == 9) {
                //关闭填写时间为任务结束日期后10天
                reportCreationList.setEndTime(conversion(FriDate, 10));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return reportCreationList;
        }
    }

    //查询是否已关闭填写
    public boolean isStop(ReportProductionList reportProductionList) throws ParseException {
        Integer creationId = reportProductionList.getCreationId();
        ReportCreationList reportCreationList = new ReportCreationList();
        reportCreationList.setCreationId(creationId);
        List<ReportCreationList> reportCreationLists = reportCreationListDao.fuzzySearchReport(reportCreationList);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        date = sdf.parse(sdf.format(date));
        if (null != reportCreationLists && !reportCreationLists.isEmpty()) {
            for (ReportCreationList reportCreation : reportCreationLists) {
                if (null != reportCreation.getEndTime()) {
                    if (reportCreation.getEndTime().getTime() < date.getTime()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    //根据关闭填写时间定时关闭报告任务
    @Scheduled(cron = "0 0 0 * * ?")//每天0点执行一次
    @SysLog("定时关闭报告任务")
    public void closeCreation() {
        //获取当前日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = sdf.parse(sdf.format(new Date()));
            reportCreationListDao.updateCreationIsEnableWhereOverdue(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //查询学生在这个时间下是在哪条产线
    @Override
    public ResultUtils selectWhichLine(String staffCode, Date missionEndTime) {
        ProductionLineList productionLineList = reportCreationListDao.selectWhichLine(staffCode, missionEndTime);
        if (null != productionLineList) {
            return new ResultUtils(100, "查詢成功", productionLineList);
        } else {
            return new ResultUtils(501, "學生不在產線內");
        }
    }

    //查询报告详细内容
    @Override
    public ResultUtils selectReportInfo(Integer reportId, Integer reportType) {
        if (null == reportType) {
            return new ResultUtils(500, "請選擇報告類型");
        }
        if (reportId == null) {
            return new ResultUtils(500, "請選擇報告");
        }
        if (reportType == 9 || reportType == 10 || reportType == 11) {//产线
            ReportProductionList reportProductionList = reportCreationListDao.selectProductionReportInfo(reportId);//查找報告表信息
            if (null == reportProductionList) {
                return new ResultUtils(500, "查詢失敗");
            }
            Integer creationId = reportProductionList.getCreationId();
            //查找任務的時間
            ReportCreationList reportCreationList = reportCreationListDao.selectTimeInCreationList(creationId);
            if (null == reportCreationList) {
                return new ResultUtils(500, "查詢失敗");
            }
            Date missionStartTime = reportCreationList.getMissionStartTime();
            Date missionEndTime = reportCreationList.getMissionEndTime();
            reportProductionList.setMissionStartTime(missionStartTime);
            reportProductionList.setMissionEndTime(missionEndTime);

            //如果是週報則要查詢日報內容
            if (reportType == 10) {
                //查找產線週報下的產線日報內容
                List<ReportProductionList> reportProductionLists = reportCreationListDao.selectDailyByWeek(reportProductionList);
                if (null != reportProductionLists && !reportProductionLists.isEmpty()) {
                    reportProductionList.setDailyReportList(reportProductionLists);
                }
            }

            String internshipState = reportProductionList.getInternshipState();
            List<Integer> internshipStateList = new ArrayList<>();
            if (internshipState != null && !internshipState.equals("")) {
                String[] split = internshipState.split(",");
                for (String state : split) {
                    if (null != state) {
                        int parseInt = Integer.parseInt(state);
                        internshipStateList.add(parseInt);
                    }
                }
                reportProductionList.setInternshipStateList(internshipStateList);
            }

            String improveCategory = reportProductionList.getImproveCategory();
            List<Integer> improveCategoryList = new ArrayList<>();
            if (improveCategory != null && !improveCategory.equals("")) {
                String[] split = improveCategory.split(",");
                for (String category : split) {
                    if (null != category) {
                        int parseInt = Integer.parseInt(category);
                        improveCategoryList.add(parseInt);
                    }
                }
                reportProductionList.setImproveCategoryList(improveCategoryList);
            }

            String mentalState = reportProductionList.getMentalState();
            List<Integer> mentalStateList = new ArrayList<>();
            if (mentalState != null && !mentalState.equals("")) {
                String[] split = mentalState.split(",");
                for (String state : split) {
                    if (null != state) {
                        int parseInt = Integer.parseInt(state);
                        mentalStateList.add(parseInt);
                    }
                }
                reportProductionList.setMentalStateList(mentalStateList);
            }

            String attendance = reportProductionList.getAttendance();
            List<Integer> attendanceList = new ArrayList<>();
            if (attendance != null && !attendance.equals("")) {
                String[] split = attendance.split(",");
                for (String at : split) {
                    if (null != at) {
                        int parseInt = Integer.parseInt(at);
                        attendanceList.add(parseInt);
                    }
                }
                reportProductionList.setAttendanceList(attendanceList);
            }

            // 查询对应产线报告的评语和评分
            if (reportType == 11) {
                String investTableName = "report_production_list";
                List<RookieApprovalInfo> rookieApprovalInfos = reportCreationListDao.selectApprovalInfo(investTableName, reportId, 10);
                if (null != rookieApprovalInfos && !rookieApprovalInfos.isEmpty()) {
                    reportProductionList.setRookieApprovalInfos(rookieApprovalInfos);
                }
            }
            //查询问题建议
            List<ReportProductionWeekQuestion> reportProductionWeekQuestions = reportCreationListDao.selectProductionQuestion(reportId);
            if (null != reportProductionWeekQuestions && !reportProductionWeekQuestions.isEmpty()) {
                reportProductionList.setReportProductionWeekQuestionList(reportProductionWeekQuestions);
            }

            //查询附件
            FileInfo fileInfo = new FileInfo();
            fileInfo.setConnectTableName("report_production_list");
            fileInfo.setConnectFieldsName("report_id");
            fileInfo.setConnectFieldsValue(reportId.toString());
            FileInfo fileInfo1 = reportCreationListDao.selectFileInfo(fileInfo);
            reportProductionList.setFileInfo(fileInfo1);
            return new ResultUtils(100, "查詢成功", reportProductionList);

        } else if (reportType == 12) {//提案改善类报告

            ReportProductionProposalList reportProductionProposalList = reportCreationListDao.selectProposalReportInfo(reportId);

            if (reportProductionProposalList != null) {
                reportProductionProposalList.setMissionTypeName("產線實習");
                Integer creationId = reportProductionProposalList.getCreationId();
                ReportCreationList reportCreationList = reportCreationListDao.selectTimeInCreationList(creationId);
                if (reportCreationList == null) {
                    return new ResultUtils(501, "查詢失敗");
                }
                Date missionStartTime = reportCreationList.getMissionStartTime();
                Date missionEndTime = reportCreationList.getMissionEndTime();
                reportProductionProposalList.setMissionStartTime(missionStartTime);
                reportProductionProposalList.setMissionEndTime(missionEndTime);
                //查询评语评分
                String investTableName = "report_production_proposal_list";
                List<RookieApprovalInfo> rookieApprovalInfos = reportCreationListDao.selectApprovalInfo(investTableName, reportId, 14);
                if (null != rookieApprovalInfos && !rookieApprovalInfos.isEmpty()) {
                    reportProductionProposalList.setRookieApprovalInfos(rookieApprovalInfos);
                }

                //把拼接的字段转成数组
                //提案类别
                String proposalType = reportProductionProposalList.getProposalType();
                List<Integer> proposalTypeList = new ArrayList<>();
                if (null != proposalType && !proposalType.equals("")) {
                    String[] split = proposalType.split(",");
                    for (String proposal : split) {
                        if (null != proposal) {
                            int parseInt = Integer.parseInt(proposal);
                            proposalTypeList.add(parseInt);
                        }
                    }
                    reportProductionProposalList.setProposalTypeList(proposalTypeList);
                }

                //分析手法
                String analyzeMethod = reportProductionProposalList.getAnalyzeMethod();
                List<Integer> analyzeMethodList = new ArrayList<>();
                if (null != analyzeMethod && !analyzeMethod.equals("")) {
                    String[] split = analyzeMethod.split(",");
                    for (String analyze : split) {
                        if (null != analyze) {
                            int parseInt = Integer.parseInt(analyze);
                            analyzeMethodList.add(parseInt);
                        }
                    }
                    reportProductionProposalList.setAnalyzeMethodList(analyzeMethodList);
                }

                //改善范畴
                String improveCategory = reportProductionProposalList.getImproveCategory();
                List<Integer> improveCategoryList = new ArrayList<>();
                if (null != improveCategory && !improveCategory.equals("")) {
                    String[] split = improveCategory.split(",");
                    for (String imp : split) {
                        if (null != imp) {
                            int parseInt = Integer.parseInt(imp);
                            improveCategoryList.add(parseInt);
                        }
                    }
                    reportProductionProposalList.setImproveCategoryList(improveCategoryList);
                }

                //查詢附件
                FileInfo fileInfo = new FileInfo();
                fileInfo.setConnectTableName("report_production_proposal_list");
                fileInfo.setConnectFieldsName("proposal_id");
                fileInfo.setConnectFieldsValue(reportId.toString());
                FileInfo fileInfo1 = reportCreationListDao.selectFileInfo(fileInfo);
                reportProductionProposalList.setFileInfo(fileInfo1);
            }
            return new ResultUtils(100, "查詢成功", reportProductionProposalList);
        } else if (reportType == 13 || reportType == 14 || reportType == 15) {//部门
            ReportDepartList reportDepartList = reportCreationListDao.selectDepartReportInfo(reportId);
            if (reportDepartList != null) {
                Integer creationId = reportDepartList.getCreationId();
                ReportCreationList reportCreationList = reportCreationListDao.selectTimeInCreationList(creationId);
                if (reportCreationList == null) {
                    return new ResultUtils(501, "查詢失敗");
                }
                Date missionStartTime = reportCreationList.getMissionStartTime();
                Date missionEndTime = reportCreationList.getMissionEndTime();
                reportDepartList.setMissionStartTime(missionStartTime);
                reportDepartList.setMissionEndTime(missionEndTime);
                if (reportType == 15) {
                    String investTableName = "report_depart_list";
                    List<RookieApprovalInfo> rookieApprovalInfos = reportCreationListDao.selectApprovalInfo(investTableName, reportId, 14);
                    if (null != rookieApprovalInfos && !rookieApprovalInfos.isEmpty()) {
                        reportDepartList.setRookieApprovalInfos(rookieApprovalInfos);
                    }
                }
                //查询附件
                //先查出有多少work
                //TODO 查找work的邏輯需要重新思考  ! !
                List<ReportDepartWork> reportDepartWorks = reportCreationListDao.selectDepartWork(reportId);
                if (null != reportDepartWorks && !reportDepartWorks.isEmpty()) {
                    for (ReportDepartWork work : reportDepartWorks) {
                        FileInfo fileInfo = new FileInfo();
                        fileInfo.setConnectTableName("report_depart_work");
                        fileInfo.setConnectFieldsName("work_id");
                        Integer workId = work.getWorkId();
                        if (workId == null) {
                            return new ResultUtils(500, "workId不能为空");
                        }
                        fileInfo.setConnectFieldsValue(workId.toString());
                        FileInfo fileInfo1 = reportCreationListDao.selectFileInfo(fileInfo);
                        work.setFileInfo(fileInfo1);
                    }
                }
                List<DepartTargetList> nextDepartTargetLists = reportCreationListDao.selectNextTarget(reportDepartList);// 下周/月的目标
                if(null != nextDepartTargetLists && !nextDepartTargetLists.isEmpty()){
                    reportDepartList.setNextDepartTargetLists(nextDepartTargetLists);
                }
                reportDepartList.setReportDepartWorkList(reportDepartWorks);


            }
            return new ResultUtils(100, "查詢成功", reportDepartList);
        } else {
            return new ResultUtils(500, "實習類型錯誤");
        }
    }

    //编辑產線报告详细内容
    @Override
    @SysLog("编辑產線报告详细内容")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ResultUtils updateReportByReportId(ReportProductionList reportProductionList) throws Exception {
        if (reportProductionList == null) {
            return new ResultUtils(500, "參數錯誤");
        }
        if (null == reportProductionList.getReportType()) {
            return new ResultUtils(500, "請傳報告類型(reportType)");
        }
        Integer lineId = reportProductionList.getLineId();
        if (null == lineId) {
            return new ResultUtils(500, "lineId不能為空");
        }
        reportProductionList.setUpdateDate(new Date());
        //当传进来是产线实习周报的时候
        if (reportProductionList.getReportType() == 10) {
            if (reportProductionList.getMentalStateList() == null || reportProductionList.getMentalStateList().isEmpty()) {
                return new ResultUtils(500, "本週狀態不能為空");
            } else {
                List<Integer> mentalStateList = reportProductionList.getMentalStateList();
                if (null == mentalStateList || mentalStateList.isEmpty()) {
                    return new ResultUtils(500, "本週狀態不能為空");
                }
                if (mentalStateList.contains(17) || mentalStateList.contains(18)) {
                    if (mentalStateList.contains(19) || mentalStateList.contains(20) || mentalStateList.contains(21)) {
                        return new ResultUtils(500, "本週狀態選擇衝突");
                    }
                } else if (mentalStateList.contains(19) || mentalStateList.contains(20) || mentalStateList.contains(21)) {
                    if (mentalStateList.contains(17) || mentalStateList.contains(18)) {
                        return new ResultUtils(500, "本週狀態選擇衝突");
                    }
                }
                StringBuilder mentalStateStr = new StringBuilder();
                for (Integer mentalState : mentalStateList) {
                    mentalStateStr.append(mentalState).append(",");
                }
                reportProductionList.setMentalState(mentalStateStr.substring(0, mentalStateStr.length() - 1));
            }

            /*if(reportProductionList.getAttendanceList()==null || reportProductionList.getAttendanceList().isEmpty()){
                return new ResultUtils(500,"本週考勤不能為空");
            }else {
                List<Integer> attendanceList = reportProductionList.getAttendanceList();
                if(null ==attendanceList || attendanceList.isEmpty()){
                    return new ResultUtils(500,"本週考勤不能為空");
                }
                if(attendanceList.contains(22)){
                    if(attendanceList.contains(23) || attendanceList.contains(24)){
                        return new ResultUtils(500,"本週考勤選擇衝突");
                    }
                }else if(attendanceList.contains(23) || attendanceList.contains(24)){
                    if(attendanceList.contains(22)){
                        return new ResultUtils(500,"本週考勤選擇衝突");
                    }
                }
                StringBuilder attendanceStr =  new StringBuilder();
                for (Integer attendance : attendanceList ) {
                    attendanceStr.append(attendance).append(",");
                }
                reportProductionList.setAttendance(attendanceStr.substring(0,attendanceStr.length()-1));
            }*/
        }

        //当传进来的是产线实习月报的时候
        if (reportProductionList.getReportType() == 11) {
            //获取improve_category列表，如果不为空则拼接再set
            if (null == reportProductionList.getImproveCategoryList() || reportProductionList.getImproveCategoryList().isEmpty()) {
                return new ResultUtils(500, "改善範疇不能為空");
            } else {
                List<Integer> improveCategoryList = reportProductionList.getImproveCategoryList();
                if (null == improveCategoryList || improveCategoryList.isEmpty()) {
                    return new ResultUtils(500, "改善範疇不能為空");
                }
                StringBuilder improveCategoryStr = new StringBuilder();
                for (Integer improveCategory : improveCategoryList) {
                    improveCategoryStr.append(improveCategory).append(",");
                }
                reportProductionList.setImproveCategory(improveCategoryStr.substring(0, improveCategoryStr.length() - 1));
            }

            //拼接internship_state(实习阶段)
            if (null == reportProductionList.getInternshipStateList() || reportProductionList.getInternshipStateList().isEmpty()) {
                return new ResultUtils(500, "實習階段不能為空");
            } else {
                List<Integer> internshipStateList = reportProductionList.getInternshipStateList();
                if (null == internshipStateList || internshipStateList.isEmpty()) {
                    return new ResultUtils(500, "實習階段不能為空");
                }
                StringBuilder internshipStateStr = new StringBuilder();
                for (Integer internshipState : internshipStateList) {
                    internshipStateStr.append(internshipState).append(",");
                }
                reportProductionList.setInternshipState(internshipStateStr.substring(0, internshipStateStr.length() - 1));
            }

            if (reportProductionList.getQuestionDescription() == null || reportProductionList.getQuestionDescription().equals("")) {
                return new ResultUtils(500, "發現問題不能為空");
            }
            if (reportProductionList.getQuestionSuggestion() == null || reportProductionList.getQuestionSuggestion().equals("")) {
                return new ResultUtils(500, "解決方案不能為空");
            }
        }
        //问题与建议
        Integer isHaveProblem = reportProductionList.getIsHaveProblem();
        if (isHaveProblem == null) {
            return new ResultUtils(500, "請傳isHaveProblem");
        }
        if (isHaveProblem == 1) {
            List<ReportProductionWeekQuestion> reportProductionWeekQuestionList
                    = reportProductionList.getReportProductionWeekQuestionList();
            if (reportProductionWeekQuestionList == null || reportProductionWeekQuestionList.isEmpty()) {
                return new ResultUtils(500, "問題和建議不能為空");
            } else {
                //编辑的时候要先删除再添加
                reportCreationListDao.deleteWeekQuestionByReportId(reportProductionList.getReportId());
                for (ReportProductionWeekQuestion reportProductionWeekQuestion : reportProductionWeekQuestionList) {
                    reportProductionWeekQuestion.setReportId(reportProductionList.getReportId());
                    Integer questionCount = reportCreationListDao.insertProductionQuestion(reportProductionWeekQuestion);
                    if (null == questionCount || questionCount <= 0) {
                        throw new Exception("添加問題與建議失敗");
                    }
                }
            }
        } else if (isHaveProblem == 0) {
            reportCreationListDao.deleteWeekQuestionByReportId(reportProductionList.getReportId());
        }

        Integer count = reportCreationListDao.updateReportByReportId(reportProductionList);
        if (null == count || count <= 0) {
            throw new Exception("編輯報告失敗");
        }

        return new ResultUtils(100, "編輯成功", reportProductionList);
    }

    //編輯提案改善報告
    @Override
    @SysLog("編輯提案改善報告")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ResultUtils updateProposalReport(ReportProductionProposalList reportProductionProposalList) throws Exception {
        if (reportProductionProposalList == null) {
            return new ResultUtils(500, "參數錯誤");
        }
        //校验
        String proposalTheme = reportProductionProposalList.getProposalTheme();
        if (null == proposalTheme || proposalTheme.equals("")) {
            return new ResultUtils(500, "提案主體不能為空");
        }
        String analyzeContent = reportProductionProposalList.getAnalyzeContent();
        if (null == analyzeContent || analyzeContent.equals("")) {
            return new ResultUtils(500, "分析內容不能為空");
        }
        String iPurposeDescription = reportProductionProposalList.getiPurposeDescription();
        if (null == iPurposeDescription || iPurposeDescription.equals("")) {
            return new ResultUtils(500, "目的描述不能為空");
        }
        String improveMethod = reportProductionProposalList.getImproveMethod();
        if (null == improveMethod || improveMethod.equals("")) {
            return new ResultUtils(500, "改善對策不能為空");
        }
        String executePlan = reportProductionProposalList.getExecutePlan();
        if (null == executePlan || executePlan.equals("")) {
            return new ResultUtils(500, "執行計劃不能為空");
        }
        String performanceDescription = reportProductionProposalList.getPerformanceDescription();
        if (null == performanceDescription || performanceDescription.equals("")) {
            return new ResultUtils(500, "可達績效描述不能為空");
        }
        Integer yearCostSaving = reportProductionProposalList.getYearCostSaving();
        if (yearCostSaving == null) {
            return new ResultUtils(500, "按年省不能為空");
        }
        Integer onceCostSaving = reportProductionProposalList.getOnceCostSaving();
        if (null == onceCostSaving) {
            return new ResultUtils(500, "一次性節省不能為空");
        }
        String costSavingReason = reportProductionProposalList.getCostSavingReason();
        if (null == costSavingReason) {
            return new ResultUtils(500, "節省的理由和依據不能為空");
        }

        String staffCode = tokenAnalysis.getTokenUser().getStaffCode();

        if (null == reportProductionProposalList.getProposalTypeList() || reportProductionProposalList.getProposalTypeList().isEmpty()) {
            return new ResultUtils(500, "提案類別不能為空");
        } else {
            List<Integer> proposalTypeList = reportProductionProposalList.getProposalTypeList();
            if (null == proposalTypeList || proposalTypeList.isEmpty()) {
                return new ResultUtils(500, "提案類別不能為空");
            }
            StringBuilder proposalTypeStr = new StringBuilder();
            for (Integer proposalType : proposalTypeList) {
                proposalTypeStr.append(proposalType).append(",");
            }
            reportProductionProposalList.setProposalType(proposalTypeStr.substring(0, proposalTypeStr.length() - 1));
        }

        if (null == reportProductionProposalList.getAnalyzeMethodList() || reportProductionProposalList.getAnalyzeMethodList().isEmpty()) {
            return new ResultUtils(500, "分析手法不能為空");
        } else {
            List<Integer> analyzeMethodList = reportProductionProposalList.getAnalyzeMethodList();
            if (null == analyzeMethodList || analyzeMethodList.isEmpty()) {
                return new ResultUtils(500, "分析手法不能為空");
            }
            StringBuilder analyzeMethodStr = new StringBuilder();
            for (Integer analyzeMethod : analyzeMethodList) {
                analyzeMethodStr.append(analyzeMethod).append(",");
            }
            reportProductionProposalList.setAnalyzeMethod(analyzeMethodStr.substring(0, analyzeMethodStr.length() - 1));
        }

        if (null == reportProductionProposalList.getImproveCategoryList() || reportProductionProposalList.getImproveCategoryList().isEmpty()) {
            return new ResultUtils(500, "改善範疇不能為空");
        } else {
            List<Integer> improveCategoryList = reportProductionProposalList.getImproveCategoryList();
            if (null == improveCategoryList || improveCategoryList.isEmpty()) {
                return new ResultUtils(500, "改善範疇不能為空");
            }
            StringBuilder improveCategoryStr = new StringBuilder();
            for (Integer improveCategory : improveCategoryList) {
                improveCategoryStr.append(improveCategory).append(",");
            }
            reportProductionProposalList.setImproveCategory(improveCategoryStr.substring(0, improveCategoryStr.length() - 1));
        }

        Integer proposalId = reportProductionProposalList.getProposalId();
        FileInfo fileInfo = new FileInfo();
        fileInfo.setConnectTableName("report_production_proposal_list");
        fileInfo.setConnectFieldsName("proposal_id");
        fileInfo.setConnectFieldsValue(proposalId.toString());
        reportCreationListDao.deleteAllFile(fileInfo);//先刪除文件
        FileInfo fileInfo1 = reportProductionProposalList.getFileInfo();
        if (fileInfo1 != null) {
            fileInfo1.setConnectTableName("report_production_proposal_list");
            fileInfo1.setConnectFieldsName("proposal_id");
            fileInfo1.setConnectFieldsValue(proposalId.toString());
            fileInfo1.setUpdateUserCode(staffCode);
            fileInfo1.setProjId(10);
            Integer count = reportCreationListDao.insertFileInfo(fileInfo1);
            if (count == null || count <= 0) {
                throw new Exception("插入文件失敗");
            }
        }
        Integer count = reportCreationListDao.updateProposalReport(reportProductionProposalList);
        if (null == count || count <= 0) {
            return new ResultUtils(500, "修改失敗");
        }
        return new ResultUtils(100, "修改成功", reportProductionProposalList);
    }

    @Override
    //查詢月報下所有週報的评价(評分評語
    public ResultUtils selectWeekApprovalInfo(Integer missionType, Integer reportId,Integer creationId) {
        Integer reportType = 0;
        if (missionType == 7) {
            reportType = 10;
        } else if (missionType == 8) {
            reportType = 14;
        }
        //Integer creationId = null;
        String staffCode =  tokenAnalysis.getTokenUser().getStaffCode();
        if(creationId==null){
             if(reportType == 10){
                ReportProductionList reportProductionList = reportCreationListDao.selectProductionReportInfo(reportId);
                if(null != reportProductionList){
                    creationId = reportProductionList.getCreationId();
                    staffCode = reportProductionList.getStaffCode();
                }
            }
            if(reportType == 14){
                ReportDepartList reportDepartList = reportCreationListDao.selectDepartReportInfo(reportId);
                if(null != reportDepartList){
                    creationId = reportDepartList.getCreationId();
                    staffCode = reportDepartList.getStaffCode();
                }
            }
        }


        List<RookieApprovalInfo> rookieApprovalInfos = reportCreationListDao.selectWeekApprovalInfo(reportType, creationId, staffCode, reportId);
        if (null != rookieApprovalInfos && !rookieApprovalInfos.isEmpty()) {
            return new ResultUtils(100, "查詢成功", rookieApprovalInfos);
        } else if (null != rookieApprovalInfos && rookieApprovalInfos.isEmpty()) {
            return new ResultUtils(100, "查找為空", rookieApprovalInfos);
        } else {
            return new ResultUtils(500, "查找失敗");
        }
    }

    @SysLog("刪除成員管理的成員")
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ResultUtils deleteMember(String staffCode) {
        if (staffCode == null || staffCode.equals("")) {
            return new ResultUtils(500, "請選擇學生");
        }
        //先查出学生有哪些产线报告
        List<Integer> productionReportIds = reportCreationListDao.selectProductionReportIdByStaffCode(staffCode);
        //再查出学生有哪些部门报告
        List<Integer> departReportIds = reportCreationListDao.selectDepartReportIdByStaffCode(staffCode);
        if (null != productionReportIds && !productionReportIds.isEmpty()) {
            for (Integer productionReportId : productionReportIds) {
                reportCreationListDao.deleteWeekQuestionByReportId(productionReportId);//删除所有产线问题与建议
            }
        }
        if (null != departReportIds && !departReportIds.isEmpty()) {
            for (Integer departReportId : departReportIds) {
                reportCreationListDao.deleteDepartWork(departReportId);//刪除所有部門工作信息
                reportCreationListDao.deleteReportDepartTarget(departReportId);//刪除所有部門目標中間信息
            }
        }
        //根据工号查询其是否为小组组长
        Integer count = productionLineListDao.selectIsSquadLeader(staffCode);
        if (null != count && count > 0) {
            productionLineListDao.delSquadLeader(staffCode);//删除小组组长信息
        }
        productionLineListDao.deleteStudentInClassStudentInfo(staffCode);//刪除學生表
        productionLineListDao.updateLineStudentStatusByLineId(null, staffCode);//刪除產線學生表
        reportCreationListDao.deleteLineReport(staffCode);//刪除產線實習報告表
        reportCreationListDao.deleteProposalReport(staffCode);//刪除提案改善表
        reportCreationListDao.deleteDepartReport(staffCode);//刪除部門報告表
        reportCreationListDao.deleteStaffRole(staffCode);//刪除角色表
        return new ResultUtils(100, "刪除成功");
    }

    //填寫部門報告時查找目標和工作
    @Override
    public ResultUtils selectTargetInfo(ReportDepartList reportDepartList) {
        if(reportDepartList == null){
            return new ResultUtils(500,"參數錯誤");
        }
        //根据工号找目标单号,再根据时间查询，只查询了对应单号對應時間的目標
        Integer reportType = reportDepartList.getReportType();
        String staffCode = reportDepartList.getStaffCode();
        List<ReportDepartWork> reportDepartWorks = new ArrayList<>();
        List<DepartTargetList> departTargetLists = reportCreationListDao.selectTarget(reportDepartList);// 本周/月目标

        Date missionStartTime = reportDepartList.getMissionStartTime();

        List<DepartTargetList> nextDepartTargetLists = reportCreationListDao.selectNextTarget(reportDepartList);// 下周/月的目标
        //還需要查詢遺留的
        List<ReportDepartWork> leaveOverMissionList = reportCreationListDao.selectLeaveOverMission(reportDepartList);//遗留任务

        //查詢上週自己設立的下週其他任務
        Integer nextOtherMissionId = 6;//默認是週報下週其他任務tab
        Integer nextHelpMissionId = 7;//默認是週報下周需要協調、幫助任務的tab
        if(reportType == 15){
            nextOtherMissionId = 10;
            nextHelpMissionId = 11;
        }
        //missionStartTime = conversion(missionStartTime,-upDateNum);
        //查找上週設立的下週其他任務
        List<ReportDepartWork> nextOtherMissionList = reportCreationListDao.selectNextOtherMission(nextOtherMissionId, missionStartTime, reportType,staffCode);

        //查詢上週設立的下週需要協調、幫助的工作
        List<ReportDepartWork> helpMissionList = reportCreationListDao.selectNextOtherMission(nextHelpMissionId, missionStartTime, reportType,staffCode);

        //查找

        //新增到targetList
        if(null != departTargetLists && !departTargetLists.isEmpty()){
            reportDepartList.setDepartTargetLists(departTargetLists);
        }
        if(null != nextDepartTargetLists && !nextDepartTargetLists.isEmpty()){
            reportDepartList.setNextDepartTargetLists(nextDepartTargetLists);
        }


        //新增到workList
        if(null != leaveOverMissionList && !leaveOverMissionList.isEmpty()){
            reportDepartWorks.addAll(leaveOverMissionList);
        }
        if(null != nextOtherMissionList && !nextOtherMissionList.isEmpty()){
            reportDepartWorks.addAll(nextOtherMissionList);
        }
        if(null != helpMissionList && !helpMissionList.isEmpty()){
            reportDepartWorks.addAll(helpMissionList);
        }

        if(null != reportDepartWorks && !reportDepartWorks.isEmpty()){
            for (ReportDepartWork reportDepartWork:reportDepartWorks ) {
                FileInfo fileInfo = new FileInfo();
                fileInfo.setConnectTableName("report_depart_work");
                fileInfo.setConnectFieldsName("work_id");
                Integer workId = reportDepartWork.getWorkId();
                if (workId == null) {
                    return new ResultUtils(500, "workId不能为空");
                }
                fileInfo.setConnectFieldsValue(workId.toString());
                FileInfo fileInfo1 = reportCreationListDao.selectFileInfo(fileInfo);
                reportDepartWork.setFileInfo(fileInfo1);
            }
            reportDepartList.setReportDepartWorkList(reportDepartWorks);
        }
        if(null == reportDepartList.getReportDepartWorkList()){
            reportDepartList.setReportDepartWorkList(new ArrayList<>());
        }
        return new ResultUtils(100,"查詢目標及任務成功",reportDepartList);
    }

    @Override
    //填写产线周报时查出底下的日报内容
    public ResultUtils selectDailyByWeek(Integer creationId) {
        if (null == creationId) {
            return new ResultUtils(500, "参数错误");
        }
        //查找任務的時間
        ReportCreationList reportCreationList = reportCreationListDao.selectTimeInCreationList(creationId);
        if (null == reportCreationList) {
            return new ResultUtils(500, "查詢时间失敗");
        }
        String staffCode = tokenAnalysis.getTokenUser().getStaffCode();
        ReportProductionList reportProductionList = new ReportProductionList();
        Date missionStartTime = reportCreationList.getMissionStartTime();
        Date missionEndTime = reportCreationList.getMissionEndTime();
        reportProductionList.setMissionStartTime(missionStartTime);
        reportProductionList.setMissionEndTime(missionEndTime);
        reportProductionList.setCreationId(creationId);
        reportProductionList.setStaffCode(staffCode);

        //查找產線週報下的產線日報內容
        List<ReportProductionList> reportProductionLists = reportCreationListDao.selectDailyByWeek(reportProductionList);

        //TODO 週報查日報內容返給前端

        /*if (null != reportProductionLists && !reportProductionLists.isEmpty()) {
            //格式化時間
             for (ReportProductionList productionReport : reportProductionLists) {
                Calendar cl = Calendar.getInstance();
                cl.setTime(productionReport.getMissionStartTime());
                String dailyDate = cl.get(Calendar.YEAR) + "/" + cl.get(Calendar.MONTH) + "/" + cl.get(Calendar.DATE);
                productionReport.setDailyDate(dailyDate);
             }
            reportProductionList.setDailyReportList(reportProductionLists);
        }*/
        return new ResultUtils(100, "查询成功", reportProductionLists);
    }

    //根據當前登錄人展示不同的下拉列表(根據是部門還是產線展示不同的報告類型列表)
    //暂时搁置
    public ResultUtils selectMenuByRole() {
        Integer tab = 2;//tab固定是2
        Integer pid = 7;//如果是產線的主管是7，部門的主管則是8
        String staffCode = tokenAnalysis.getTokenUser().getStaffCode();
        List<Integer> roles = productionLineListDao.selectRoleByStaffCode(staffCode);//查出當前登錄人的所有角色
        //初始化一個List的TypeList的對象
        List<TypeList> typeLists = new ArrayList<>();
        if (null != roles && !roles.isEmpty()) {
            //如果他是產線的主管(pid就為默認的7
            if (roles.contains(10) || roles.contains(11) || roles.contains(12) || roles.contains(19)) {
                List<TypeList> productionMenuList = reportCreationListDao.selectTypeListByTab(tab, pid);
                if (null != productionMenuList && !productionMenuList.isEmpty()) {
                    typeLists.addAll(productionMenuList);
                }
            }
            if (roles.contains(14) || roles.contains(15) || roles.contains(16) || roles.contains(17) || roles.contains(18) || roles.contains(19)) {
                //如果是部門主管pid為8
                pid = 8;
                List<TypeList> departMenuList = reportCreationListDao.selectTypeListByTab(tab, pid);
                if (null != departMenuList && !departMenuList.isEmpty()) {
                    typeLists.addAll(departMenuList);
                }
            }
        }
        if (null != typeLists && !typeLists.isEmpty()) {
            return new ResultUtils(100, "查詢成功", typeLists);
        } else if (null != typeLists && typeLists.isEmpty()) {
            return new ResultUtils(500, "查找為空", typeLists);
        } else {
            return new ResultUtils(500, "查找失敗");
        }
    }

    //获取(组成)missionName
    public String getMissionName(Integer missionType){
        Date date = new Date();
        Calendar cl = Calendar.getInstance();
        cl.setTime(date);
        int year = cl.get(Calendar.YEAR);
        int month = cl.get(Calendar.MONTH) + 1;
        String typeName = "";
        if(missionType == 98){
            typeName = "产线实习考核任务";
        }
        if(missionType == 99){
            typeName = "部门实习考核任务";
        }
        if(missionType == 100){
            typeName = "转正调薪考核任务";
        }

        if(missionType == 9){
            typeName = "產線實習日報";
        }
        if(missionType == 10){
            typeName = "產線實習周報";
        }
        if(missionType == 11){
            typeName = "產線實習月報";
        }
        if(missionType == 12){
            typeName = "提案改善報告";
        }

        if(missionType == 13){
            typeName = "部門實習日報";
        }
        if(missionType == 14){
            typeName = "部門實習週報";
        }
        if(missionType == 15){
            typeName = "部門實習月報";
        }
        if(missionType == 16){
            typeName = "重大专案类报告";
        }

        String monthStr = month+"";
        if(month<10){
            monthStr = "0" + month;
        }
        String missionName = "" + year + monthStr + typeName;
        return missionName;
    }

    //查询届别
    @Override
    public ResultUtils selectClassPeriodList(){
        List<ClassGradeInfo> classPeriodList = reportCreationListDao.selectClassPeriodList();
        if(null != classPeriodList && !classPeriodList.isEmpty()){
            return new ResultUtils(100,"查詢成功",classPeriodList);
        }else if(null != classPeriodList && classPeriodList.isEmpty()){
            return new ResultUtils(100,"查詢為空",classPeriodList);
        }else {
            return new ResultUtils(500,"查詢屆別失敗");
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    //新增考勤信息
    @SysLog("新增考勤信息")
    public ResultUtils insertAttendance(List<AttendanceRecord> attendanceRecords){
        if(attendanceRecords ==null || attendanceRecords.isEmpty()){
            return new ResultUtils(500,"考勤信息不能為空");
        }

        for (AttendanceRecord attendance : attendanceRecords) {
            String staffCode = tokenAnalysis.getTokenUser().getStaffCode();
            //判断是否是菁干班成员
            String studentCode = attendance.getStaffCode();
            Integer isExist = reportCreationListDao.selectStudentIsExist(studentCode);
            if(isExist==null || isExist<=0){
                return new ResultUtils(500,attendance.getStaffName()+"不是菁干班成员");
            }
            //轉換類型和格式
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                if(attendance.getMorningStartTimeDate()!=null){
                    Date morningStartTime = sdf.parse(sdf.format(attendance.getMorningStartTimeDate()));
                    String morningStratTimeStr = transitionDate(morningStartTime);
                    attendance.setMorningStartTime(morningStratTimeStr);
                }
                if(null != attendance.getMorningEndTimeDate()){
                    Date morningEndTime = sdf.parse(sdf.format(attendance.getMorningEndTimeDate()));
                    String morningEndTimeStr = transitionDate(morningEndTime);
                    attendance.setMorningEndTime(morningEndTimeStr);
                }
                if(null != attendance.getAfternoonStartTimeDate()){
                    Date afternoonStartTime = sdf.parse(sdf.format(attendance.getAfternoonStartTimeDate()));
                    String afternoonStartTimeStr = transitionDate(afternoonStartTime);
                    attendance.setAfternoonStartTime(afternoonStartTimeStr);
                }
                if(null != attendance.getAfternoonEndTimeDate()){
                    Date AfternoonEndTime = sdf.parse(sdf.format(attendance.getAfternoonEndTimeDate()));
                    String afternoonEndTimeStr = transitionDate(AfternoonEndTime);
                    attendance.setAfternoonEndTime(afternoonEndTimeStr);
                }
                if(null != attendance.getStartExtraWorkDate()){
                    Date startExtraWork = sdf.parse(sdf.format(attendance.getStartExtraWorkDate()));
                    String startExtraWorkStr = transitionDate(startExtraWork);
                    attendance.setStartExtraWork(startExtraWorkStr);
                }
                if(null != attendance.getEndExtraWorkDate()){
                    Date endExtraWork = sdf.parse(sdf.format(attendance.getEndExtraWorkDate()));
                    String endExtraWorkStr = transitionDate(endExtraWork);
                    attendance.setEndExtraWork(endExtraWorkStr);
                }

            }catch (Exception e){
                e.printStackTrace();
            }

            attendance.setCreateCode(staffCode);
            String attendanceType = attendance.getAttendanceType();
            Integer examineTypeId = null;
            if(null != attendanceType && !attendanceType.isEmpty()){
                if(attendanceType.contains("迟到") || attendanceType.contains("遲到")){
                    examineTypeId = 20;
                }
                if(attendanceType.contains("早退")){
                    examineTypeId = 21;
                }
                if(attendanceType.contains("病假") ){
                    examineTypeId = 22;
                }
                if(attendanceType.contains("事假")){
                    examineTypeId = 23;
                }
                if(attendanceType.contains("曠工") || attendanceType.contains("旷工")){
                    examineTypeId = 24;
                }
            }
            if(examineTypeId!=null){
                attendance.setExamineTypeId(examineTypeId);
            }else {
                return new ResultUtils(500,"examineTypeId錯誤,不能為空");
            }
            Integer integer = reportCreationListDao.insertAttendance(attendance);
            if(integer == null || integer<=0){
                return new ResultUtils(500,"添加考勤信息失敗");
            }
        }
        return new ResultUtils(100,"添加成功");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    //新增獎懲信息
    @SysLog("新增獎懲信息")
    public ResultUtils importRewardsAndPunishment(List<RewardPunishRecord> rewardPunishRecords){
        if(rewardPunishRecords ==null || rewardPunishRecords.isEmpty()){
            return new ResultUtils(500,"獎懲信息不能為空");
        }

        for (RewardPunishRecord rewardPunishRecord : rewardPunishRecords) {
            //判断是否是菁干班成员
            String studentCode = rewardPunishRecord.getStaffCode();
            Integer isExist = reportCreationListDao.selectStudentIsExist(studentCode);
            if(isExist==null || isExist<=0){
                return new ResultUtils(500,rewardPunishRecord.getStaffName()+"不是菁干班成员");
            }
            String staffCode = tokenAnalysis.getTokenUser().getStaffCode();
            rewardPunishRecord.setCreateCode(staffCode);
            String examineType = rewardPunishRecord.getExamineType();
            Integer examineTypeId = null;
            if(null != examineType){
                if(examineType.contains("事業群活動")){
                    examineTypeId = 25;
                }
                if(examineType.contains("集體活動")){
                    examineTypeId = 26;
                }
                if(examineType.contains("座談會發言")){
                    examineTypeId = 27;
                }
                if(examineType.contains("擔任班長")){
                    examineTypeId = 28;
                }
                if(examineType.contains("《鴻橋》發表")){
                    examineTypeId = 29;
                }
                if(examineType.contains("嘉獎") || examineType.contains("嘉奖")){
                    examineTypeId = 30;
                }
                if(examineType.contains("小功")){
                    examineTypeId = 31;
                }
                if(examineType.contains("大功")){
                    examineTypeId = 32;
                }
                if(examineType.contains("警告")){
                    examineTypeId = 33;
                }
                if(examineType.contains("小過") || examineType.contains("小过")){
                    examineTypeId = 34;
                }
                if(examineType.contains("大過") || examineType.contains("大过")){
                    examineTypeId = 35;
                }
            }

            if(examineTypeId!=null){
                rewardPunishRecord.setExamineTypeId(examineTypeId);
            }else {
                return new ResultUtils(500,"examineTypeId錯誤,不能為空");
            }
            Integer integer = reportCreationListDao.importRewardsAndPunishment(rewardPunishRecord);
            if(integer == null || integer<=0){
                return new ResultUtils(500,"添加獎懲信息失敗");
            }
        }
        return new ResultUtils(100,"添加成功");
    }

    //查询奖惩记录
    @Override
    public ResultUtils queryRewardPunishRecord(Integer pageNum) {
        PageHelper.startPage(pageNum,10);
        List<RewardPunishRecord> queryRewardPunishRecord = reportCreationListDao.queryRewardPunishRecord();
        PageInfo<RewardPunishRecord> pageInfo = new PageInfo<>(queryRewardPunishRecord);
        return new ResultUtils(100,"查詢成功",pageInfo);
    }

    //查询考勤记录
    @Override
    public ResultUtils queryAttendanceRecord(Integer pageNum) {
        PageHelper.startPage(pageNum,10);
        List<AttendanceRecord> attendanceRecords = reportCreationListDao.queryAttendanceRecord();
        PageInfo<AttendanceRecord> pageInfo = new PageInfo<>(attendanceRecords);
        return new ResultUtils(100,"查詢成功",pageInfo);
    }

    //分页查询成员管理信息
    @Override
    public ResultUtils selectMemberByPage(Integer pageNum){
        PageHelper.startPage(pageNum, 10);
        List<ClassStudentInfo> menberInfoList = reportCreationListDao.selectMenberInfoList();
        PageInfo<ClassStudentInfo> pg = new PageInfo<>(menberInfoList);
        return new ResultUtils(100,"查询成功",pg);
    }

    //编辑部门报告
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ResultUtils updateDepartReport(ReportDepartList reportDepartList) throws Exception{
        if(null == reportDepartList){
            return new ResultUtils(500,"參數錯誤");
        }
        Integer reportType = reportDepartList.getReportType();
        if(null == reportType){
            return new ResultUtils(500,"reportType不能為空");
        }
        String staffCode = tokenAnalysis.getTokenUser().getStaffCode();

        if(reportType == 14 || reportType == 15){//如果是部門實習週報或月報
            String workHarvest = reportDepartList.getWorkHarvest();
            String lifeHarvest = reportDepartList.getLifeHarvest();
            String workSuggestion = reportDepartList.getWorkSuggestion();
            if(null == workHarvest || workHarvest.equals("")){
                return new ResultUtils(500,"工作心得不能為空");
            }
            if(null == lifeHarvest || lifeHarvest.equals("")){
                return new ResultUtils(500,"生活心得不能為空");
            }
            if(null == workSuggestion || workSuggestion.equals("")){
                return new ResultUtils(500,"工作中發現的問題不能為空");

            }
            Integer reportCount = reportCreationListDao.updateReportDepartList(reportDepartList);
            if(null == reportCount || reportCount<=0){
                return new ResultUtils(500,"編輯報告失敗");
            }
        }

        //先把原来的work删掉
        reportCreationListDao.deleteDepartWork(reportDepartList.getReportId());

        //取出統一存在這裡的work(多tab)
        List<ReportDepartWork> reportDepartWorkList = reportDepartList.getReportDepartWorkList();
        if(null != reportDepartWorkList && !reportDepartWorkList.isEmpty()){
            for (ReportDepartWork reportDepartWork: reportDepartWorkList) {
                if(null == reportDepartWork.getWorkName() || reportDepartWork.getWorkName().equals("")){
                    return new ResultUtils(500,"工作名稱不能為空");
                }
                if(null == reportDepartWork.getWorkContent() || reportDepartWork.getWorkContent().equals("")){
                    return new ResultUtils(500,"工作內容不能為空");
                }
                reportDepartWork.setReportId(reportDepartList.getReportId());
                if(null == reportDepartWork.getPid()){
                    reportDepartWork.setPid(0);
                }
                String fulfillmentOfSchedule = reportDepartWork.getFulfillmentOfSchedule();
                //校验
                Integer tab = reportDepartWork.getTab();
                if (null != tab) {
                    List<Integer> tabModel = Arrays.asList(0,2,3,4,8,9,10);
                    if(tabModel.contains(tab)){
                        if (reportDepartWork.getFulfillmentOfSchedule() == null || reportDepartWork.getFulfillmentOfSchedule().equals("")) {
                            throw new Exception("完成進度不能為空");
                        }
                        //為周报月报时做以下判断
                        String totalTime = reportDepartWork.getTotalTime();
                        if(null == totalTime || totalTime.equals("")){
                            return new ResultUtils(500,"完成用時不能為空");
                        }
                        if(null != fulfillmentOfSchedule && fulfillmentOfSchedule.equals("100")){
                            String finishProof = reportDepartWork.getFinishProof();
                            if(null == finishProof || finishProof.equals("")){
                                return new ResultUtils(500,"證據支持不能為空");
                            }
                        }else if(null != fulfillmentOfSchedule && !fulfillmentOfSchedule.equals("100")){
                            String unfinishedReason = reportDepartWork.getUnfinishedReason();
                            if(null == unfinishedReason || unfinishedReason.equals("")){
                                return new ResultUtils(500,"未完成原因不能為空");
                            }
                            if(reportType == 14 || reportType == 15){
                                Integer isContinue = reportDepartWork.getIsContinue();
                                if(null == isContinue){
                                    return new ResultUtils(500,"請選擇是否繼續");
                                }
                                if(isContinue == 1){
                                    String nextWeekWork = reportDepartWork.getNextWeekWork();
                                    if(null == nextWeekWork || nextWeekWork.equals("")){
                                        return new ResultUtils(500,"下週待做不能為空");
                                    }
                                }
                            }
                        }
                    }
                }


                //如果本周待做任务不为空，则为上周遗留下来的任务
                if(null != reportDepartWork.getToDoWork() && !reportDepartWork.getToDoWork().equals("")){
                    Integer pid = reportDepartWork.getPid();
                    Integer workId = reportDepartWork.getWorkId();
                    //如果完成进度是100,已完成
                    if(pid == null){
                        pid = 0;
                    }
                    if(null != fulfillmentOfSchedule && fulfillmentOfSchedule.equals("100")){
                        //则把这条任务之前没做完的任务是否继续设为0
                        reportDepartWork.setIsContinue(0);
                        do {
                            reportCreationListDao.updateWorkIsContinue(workId,0);
                            //查找workId是这条记录的pid的work(找遗留下来的父work)
                            ReportDepartWork pidWork = reportCreationListDao.selectWorkPid(pid);
                            if(null != pidWork){
                                workId = pidWork.getWorkId();
                                pid = pidWork.getPid();
                                if(pid == null){
                                    pid = 0;
                                }
                            }
                        }while (pid!=0);
                    }else {//如果不等于100，就都是未完成
                        //如果从已完成遗留任务改成了未完成的状态
                        //则把这条任务之前的父work的是否继续设为1
                        do {
                            reportCreationListDao.updateWorkIsContinue(workId,1);
                            //查找workId是这条记录的pid的work(找遗留下来的父work)
                            ReportDepartWork pidWork = reportCreationListDao.selectWorkPid(pid);
                            if(null != pidWork){
                                workId = pidWork.getWorkId();
                                pid = pidWork.getPid();
                                if(pid == null){
                                    pid = 0;
                                }
                            }
                        }while (pid!=0);
                    }
                }

                //再添加新的work
                Integer insertCount = reportCreationListDao.insertDepartWork(reportDepartWork);
                if(null == insertCount || insertCount<=0){
                    return new ResultUtils(500,"新增時失敗");
                }

                FileInfo fileInfo = reportDepartWork.getFileInfo();
                if (fileInfo != null) {
                    fileInfo.setConnectTableName("report_depart_work");
                    fileInfo.setConnectFieldsName("work_id");
                    Integer workId = reportDepartWork.getWorkId();
                    if (workId == null) {
                        return new ResultUtils(500, "workId不能为空");
                    }
                    fileInfo.setConnectFieldsValue(workId.toString());
                    fileInfo.setUpdateUserCode(staffCode);
                    fileInfo.setProjId(10);
                    //先删除原表数据
                    reportCreationListDao.deleteAllFile(fileInfo);
                    //再新增新一条数据
                    Integer fileCount = reportCreationListDao.insertFileInfo(fileInfo);
                    if (fileCount == null || fileCount <= 0) {
                        return new ResultUtils(500,"插入文件失敗");
                    }
                }

            }
        }
        return new ResultUtils(101,"編輯成功");
    }

    //获取时间，转换成只有时分秒的字符串的方法
    public String transitionDate(Date date){

        Calendar cl = Calendar.getInstance();
        cl.setTime(date);
        int hour = cl.get(Calendar.HOUR_OF_DAY);
        int minute = cl.get(Calendar.MINUTE);
        int second = cl.get(Calendar.SECOND);
        String hourStr = hour+"";
        String minuteStr = minute+"";
        String secondStr = second+"";
        if(hour<10){
            hourStr = "0" + hour;
        }
        if(minute<10){
            minuteStr = "0" + minute;
        }
        if(second<10){
            secondStr = "0" + second;
        }
        String parseDateStr = hourStr + ":" + minuteStr + ":" + secondStr;

        return parseDateStr;
    }
}
