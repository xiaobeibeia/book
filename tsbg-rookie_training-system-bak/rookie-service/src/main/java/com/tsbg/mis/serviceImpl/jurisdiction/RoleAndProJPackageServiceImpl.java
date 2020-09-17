package com.tsbg.mis.serviceImpl.jurisdiction;

import com.tsbg.mis.jurisdiction.bag.RoleAndProJPackage;
import com.tsbg.mis.dao.jurisdiction.RoleAndProJPackageDao;
import com.tsbg.mis.service.jurisdiction.RoleAndProJPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleAndProJPackageServiceImpl implements RoleAndProJPackageService {

    @Autowired
    private RoleAndProJPackageDao roleAndProJPackageDao;

    @Override
    public List<RoleAndProJPackage> selectRoleAndProj2() {
        return roleAndProJPackageDao.selectRoleAndProj2();
    }
}
