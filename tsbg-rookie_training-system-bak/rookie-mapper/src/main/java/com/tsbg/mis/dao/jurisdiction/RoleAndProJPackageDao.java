package com.tsbg.mis.dao.jurisdiction;

import com.tsbg.mis.jurisdiction.bag.RoleAndProJPackage;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RoleAndProJPackageDao {

    //配置映射user_role
    List<RoleAndProJPackage> selectRoleAndProj2();
}
