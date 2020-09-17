package com.tsbg.mis.service.jurisdiction;

import com.tsbg.mis.jurisdiction.bag.RoleAndProJPackage;

import java.util.List;

public interface RoleAndProJPackageService {

    //配置映射user_role
    List<RoleAndProJPackage> selectRoleAndProj2();
}
