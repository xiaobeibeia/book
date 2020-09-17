package com.tsbg.mis.dao.signed;

import com.tsbg.mis.signed.model.SignedNode;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 签核节点信息表，用于存储定义出来的所有用于签核的节点信息(SignedNode)表数据库访问层
 *
 * @author makejava
 * @since 2020-06-08 17:13:48
 */
public interface SignedNodeDao {

    /**
     * 通过ID查询单条数据
     *
     * @param signNodeId 主键
     * @return 实例对象
     */
    SignedNode queryById(Integer signNodeId);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<SignedNode> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param signedNode 实例对象
     * @return 对象列表
     */
    List<SignedNode> queryAll(SignedNode signedNode);

    /**
     * 新增数据
     *
     * @param signedNode 实例对象
     * @return 影响行数
     */
    int insert(SignedNode signedNode);

    /**
     * 修改数据
     *
     * @param signedNode 实例对象
     * @return 影响行数
     */
    int update(SignedNode signedNode);

    /**
     * 通过主键删除数据
     *
     * @param signNodeId 主键
     * @return 影响行数
     */
    int deleteById(Integer signNodeId);

    /**
     * 根据business_id逻辑删除
     *
     * @param businessId
     * @return
     */
    int deleteByBusinessId(Integer businessId);
}