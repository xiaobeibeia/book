package com.tsbg.mis.dao.signed;

import com.tsbg.mis.signed.model.SignedType;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 签核类型信息表，用于存储各种不同的签核类型详细信息(SignedType)表数据库访问层
 *
 * @author makejava
 * @since 2020-06-08 17:13:48
 */
public interface SignedTypeDao {

    /**
     * 通过ID查询单条数据
     *
     * @param signTypeId 主键
     * @return 实例对象
     */
    SignedType queryById(Integer signTypeId);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<SignedType> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param signedType 实例对象
     * @return 对象列表
     */
    List<SignedType> queryAll(SignedType signedType);

    /**
     * 新增数据
     *
     * @param signedType 实例对象
     * @return 影响行数
     */
    int insert(SignedType signedType);

    /**
     * 修改数据
     *
     * @param signedType 实例对象
     * @return 影响行数
     */
    int update(SignedType signedType);

    /**
     * 通过主键删除数据
     *
     * @param signTypeId 主键
     * @return 影响行数
     */
    int deleteById(Integer signTypeId);

}