package com.tsbg.mis.dao.rookie;

import com.tsbg.mis.rookie.model.ProductionLineList;
import com.tsbg.mis.rookie.model.ReportProductionList;
import com.tsbg.mis.rookie.model.RookieApprovalInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 报告管理---产线实习报告(ReportProductionList)表数据库访问层
 *
 * @author makejava
 * @since 2020-06-19 14:30:26
 */
public interface ReportProductionListDao {

    /**
     * 通过ID查询单条数据
     *
     * @param reportId 主键
     * @return 实例对象
     */
    ReportProductionList queryById(Integer reportId);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<ReportProductionList> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param reportProductionList 实例对象
     * @return 对象列表
     */
    List<ReportProductionList> queryAll(ReportProductionList reportProductionList);

    /**
     * 新增数据
     *
     * @param reportProductionList 实例对象
     * @return 影响行数
     */
    int insert(ReportProductionList reportProductionList);

    /**
     * 修改数据
     *
     * @param reportProductionList 实例对象
     * @return 影响行数
     */
    int update(ReportProductionList reportProductionList);

    /**
     * 通过主键删除数据
     *
     * @param reportId 主键
     * @return 影响行数
     */
    int deleteById(Integer reportId);

    /**
     * 根据工号查询该用户对应的产线所有领导
     *
     * @param lineId
     * @return
     */
    ProductionLineList inquireWithStaffCode(Integer lineId);

    /**
     * 修改提案改善报告
     *
     * @param proposalId
     * @return
     */
    int updateProposal(Integer proposalId);

    /**
     * 修改产线报告状态 -> 通过
     *
     * @param rookieApprovalInfo
     * @return
     */
    int updateReport(RookieApprovalInfo rookieApprovalInfo);

    /**
     * 根据工号查询该用户对应的产线所有领导
     *
     * @param submitStaffCode
     * @return
     */
    ProductionLineList inquireByStaffCode(String submitStaffCode);

    /**
     * 修改产线报告状态 -> 被驳回
     *
     * @param rookieApprovalInfo
     * @return
     */
    int updateReportUnPass(RookieApprovalInfo rookieApprovalInfo);

    /**
     * 修改月目标状态 -> 通过
     *
     * @param rookieApprovalInfo
     * @return
     */
    int updateTarget(RookieApprovalInfo rookieApprovalInfo);


    /**
     * 修改月目标状态 -> 被驳回
     *
     * @param rookieApprovalInfo
     * @return
     */
    int updateTargetUnPass(RookieApprovalInfo rookieApprovalInfo);


    /**
     * 修改部门报告状态 -> 待审核
     *
     * @param first
     * @return
     */
    int updateUnAudit(RookieApprovalInfo first);
}