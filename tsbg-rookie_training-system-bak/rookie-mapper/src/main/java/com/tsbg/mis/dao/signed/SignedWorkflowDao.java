package com.tsbg.mis.dao.signed;

import com.tsbg.mis.signed.model.SignedWorkflow;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 签核流程信息表，用于存储已经定好的签核流程与主管信息(SignedWorkflow)表数据库访问层
 *
 * @author makejava
 * @since 2020-06-08 17:13:48
 */
public interface SignedWorkflowDao {

    /**
     * 通过ID查询单条数据
     *
     * @param signWorkflowId 主键
     * @return 实例对象
     */
    SignedWorkflow queryById(Integer signWorkflowId);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<SignedWorkflow> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param signedWorkflow 实例对象
     * @return 对象列表
     */
    List<SignedWorkflow> queryAll(SignedWorkflow signedWorkflow);

    /**
     * 新增数据
     *
     * @param signedWorkflow 实例对象
     * @return 影响行数
     */
    int insert(SignedWorkflow signedWorkflow);

    /**
     * 修改数据
     *
     * @param signedWorkflow 实例对象
     * @return 影响行数
     */
    int update(SignedWorkflow signedWorkflow);

    /**
     * 通过主键删除数据
     *
     * @param signWorkflowId 主键
     * @return 影响行数
     */
    int deleteById(Integer signWorkflowId);

    /**
     * 通过 businessIds 查询所有节点信息
     *
     * @param businessIds
     * @return
     */
    List<SignedWorkflow> queryAllByBusinessIds(List<Integer> businessIds);

    /**
     * 根据business_id逻辑删除
     *
     * @param businessId
     */
    int deleteByBusinessId(Integer businessId);
}