package com.tsbg.mis.serviceImpl.jurisdiction;

import com.tsbg.mis.dao.jurisdiction.PermRoleDao;
import com.tsbg.mis.service.jurisdiction.PermRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermRoleServiceImpl implements PermRoleService {

    @Autowired
    private PermRoleDao permRoleDao;

    @Override
    public List<Integer> selectPowerListByRoleList(List<Integer> roleList) {
        return permRoleDao.selectPowerListByRoleList(roleList);
    }
}
