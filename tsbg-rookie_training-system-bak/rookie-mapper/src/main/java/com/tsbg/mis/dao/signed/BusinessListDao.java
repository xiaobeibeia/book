package com.tsbg.mis.dao.signed;

import com.tsbg.mis.signed.model.BusinessList;
import com.tsbg.mis.signed.model.SignedWorkflow;
import com.tsbg.mis.signed.vo.SignedWorkflowVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 签核事务列表，用于存储需要签核的事务的详细信息，每种签核流程对应一个id(BusinessList)表数据库访问层
 *
 * @author makejava
 * @since 2020-06-08 17:13:34
 */
public interface BusinessListDao {

    /**
     * 通过ID查询单条数据
     *
     * @param businessId 主键
     * @return 实例对象
     */
    BusinessList queryById(Integer businessId);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<BusinessList> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param businessList 实例对象
     * @return 对象列表
     */
    List<BusinessList> queryAll(BusinessList businessList);

    /**
     * 新增数据
     *
     * @param businessList 实例对象
     * @return 影响行数
     */
    int insert(BusinessList businessList);

    /**
     * 修改数据
     *
     * @param businessList 实例对象
     * @return 影响行数
     */
    int update(BusinessList businessList);

    /**
     * 通过主键删除数据
     *
     * @param businessId 主键
     * @return 影响行数
     */
    int deleteById(Integer businessId);

    /**
     * 查询菁干班系统所有签核流程
     *
     * @return
     */
    List<SignedWorkflowVo> inquire();

    /**
     * 根据 businessId 查询菁干班系统签核流程
     *
     * @return
     */
    SignedWorkflowVo inquireById(Integer businessId);
}