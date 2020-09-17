package com.tsbg.mis.dao.signed;

import com.tsbg.mis.signed.model.ManagerNode;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 签核主管与节点对应表，用于存储具有对应节点签核权限的主管信息(ManagerNode)表数据库访问层
 *
 * @author makejava
 * @since 2020-06-08 17:13:42
 */
public interface ManagerNodeDao {

    /**
     * 通过ID查询单条数据
     *
     * @param manaNodeId 主键
     * @return 实例对象
     */
    ManagerNode queryById(Integer manaNodeId);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<ManagerNode> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param managerNode 实例对象
     * @return 对象列表
     */
    List<ManagerNode> queryAll(ManagerNode managerNode);

    /**
     * 新增数据
     *
     * @param managerNode 实例对象
     * @return 影响行数
     */
    int insert(ManagerNode managerNode);

    /**
     * 修改数据
     *
     * @param managerNode 实例对象
     * @return 影响行数
     */
    int update(ManagerNode managerNode);

    /**
     * 通过主键删除数据
     *
     * @param manaNodeId 主键
     * @return 影响行数
     */
    int deleteById(Integer manaNodeId);

}