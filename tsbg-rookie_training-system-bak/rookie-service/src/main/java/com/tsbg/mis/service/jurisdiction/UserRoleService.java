package com.tsbg.mis.service.jurisdiction;

import com.tsbg.mis.jurisdiction.bag.RoleAndProJPackage;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface UserRoleService {

    //插入数据到user_role
    int insertData(@Param("uid") Integer userId, @Param("rid") Integer roleId, @Param("creatorId") Integer creatorId, @Param("createDate") Date date, @Param("projId") Integer projId);

    //查询当前登录用户所拥有的项目情况
    List<RoleAndProJPackage> selectProJMsgByUid(Integer uid);

    //根据用户的userId查询出其所拥有的角色插入到字段role_list
    List<Integer> getRole(Integer uid);

}
