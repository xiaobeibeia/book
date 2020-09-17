package com.tsbg.mis.serviceImpl.jurisdiction;

import com.tsbg.mis.jurisdiction.bag.RoleAndProJPackage;
import com.tsbg.mis.dao.jurisdiction.UserRoleDao;
import com.tsbg.mis.service.jurisdiction.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    @Autowired
    private UserRoleDao userRoleDao;

    @Override
    public int insertData(Integer userId, Integer roleId, Integer creatorId, Date date, Integer projId) {
        return userRoleDao.insertData(userId,roleId,creatorId,date,projId);
    }

    @Override
    public List<RoleAndProJPackage> selectProJMsgByUid(Integer uid) {
        return userRoleDao.selectProJMsgByUid(uid);
    }

    @Override
    public List<Integer> getRole(Integer uid) {
        return userRoleDao.getRole(uid);
    }

}
