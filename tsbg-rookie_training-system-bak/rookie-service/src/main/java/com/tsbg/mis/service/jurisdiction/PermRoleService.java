package com.tsbg.mis.service.jurisdiction;

import java.util.List;

public interface PermRoleService {

    //根据角色列表查询权限列表
    List<Integer> selectPowerListByRoleList(List<Integer> roleList);
}
