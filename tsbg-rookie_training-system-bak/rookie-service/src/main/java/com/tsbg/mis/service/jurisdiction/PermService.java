package com.tsbg.mis.service.jurisdiction;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public interface PermService {
    /**
     * 查询某角色  菜单列表   权限列表
     */
    JSONObject getMyUserPermission(String userCode, Integer projId);

    //生态系统权限列表查询
    String selectPermListByUserCode(String userCode);

    //根据权限列表查询对应权限详情
    List<String> selectPowerDetailByPowerList(List<Integer> powerList);

    //固资系统权限列表查询
    String selectAssetsPowerByUserCode(String userCode);
}
