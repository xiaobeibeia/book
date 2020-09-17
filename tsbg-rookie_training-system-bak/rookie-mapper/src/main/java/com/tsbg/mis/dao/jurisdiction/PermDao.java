package com.tsbg.mis.dao.jurisdiction;

import com.alibaba.fastjson.JSONObject;
import com.tsbg.mis.jurisdiction.model.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PermDao {
    int deleteByPrimaryKey(Integer permId);

    int insert(Permission record);

    int insertSelective(Permission record);

    Permission selectByPrimaryKey(Integer permId);

    int updateByPrimaryKeySelective(Permission record);

    int updateByPrimaryKey(Permission record);

    //根据角色ID查询角色对应的权限信息
    List<String> findPermissionByRoleId2(@Param("roleId") Integer roleId);

    /**
     * 查询用户的角色 菜单 权限
     */
    JSONObject getMyUserPermission(String userCode, Integer projId);

    //根据permission查询对应权限ID
    List<Integer> selectPermIdByPerm(String[] perm);

    //根据权限返回对应权限名
    String selectPermission(String name);

    //生态系统权限列表查询
    String selectPermListByUserCode(String userCode);

    //根据权限列表查询对应权限详情
    List<String> selectPowerDetailByPowerList(List<Integer> powerList);

    //固资系统权限列表查询
    String selectAssetsPowerByUserCode(String userCode);
}
