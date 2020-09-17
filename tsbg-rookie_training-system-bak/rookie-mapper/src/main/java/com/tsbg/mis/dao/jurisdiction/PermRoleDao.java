package com.tsbg.mis.dao.jurisdiction;

import com.tsbg.mis.jurisdiction.model.PermRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PermRoleDao {
    int deleteByPrimaryKey(Integer permroleId);

    int insert(PermRole record);

    int insertSelective(PermRole record);

    PermRole selectByPrimaryKey(Integer permroleId);

    int updateByPrimaryKeySelective(PermRole record);

    int updateByPrimaryKey(PermRole record);

    //根据角色列表查询权限列表
    List<Integer> selectPowerListByRoleList(List<Integer> roleList);
}
