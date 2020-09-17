package com.tsbg.mis.dao.rookie;

import com.tsbg.mis.rookie.model.RookieApprovalInfo;
import com.tsbg.mis.rookie.vo.MonthTargetApprovalVo;
import com.tsbg.mis.rookie.vo.RookieApprovalInfoVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 签核信息记录表(RookieApprovalInfo)表数据库访问层
 *
 * @author makejava
 * @since 2020-06-19 15:29:16
 */
public interface RookieApprovalInfoDao {

    /**
     * 通过ID查询单条数据
     *
     * @param rookieApprovalId 主键
     * @return 实例对象
     */
    RookieApprovalInfo queryById(Integer rookieApprovalId);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<RookieApprovalInfo> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param rookieApprovalInfo 实例对象
     * @return 对象列表
     */
    List<RookieApprovalInfo> queryAll(RookieApprovalInfo rookieApprovalInfo);

    /**
     * 新增数据
     *
     * @param rookieApprovalInfo 实例对象
     * @return 影响行数
     */
    int insert(RookieApprovalInfo rookieApprovalInfo);

    /**
     * 修改数据
     *
     * @param rookieApprovalInfo 实例对象
     * @return 影响行数
     */
    int update(RookieApprovalInfo rookieApprovalInfo);

    /**
     * 通过主键删除数据
     *
     * @param rookieApprovalId 主键
     * @return 影响行数
     */
    int deleteById(Integer rookieApprovalId);

    /**
     * 查询主管所有的签核项目(待签核放前面，已签核的按照时间降序排列) 产线报告
     *
     * @param build
     * @return
     */
    List<RookieApprovalInfoVo> queryAllInfoVo(RookieApprovalInfo build);

    /**
     * 查询主管所有的签核项目(待签核放前面，已签核的按照时间降序排列) 提案改善
     *
     * @param build
     * @return
     */
    List<RookieApprovalInfoVo> queryAllInfoVoFromProposal(RookieApprovalInfo build);

    /**
     * 查询 levelTypeName
     *
     * @param levelType
     * @return
     */
    String queryLevelTypeName(Integer levelType);

    /**
     * 查询所有月目标审核任务
     *
     * @param staffCode
     * @return
     */
    List<MonthTargetApprovalVo> queryMonthTargetApproval(String staffCode);

    /**
     * 查询主管所有的签核项目(待签核放前面，已签核的按照时间降序排列) 部门报告
     *
     * @param build
     * @return
     */
    List<RookieApprovalInfoVo> queryAllInfoVoFromDepart(RookieApprovalInfo build);
}