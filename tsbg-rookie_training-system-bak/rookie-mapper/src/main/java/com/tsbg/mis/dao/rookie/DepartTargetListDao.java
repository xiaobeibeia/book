package com.tsbg.mis.dao.rookie;

import com.tsbg.mis.rookie.model.DepartTargetList;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 部门管理---部门目标信息表
(DepartTargetList)表数据库访问层
 *
 * @author makejava
 * @since 2020-08-06 10:58:04
 */
public interface DepartTargetListDao {

    /**
     * 通过ID查询单条数据
     *
     * @param targetId 主键
     * @return 实例对象
     */
    DepartTargetList queryById(Integer targetId);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<DepartTargetList> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param departTargetList 实例对象
     * @return 对象列表
     */
    List<DepartTargetList> queryAll(DepartTargetList departTargetList);

    /**
     * 新增数据
     *
     * @param departTargetList 实例对象
     * @return 影响行数
     */
    int insert(DepartTargetList departTargetList);

    /**
     * 修改数据
     *
     * @param departTargetList 实例对象
     * @return 影响行数
     */
    int update(DepartTargetList departTargetList);

    /**
     * 通过主键删除数据
     *
     * @param targetId 主键
     * @return 影响行数
     */
    int deleteById(Integer targetId);

    /**
     * 检查一组 targetNum 是否是同一个月目标
     * @param targetNum
     * @return
     */
    List<DepartTargetList> checkSameMonthGoal(String targetNum);
}